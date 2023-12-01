
package Negocio.Pelicula;

import java.util.ArrayList;

import Integracion.DAOFactory.DAOFactory;
import Integracion.Pase.DAOPase;
import Integracion.Pelicula.DAOPelicula;
import Integracion.Productora.DAOProductora;
import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;
import Negocio.Pase.TPase;
import Negocio.Productora.TProductora;

/*
 *  CÓDIGOS DE ERROR
 *  ----------------
 *  · -1: Error con la BD
 *  · -2: No existe pelicula
 *  · -3: Película inactiva 
 *  · -4: Película activa
 *  · -5: No existe productora
 *  · -6: Productora inactiva
 *  · -7: Película y productora ya asociadas
 *  · -8: Película y productoras no asociadas
 *  · -9: Título repetido
 */

public class ASPeliculaImp implements ASPelicula {

	public Integer Alta(TPelicula pelicula) {

		Transaction t = null;

		try {

			// empezamos transacción:
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			t.start();

			// obtenemos dao:
			DAOFactory daof = DAOFactory.getInstance();
			DAOPelicula daoPelicula = daof.getDAOPelicula();

			// comprobamos que no existe ya una película con ese título
			TPelicula peliculaExistente = daoPelicula.ReadByTitulo(pelicula.GetTitulo());
			if (peliculaExistente != null && peliculaExistente.GetID() >= 0) {
				if (peliculaExistente.GetActivo()) {
					t.rollback();
					return -4;
				}
				// reactivación
				pelicula.SetActivo(true);
				pelicula.SetID(peliculaExistente.GetID());
				Integer exito = daoPelicula.Update(pelicula);
				if (exito < 0) {
					t.rollback();
					return -1;
				}
				t.commit();
				return exito;
			} else if (peliculaExistente == null) {
				// damos de alta
				Integer id = daoPelicula.Create(pelicula);
				if (id < 0) {
					t.rollback();
					return -1;
				}

				t.commit();
				return id;
			} else { // peliculaExistente.GetID() < 0
				t.rollback();
				return -1;
			}

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return -1;
		}

	}

	public Integer Baja(Integer id) {

		Transaction t = null;

		try {

			// empezamos transacción:
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			t.start();

			// obtenemos dao:
			DAOFactory daof = DAOFactory.getInstance();
			DAOPelicula daoPelicula = daof.getDAOPelicula();
			DAOProductora daoProductora = daof.getDAOProductora();
			DAOPase daoPase = daof.getDAOPase();

			// comprobamos que existe la película
			TPelicula pelicula = daoPelicula.Read(id);
			if (pelicula == null) {
				t.rollback();
				return -2;
			}
			else if (pelicula.GetID() < 0) {
				t.rollback();
				return -1;
			}

			// comprobamos que la película estaba activa
			if (!pelicula.GetActivo()) {
				t.rollback();
				return -3;
			}

			Integer exito = daoPelicula.Delete(id);
			if (exito < 0) {
				t.rollback();
				return -1;
			}

			// eliminamos las asociaciones <pelicula, productora> de la película
			ArrayList<TProductora> productoras = daoProductora.ReadAllByPelicula(pelicula.GetID());
			if (productoras.size() > 0 && productoras.get(0).GetID() < 0) {
				t.rollback();
				return -1;
			}
			for (TProductora p : productoras) {
				exito = daoPelicula.RemoveProductora(p.GetID(), pelicula.GetID());
				if (exito < 0) {
					t.rollback();
					return -1;
				}
			}

			// eliminamos los pases que emitían la película
			ArrayList<TPase> pases = daoPase.ReadAllByPelicula(pelicula.GetID());
			if (pases.size() > 0 && pases.get(0).GetID() < 0) {
				t.rollback();
				return -1;
			}
			for (TPase p : pases) {
				exito = daoPase.Delete(p.GetID());
				if (exito < 0) {
					t.rollback();
					return -1;
				}
			}

			t.commit();
			return 0;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return -1;
		}

	}

	public TPelicula Mostrar(Integer id) {

		Transaction t = null;

		try {

			// empezamos transacción:
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			t.start();

			// obtenemos dao:
			DAOFactory daof = DAOFactory.getInstance();
			DAOPelicula dao = daof.getDAOPelicula();

			// comprobamos que existe la película
			TPelicula pelicula = dao.Read(id);
			if (pelicula == null) {
				t.rollback();
				return new TPelicula(-2);
			}
			else if (pelicula.GetID() < 0) {
				t.rollback();
				pelicula.SetID(-1);
				return pelicula;
			}

			t.commit();
			return pelicula;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return new TPelicula(-1);
		}

	}
	
	public ArrayList<TPelicula> Listar() {
		Transaction t = null;

		try {

			// empezamos transacción:
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			t.start();

			// obtenemos dao:
			DAOFactory daof = DAOFactory.getInstance();
			DAOPelicula daop = daof.getDAOPelicula();

			ArrayList<TPelicula> peliculas = daop.ReadAll();

			// error con la BD
			if (peliculas.size() > 0 && peliculas.get(0).GetID() < 0) {
				t.rollback();
				peliculas.get(0).SetID(-1);
				return peliculas;
			}

			t.commit();
			return peliculas;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			ArrayList<TPelicula> error = new ArrayList<TPelicula>();
			error.add(new TPelicula(-1));
			return error;
		}

	}

	public TPelicula MostrarParaModificar(Integer id) {

		Transaction t = null;

		try {

			// empezamos transacción:
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			t.start();

			// obtenemos dao:
			DAOFactory daof = DAOFactory.getInstance();
			DAOPelicula dao = daof.getDAOPelicula();

			// comprobamos que existe la película
			TPelicula pelicula = dao.Read(id);
			if (pelicula == null) {
				t.rollback();
				return new TPelicula(-2);
			}
			else if (pelicula.GetID() < 0) {
				t.rollback();
				pelicula.SetID(-1);
				return pelicula;
			}
			
			// comprobamos que la película está activa
			if (!pelicula.GetActivo()) {
				t.rollback();
				pelicula.SetID(-3);
				return pelicula;
			}

			t.commit();
			return pelicula;

		} 
		catch (Exception e) {
			if (t != null)
				t.rollback();
			return new TPelicula(-1);
		}

	}
	
	public Integer Modificar(TPelicula pelicula) {

		Transaction t = null;

		try {

			// empezamos transacción:
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			t.start();

			// obtenemos dao:
			DAOFactory daof = DAOFactory.getInstance();
			DAOPelicula daop = daof.getDAOPelicula();

			TPelicula peliculaAnt = daop.Read(pelicula.GetID());

			// comprobamos que existe la película
			if (peliculaAnt == null) {
				t.rollback();
				return -2;
			}
			else if (peliculaAnt.GetID() < 0) {
				t.rollback();
				return -1;
			}

			// comprobamos que la película está activa
			if (!peliculaAnt.GetActivo()) {
				t.rollback();
				return -3;
			}

			// comprobamos que, si se ha modificado el título, no se ha puesto uno ya existente
			if (!pelicula.GetTitulo().equals(peliculaAnt.GetTitulo())) {
				TPelicula peliculaExistente = daop.ReadByTitulo(pelicula.GetTitulo());
				if (peliculaExistente != null&& peliculaExistente.GetID() >= 0) {
					t.rollback();
					return -9;
				}
				if (peliculaExistente != null && peliculaExistente.GetID() < 0) {
					t.rollback();
					return -1;
				}
			}
			
			// modificamos
			Integer exito = daop.Update(pelicula);
			if (exito < 0) {
				t.rollback();
				return -1;
			}
			
			t.commit();
			return 0;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return -1;
		}

	}

	public ArrayList<TPelicula> ListarPorProductora(Integer idProductora) {

		Transaction t = null;

		try {
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();

			if (t == null) {
				throw new Exception("Transaccion nula");
			}
			t.start();

			DAOFactory daof = DAOFactory.getInstance();
			DAOPelicula daop = daof.getDAOPelicula();
			DAOProductora daoProductora = daof.getDAOProductora();

			ArrayList<TPelicula> peliculas = new ArrayList<>();

			TProductora productora = daoProductora.Read(idProductora);
			if (productora == null) {
				t.rollback();
				peliculas.add(new TPelicula(-5));
				return peliculas;
			}
			else if (productora.GetID() < 0) {
				t.rollback();
				peliculas.add(new TPelicula(-1));
				return peliculas;
			}
			
			if (productora.GetActivo() == false) {
				t.rollback();
				peliculas.add(new TPelicula(-6));
				return peliculas;
			}

			peliculas = daop.ReadAllByProductora(idProductora);
			if (peliculas.size() > 0 && peliculas.get(0).GetID() < 0) {
				t.rollback();
				peliculas.get(0).SetID(-1);
				return peliculas;
			}

			ArrayList<TPelicula> peliculasActivas = new ArrayList<>();
			for (TPelicula aux : peliculas) {
				if (aux.GetActivo() == true)
					peliculasActivas.add(aux);
			}

			t.commit();
			return peliculasActivas;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			ArrayList<TPelicula> error = new ArrayList<>();
			error.add(new TPelicula(-1));
			return error;
		}
	}

	public Integer AnadirProductora(Integer idProductora, Integer idPelicula) {

		Transaction t = null;

		try {

			// empezamos transacción:
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			t.start();

			// obtenemos daos:
			DAOFactory daof = DAOFactory.getInstance();
			DAOPelicula daoPelicula = daof.getDAOPelicula();
			DAOProductora daoProductora = daof.getDAOProductora();

			TPelicula pelicula = daoPelicula.Read(idPelicula);
			TProductora productora = daoProductora.Read(idProductora);

			// comprobamos que existe la película
			if (pelicula == null) {
				t.rollback();
				return -2;
			}
			else if (pelicula.GetID() < 0) {
				t.rollback();
				return -1;
			}

			// comprobamos que la película está activa
			if (!pelicula.GetActivo()) {
				t.rollback();
				return -3;
			}

			// comprobamos que existe la productora
			if (productora == null) {
				t.rollback();
				return -5;
			}
			else if (productora.GetID() < 0) {
				t.rollback();
				return -1;
			}

			// comprobamos que la productora está activa
			if (!productora.GetActivo()) {
				t.rollback();
				return -6;
			}

			// comprobamos que la película no estaba ya asociada con la
			// productora
			Integer existe = daoPelicula.LinkedProductora(productora.GetID(), pelicula.GetID());
			if (existe == 1) {
				t.rollback();
				return -7;
			}

			if (existe == -1) {
				t.rollback();
				return -1;
			}

			Integer exito = daoPelicula.AddProductora(productora.GetID(), pelicula.GetID());
			if (exito < 0) {
				t.rollback();
				return -1;
			}

			t.commit();
			return 0;

		} catch (Exception e) {
			if (t != null) {
				t.rollback();
			}
			return -1;
		}

	}

	public Integer QuitarProductora(Integer idProductora, Integer idPelicula) {
		Transaction t = null;

		try {

			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();

			if (t == null) {
				throw new Exception("Error transaccional");
			}
			t.start();

			DAOFactory daof = DAOFactory.getInstance();
			DAOPelicula daopel = daof.getDAOPelicula();
			DAOProductora daop = daof.getDAOProductora();

			TPelicula pelicula = daopel.Read(idPelicula);

			if (pelicula == null) {
				t.rollback();
				return -2;
			}
			else if (pelicula.GetID() < 0) {
				t.rollback();
				return -1;
			}

			if (!pelicula.GetActivo()) {
				t.rollback();
				return -3;
			}

			TProductora productora = daop.Read(idProductora);

			if (productora == null) {
				t.rollback();
				return -5;
			}
			else if (productora.GetID() < 0) {
				t.rollback();
				return -1;
			}

			if (!productora.GetActivo()) {
				t.rollback();
				return -6;
			}

			// comprobamos que estaban ya asociadas
			Integer existe = daopel.LinkedProductora(idProductora, idPelicula);
			if (existe == 0) {
				t.rollback();
				return -8;
			}

			if (existe == -1) {
				t.rollback();
				return -1;
			}

			Integer i = daopel.RemoveProductora(idProductora, idPelicula);
			if (i < 0) {
				t.rollback();
				return -1;
			}

			t.commit();
			return 0;

		} catch (Exception e) {
			if (t != null) {
				t.rollback();
			}
			return -1;
		}
	}

}