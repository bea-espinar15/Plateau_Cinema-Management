package Integracion.Pase;

import Negocio.Pase.TPase;

import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DAOPaseImp implements DAOPase {

	// Operaciones DAO
	final String CREATE = "INSERT INTO pase(horario, precioActual, asientosLibres, idSala, idPelicula, activo) VALUES (?,?,?,?,?,?)";
	final String UPDATE = "UPDATE pase SET horario = ?, precioActual = ?, asientosLibres = ?, idPelicula = ?, idSala = ?, activo = ? WHERE id = ?";
	final String DELETE = "UPDATE pase SET activo = false WHERE id = ?";
	final String GETALL = "SELECT * FROM pase FOR UPDATE";
	final String GETALLBYSALA = "SELECT * FROM pase WHERE idSala = ? FOR UPDATE";
	final String GETALLBYPELICULA = "SELECT * FROM pase WHERE idPelicula = ? FOR UPDATE";
	final String GETALLBYCOMPRA = "SELECT pase.* FROM pase JOIN lineacompra ON pase.id = lineacompra.idPase WHERE lineacompra.idCompra = ? FOR UPDATE";
	final String READ = "SELECT * FROM pase WHERE id = ? FOR UPDATE";
	final String READBYKEY = "SELECT * FROM pase WHERE idSala = ? AND horario = ? FOR UPDATE";

	public Integer Create(TPase pase) {
		
		PreparedStatement stmt = null;
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
			stmt = c.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, dateToTimestampString(pase.GetHorario()));
			stmt.setInt(2, pase.GetPrecioActual());
			stmt.setInt(3, pase.GetAsientosLibres());
			stmt.setInt(4, pase.GetIDSala());
			stmt.setInt(5, pase.GetIDPelicula());
			stmt.setBoolean(6, true);

			// Devuelve -1 si no se ha guardado el cliente
			if (stmt.executeUpdate() == 0)
				return -1;

			ResultSet rs = stmt.getGeneratedKeys();
			int id = -1;
			if (rs.next()) {
				id = (rs.getInt(1));
				rs.close();
			}
			return id;
		} catch (Exception e) {
			return -1;
		} finally {
			close(stmt);
		}
	}

	public TPase Read(Integer id) {
		
		PreparedStatement stmt = null;
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
			stmt = c.prepareStatement(READ);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				TPase pase = new TPase();
				pase.SetID(rs.getInt("id"));
				pase.SetActivo(rs.getBoolean("activo"));
				pase.SetHorario(stringToDate(rs.getString("horario")));
				pase.SetPrecioActual(rs.getInt("precioActual"));
				pase.SetIDSala(rs.getInt("idSala"));
				pase.SetIDPelicula(rs.getInt("idPelicula"));
				pase.SetAsientosLibres(rs.getInt("asientosLibres"));
				return pase;
			} else {// el pase no existe
				return null;
			}
		} catch (Exception e) {
			return new TPase(-1);
		} finally {
			close(stmt);
		}
	}

	public Integer Update(TPase pase) {
		
		PreparedStatement stmt = null;
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
			stmt = c.prepareStatement(UPDATE);
			stmt.setString(1, dateToTimestampString(pase.GetHorario()));
			stmt.setInt(2, pase.GetPrecioActual());
			stmt.setInt(3, pase.GetAsientosLibres());
			stmt.setInt(4, pase.GetIDPelicula());
			stmt.setInt(5, pase.GetIDSala());
			stmt.setBoolean(6, pase.GetActivo());
			stmt.setInt(7, pase.GetID());
			// Devuelve un número positivo si la operacion ha tenido exito
			if (stmt.executeUpdate() == 0)
				return -1;
			return pase.GetID();
		} catch (Exception e) {
			return -1;
		} finally {
			close(stmt);
		}
	}

	public Integer Delete(Integer id) {
		
		PreparedStatement stmt = null;
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
			stmt = c.prepareStatement(DELETE);
			stmt.setInt(1, id);
			// Devuelve un número positivo si la operacion ha tenido exito
			int exito = 1;
			if (stmt.executeUpdate() == 0)
				exito = -1;
			return exito;
		} catch (Exception e) {
			return -1;
		} finally {
			close(stmt);
		}
	}

	public ArrayList<TPase> ReadAll() {
		
		ArrayList<TPase> list = new ArrayList<TPase>();
		Transaction t = null;
		PreparedStatement stmt = null;
		
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
			stmt = c.prepareStatement(GETALL);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				TPase pase = new TPase();
				pase.SetID(rs.getInt("id"));
				pase.SetHorario(stringToDate(rs.getString("horario")));
				pase.SetActivo(rs.getBoolean("activo"));
				pase.SetPrecioActual(rs.getInt("precioActual"));
				pase.SetIDSala(rs.getInt("idSala"));
				pase.SetIDPelicula(rs.getInt("idPelicula"));
				pase.SetAsientosLibres(rs.getInt("asientosLibres"));
				list.add(pase);
			}
		} catch (Exception e) {
			list = new ArrayList<TPase>();
			list.add(new TPase(-1));
			return list;
		} finally {
			close(stmt);
		}
		return list;
	}

	public ArrayList<TPase> ReadAllBySala(Integer id_sala) {

		ArrayList<TPase> list = new ArrayList<TPase>();
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
			PreparedStatement stmt = c.prepareStatement(GETALLBYSALA);
			stmt.setInt(1, id_sala);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				TPase pase = new TPase();
				pase.SetID(rs.getInt("id"));
				pase.SetHorario(stringToDate(rs.getString("horario")));
				pase.SetActivo(rs.getBoolean("activo"));
				pase.SetPrecioActual(rs.getInt("precioActual"));
				pase.SetIDSala(rs.getInt("idSala"));
				pase.SetIDPelicula(rs.getInt("idPelicula"));
				pase.SetAsientosLibres(rs.getInt("asientosLibres"));
				list.add(pase);
			}
			stmt.close();
			rs.close();
			return list;
		} catch (Exception e) {
			list = new ArrayList<TPase>();
			list.add(new TPase(-1));
			return list;
		} 
	}

	public ArrayList<TPase> ReadAllByPelicula(Integer id_pelicula) {

		ArrayList<TPase> list = new ArrayList<TPase>();
		Transaction t = null;
		PreparedStatement stmt = null;
		
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
			stmt = c.prepareStatement(GETALLBYPELICULA);
			stmt.setInt(1, id_pelicula);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				TPase pase = new TPase();
				pase.SetID(rs.getInt("id"));
				pase.SetHorario(stringToDate(rs.getString("horario")));
				pase.SetActivo(rs.getBoolean("activo"));
				pase.SetPrecioActual(rs.getInt("precioActual"));
				pase.SetIDSala(rs.getInt("idSala"));
				pase.SetIDPelicula(rs.getInt("idPelicula"));
				pase.SetAsientosLibres(rs.getInt("asientosLibres"));
				list.add(pase);
			}
		} catch (Exception e) {
			list = new ArrayList<TPase>();
			list.add(new TPase(-1));
			return list;
		} finally {
			close(stmt);
		}
		return list;
	}

	public ArrayList<TPase> ReadAllByCompra(Integer id_compra) {

		ArrayList<TPase> list = new ArrayList<TPase>();
		Transaction t = null;
		PreparedStatement stmt = null;
		
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
			stmt = c.prepareStatement(GETALLBYCOMPRA);
			stmt.setInt(1, id_compra);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				TPase pase = new TPase();
				pase.SetID(rs.getInt("id"));
				pase.SetHorario(stringToDate(rs.getString("horario")));
				pase.SetActivo(rs.getBoolean("activo"));
				pase.SetPrecioActual(rs.getInt("precioActual"));
				pase.SetIDSala(rs.getInt("idSala"));
				pase.SetIDPelicula(rs.getInt("idPelicula"));
				pase.SetAsientosLibres(rs.getInt("asientosLibres"));
				list.add(pase);
			}
		} catch (Exception e) {
			list = new ArrayList<TPase>();
			list.add(new TPase(-1));
			return list;
		} finally {
			close(stmt);
		}
		return list;
	}

	@Override
	public TPase ReadByKey(Integer id_sala, Date horario) {

		PreparedStatement stmt = null;
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
			stmt = c.prepareStatement(READBYKEY);
			stmt.setInt(1, id_sala);
			stmt.setString(2, dateToTimestampString(horario));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				TPase pase = new TPase();
				pase.SetIDSala(rs.getInt("idSala"));
				pase.SetHorario(stringToDate(rs.getString("horario")));
				pase.SetID(rs.getInt("id"));
				pase.SetActivo(rs.getBoolean("activo"));
				pase.SetPrecioActual(rs.getInt("precioActual"));
				pase.SetIDPelicula(rs.getInt("idPelicula"));
				pase.SetAsientosLibres(rs.getInt("asientosLibres"));
				return pase;
			} else {
				return null;
			}
		} catch (Exception e) {
			return new TPase(-1);
		} finally {
			close(stmt);
		}
	}

	// Cerrar statement
	private static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	// Cambiar formato de java.util.Date a java.sql.Timestamp
	private String dateToTimestampString(Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		return format.format(date.getTime());
	}
	
	// Cambiar formato de String a java.util.Date
	private Date stringToDate(String date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		try {
			return format.parse(date);
		} catch (ParseException e) {
			return new Date();
		}
	}
}