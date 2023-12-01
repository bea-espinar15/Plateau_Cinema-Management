
package Integracion.Pelicula;

import Negocio.Pelicula.TPelicula;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;

public class DAOPeliculaImp implements DAOPelicula {

	final String GETALLBYPRODUCTORA = "SELECT * FROM Pelicula NATURAL JOIN Produce WHERE id=? FOR UPDATE";
	final String ANADIRPRODUCTORA = "INSERT INTO PeliculaProductora(idProductora, idPelicula) VALUES (?,?)";
	final String QUITARPRODUCTORA = "DELETE FROM PeliculaProductora(idProductora, idPelicula) WHERE idProductora = ? && idPelicula = ?";
	final String LINKEDLIST = "SELECT * FROM PeliculaProductora(idProductora, idPelicula) WHERE idProductora = ? && idPelicula = ?";

	public Integer Create(TPelicula pelicula) {

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
			PreparedStatement stmt = c.prepareStatement(
					"INSERT INTO Pelicula(titulo, genero, director, duracion, activo) VALUES(?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, pelicula.GetTitulo());
			stmt.setString(2, pelicula.GetGenero());
			stmt.setString(3, pelicula.GetDirector());
			stmt.setInt(4, pelicula.GetDuracion());
			stmt.setBoolean(5, true);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			Integer i = -1;
			if (rs.next()) {
				i = rs.getInt(1);
			}
			stmt.close();
			rs.close();
			return i;
		} catch (Exception e) {
			return -1;
		}
	}

	public TPelicula Read(Integer id) {

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
			stmt = c.prepareStatement("SELECT * FROM Pelicula WHERE id = " + id + " FOR UPDATE");
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				TPelicula pelicula = new TPelicula();
				pelicula.SetID(rs.getInt("id"));
				pelicula.SetActivo(rs.getBoolean("activo"));
				pelicula.SetTitulo(rs.getString("titulo"));
				pelicula.SetDirector(rs.getString("director"));
				pelicula.SetGenero(rs.getString("genero"));
				pelicula.SetDuracion(rs.getInt("duracion"));
				return pelicula;
			}
			else
				return null;
		} catch (Exception e) {
			return new TPelicula(-1);
		} finally {
			close(stmt);
		}
	}

	public Integer Update(TPelicula pelicula) {

		Transaction t = null;
		PreparedStatement statement = null;

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
			statement = c.prepareStatement(
					"" + "UPDATE Pelicula SET titulo = " + "'" + pelicula.GetTitulo() + "', " + "genero = '"
							+ pelicula.GetGenero() + "', director = '" + pelicula.GetDirector() + "', duracion = "
							+ pelicula.GetDuracion() + ", activo = "+pelicula.GetActivo() + " WHERE id = " + pelicula.GetID(),
					Statement.RETURN_GENERATED_KEYS);

			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			if(!rs.next())return pelicula.GetID();
			else return -1;
		
			
		} catch (Exception e) {
			return -1;
		} finally {
			close(statement);
		}
	}

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
			PreparedStatement stmt = c.prepareStatement("UPDATE pelicula SET activo = false WHERE id = ?");
			stmt.setInt(1, id);
			stmt.executeUpdate();
			stmt.close();
			return id;
		} catch (Exception e) {
			return -1;
		}
	}

	public ArrayList<TPelicula> ReadAll() {

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
			PreparedStatement statement = c.prepareStatement("SELECT * FROM Pelicula FOR UPDATE",
					Statement.RETURN_GENERATED_KEYS);
			ResultSet result = statement.executeQuery();

			ArrayList<TPelicula> peliculas = new ArrayList<TPelicula>();

			while (result.next()) {
				Integer id = result.getInt("id");
				Boolean act = result.getBoolean("activo");
				String tit = result.getString("titulo");
				String dir = result.getString("director");
				String gen = result.getString("genero");
				Integer dur = result.getInt("duracion");
				peliculas.add(new TPelicula(id, act, tit, dir, gen, dur));
			}
			statement.close();
			result.close();
			return peliculas;

		} catch (Exception e) {
			ArrayList<TPelicula> error = new ArrayList<TPelicula>();
			error.add(new TPelicula(-1));
			return error;
		}

	}

	public ArrayList<TPelicula> ReadAllByProductora(Integer id_productora) {

		Transaction t = null;
		PreparedStatement statement = null;

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
			statement = c.prepareStatement("SELECT Pelicula.* FROM Pelicula JOIN PeliculaProductora ON id = idPelicula WHERE idProductora = ? FOR UPDATE");
			statement.setInt(1, id_productora);
			ResultSet result = statement.executeQuery();

			ArrayList<TPelicula> peliculas = new ArrayList<TPelicula>();

			while (result.next()) {
				Integer id = result.getInt("id");
				Boolean act = result.getBoolean("activo");
				String tit = result.getString("titulo");
				String dir = result.getString("director");
				String gen = result.getString("genero");
				Integer dur = result.getInt("duracion");
				peliculas.add(new TPelicula(id, act, tit, dir, gen, dur));
			}

			return peliculas;

		} catch (Exception e) {
			ArrayList<TPelicula> error = new ArrayList<TPelicula>();
			error.add(new TPelicula(-1));
			return error;
		} finally {
			close(statement);
		}

	}

	public TPelicula ReadByTitulo(String titulo) {

		Transaction t = null;
		PreparedStatement statement = null;

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
			statement = c.prepareStatement("SELECT * FROM Pelicula WHERE titulo = ? FOR UPDATE");
			statement.setString(1, titulo);
			ResultSet result = statement.executeQuery();

			if (result.next()) {
				Integer id = result.getInt("id");
				Boolean act = result.getBoolean("activo");
				String tit = result.getString("titulo");
				String dir = result.getString("director");
				String gen = result.getString("genero");
				Integer dur = result.getInt("duracion");
				statement.close();
				return new TPelicula(id, act, tit, dir, gen, dur);
			} else
				return null;
		} catch (Exception e) {
			return new TPelicula(-1);
		} finally {
			close(statement);
		}
	}

	public Integer AddProductora(Integer idProductora, Integer idPelicula) {

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
					"INSERT INTO PeliculaProductora (idProductora, idPelicula) VALUES (?,?)",
					Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, idProductora);
			statement.setInt(2, idPelicula);
			int affectedRows = statement.executeUpdate();

			statement.close();

			if (affectedRows == 0)
				throw new Exception("Error al modificar");

			return 0;
		} catch (Exception e) {
			return -1;
		}
	}

	public Integer RemoveProductora(Integer idProductora, Integer idPelicula) {

		Transaction t = null;
		PreparedStatement statement = null;

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
			statement = c.prepareStatement(
					"DELETE FROM PeliculaProductora WHERE idProductora = ? AND idPelicula = ?",
					Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, idProductora);
			statement.setInt(2, idPelicula);
			int affectedRows = statement.executeUpdate();

			if (affectedRows == 0)
				throw new Exception("Error al modificar");

			return 0;

		} catch (Exception e) {
			return -1;
		} finally {
			close(statement);
		}

	}

	// 0 si no están asociadas
	// 1 si están asociadas
	// -1 si error
	public Integer LinkedProductora(Integer idProductora, Integer idPelicula) {

		Transaction t = null;
		PreparedStatement statement = null;

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
			statement = c.prepareStatement(
					"SELECT * FROM PeliculaProductora WHERE idProductora = ? AND idPelicula = ?");
			statement.setInt(1, idProductora);
			statement.setInt(2, idPelicula);
			ResultSet rs = statement.executeQuery();

			if (rs.next())
				return 1;
			else
				return 0;
		} catch (Exception e) {
			return -1;
		} finally {
			close(statement);
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
}