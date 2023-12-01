
package Negocio.Compra;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import Integracion.Cliente.DAOCliente;
import Integracion.Compra.DAOCompra;
import Integracion.Compra.DAOLineaCompra;
import Integracion.DAOFactory.DAOFactory;
import Integracion.Pase.DAOPase;
import Integracion.Pelicula.DAOPelicula;
import Integracion.QueryFactory.Query;
import Integracion.QueryFactory.QueryFactory;
import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;
import Negocio.Cliente.TCliente;
import Negocio.Cliente.TClienteNormal;
import Negocio.Cliente.TClienteVip;
import Negocio.Pase.TPase;
import Negocio.Pelicula.TPelicula;

/*
 *	CÓDIGOS DE ERROR 
 *  ----------------
 *  · -1: Error con la Base de Datos
 *  · -2: No existe pase
 *  · -3: Pase inactivo
 *  · -4: No existe compra
 *  · -5: El pase no está incluido en la compra
 *  · -6: No existe cliente
 *  · -7: Cliente inactivo
 *  · -8: Compra vacía
 *  · -9: Pase anterior a fecha actual
 *  · -10: No hay suficientes asientos libres
 *  · -11: No existe película
 *  · -12: Película inactiva
 */

public class ASCompraImp implements ASCompra {

	public Integer Cerrar(TCompra compra, HashMap<Integer, TLineaCompra> compraPases) {

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
			DAOCompra daoCompra = daof.getDAOCompra();
			DAOPase daoPase = daof.getDAOPase();
			DAOCliente daoCliente = daof.getDAOCliente();
			DAOLineaCompra daoLC = daof.getDAOLineaCompra();

			Integer idCliente = compra.GetIDCliente();
			TCliente cliente = daoCliente.Read(idCliente);

			// comprobamos que existe el cliente
			if (cliente == null) {
				t.rollback();
				return -6;
			}
			else if (cliente.GetID() < 0) {
				t.rollback();
				return -1;
			}

			// comprobamos que el cliente está activo
			if (cliente.GetActivo() == false) {
				t.rollback();
				return -7;
			}

			// creamos array
			ArrayList<TLineaCompra> lineaCompra = new ArrayList<TLineaCompra>();
			for (Integer i : compraPases.keySet()) {
				lineaCompra.add(compraPases.get(i));
			}

			// comprobamos que la compra no está vacía
			if (lineaCompra.size() == 0) {
				t.rollback();
				return -8;
			}

			Integer precioTotal = 0;
			Date fechaActual = new Date();

			// Validación de pases, calcular el precioTotal para la creación de
			// la compra.

			for (TLineaCompra aux : lineaCompra) {

				TPase pase = daoPase.Read(aux.GetIDPase());

				// comprobamos que existe el pase
				if (pase == null) {
					t.rollback();
					return -2;
				}
				else if (pase.GetID() < 0) {
					t.rollback();
					return -1;
				}

				// comprobamos que está activo
				if (pase.GetActivo() == false) {
					t.rollback();
					return -3;
				}

				// comprobamos que el pase se emite después de la fecha actual
				if (pase.GetHorario().before(fechaActual)) {
					t.rollback();
					return -9;
				}

				// comprobamos que hay suficientes asientos
				if (aux.GetNEntradas() > pase.GetAsientosLibres()) {
					t.rollback();
					return -10;
				}

				// ponemos precio a la lineaCompra
				aux.SetPrecio(pase.GetPrecioActual());

				// acumulamos precio total
				precioTotal += aux.GetPrecio() * aux.GetNEntradas();

				// actualizamos asientos libres del pase
				pase.SetAsientosLibres(pase.GetAsientosLibres() - aux.GetNEntradas());
				Integer exito = daoPase.Update(pase);
				if (exito < 0) {
					t.rollback();
					return -1;
				}

			}

			// si el cliente es VIP aplicamos descuento
			if (cliente instanceof TClienteVip) {
				Integer dcto = 100 - ((TClienteVip) cliente).GetDescuento();
				precioTotal = precioTotal * dcto / 100;
			}
			
			// creamos la compra
			compra.SetFecha(fechaActual);
			compra.SetPrecioTotal(precioTotal);
			Integer exito = daoCompra.Create(compra);
			Integer idCompra = exito;
			if (exito < 0) {
				t.rollback();
				return -1;
			}

			for (TLineaCompra aux : lineaCompra) {

				aux.SetIDCompra(exito);
				// creamos la lineaCompra
				Integer exitoLC = daoLC.Create(aux);
				if (exitoLC < 0) {
					t.rollback();
					return -1;
				}
			}

			// si el cliente es normal actualizamos facturacion
			if (cliente instanceof TClienteNormal) {
				((TClienteNormal) cliente).SetFacturacion(((TClienteNormal) cliente).GetFacturacion() + precioTotal);
				exito = daoCliente.Update(cliente);
				if (exito < 0) {
					t.rollback();
					return -1;
				}
			}

			t.commit();
			return idCompra;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return -1;
		}
	}

	public TCompra Mostrar(Integer id) {

		Transaction t = null;

		try {
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			t.start();

			DAOFactory daof = DAOFactory.getInstance();
			DAOCompra dao = daof.getDAOCompra();

			TCompra compra = dao.Read(id);
			if (compra == null) {
				t.rollback();
				return new TCompra(-4);
			}
			else if (compra.GetID() < 0) {
				t.rollback();
				compra.SetID(-1);
				return compra;
			}

			t.commit();
			return compra;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return new TCompra(-1);
		}

	}

	public ArrayList<TCompra> Listar() {

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
			DAOCompra dao = daof.getDAOCompra();

			ArrayList<TCompra> compras = dao.ReadAll();

			// error con la BD
			if (compras.size() > 0 && compras.get(0).GetID() < 0) {
				t.rollback();
				compras.get(0).SetID(-1);
				return compras;
			}

			t.commit();
			return compras;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			ArrayList<TCompra> error = new ArrayList<TCompra>();
			error.add(new TCompra(-1));
			return error;
		}

	}

	public TCompraEnDetalle MostrarEnDetalle(Integer id) {

		Transaction t = null;

		try {
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();

			if (t == null)
				throw new Exception("Error Transaccional");
			t.start();

			DAOFactory daof = DAOFactory.getInstance();
			DAOCompra daoCompra = daof.getDAOCompra();
			DAOCliente daoCliente = daof.getDAOCliente();
			DAOPase daoPase = daof.getDAOPase();

			TCompraEnDetalle compraDetallada = new TCompraEnDetalle();
			compraDetallada.SetCompra(new TCompra(id));

			TCompra compra = daoCompra.Read(id);
			if (compra == null) {
				t.rollback();
				compraDetallada.GetCompra().SetID(-4);
				return compraDetallada;
			}
			else if (compra.GetID() < 0) {
				t.rollback();
				compraDetallada.GetCompra().SetID(-1);
				return compraDetallada;
			}

			compraDetallada.SetCompra(compra);

			TCliente cliente = daoCliente.Read(compra.GetIDCliente());
			if (cliente == null) {
				t.rollback();
				compraDetallada.GetCompra().SetID(-6);
				return compraDetallada;
			}
			else if (cliente.GetID() < 0) {
				t.rollback();
				compraDetallada.GetCompra().SetID(-1);
				return compraDetallada;
			}
			compraDetallada.SetCliente(cliente);

			ArrayList<TPase> pases = daoPase.ReadAllByCompra(id);
			if (pases.size() > 0 && pases.get(0).GetID() < 0) {
				t.rollback();
				compraDetallada.GetCompra().SetID(-1);
				return compraDetallada;
			}
			compraDetallada.SetPase(pases);

			t.commit();
			return compraDetallada;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			TCompraEnDetalle error = new TCompraEnDetalle();
			error.SetCompra(new TCompra(-1));
			return error;
		}
	}

	public ArrayList<TCompra> ListarPorCliente(Integer idCliente) {

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
			DAOCompra daoCompra = daof.getDAOCompra();
			DAOCliente daoCliente = daof.getDAOCliente();

			ArrayList<TCompra> compras = new ArrayList<>();

			TCliente cliente = daoCliente.Read(idCliente);

			// comprobamos que existe el cliente
			if (cliente == null) {
				t.rollback();
				compras.add(new TCompra(-6));
				return compras;
			}
			else if (cliente.GetID() < 0) {
				t.rollback();
				compras.add(new TCompra(-1));
				return compras;
			}

			// comprobamos que el cl
			if (cliente.GetActivo() == false) {
				t.rollback();
				compras.add(new TCompra(-7));
				return compras;
			}

			compras = daoCompra.ReadAllByCliente(idCliente);
			// error con la BD
			if (compras.size() > 0 && compras.get(0).GetID() < 0) {
				t.rollback();
				compras.get(0).SetID(-1);
			}

			t.commit();
			return compras;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			ArrayList<TCompra> compras = new ArrayList<>();
			compras.add(new TCompra(-1));
			return compras;
		}
	}

	public ArrayList<TCompra> ListarPorPase(Integer idPase) {

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
			DAOCompra daoCompra = daof.getDAOCompra();
			DAOPase daoPase = daof.getDAOPase();

			ArrayList<TCompra> compras = new ArrayList<TCompra>();

			TPase pase = daoPase.Read(idPase);
			// no existe pase
			if (pase == null) {
				t.rollback();
				compras.add(new TCompra(-2));
				return compras;
			}
			else if (pase.GetID() < 0) {
				t.rollback();
				compras.add(new TCompra(-1));
				return compras;
			}
			// pase inactivo
			if (pase.GetActivo() == false) {
				t.rollback();
				compras.add(new TCompra(-3));
				return compras;
			}

			compras = daoCompra.ReadAllByPase(idPase);

			// error con la BD
			if (compras.size() > 0 && compras.get(0).GetID() < 0) {
				t.rollback();
				compras.get(0).SetID(-1);
				return compras;
			}

			t.commit();
			return compras;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			ArrayList<TCompra> error = new ArrayList<TCompra>();
			error.add(new TCompra(-1));
			return error;
		}

	}

	public Integer CalcularTotalComprasPorPelicula(Integer idPelicula) {

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

			TPelicula pelicula = daoPelicula.Read(idPelicula);
			// comprobamos que la película existe
			if (pelicula == null) {
				t.rollback();
				return -11;
			}
			else if (pelicula.GetID() < 0) {
				t.rollback();
				return -1;
			}

			// comprobamos que la película está activa
			if (!pelicula.GetActivo()) {
				t.rollback();
				return -12;
			}

			QueryFactory queryF = QueryFactory.getInstance();
			Query q = queryF.generateQuery(1);
			Integer numCompras = (Integer) q.execute(idPelicula);
			if (numCompras < 0) {
				t.rollback();
				return -1;
			}

			t.commit();
			return numCompras;

		} catch (Exception e) {
			if (t != null)
				t.rollback();
			return -1;
		}

	}

	public Integer DevolverCompra(Integer idCompra, Integer idPase, Integer entradasDevueltas) {

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
			DAOCompra daoCompra = daof.getDAOCompra();
			DAOPase daoPase = daof.getDAOPase();
			DAOCliente daoCliente = daof.getDAOCliente();
			DAOLineaCompra daoLC = daof.getDAOLineaCompra();

			// comprobamos que existe la compra
			TCompra compra = daoCompra.Read(idCompra);
			if (compra == null) {
				t.rollback();
				return -4;
			}
			else if (compra.GetID() < 0) {
				t.rollback();
				return -1;
			}

			// comprobamos que existe el pase
			TPase pase = daoPase.Read(idPase);
			if (pase == null) {
				t.rollback();
				return -2;
			}
			else if (pase.GetID() < 0) {
				t.rollback();
				return -1;
			}

			// comprobamos que el pase estaba incluido en la compra
			TLineaCompra lineaCompra = daoLC.Read(idPase, idCompra);
			if (lineaCompra == null) {
				t.rollback();
				return -5;
			}
			// error con la BD
			else if (lineaCompra.GetIDCompra() < 0) {
				t.rollback();
				return -1;
			}

			// devolvemos las entradas
			Integer entradasDevueltasReal = minimo(lineaCompra.GetNEntradas(), entradasDevueltas);
			lineaCompra.SetNEntradas(lineaCompra.GetNEntradas() - entradasDevueltasReal);

			// actualizamos el precio de la compra
			Integer precioAnt = compra.GetPrecioTotal();
			Integer nuevoPrecio = precioAnt - (entradasDevueltasReal * lineaCompra.GetPrecio());
			if (nuevoPrecio < 0) // puede quedarse en negativo por el descuento VIP
				nuevoPrecio = 0;
			compra.SetPrecioTotal(nuevoPrecio);
			

			// actualizamos los asientos libres del pase
			Integer asientos = pase.GetAsientosLibres() + entradasDevueltasReal;
			pase.SetAsientosLibres(asientos);

			// si el cliente es normal, actualizamos su facturación
			TCliente cliente = daoCliente.Read(compra.GetIDCliente());
			if (cliente == null || cliente != null && cliente.GetID() < 0) {
				t.rollback();
				return -1;
			}
			if (cliente instanceof TClienteNormal) {
				Integer facturacion = ((TClienteNormal) cliente).GetFacturacion() - precioAnt;
				facturacion += compra.GetPrecioTotal();
				((TClienteNormal) cliente).SetFacturacion(facturacion);
			}

			// actualizamos
			Integer exito = daoCompra.Update(compra);
			if (exito < 0) {
				t.rollback();
				return -1;
			}
			exito = daoCliente.Update(cliente);
			if (exito < 0) {
				t.rollback();
				return -1;
			}
			exito = daoPase.Update(pase);
			if (exito < 0) {
				t.rollback();
				return -1;
			}
			exito = daoLC.Update(lineaCompra);
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

	private Integer minimo(Integer n, Integer m) {
		if (n < m)
			return n;
		else
			return m;
	}

	@Override
	public ArrayList<TLineaCompra> VerResumenDeCompra(Integer idCompra) {

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
			DAOCompra daoCompra = daof.getDAOCompra();
			DAOLineaCompra daoLC = daof.getDAOLineaCompra();
			
			ArrayList<TLineaCompra> lineasCompra = new ArrayList<TLineaCompra>();
			
			// comprobamos que existe la compra
			TCompra compra = daoCompra.Read(idCompra);
			if (compra == null) {
				t.rollback();
				lineasCompra.add(new TLineaCompra(-4));
				return lineasCompra;
			}
			else if (compra.GetID() < 0) {
				t.rollback();
				lineasCompra.add(new TLineaCompra(-1));
				return lineasCompra;
			}
			
			lineasCompra = daoLC.ReadAllByCompra(idCompra);
			// error con la BD
			if (lineasCompra.size() > 0 && lineasCompra.get(0).GetIDCompra() < 0) {
				t.rollback();
				lineasCompra.get(0).SetIDCompra(-1);
				return lineasCompra;
			}
			
			t.commit();
			return lineasCompra;
			
		}
		catch (Exception e) {
			if (t != null)
				t.rollback();
			ArrayList<TLineaCompra> error = new ArrayList<TLineaCompra>();
			error.add(new TLineaCompra(-1));
			return error;
		}
		
	}

}