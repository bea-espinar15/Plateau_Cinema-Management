
package Integracion.Compra;

import Negocio.Compra.TCompra;
import Negocio.Compra.TLineaCompra;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;

public class DAOCompraImp implements DAOCompra {

	@Override
	public Integer Create(TCompra compra) {

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
			PreparedStatement statement = c.prepareStatement(
					"INSERT INTO Compra(precioTotal,fecha, idCliente) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, compra.GetPrecioTotal());
			statement.setDate(2, new Date(compra.GetFecha().getTime()));
			statement.setInt(3, compra.GetIDCliente());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			Integer id = -1;
			if (result.next())
				id = result.getInt(1);
			statement.close();
			result.close();
			return id;
		} catch (Exception e) {
			return -4;
		}
	}

	@Override
	public TCompra Read(Integer id) {

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
			PreparedStatement statement = c.prepareStatement("SELECT * FROM Compra WHERE id = ? FOR UPDATE");
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();
			TCompra compra = null;
			if (result.next())
				compra = new TCompra(result.getInt(1), result.getInt(2), new java.util.Date(result.getDate(3).getTime()), result.getInt(4));
			statement.close();
			result.close();
			return compra;

		} catch (Exception e) {
			return new TCompra(-4);
		}

	}

	@Override
	public Integer Update(TCompra compra) {

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
			PreparedStatement statement = c
					.prepareStatement("UPDATE Compra SET precioTotal = ?, fecha = ?, idCliente = ? WHERE id = ?");
			statement.setInt(1, compra.GetPrecioTotal());
			statement.setDate(2, new Date(compra.GetFecha().getTime()));
			statement.setInt(3, compra.GetIDCliente());
			statement.setInt(4, compra.GetID());
			statement.executeUpdate();
			statement.close();
			return 0;
		} catch (Exception e) {
			return -4;
		}
	}

	@Override
	public Integer Delete(Integer id) {

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
			PreparedStatement statement = c.prepareStatement("DELETE FROM Compra WHERE id = ?");
			statement.setInt(1, id);
			statement.executeUpdate();
			statement.close();
			return 0;
		} catch (Exception e) {
			return -4;
		}
	}

	@Override
	public ArrayList<TCompra> ReadAll() {

		ArrayList<TCompra> compras = new ArrayList<>();
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
			PreparedStatement statement = c.prepareStatement("SELECT * FROM Compra FOR UPDATE");
			ResultSet result = statement.executeQuery();
			while (result.next())
				compras.add(new TCompra(result.getInt(1), result.getInt(2),  new java.util.Date(result.getDate(3).getTime()), result.getInt(4)));
			statement.close();
			result.close();	
			return compras;
		} catch (Exception e) {
		//	e.printStackTrace();
			compras = new ArrayList<TCompra>();
			compras.add(new TCompra(-1));
			return compras;
		}
	}

	@Override
	public ArrayList<TCompra> ReadAllByCliente(Integer idCliente) {

		ArrayList<TCompra> compras = new ArrayList<>();
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
			PreparedStatement statement = c.prepareStatement("SELECT * FROM Compra WHERE idCliente = ? FOR UPDATE");
			statement.setInt(1, idCliente);
			ResultSet result = statement.executeQuery();
			while (result.next())
				compras.add(new TCompra(result.getInt(1), result.getInt(2),  new java.util.Date(result.getDate(3).getTime()), result.getInt(4)));
			statement.close();
			result.close();
			return compras;
		} catch (Exception e) {
			//e.printStackTrace();
			compras = new ArrayList<TCompra>();
			compras.add(new TCompra(-4));
			return compras;
		}

	}

	@Override
	public ArrayList<TCompra> ReadAllByPase(Integer idPase) {

		ArrayList<TCompra> compras = new ArrayList<>();
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
			PreparedStatement statement = c.prepareStatement("SELECT Compra.* FROM Compra JOIN LineaCompra ON Compra.id = LineaCompra.idCompra WHERE idPase = ? FOR UPDATE");
			statement.setInt(1, idPase);
			ResultSet result = statement.executeQuery();
			while (result.next()){
				compras.add(new TCompra(result.getInt(1), result.getInt(2), new java.util.Date(result.getDate(3).getTime()), result.getInt(4)));
			}
			statement.close();
			result.close();
			return compras;
		} catch (Exception e) {
			compras = new ArrayList<TCompra>();
			compras.add(new TCompra(-4));
			return compras;
		}

	}
}