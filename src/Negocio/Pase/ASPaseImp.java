package Negocio.Pase;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;

import Integracion.Compra.DAOCompra;
import Integracion.DAOFactory.DAOFactory;
import Integracion.Pase.DAOPase;
import Integracion.Pelicula.DAOPelicula;
import Integracion.Sala.DAOSala;
import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;
import Negocio.Compra.TCompra;
import Negocio.Pelicula.TPelicula;
import Negocio.Sala.TSala;
import utilities.Pair;

/*
 *	CÓDIGOS DE ERROR 
 *  ----------------
 *  · -1: Error con la Base de Datos
 *  · -2: No existe el pase
 *  · -3: No existe la sala
 *  · -4: Sala inactiva
 *  · -5: No existe la película
 *  · -6: Película inactiva
 *  · -7: No existe la compra
 *  · -8: Pase inactivo
 *  · -9: Pase activo
 *  · -10: Pase anterior a fecha actual
 *  · -11: Precio <= 0
 *  · -12: Horarios se superponen
 *  · -13: Pase incluido en compra
 */

public class ASPaseImp implements ASPase {

	public Integer Alta(TPase pase) {

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
			DAOPase daoPase = daof.getDAOPase();
			DAOSala daoSala = daof.getDAOSala();
			DAOPelicula daoPelicula = daof.getDAOPelicula();

			// comprobamos que el pase con los atributos <horario, idSala> no
			// existía
			TPase paseExistente = daoPase.ReadByKey(pase.GetIDSala(), pase.GetHorario());
			if (paseExistente != null && paseExistente.GetID() > 0) {
				if (paseExistente.GetActivo()) {
					t.rollback();
					return -9;
				}
				// reactivación
				else {
					Pair<Integer, TSala> check = checkAltaMod(pase, daoPase, daoSala, daoPelicula);
					if (check.getFirst() == 0) {
						pase.SetActivo(true);
						pase.SetID(paseExistente.GetID());
						pase.SetAsientosLibres(paseExistente.GetAsientosLibres());
						Integer exito = daoPase.Update(pase);
						if (exito < 0) {
							t.rollback();
							return -1;
						}
						t.commit();
						return exito;
					} else {
						t.rollback();
						return check.getFirst();
					}
				}
			} else if (paseExistente != null) {
				t.rollback();
				return -1;
			} else {
				// damos de alta
				Pair<Integer, TSala> check = checkAltaMod(pase, daoPase, daoSala, daoPelicula);
				if (check.getFirst() == 0) {
					Integer aforo = check.getSecond().GetAforo();
					pase.SetAsientosLibres(aforo);
					Integer id = daoPase.Create(pase);
					if (id < 0) {
						t.rollback();
						return -1;
					}
					t.commit();
					return id;
				} else {
					t.rollback();
					return check.getFirst();
				}
			}

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return -1;
		}

	}

	private Pair<Integer, TSala> checkAltaMod(TPase pase, DAOPase daoPase, DAOSala daoSala, DAOPelicula daoPelicula) {

		TSala sala = daoSala.Read(pase.GetIDSala());
		// comprobamos que la sala existe
		if (sala == null)
			return new Pair<Integer, TSala>(-3, null);
		else if (sala.GetId() < 0) 
			return new Pair<Integer, TSala>(-1, null);

		// comprobamos que la sala está activa
		if (!sala.GetActivo())
			return new Pair<Integer, TSala>(-4, null);

		// comprobamos que la película existe
		TPelicula pelicula = null;
		pelicula = daoPelicula.Read(pase.GetIDPelicula());									
		if (pelicula == null)
			return new Pair<Integer, TSala>(-5, null);
		else if (pelicula.GetID() < 0)
			return new Pair<Integer, TSala>(-1, null);

		// comprobamos que la película está activa
		if (!pelicula.GetActivo())
			return new Pair<Integer, TSala>(-6, null);

		// comprobamos que el horario del pase es posterior a la fecha actual
		Date fechaActual = new Date();
		if (pase.GetHorario().before(fechaActual))
			return new Pair<Integer, TSala>(-10, null);

		// comprobamos que el precio es mayor que 0
		if (pase.GetPrecioActual() <= 0)
			return new Pair<Integer, TSala>(-11, null);

		// comprobamos que el horario del pase no se superpone con ninguno de
		// los que se emiten en esa sala
		try {

			Date ini = pase.GetHorario();
			Date fin = calcularFin(ini, pelicula.GetDuracion());
			ArrayList<TPase> pases = daoPase.ReadAllBySala(sala.GetId());
			// error con la BD
			if (pases.size() > 0 && pases.get(0).GetID() < 0)
				return new Pair<Integer, TSala>(-1, null);

			for (TPase p : pases) {
				if (p.GetID() != pase.GetID()) {
					if (p.GetActivo()) {
						TPelicula peliAux = daoPelicula.Read(p.GetIDPelicula());
						if (peliAux == null || peliAux != null && peliAux.GetID() < 0)
							return new Pair<Integer, TSala>(-1, null);						
						Date iniAux = p.GetHorario();
						Date finAux = calcularFin(iniAux, peliAux.GetDuracion());
						if (iniAux.before(fin) && finAux.after(ini)) {
							return new Pair<Integer, TSala>(-12, null);
						}
					}
				}
			}

		} catch (Exception e) {
			return new Pair<Integer, TSala>(-1, null);
		}

		return new Pair<Integer, TSala>(0, sala);

	}

	private Date calcularFin(Date ini, int duracion) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(ini);
		cal.add(Calendar.MINUTE, duracion);
		return cal.getTime();
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
			DAOPase dao = daof.getDAOPase();

			// comprobamos que existe el pase
			TPase pase = dao.Read(id);
			if (pase == null) {
				t.rollback();
				return -2;
			}
			else if (pase.GetID() < 0) {
				t.rollback();
				return -1;
			}

			// comprobamos que el pase estaba activo
			if (!pase.GetActivo()) {
				t.rollback();
				return -8;
			}

			Integer exito = dao.Delete(id);
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

	public Integer Modificar(TPase pase) {

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
			DAOPase daoPase = daof.getDAOPase();
			DAOSala daoSala = daof.getDAOSala();
			DAOPelicula daoPelicula = daof.getDAOPelicula();
			DAOCompra daoCompra = daof.getDAOCompra();

			TPase paseAnt = daoPase.Read(pase.GetID());

			// comprobamos que existe el pase
			if (paseAnt == null) {
				t.rollback();
				return -2;
			}
			else if (paseAnt.GetID() < 0) {
				t.rollback();
				return -1;
			}

			// comprobamos que el pase está activo
			if (!paseAnt.GetActivo()) {
				t.rollback();
				return -8;
			}

			// comprobamos que, si se ha modificado, el pase con los atributos
			// <horario, idSala> no existía
			if (!pase.GetHorario().equals(paseAnt.GetHorario()) || pase.GetIDSala() != paseAnt.GetIDSala()) {
				TPase paseExistente = daoPase.ReadByKey(pase.GetIDSala(), pase.GetHorario());
				if (paseExistente != null && paseExistente.GetID() > 0) {
					t.rollback();
					return -9;
				} 
				else if (paseExistente != null) { // id < 0
					t.rollback();
					return -1;
				}				
			}
			
			// si se ha modificado la sala, comprobamos que el pase no se incluía en ninguna compra
			if (!pase.GetIDSala().equals(paseAnt.GetIDSala())) {
				ArrayList<TCompra> compras = daoCompra.ReadAllByPase(pase.GetID());
				if (compras.size() > 0 && compras.get(0).GetID() < 0) {
					t.rollback();
					return -1;
				}
				else if (compras.size() > 0) {
					t.rollback();
					return -13;
				}
			}

			// modificamos
			Pair<Integer, TSala> check = checkAltaMod(pase, daoPase, daoSala, daoPelicula);
			if (check.getFirst() == 0) {
				// si se cambia la sala actualizamos asientos libres
				if (pase.GetIDSala() != paseAnt.GetIDSala()) {
					Integer aforo = check.getSecond().GetAforo();
					pase.SetAsientosLibres(aforo);
				}
				Integer id = daoPase.Update(pase);
				if (id < 0) {
					t.rollback();
					return -1;
				}
				t.commit();
				return id;
			} else {
				t.rollback();
				return check.getFirst();
			}
		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return -1;
		}

	}

	public TPase Mostrar(Integer id) {

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
			DAOPase dao = daof.getDAOPase();

			// comprobamos que existe el pase
			TPase pase = dao.Read(id);
			if (pase == null) {
				t.rollback();
				return new TPase(-2);
			}
			else if (pase.GetID() < 0) {
				t.rollback();
				pase.SetID(-1);
				return pase;
			}

			t.commit();
			return pase;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return new TPase(-1);
		}

	}

	public TPase MostrarParaModificar(Integer id) {

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
			DAOPase dao = daof.getDAOPase();

			// comprobamos que existe el pase
			TPase pase = dao.Read(id);
			if (pase == null) {
				t.rollback();
				return new TPase(-2);
			}
			else if (pase.GetID() < 0) {
				t.rollback();
				pase.SetID(-1);
				return pase;
			}

			// comprobamos que esté activo el pase
			if (!pase.GetActivo()) {
				t.rollback();
				pase.SetID(-8);
				return pase;
			}

			t.commit();
			return pase;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return new TPase(-1);
		}

	}

	public ArrayList<TPase> Listar() {

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
			DAOPase dao = daof.getDAOPase();

			ArrayList<TPase> pases = dao.ReadAll();

			// error con la BD
			if (pases.size() > 0 && pases.get(0).GetID() < 0) {
				t.rollback();
				pases.get(0).SetID(-1);
				return pases;
			}

			t.commit();
			return pases;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			ArrayList<TPase> error = new ArrayList<TPase>();
			error.add(new TPase(-1));
			return error;
		}

	}

	public ArrayList<TPase> ListarPorSala(Integer idSala) {

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
			DAOPase daoPase = daof.getDAOPase();
			DAOSala daoSala = daof.getDAOSala();

			ArrayList<TPase> pasesAux = new ArrayList<TPase>();

			// comprobamos que la sala existe
			TSala sala = daoSala.Read(idSala);
			if (sala == null) {
				t.rollback();
				pasesAux.add(new TPase(-3));
				return pasesAux;
			}
			else if (sala.GetId() < 0) {
				t.rollback();
				pasesAux.add(new TPase(-1));
				return pasesAux;
			}

			// comprobamos que la sala está inactiva
			if (!sala.GetActivo()) {
				t.rollback();
				pasesAux.add(new TPase(-4));
				return pasesAux;
			}

			pasesAux = daoPase.ReadAllBySala(idSala);

			// error con la BD
			if (pasesAux.size() > 0 && pasesAux.get(0).GetID() < 0) {
				t.rollback();
				pasesAux.get(0).SetID(-1);
				return pasesAux;
			}

			// nos quedamos sólo con los activos
			ArrayList<TPase> pases = new ArrayList<TPase>();
			for (TPase aux : pasesAux) {
				if (aux.GetActivo())
					pases.add(aux);
			}

			t.commit();
			return pases;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			ArrayList<TPase> error = new ArrayList<TPase>();
			error.add(new TPase(-1));
			return error;
		}

	}

	public ArrayList<TPase> ListarPorPelicula(Integer idPelicula) {

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
			DAOPase daoPase = daof.getDAOPase();
			DAOPelicula daoPelicula = daof.getDAOPelicula();

			ArrayList<TPase> pasesAux = new ArrayList<TPase>();

			// comprobamos que la película existe
			TPelicula pelicula = daoPelicula.Read(idPelicula);
			if (pelicula == null) {
				t.rollback();
				pasesAux.add(new TPase(-5));
				return pasesAux;
			}
			else if (pelicula.GetID() < 0) {
				t.rollback();
				pasesAux.add(new TPase(-1));
				return pasesAux;
			}

			// comprobamos que la película está inactiva
			if (!pelicula.GetActivo()) {
				t.rollback();
				pasesAux.add(new TPase(-6));
				return pasesAux;
			}

			pasesAux = daoPase.ReadAllByPelicula(idPelicula);

			// error con la BD
			if (pasesAux.size() > 0 && pasesAux.get(0).GetID() < 0) {
				t.rollback();
				pasesAux.get(0).SetID(-1);
				return pasesAux;
			}

			// nos quedamos sólo con los activos
			ArrayList<TPase> pases = new ArrayList<TPase>();
			for (TPase aux : pasesAux) {
				if (aux.GetActivo())
					pases.add(aux);
			}

			t.commit();
			return pases;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			ArrayList<TPase> error = new ArrayList<TPase>();
			error.add(new TPase(-1));
			return error;
		}

	}

	public ArrayList<TPase> ListarPorCompra(Integer idCompra) {

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
			DAOPase daoPase = daof.getDAOPase();
			DAOCompra daoCompra = daof.getDAOCompra();

			ArrayList<TPase> pases = new ArrayList<TPase>();

			// comprobamos que la compra existe
			TCompra compra = daoCompra.Read(idCompra);
			if (compra == null) {
				t.rollback();
				pases.add(new TPase(-7));
				return pases;
			}
			else if (compra.GetID() < 0) {
				t.rollback();
				pases.add(new TPase(-1));
				return pases;
			}

			pases = daoPase.ReadAllByCompra(idCompra);

			// error con la BD
			if (pases.size() > 0 && pases.get(0).GetID() < 0) {
				t.rollback();
				pases.get(0).SetID(-1);
				return pases;
			}

			t.commit();
			return pases;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			ArrayList<TPase> error = new ArrayList<TPase>();
			error.add(new TPase(-1));
			return error;
		}

	}

}