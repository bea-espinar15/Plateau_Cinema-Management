package Negocio.Cliente;

import java.util.ArrayList;

import Integracion.Cliente.DAOCliente;
import Integracion.DAOFactory.DAOFactory;
import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;

/*	Códigos de error:
 * 	-2 : El cliente no existe
 *  -3 : El cliente activo ya existe
 *  -4 : Hubo un error con la base de datos (No se puede acceder a la conexion, error SQL)
 *  -5 : El cliente no está activo
 *  -6 : Ya existe un cliente con el DNI indicado
 *  -7: No se ha seleccionado ningún tipo de Cliente
 * */

public class ASClienteImp implements ASCliente {
	
	@SuppressWarnings("unused")
	public Integer Alta(TCliente cliente) {
		
		Transaction t = null;
		
		try{
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if(t == null)
				throw new Exception("Transacción nula");
			t.start();
			
			DAOFactory daof = DAOFactory.getInstance();
			DAOCliente daoc = daof.getDAOCliente();
			
			// Comprobar si existe el dni del cliente
			
			TCliente cliente_existente = daoc.ReadByDNI(cliente.GetDNI());
			if (cliente_existente != null && cliente_existente.GetID() >= 0){
				if(cliente_existente.GetActivo()){
					t.rollback();
					return -3;
				}else {
					cliente.SetID(cliente_existente.GetID());
					cliente.SetActivo(true);
					
					Integer id;
					if(cliente instanceof TClienteNormal){
						TClienteNormal cliente_normal = (TClienteNormal) cliente;
						cliente_normal.SetFacturacion(((TClienteNormal)cliente_existente).GetFacturacion());
						id = daoc.Update(cliente_normal);
					}else if(cliente instanceof TClienteVip){
						TClienteVip cliente_vip = (TClienteVip) cliente;
						int descuento;
						if(cliente_vip.GetAntiguedad() > 5){
							descuento = 25;
						}else{
							descuento = cliente_vip.GetAntiguedad() * 5;
						}
						cliente_vip.SetDescuento(descuento);
						id = daoc.Update(cliente_vip);
					}else{
						t.rollback();
						return -7;
					}
					
					Integer id_r = daoc.Update(cliente);
					if(id_r < 0){
						t.rollback();
						return -4;
					}
					t.commit();
					return id_r;
				}
			}
			else if (cliente_existente != null) { // id < 0
				t.rollback();
				return -4;
			}
			
			Integer id;
			if(cliente instanceof TClienteNormal){
				TClienteNormal cliente_normal = (TClienteNormal) cliente;
				cliente_normal.SetFacturacion(0);
				id = daoc.Create(cliente_normal);
			}else{
				TClienteVip cliente_vip = (TClienteVip) cliente;
				int descuento;
				if(cliente_vip.GetAntiguedad() > 5){
					descuento = 25;
				}else{
					descuento = cliente_vip.GetAntiguedad() * 5;
				}
				cliente_vip.SetDescuento(descuento);
				id = daoc.Create(cliente_vip);
			}
			
			if(id < 0){
				t.rollback();
				return -4;
			}
			t.commit();
			return id;
		}catch(Exception e){
			if(t != null){ t.rollback();}
			return -4;
		}
	}

	public Integer Baja(Integer id) {
		
		Transaction t = null;
		
		try{
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if(t == null)
				throw new Exception("Transaccion nula");
			t.start();
		
			DAOFactory daof = DAOFactory.getInstance();
			DAOCliente daoc = daof.getDAOCliente();
			
			TCliente cliente = daoc.Read(id);
			if(cliente == null){
				t.rollback();
				return -2;
			}
			else if (cliente.GetID() < 0) {
				t.rollback();
				return -4;
			}
			
			if(!cliente.GetActivo()){
				t.rollback();
				return -5;
			}
			
			Integer id_baja = daoc.Delete(id);
			if(id_baja < 0){
				t.rollback();
				return -4;
			}
			
			t.commit();
			return id_baja;
		}catch(Exception e){
			if(t != null){
				t.rollback();
			}
			return -4;
		}
	}

	public Integer Modificar(TCliente cliente) {
		Transaction t = null;
		
		try{
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if(t == null){
				throw new Exception("Transaccion nula");
			}
			t.start();
			
			DAOFactory daof = DAOFactory.getInstance();
			DAOCliente daoc = daof.getDAOCliente();
			
			cliente.SetActivo(true);
			
			TCliente cl = daoc.ReadByDNI(cliente.GetDNI());
			if((cl != null) && cl.GetID() > 0 && (cl.GetID() != cliente.GetID()) ){
				t.rollback();
				return -6;
			}
			else if (cl != null && cl.GetID() < 0) {
				t.rollback();
				return -4;
			}
			
			if(cliente instanceof TClienteVip){
				int descuento;
				if(((TClienteVip) cliente).GetAntiguedad() > 5)
					descuento = 25;
				else{
					descuento = ((TClienteVip) cliente).GetAntiguedad() * 5;
				}
				((TClienteVip) cliente).SetDescuento(descuento);
			}
			int n = daoc.Update(cliente);
			
			if(n < 0){
				t.rollback();
				return -4;
			}
			
			t.commit();
			
			return n;
			
		}catch(Exception e){
			if(t != null){
				t.rollback();
			}
			return -4;
		}
	}

	public TCliente Mostrar(Integer id) {
		Transaction t = null;
		
		try{
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if(t == null){
				throw new Exception("Transaccion nula");
			}
			t.start();
			
			DAOFactory daof = DAOFactory.getInstance();
			DAOCliente daoc = daof.getDAOCliente();
			
			TCliente cliente = daoc.Read(id);
			
			if(cliente == null){
				t.rollback();
				return new TClienteNormal(-2);
			}
			else if (cliente.GetID() < 0) {
				t.rollback();
				return new TClienteNormal(-4);
			}
			
			t.commit();
			return cliente;
		}catch(Exception e){
			if(t != null){
				t.rollback();
			}
			return new TClienteNormal(-4);
		}
	}
	
	@Override
	public TCliente MostrarParaModificar(Integer id) {
		Transaction t = null;
		
		try{
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if(t == null){
				throw new Exception("Transaccion nula");
			}
			t.start();
			
			DAOFactory daof = DAOFactory.getInstance();
			DAOCliente daoc = daof.getDAOCliente();
			
			TCliente cliente = daoc.Read(id);
			
			if(cliente == null){
				t.rollback();
				return new TClienteNormal(-2);
			}
			else if (cliente.GetID() < 0) {
				t.rollback();
				return new TClienteNormal(-4);
			}
			
			if(!cliente.GetActivo()){
				t.rollback();
				return new TClienteNormal(-5);
			}
			
			t.commit();
			return cliente;
		}catch(Exception e){
			if(t != null){
				t.rollback();
			}
			return new TClienteNormal(-4);
		}
	}

	public ArrayList<TCliente> Listar() {
		Transaction t = null;
		try{
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if(t == null){
				throw new Exception("Transaccion nula");
			}
			t.start();
			
			DAOFactory daof = DAOFactory.getInstance();
			DAOCliente daoc = daof.getDAOCliente();
			
			ArrayList<TCliente> clientes = daoc.ReadAll();
			
			if(clientes.size() > 0 && clientes.get(0).GetID() < 0){
				t.rollback();
				clientes.get(0).SetID(-4);
				return clientes;
			}
			
			t.commit();
			return clientes;
			
		}catch(Exception e){
			if(t != null) t.rollback();
			ArrayList<TCliente> clientes = new ArrayList<TCliente>();
			clientes.add(new TClienteNormal(-4));
			return clientes;
		}
	}

	public ArrayList<TClienteNormal> ListarNormal() {
		Transaction t = null;
		try{
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if(t == null){
				throw new Exception("Transaccion nula");
			}
			t.start();
			
			DAOFactory daof = DAOFactory.getInstance();
			DAOCliente daoc = daof.getDAOCliente();
			
			ArrayList<TClienteNormal> clientes = daoc.readAllNormal();
			
			if(clientes.size() > 0 && clientes.get(0).GetID() < 0){
				t.rollback();
				clientes.get(0).SetID(-4);
				return clientes;
			}
			
			t.commit();
			return clientes;
			
		}catch(Exception e){
			if(t != null) t.rollback();
			ArrayList<TClienteNormal> clientes = new ArrayList<TClienteNormal>();
			clientes.add(new TClienteNormal(-4));
			return clientes;
		}
	}

	public ArrayList<TClienteVip> ListarVIP() {
		Transaction t = null;
		try{
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			t = tm.getTransaction();
			if(t == null){
				throw new Exception("Transaccion nula");
			}
			t.start();
			
			DAOFactory daof = DAOFactory.getInstance();
			DAOCliente daoc = daof.getDAOCliente();
			
			ArrayList<TClienteVip> clientes = daoc.ReadAllVIP();
			
			if(clientes.size() > 0 && clientes.get(0).GetID() < 0){
				t.rollback();
				clientes.get(0).SetID(-4);
				return clientes;
			}
			
			t.commit();
			return clientes;
			
		}catch(Exception e){
			if(t != null) t.rollback();
			ArrayList<TClienteVip> clientes = new ArrayList<TClienteVip>();
			clientes.add(new TClienteVip(-4));
			return clientes;
		}
	}
	
}