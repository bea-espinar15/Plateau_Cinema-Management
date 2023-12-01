
package Integracion.Sala;

import Negocio.Sala.TSala;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;
import Negocio.Sala.TSalaEstandar;
import Negocio.Sala.TSalaEspecial;

public class DAOSalaImp implements DAOSala {

	@SuppressWarnings("resource")
	public Integer Create(TSala sala) {

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
			PreparedStatement s = c.prepareStatement("INSERT INTO Sala (nombre, aforo, activo) VALUES(?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			s.setString(1, sala.GetNombre());
			s.setInt(2, sala.GetAforo());
			s.setBoolean(3, true);
			s.executeUpdate();

			ResultSet result = s.getGeneratedKeys();
			if (result.next()) {
				int id = result.getInt(1);
				// actualizamos tablas especializadas
				if (sala instanceof TSalaEstandar) {
					s = c.prepareStatement("INSERT INTO SalaEstandar (sala, _aseos) VALUES(?,?)",
							Statement.RETURN_GENERATED_KEYS);
					s.setInt(1, id);
					boolean x = false;
					if (((TSalaEstandar) sala).GetAseos())
						x = true;
					s.setBoolean(2, x);
					s.executeUpdate();
					ResultSet rs = s.getGeneratedKeys();
					if (!rs.next()) {
						s.close();
						result.close();
						return id;
					}

					s.close();
					result.close();
					return -1;

				} else {
					s = c.prepareStatement("INSERT INTO SalaEspecial (sala, _3d, _Vo) VALUES(?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
					s.setInt(1, id);
					boolean x = false;
					boolean y = false;
					if (((TSalaEspecial) sala).Get3D())
						x = true;
					if (((TSalaEspecial) sala).GetVO())
						y = true;
					s.setBoolean(2, x);
					s.setBoolean(3, y);
					s.executeUpdate();
					ResultSet rs = s.getGeneratedKeys();
					if (!rs.next()) {
						s.close();
						result.close();
						return id;
					}
					s.close();
					result.close();
					return -1;
				}
			} else {
				s.close();
				result.close();
				return -1;
			}
		} catch (Exception e) {
			return -1;
		}

	}

	public TSala Read(Integer id) {

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
			PreparedStatement statement = c.prepareStatement("SELECT * FROM sala WHERE id = ? FOR UPDATE",
					Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();
			if (result.next()) {

				Boolean activo = result.getBoolean("activo");
				String nombre = result.getString("nombre");
				Integer aforo = result.getInt("aforo");

				// comprobamos si es estándar o especial
				statement = c.prepareStatement(
						"SELECT * FROM sala JOIN salaestandar ON sala.id = salaestandar.sala WHERE sala.id = ? FOR UPDATE",
						Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, id);
				result = statement.executeQuery();

				// estándar
				if (result.next()) {
					Boolean aseos = result.getBoolean("_aseos");
					statement.close();
					result.close();
					return new TSalaEstandar(id, nombre, aforo, activo, aseos);
				}
				// especial
				else {

					statement = c.prepareStatement("SELECT * FROM salaespecial WHERE sala = ? FOR UPDATE",
							Statement.RETURN_GENERATED_KEYS);
					statement.setInt(1, id);
					result = statement.executeQuery();

					if (result.next()) {
						Boolean _3D = result.getBoolean("_3D");
						Boolean _VO = result.getBoolean("_VO");
						statement.close();
						result.close();
						return new TSalaEspecial(id, nombre, aforo, activo, _3D, _VO);
					} else {
						statement.close();
						result.close();
						return new TSalaEstandar(-1);
					}

				}
			} else {
				statement.close();
				result.close();
				return null;
			}

		} catch (Exception e) {
			return new TSalaEstandar(-1);
		}

	}

	public Integer Update(TSala sala) {

		Transaction t = null;
		PreparedStatement s = null;
		PreparedStatement auxS = null;

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
			s = c.prepareStatement("UPDATE Sala SET nombre = ?, aforo = ?, activo = ? WHERE id = ?");
			s.setString(1, sala.GetNombre());
			s.setInt(2, sala.GetAforo());
			s.setBoolean(3, sala.GetActivo());
			s.setInt(4, sala.GetId());
			
			if(s.executeUpdate() != 0){
				// actualizamos tablas especializadas
				if (sala instanceof TSalaEstandar) {
					auxS = c.prepareStatement("UPDATE SalaEstandar SET _aseos = ? WHERE sala = ?");
					auxS.setBoolean(1, ((TSalaEstandar) sala).GetAseos());
					auxS.setInt(2, sala.GetId());

					if (auxS.executeUpdate() == 0) {
						return -1;
					}
					return sala.GetId();
				} else {
					auxS = c.prepareStatement("UPDATE SalaEspecial SET _3D = ?, _VO = ? WHERE sala = ?",
							Statement.RETURN_GENERATED_KEYS);
					auxS.setBoolean(1, ((TSalaEspecial) sala).Get3D());
					auxS.setBoolean(2, ((TSalaEspecial) sala).GetVO());
					auxS.setInt(3, sala.GetId());

					if (auxS.executeUpdate() == 0) {
						return -1;
					}
					return sala.GetId();
				}
			} else {
				return -1;
			}
		} catch (Exception e) {
			return -1;
		} finally {
			close(s);
			close(auxS);
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
			PreparedStatement statement = c.prepareStatement("UPDATE Sala SET ACTIVO = FALSE WHERE id = ?",
					Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, id);
			Integer affectedRows = statement.executeUpdate();

			if (affectedRows == 0) {
				statement.close();
				return -1;
			}

			statement.close();
			return 0;
		} catch (Exception e) {
			return -1;
		}

	}

	public ArrayList<TSala> ReadAll() {

		Transaction t = null;
		PreparedStatement statement = null;
		PreparedStatement statementAux = null;
		PreparedStatement statementAux2 = null;

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
			statement = c.prepareStatement("SELECT * FROM Sala FOR UPDATE",
					Statement.RETURN_GENERATED_KEYS);
			ResultSet result = statement.executeQuery();

			ArrayList<TSala> salas = new ArrayList<TSala>();

			while (result.next()) {

				Integer idSala = result.getInt("id");
				Boolean activo = result.getBoolean("activo");
				String nombre = result.getString("nombre");
				Integer aforo = result.getInt("aforo");

				// comprobamos si es estándar o especial
				statementAux = c.prepareStatement(
						"SELECT * FROM sala JOIN salaestandar ON sala.id = salaestandar.sala WHERE sala.id = ? FOR UPDATE",
						Statement.RETURN_GENERATED_KEYS);
				statementAux.setInt(1, idSala);
				ResultSet resultAux = statementAux.executeQuery();

				// estándar
				if (resultAux.next()) {
					Boolean aseos = resultAux.getBoolean("_aseos");
					salas.add(new TSalaEstandar(idSala, nombre, aforo, activo, aseos));
				}
				// especial
				else {
					statementAux2 = c.prepareStatement("SELECT * FROM salaespecial WHERE sala = ? FOR UPDATE",
							Statement.RETURN_GENERATED_KEYS);
					statementAux2.setInt(1, idSala);
					resultAux = statementAux2.executeQuery();

					if (resultAux.next()) {
						Boolean _3D = resultAux.getBoolean("_3D");
						Boolean _VO = resultAux.getBoolean("_VO");
						salas.add(new TSalaEspecial(idSala, nombre, aforo, activo, _3D, _VO));
					} 
				}
				resultAux.close();
			}

			result.close();

			return salas;

		} catch (Exception e) {
			ArrayList<TSala> error = new ArrayList<TSala>();
			error.add(new TSalaEstandar(-1));
			return error;
		} finally {
			close(statement);
			close(statementAux);
			close(statementAux2);
		}
	}

	public ArrayList<TSalaEstandar> ReadAllEstandar() {

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
			statement = c.prepareStatement("SELECT * FROM SALA JOIN SALAESTANDAR ON id = sala FOR UPDATE",
					Statement.RETURN_GENERATED_KEYS);
			ResultSet result = statement.executeQuery();

			ArrayList<TSalaEstandar> salas = new ArrayList<TSalaEstandar>();

			while (result.next()) {
				Integer idSala = result.getInt("id");
				Boolean activo = result.getBoolean("activo");
				String nombre = result.getString("nombre");
				Integer aforo = result.getInt("aforo");
				Boolean aseos = result.getBoolean("_aseos");
				salas.add(new TSalaEstandar(idSala, nombre, aforo, activo, aseos));
			}
			result.close();
			return salas;
		} catch (Exception e) {
			ArrayList<TSalaEstandar> error = new ArrayList<TSalaEstandar>();
			error.add(new TSalaEstandar(-1));
			return error;
		} finally {
			close(statement);
		}
	}

	public ArrayList<TSalaEspecial> ReadAllEspecial() {

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
			statement = c.prepareStatement("SELECT * FROM SALA JOIN SALAESPECIAL ON id = sala FOR UPDATE",
					Statement.RETURN_GENERATED_KEYS);
			ResultSet result = statement.executeQuery();

			ArrayList<TSalaEspecial> salas = new ArrayList<TSalaEspecial>();

			while (result.next()) {
				Integer idSala = result.getInt("id");
				Boolean activo = result.getBoolean("activo");
				String nombre = result.getString("nombre");
				Integer aforo = result.getInt("aforo");
				Boolean _3D = result.getBoolean("_3D");
				Boolean _VO = result.getBoolean("_VO");
				salas.add(new TSalaEspecial(idSala, nombre, aforo, activo, _3D, _VO));
			}
			result.close();
			return salas;

		} catch (Exception e) {
			ArrayList<TSalaEspecial> error = new ArrayList<TSalaEspecial>();
			error.add(new TSalaEspecial(-1));
			return error;
		} finally {
			close(statement);
		}
	}

	public TSala ReadByNombre(String nombre) {

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
			PreparedStatement statement = c.prepareStatement("SELECT * FROM sala WHERE nombre = ? FOR UPDATE",
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, nombre);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				Integer id = result.getInt("id");
				Boolean activo = result.getBoolean("activo");
				Integer aforo = result.getInt("aforo");

				// comprobamos si es estándar o especial
				statement = c.prepareStatement(
						"SELECT * FROM sala JOIN salaestandar ON sala.id = salaestandar.sala WHERE sala.id = ? FOR UPDATE",
						Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, id);
				result = statement.executeQuery();

				// estándar
				if (result.next()) {
					Boolean aseos = result.getBoolean("_aseos");
					statement.close();
					result.close();
					return new TSalaEstandar(id, nombre, aforo, activo, aseos);
				}
				// especial
				else {
					statement = c.prepareStatement("SELECT * FROM salaespecial WHERE sala = ? FOR UPDATE",
							Statement.RETURN_GENERATED_KEYS);
					statement.setInt(1, id);
					result = statement.executeQuery();

					if (result.next()) {
						Boolean _3D = result.getBoolean("_3D");
						Boolean _VO = result.getBoolean("_VO");
						statement.close();
						result.close();
						return new TSalaEspecial(id, nombre, aforo, activo, _3D, _VO);
					} else {
						statement.close();
						result.close();
						return new TSalaEstandar(-1);
					}
				}
			} else {
				statement.close();
				result.close();
				return null;
			}

		} catch (Exception e) {
			return new TSalaEstandar(-1);
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