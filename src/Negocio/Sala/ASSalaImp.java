
package Negocio.Sala;

import java.util.ArrayList;

import Integracion.Compra.DAOCompra;
import Integracion.DAOFactory.DAOFactory;
import Integracion.Pase.DAOPase;
import Integracion.Sala.DAOSala;
import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;
import Negocio.Compra.TCompra;
import Negocio.Pase.TPase;

/*
 *  CÓDIGOS ERROR
 *  -------------
 *  · -1: Error con la Base de Datos
 *  · -2: Sala activa
 *  · -3: No existe sala
 *  · -4: Sala inactiva
 *  · -5: Nombre repetido
 *  · -6: Aforo pase vendido
 */

public class ASSalaImp implements ASSala {

	public Integer Alta(TSala sala) {

		Transaction t = null;

		try {
			// Obtener y abrir transacción
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Transaction missing");
			t.start();

			// Obtener factoría de DAOs
			DAOFactory daof = DAOFactory.getInstance();
			DAOSala daos = daof.getDAOSala();

			// Comprobar si existe nombre de ese sala

			TSala sala_existente = daos.ReadByNombre(sala.GetNombre());
			if (sala_existente != null && sala_existente.GetId() >= 0) {
				if (sala_existente.GetActivo()) {
					t.rollback();
					return -2;
				} else {
					sala.SetActivo(true);
					sala.SetId(sala_existente.GetId());
					Integer id_r = daos.Update(sala);
					if (id_r < 0) {
						t.rollback();
						return -1;
					}
					t.commit();
					return id_r; // Reactivación
				}
			} else if (sala_existente == null) {
				// Crear sala
				Integer id = daos.Create(sala);
				if (id < 0) {
					t.rollback();
					return -1;
				}
				t.commit();
				return id;
			} else { // el readByNombre ha fallado
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

			// Obtener y abrir transacción
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Transaction missing");
			t.start();

			// Obtener factoría de DAOs
			DAOFactory daof = DAOFactory.getInstance();
			DAOSala daos = daof.getDAOSala();
			DAOPase daop = daof.getDAOPase();

			// Comprobar si existe
			TSala sala = daos.Read(id);
			if (sala == null) {
				t.rollback();
				return -3; // La sala no existe
			} else if (sala.GetId() < 0) {
				t.rollback();
				return -1;
			}

			// Comprobar si está activo
			if (!sala.GetActivo()) {
				t.rollback();
				return -4; // No está activo.
			}

			// Dar de baja a una sala
			Integer id_baja = daos.Delete(id);
			if (id_baja < 0) {
				t.rollback();
				return -1; // no se ha podido dar de baja
			} else {
				ArrayList<TPase> pases = daop.ReadAllBySala(id);
				if (pases.size() > 0 && pases.get(0).GetID() < 0) {
					t.rollback();
					return -1;
				}
				for (TPase aux : pases) {
					Integer i = daop.Delete(aux.GetID());
					if (i < 0) {
						t.rollback();
						return -1;
					}
				}
			}

			t.commit();
			return id_baja;// todo ha ido bien

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return -1;
		}

	}

	public Integer Modificar(TSala sala) {

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
			DAOSala daos = daof.getDAOSala();
			DAOPase daop = daof.getDAOPase();
			DAOCompra daoc = daof.getDAOCompra();

			TSala salaAnt = daos.Read(sala.GetId());
			// comprobamos que existe la sala
			if (salaAnt == null) {
				t.rollback();
				return -3;
			} else if (salaAnt.GetId() < 0) {
				t.rollback();
				return -1;
			}

			// comprobamos que está activa
			if (!salaAnt.GetActivo()) {
				t.rollback();
				return -4;
			}

			// si se ha cambiado el nombre, comprobamos que no coincide con uno
			// existente
			if (!sala.GetNombre().equals(salaAnt.GetNombre())) {
				TSala sala_existente = daos.ReadByNombre(sala.GetNombre());
				if (sala_existente != null && sala_existente.GetId() >= 0) {
					t.rollback();
					return -5;
				} else if (sala_existente != null) { // id < 0
					t.rollback();
					return -1;
				}
			}

			// si se ha cambiado el aforo, comprobamos asientos libres de los pases
			if (sala.GetAforo() != salaAnt.GetAforo()) {
				ArrayList<TPase> pases = daop.ReadAllBySala(sala.GetId());
				if (pases.size() > 0 && pases.get(0).GetID() < 0) {
					t.rollback();
					return -1;
				}
				// para cada pase, comprobar que no se ha comprado ninguna entrada
				for (TPase p : pases) {
					ArrayList<TCompra> compras = daoc.ReadAllByPase(p.GetID());
					if (compras.size() > 0 && compras.get(0).GetID() < 0) {
						t.rollback();
						return -1;
					} else if (compras.size() > 0) {
						t.rollback();
						return -6;
					}
					// actualizamos asientos libres al nuevo aforo
					p.SetAsientosLibres(sala.GetAforo());
					Integer exito = daop.Update(p);
					if (exito < 0) {
						t.rollback();
						return -1;
					}
				}
			}

			// modificamos
			Integer exito = daos.Update(sala);
			if (exito < 0) {
				t.rollback();
				return -1;
			}

			t.commit();
			return sala.GetId();

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return -1;
		}
	}

	public TSala MostrarParaModificar(Integer id) {

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
			DAOSala daos = daof.getDAOSala();

			TSala sala = daos.Read(id);
			// comprobamos que existe
			if (sala == null) {
				t.rollback();
				return new TSalaEstandar(-3);
			} else if (sala.GetId() < 0) {
				t.rollback();
				return new TSalaEstandar(-1);
			}

			// comprobamos que está activa
			if (!sala.GetActivo()) {
				t.rollback();
				return new TSalaEstandar(-4);
			}

			t.commit();
			return sala;

		} catch (Exception e) {
			if (t != null) {
				t.rollback();
			}
			return new TSalaEstandar(-1);
		}

	}

	public TSala Mostrar(Integer id) {

		Transaction t = null;
		try {

			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null) {
				throw new Exception("Error transaccional");
			}
			t.start();

			DAOFactory dao = DAOFactory.getInstance();
			DAOSala daoSala = dao.getDAOSala();

			TSala sala = daoSala.Read(id);
			if (sala == null) {
				t.rollback();
				return new TSalaEstandar(-3);
			} else if (sala.GetId() < 0) {
				t.rollback();
				return new TSalaEstandar(-1);
			}

			t.commit();
			return sala;

		} catch (Exception e) {
			if (t != null) {
				t.rollback();
			}
			return new TSalaEstandar(-1);
		}

	}

	public ArrayList<TSala> Listar() {

		Transaction t = null;
		try {

			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null) {
				throw new Exception("Error transaccional");
			}
			t.start();

			DAOFactory dao = DAOFactory.getInstance();
			DAOSala daoSala = dao.getDAOSala();

			ArrayList<TSala> listaSalas = daoSala.ReadAll();

			if (listaSalas.size() > 0 && listaSalas.get(0).GetId() < 0) {
				t.rollback();
				listaSalas.get(0).SetId(-1);
				return listaSalas;
			}

			t.commit();
			return listaSalas;

		} catch (Exception e) {

			if (t != null)
				t.rollback();
			ArrayList<TSala> listaSalas = new ArrayList<TSala>();
			listaSalas.add(new TSalaEstandar(-1));
			return listaSalas;

		}
	}

	public ArrayList<TSalaEstandar> ListarEstandar() {
		Transaction t = null;
		try {

			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null) {
				throw new Exception("Error transaccional");
			}
			t.start();

			DAOFactory dao = DAOFactory.getInstance();
			DAOSala daoSala = dao.getDAOSala();

			ArrayList<TSalaEstandar> listaSalas = daoSala.ReadAllEstandar();

			if (listaSalas.size() > 0 && listaSalas.get(0).GetId() < 0) {
				t.rollback();
				listaSalas.get(0).SetId(-1);
				return listaSalas;
			}

			t.commit();
			return listaSalas;

		} catch (Exception e) {

			if (t != null)
				t.rollback();
			ArrayList<TSalaEstandar> listaSalas = new ArrayList<TSalaEstandar>();
			listaSalas.add(new TSalaEstandar(-1));
			return listaSalas;

		}
	}

	public ArrayList<TSalaEspecial> ListarEspecial() {
		Transaction t = null;
		try {

			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null) {
				throw new Exception("Error transaccional");
			}
			t.start();

			DAOFactory dao = DAOFactory.getInstance();
			DAOSala daoSala = dao.getDAOSala();

			ArrayList<TSalaEspecial> listaSalas = daoSala.ReadAllEspecial();

			if (listaSalas.size() > 0 && listaSalas.get(0).GetId() < 0) {
				t.rollback();
				listaSalas.get(0).SetId(-1);
				return listaSalas;
			}

			t.commit();
			return listaSalas;

		} catch (Exception e) {

			if (t != null)
				t.rollback();
			ArrayList<TSalaEspecial> listaSalas = new ArrayList<TSalaEspecial>();
			listaSalas.add(new TSalaEspecial(-1));
			return listaSalas;

		}
	}

}