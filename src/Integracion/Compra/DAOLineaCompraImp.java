
package Integracion.Compra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;
import Negocio.Compra.TLineaCompra;

public class DAOLineaCompraImp implements DAOLineaCompra{

	@Override
	public Integer Create(TLineaCompra lineaCompra) {
		
		Transaction t = null;
		
		try {
			
			// obtenemos transacción actual:
			TransactionManager tm = TransactionManager.getInstance();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			
			// obtenemos conexión:
			Connection c = (Connection)t.getResource();
			if (c == null)
				throw new Exception("Error de conexión con la BD");
			
			// creamos la sentencia SQL
			PreparedStatement statement = c.prepareStatement("INSERT INTO LineaCompra (idCompra, idPase, nEntradas, precio) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, lineaCompra.GetIDCompra());
			statement.setInt(2, lineaCompra.GetIDPase());
			statement.setInt(3, lineaCompra.GetNEntradas());
			statement.setInt(4, lineaCompra.GetPrecio());
			int affectedRows = statement.executeUpdate();
			
			statement.close();
			
			if (affectedRows == 0)
				throw new Exception("Error al insertar");
						
			return 0;
			
		}
		catch (Exception e) {
			return -1;
		}

	}

	@Override
	public TLineaCompra Read(Integer idPase, Integer idCompra) {

		Transaction t = null;
		
		try {
			
			// obtenemos transacción actual:
			TransactionManager tm = TransactionManager.getInstance();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			
			// obtenemos conexión:
			Connection c = (Connection)t.getResource();
			if (c == null)
				throw new Exception("Error de conexión con la BD");
			
			// creamos la sentencia SQL
			PreparedStatement statement = c.prepareStatement("SELECT * FROM LineaCompra WHERE idCompra = ? AND idPase = ? FOR UPDATE", Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, idCompra);
			statement.setInt(2, idPase);
			ResultSet result = statement.executeQuery();
			
			TLineaCompra lineaCompra = null;
			
			if (result.next())
				lineaCompra = new TLineaCompra(result.getInt("idCompra"), result.getInt("idPase"), result.getInt("nEntradas"), result.getInt("precio"));
				
			result.close();
			statement.close();			
			return lineaCompra;
			
		}
		catch (Exception e) {
			return new TLineaCompra(-1);
		}
		
	}

	@Override
	public Integer Update(TLineaCompra lineaCompra) {
		
		Transaction t = null;
		
		try {
			
			// obtenemos transacción actual:
			TransactionManager tm = TransactionManager.getInstance();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			
			// obtenemos conexión:
			Connection c = (Connection)t.getResource();
			if (c == null)
				throw new Exception("Error de conexión con la BD");
			
			// creamos la sentencia SQL
			PreparedStatement statement = c.prepareStatement("UPDATE LineaCompra SET nEntradas = ?, precio = ? WHERE idCompra = ? AND idPase = ?", Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, lineaCompra.GetNEntradas());
			statement.setInt(2, lineaCompra.GetPrecio());
			statement.setInt(3, lineaCompra.GetIDCompra());
			statement.setInt(4, lineaCompra.GetIDPase());
			int affectedRows = statement.executeUpdate();
			
			statement.close();
			
			if (affectedRows == 0)
				throw new Exception("Error al modificar");
						
			return 0;
			
		}
		catch (Exception e) {
			return -1;
		}

	}

	// Nunca se llama a este método (implementado para tener CRUD)
	@Override
	public Integer Delete(Integer idCompra, Integer idPase) {
		
		Transaction t = null;
		
		try {
			
			// obtenemos transacción actual:
			TransactionManager tm = TransactionManager.getInstance();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			
			// obtenemos conexión:
			Connection c = (Connection)t.getResource();
			if (c == null)
				throw new Exception("Error de conexión con la BD");
			
			// creamos la sentencia SQL
			PreparedStatement statement = c.prepareStatement("DELETE FROM LineaCompra WHERE idCompra = ? AND idPase = ?", Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, idCompra);
			statement.setInt(2, idPase);
			int affectedRows = statement.executeUpdate();
			
			statement.close();
			
			if (affectedRows == 0)
				throw new Exception("Error al modificar");
						
			return 0;
			
		}
		catch (Exception e) {
			return -1;
		}

	}

	// Nunca se llama a este método (implementado para tener CRUD) 
	@Override
	public ArrayList<TLineaCompra> ReadAll() {

		Transaction t = null;
		
		try {
			
			// obtenemos transacción actual:
			TransactionManager tm = TransactionManager.getInstance();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");
			
			// obtenemos conexión:
			Connection c = (Connection)t.getResource();
			if (c == null)
				throw new Exception("Error de conexión con la BD");
			
			// creamos la sentencia SQL
			PreparedStatement statement = c.prepareStatement("SELECT * FROM LineaCompra FOR UPDATE", Statement.RETURN_GENERATED_KEYS);
			ResultSet result = statement.executeQuery();
			
			ArrayList<TLineaCompra> lineasCompra = new ArrayList<TLineaCompra>();
			
			while (result.next()) {
				Integer idC = result.getInt("idCompra");
				Integer idP = result.getInt("idPase");
				Integer nE = result.getInt("nEntradas");
				Integer pre = result.getInt("precio");
				lineasCompra.add(new TLineaCompra(idC, idP, nE, pre));
			}

			result.close();
			statement.close();
			return lineasCompra;
			
		}
		catch (Exception e) {
			ArrayList<TLineaCompra> error = new ArrayList<TLineaCompra>();
			error.add(new TLineaCompra(-1));
			return error;
		}
	}

	@Override
	public ArrayList<TLineaCompra> ReadAllByCompra(Integer idCompra) {
		ArrayList<TLineaCompra> lineaCompras = new ArrayList<>();
		Transaction t = null;

		try {
			
			// obtenemos transacción actual:
			TransactionManager tm = TransactionManager.getInstance();
			t = tm.getTransaction();
			if (t == null)
				throw new Exception("Error transaccional");

			// obtenemos conexión:
			Connection c = (Connection) t.getResource();
			if (c == null)
				throw new Exception("Error de conexión con la BD");

			// creamos la sentencia SQL
			PreparedStatement statement = c.prepareStatement("SELECT * FROM LineaCompra WHERE idCompra = ? FOR UPDATE");
			statement.setInt(1, idCompra);
			ResultSet result = statement.executeQuery();
			while (result.next()){
				lineaCompras.add(new TLineaCompra(result.getInt("idCompra"), result.getInt("idPase"), result.getInt("nEntradas"), result.getInt("precio")));
			}
			statement.close();
			result.close();
			return lineaCompras;
		} catch (Exception e) {
			lineaCompras = new ArrayList<TLineaCompra>();
			lineaCompras.add(new TLineaCompra(-4));
			return lineaCompras;
		}
	}
}
