
// @author Santi

package Negocio.Productora;

import Integracion.DAOFactory.DAOFactory;
import Integracion.Pelicula.DAOPelicula;
import Integracion.Productora.DAOProductora;
import Integracion.QueryFactory.Query;
import Integracion.QueryFactory.QueryFactory;
import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;
import Negocio.Pelicula.TPelicula;
import utilities.Pair;

import java.util.Date;
import java.util.ArrayList;

/*
 *  CÓDIGOS ERROR 
 *  -------------
 *  · -1: Error con la base de datos
 *  · -2: Productora activa (nombre)
 *  · -3: Productora activa (cif)
 *  · -4: Teléfono no válido
 *  · -5: No existe productora
 *  · -6: Productora inactiva
 *  · -7: No existe película
 *  · -8: Película inactiva
 *  · -9: Fecha fin > fecha actual
 *  · -10: Fecha ini > fecha fin
 *  · -11: No existe ninguna productora activa
 *  · -12: Nombre repetido
 *  · -13: CIF repetido
 */

public class ASProductoraImp implements ASProductora {

	public Integer Alta(TProductora productora) {
		Integer id = -1;
		Transaction t = null;
		try {

			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			t.start();

			DAOFactory daof = DAOFactory.getInstance();
			DAOProductora daoProductora = daof.getDAOProductora();

			TProductora productoraCIF = daoProductora.ReadByCIF(productora.GetCIF());
			TProductora productoraNom = daoProductora.ReadByNombre(productora.GetNombre());

			if (productoraCIF == null) {
				if (productoraNom == null) {
					if (productora.GetTelefono() < 100000000 || productora.GetTelefono() > 999999999) {
						t.rollback();
						return -4;
					} else {
						id = daoProductora.Create(productora);
						if (id < 0) {
							t.rollback();
							return -1;
						}
					}
				}
				else if (productoraNom.GetID() < 0) {
					t.rollback();
					return -1;
				}
				else {
					if (productoraNom.GetActivo() == false) {
						productora.SetActivo(true);
						productora.SetID(productoraNom.GetID());
						if (productora.GetTelefono() < 100000000 || productora.GetTelefono() > 999999999) {
							t.rollback();
							return -4;
						} else {
							Integer idUpdate = daoProductora.Update(productora);
							if (idUpdate < 0) {
								t.rollback();
								return -1;
							}
							t.commit();
							return idUpdate;
						}
					} else {
						t.rollback();
						return -2;
					}
				}
			}
			else if (productoraCIF.GetID() < 0) {
				t.rollback();
				return -1;
			}
			else {
				if (productoraCIF.GetActivo() == false) {
					if (productoraNom != null && productoraNom.GetID() < 0) {
						t.rollback();
						return -1;
					}
					if (productoraNom != null && productoraNom.GetID() != productoraCIF.GetID()) {
						t.rollback();
						return -12;
					}
					productora.SetActivo(true);
					productora.SetID(productoraCIF.GetID());
					if (productora.GetTelefono() < 100000000 || productora.GetTelefono() > 999999999) {
						t.rollback();
						return -4;
					} else {
						Integer idUpdate = daoProductora.Update(productora);
						if (idUpdate < 0) {
							t.rollback();
							return -1;
						}

						t.commit();
						return idUpdate;
					}
				} else {
					t.rollback();
					return -3;
				}
			}

			t.commit();
			return id;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return -1;
		}
	}

	public Integer Baja(Integer id) {
		Transaction t = null;

		try {
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			t.start();

			DAOFactory daof = DAOFactory.getInstance();
			DAOProductora daoProductora = daof.getDAOProductora();
			DAOPelicula daoPelicula = daof.getDAOPelicula();

			TProductora productora = daoProductora.Read(id);
			if (productora == null) {
				t.rollback();
				return -5;
			}
			else if (productora.GetID() < 0) {
				t.rollback();
				return -1;
			}
			
			if (productora.GetActivo() == false) {
				t.rollback();
				return -6;
			}

			Integer idBaja = daoProductora.Delete(id);
			if (idBaja < 0) {
				t.rollback();
				return -1;
			}

			// eliminamos las asociaciones <pelicula, productora> de la
			// productora
			ArrayList<TPelicula> peliculas = daoPelicula.ReadAllByProductora(productora.GetID());
			if (peliculas.size() > 0 && peliculas.get(0).GetID() < 0) {
				t.rollback();
				return -1;
			}
			for (TPelicula p : peliculas) {
				Integer exito = daoPelicula.RemoveProductora(productora.GetID(), p.GetID());
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

	public Integer Modificar(TProductora productora) {
		Integer id = -1;
		Transaction t = null;
		try {

			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			t.start();

			DAOFactory daof = DAOFactory.getInstance();
			DAOProductora daoProductora = daof.getDAOProductora();

			TProductora original = daoProductora.Read(productora.GetID());
						
			// Comprobamos que no exista productora con ese ID
			if (original == null) {
				t.rollback();
				return -5;
			}
			else if (original.GetID() < 0) {
				t.rollback();
				return -1;
			}
			
			// Comprobamos que no esta inactiva
			if(!original.GetActivo()){
				t.rollback();
				return -6;
			}

			TProductora productoraCIF = daoProductora.ReadByCIF(productora.GetCIF());
			
			// Comprobamos que no exista productora con CIF si se ha modificado
			if(!original.GetCIF().equals(productora.GetCIF()) && productoraCIF != null && productoraCIF.GetID() > 0){
				t.rollback();
				return -13;
			}
			if (productoraCIF != null && productoraCIF.GetID() < 0) {
				t.rollback();
				return -1;
			}
			
			TProductora productoraNom = daoProductora.ReadByNombre(productora.GetNombre());
			
			// Comprobamos que no exista productora con Nombre si se ha modificado
			if(!original.GetNombre().equals(productora.GetNombre()) && productoraNom != null && productoraNom.GetID() > 0){
				t.rollback();
				return -12;
			}
			if (productoraNom != null && productoraNom.GetID() < 0) {
				t.rollback();
				return -1;
			}
			
			// Comprobamos que el telefono es correcto si se ha modificado
			if (!original.GetTelefono().equals(productora.GetTelefono()) && (productora.GetTelefono() < 100000000 || productora.GetTelefono() > 999999999)) {
				t.rollback();
				return -4;
			}
			
			// Se cumplen todas las reglas de negocio, modificamos
			id = daoProductora.Update(productora);
			if (id < 0) {
				t.rollback();
				return -1;
			}
			t.commit();
			return id;
		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return -1;
		}
	}

	public TProductora Mostrar(Integer id) {
		Transaction t = null;

		try {
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			t.start();

			DAOFactory daof = DAOFactory.getInstance();
			DAOProductora daoProductora = daof.getDAOProductora();

			TProductora productora = daoProductora.Read(id);
			if (productora == null) {
				t.rollback();
				return new TProductora(-5);
			}
			else if (productora.GetID() < 0) {
				t.rollback();
				productora.SetID(-1);
				return productora;
			}

			t.commit();
			return productora;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return new TProductora(-1);
		}
	}

	public ArrayList<TProductora> Listar() {
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
			DAOProductora daoProductora = daof.getDAOProductora();

			ArrayList<TProductora> productoras = daoProductora.ReadAll();

			// error con la BD
			if (productoras.size() > 0 && productoras.get(0).GetID() < 0) {
				t.rollback();
				productoras.get(0).SetID(-1);
				return productoras;
			}

			t.commit();
			return productoras;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			ArrayList<TProductora> error = new ArrayList<>();
			error.add(new TProductora(-1));
			return error;
		}
	}

	public ArrayList<TProductora> ListarPorPelicula(Integer idPelicula) {
		Transaction t = null;
		try {
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			t.start();

			DAOFactory daof = DAOFactory.getInstance();
			DAOProductora daoProductora = daof.getDAOProductora();
			DAOPelicula daoPelicula = daof.getDAOPelicula();

			ArrayList<TProductora> productoras = new ArrayList<>();

			TPelicula pelicula = daoPelicula.Read(idPelicula);
			if (pelicula == null) {
				t.rollback();
				productoras.add(new TProductora(-7));
				return productoras;
			}
			else if (pelicula.GetID() < 0) {
				t.rollback();
				productoras.add(new TProductora(-1));
				return productoras;
			}

			if (pelicula.GetActivo() == false) {
				t.rollback();
				productoras.add(new TProductora(-8));
				return productoras;
			}

			productoras = daoProductora.ReadAllByPelicula(idPelicula);
			if (productoras.size() > 0 && productoras.get(0).GetID() < 0) {
				t.rollback();
				productoras.get(0).SetID(-1);
				return productoras;
			}

			ArrayList<TProductora> productorasActivas = new ArrayList<>();
			for (TProductora aux : productoras) {
				if (aux.GetActivo() == true)
					productorasActivas.add(aux);
			}

			t.commit();
			return productorasActivas;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			ArrayList<TProductora> productoras = new ArrayList<>();
			productoras.add(new TProductora(-1));
			return productoras;
		}
	}

	public Pair<ArrayList<TProductora>, Integer> ProductoraMasIngresos(Pair<Date, Date> aux) {
		Transaction t = null;
		Date fechaActual = new Date();

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
			DAOProductora dao = daof.getDAOProductora();

			Pair<ArrayList<TProductora>, Integer> productoras = new Pair<ArrayList<TProductora>, Integer>();

			// comprobamos que existe al menos una productora activa
			ArrayList<TProductora> prods = dao.ReadAll();
			if (prods.size() > 0 && prods.get(0).GetID() < 0) {
				t.rollback();
				productoras.setObj2(-1);
				return productoras;
			}
			ArrayList<TProductora> prodsActivas = new ArrayList<TProductora>();
			for (TProductora p : prods) {
				if (p.GetActivo())
					prodsActivas.add(p);
			}
			if (prodsActivas.size() == 0) {
				t.rollback();
				productoras.setObj2(-11);
				return productoras;
			}

			// comprobamos fechas
			if (aux.getFirst().before(aux.getSecond())) {
				if (aux.getSecond().after(fechaActual)) {
					t.rollback();
					productoras.setObj2(-9);
					return productoras;
				} else {
					QueryFactory qf = QueryFactory.getInstance();
					Query productorasMasIngresos = qf.generateQuery(2);
					productoras = (Pair<ArrayList<TProductora>, Integer>) productorasMasIngresos.execute(aux);
				}
			} else {
				t.rollback();
				productoras.setObj2(-10);
				return productoras;
			}
			t.commit();
			return productoras;

		} catch (Exception e) {
			e.printStackTrace();
			return new Pair<ArrayList<TProductora>, Integer>(null, -1);
		}
	}

	@Override
	public TProductora MostrarParaModificar(Integer id) {
		Transaction t = null;

		try {
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			t.start();

			DAOFactory daof = DAOFactory.getInstance();
			DAOProductora daoProductora = daof.getDAOProductora();

			TProductora productora = daoProductora.Read(id);
			if (productora == null) {
				t.rollback();
				return new TProductora(-5);
			}
			else if (productora.GetID() < 0) {
				t.rollback();
				productora.SetID(-1);
				return productora;
			}
			
			if (!productora.GetActivo()) {
				t.rollback();
				productora.SetID(-6);
				return productora;
			}

			t.commit();
			return productora;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return new TProductora(-1);
		}
	}

}
