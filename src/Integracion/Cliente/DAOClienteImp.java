package Integracion.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;
import Negocio.Cliente.TCliente;
import Negocio.Cliente.TClienteNormal;
import Negocio.Cliente.TClienteVip;

public class DAOClienteImp implements DAOCliente {

	@SuppressWarnings("resource")
	@Override
	public Integer Create(TCliente cliente) {

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
			PreparedStatement statement = c.prepareStatement("INSERT INTO Cliente (dni, nombre, correo) VALUES(?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, cliente.GetDNI());
			statement.setString(2, cliente.GetNombre());
			statement.setString(3, cliente.GetCorreo());
			statement.executeUpdate();

			ResultSet result = statement.getGeneratedKeys();
			if (result.next()) {
				int id = result.getInt(1);

				if (cliente instanceof TClienteNormal) {
					statement = c.prepareStatement("INSERT INTO ClienteNormal (cliente, facturacion) VALUES(?,?)");
					statement.setInt(1, id);
					statement.setInt(2, ((TClienteNormal) cliente).GetFacturacion());

					if (statement.executeUpdate() == 0) {
						statement.close();
						result.close();
						return -1;
					}

					statement.close();
					result.close();
					return id;

				} else {
					statement = c
							.prepareStatement("INSERT INTO ClienteVip (cliente, descuento, antiguedad) VALUES(?,?,?)");
					statement.setInt(1, id);
					statement.setInt(2, ((TClienteVip) cliente).GetDescuento());
					statement.setInt(3, ((TClienteVip) cliente).GetAntiguedad());

					if (statement.executeUpdate() == 0) {
						statement.close();
						result.close();
						return -1;
					}

					statement.close();
					result.close();
					return id;
				}
			} else {
				return -1;
			}
		} catch (Exception e) {
			return -1;
		}
	}

	@Override
	public TCliente Read(Integer id) {

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
					"SELECT * FROM Cliente AS c JOIN ClienteNormal AS cn ON c.id=cn.cliente WHERE c.id=? FOR UPDATE");
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				TClienteNormal cliente = new TClienteNormal();
				cliente.SetID(result.getInt(1));
				cliente.SetDNI(result.getString(2));
				cliente.SetNombre(result.getString(3));
				cliente.SetCorreo(result.getString(4));
				cliente.SetActivo(result.getBoolean(5));
				cliente.SetFacturacion(result.getInt(7));
				result.close();
				statement.close();
				return cliente;
			} else {
				statement = c.prepareStatement(
						"SELECT * FROM Cliente AS c JOIN ClienteVip AS cv ON c.id=cv.cliente WHERE c.id=? FOR UPDATE");
				statement.setInt(1, id);
				result = statement.executeQuery();
				if (result.next()) {
					TClienteVip cliente = new TClienteVip();
					cliente.SetID(result.getInt(1));
					cliente.SetDNI(result.getString(2));
					cliente.SetNombre(result.getString(3));
					cliente.SetCorreo(result.getString(4));
					cliente.SetActivo(result.getBoolean(5));
					cliente.SetDescuento(result.getInt(7));
					cliente.SetAntiguedad(result.getInt(8));
					result.close();
					statement.close();
					return cliente;
				} else {
					statement.close();
					result.close();
					return null;
				}
			}
		} catch (Exception e) {
			return new TClienteNormal(-1);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public Integer Update(TCliente cliente) {

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
					.prepareStatement("UPDATE Cliente SET dni=?, nombre=?, correo=?, activo=? WHERE id=?");
			statement.setString(1, cliente.GetDNI());
			statement.setString(2, cliente.GetNombre());
			statement.setString(3, cliente.GetCorreo());
			statement.setBoolean(4, cliente.GetActivo());
			statement.setInt(5, cliente.GetID());
			statement.executeUpdate();

			if (cliente instanceof TClienteNormal) {
				statement = c.prepareStatement("UPDATE ClienteNormal SET facturacion=? WHERE cliente=?");
				statement.setInt(1, ((TClienteNormal) cliente).GetFacturacion());
				statement.setInt(2, cliente.GetID());

				statement.executeUpdate();
				statement.close();

			} else if (cliente instanceof TClienteVip) {
				statement = c.prepareStatement("UPDATE ClienteVip SET descuento=?, antiguedad=? WHERE cliente=?");
				statement.setInt(1, ((TClienteVip) cliente).GetDescuento());
				statement.setInt(2, ((TClienteVip) cliente).GetAntiguedad());
				statement.setInt(3, cliente.GetID());

				statement.executeUpdate();
				statement.close();
			}
			return cliente.GetID();
		} catch (Exception e) {
			return -1;
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
			PreparedStatement statement = c.prepareStatement("UPDATE Cliente SET activo=false WHERE id=?");
			statement.setInt(1, id);

			statement.executeUpdate();
			statement.close();
			return id;
		} catch (Exception e) {
			return -1;
		}
	}

	@Override
	public ArrayList<TClienteVip> ReadAllVIP() {

		ArrayList<TClienteVip> list = new ArrayList<TClienteVip>();

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
			PreparedStatement statement = c.prepareStatement("SELECT * FROM Cliente FOR UPDATE");
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				try {
					PreparedStatement statementCV = c.prepareStatement(
							"SELECT cv.descuento, cv.antiguedad FROM ClienteVip AS cv WHERE cv.cliente=? FOR UPDATE");
					statementCV.setInt(1, result.getInt(1));
					ResultSet resultCV = statementCV.executeQuery();

					if (resultCV.next()) {
						TClienteVip clienteVip = new TClienteVip();
						clienteVip.SetID(result.getInt(1));
						clienteVip.SetDNI(result.getString(2));
						clienteVip.SetNombre(result.getString(3));
						clienteVip.SetCorreo(result.getString(4));
						clienteVip.SetActivo(result.getBoolean(5));
						clienteVip.SetDescuento(resultCV.getInt(1));
						clienteVip.SetAntiguedad(resultCV.getInt(2));

						statementCV.close();
						resultCV.close();
						list.add(clienteVip);
					}
				} catch (Exception exception) {
					list = new ArrayList<TClienteVip>();
					list.add(new TClienteVip(-1));
					return list;
				}
			}

			statement.close();
			result.close();

			return list;

		} catch (Exception exception) {
			list = new ArrayList<TClienteVip>();
			list.add(new TClienteVip(-1));
			return list;
		}

	}

	@Override
	public ArrayList<TClienteNormal> readAllNormal() {

		ArrayList<TClienteNormal> list = new ArrayList<TClienteNormal>();

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
			PreparedStatement statement = c.prepareStatement("SELECT * FROM Cliente FOR UPDATE");
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				try {
					PreparedStatement statementCN = c.prepareStatement(
							"SELECT cn.facturacion FROM ClienteNormal AS cn WHERE cn.cliente=? FOR UPDATE");
					statementCN.setInt(1, result.getInt(1));
					ResultSet resultCN = statementCN.executeQuery();

					if (resultCN.next()) {
						TClienteNormal clienteNormal = new TClienteNormal();
						clienteNormal.SetID(result.getInt(1));
						clienteNormal.SetDNI(result.getString(2));
						clienteNormal.SetNombre(result.getString(3));
						clienteNormal.SetCorreo(result.getString(4));
						clienteNormal.SetActivo(result.getBoolean(5));
						clienteNormal.SetFacturacion(resultCN.getInt(1));

						statementCN.close();
						resultCN.close();
						list.add(clienteNormal);
					}
				} catch (Exception exception) {
					list = new ArrayList<TClienteNormal>();
					list.add(new TClienteNormal(-1));
					return list;
				}
			}

			statement.close();
			result.close();

			return list;

		} catch (Exception exception) {
			list = new ArrayList<TClienteNormal>();
			list.add(new TClienteNormal(-1));
			return list;
		}

	}

	@Override
	public TCliente ReadByDNI(String dni) {

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
					"SELECT * FROM Cliente AS c JOIN ClienteNormal AS cn ON c.id=cn.cliente WHERE c.dni=? FOR UPDATE");
			statement.setString(1, dni);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				TClienteNormal cliente = new TClienteNormal();
				cliente.SetID(result.getInt(1));
				cliente.SetDNI(result.getString(2));
				cliente.SetNombre(result.getString(3));
				cliente.SetCorreo(result.getString(4));
				cliente.SetActivo(result.getBoolean(5));
				cliente.SetFacturacion(result.getInt(7));

				result.close();
				statement.close();

				return cliente;
			} else {
				statement = c.prepareStatement(
						"SELECT * FROM Cliente AS c JOIN ClienteVip AS cv ON c.id=cv.cliente WHERE c.dni=? FOR UPDATE");
				statement.setString(1, dni);
				result = statement.executeQuery();
				if (result.next()) {
					TClienteVip cliente = new TClienteVip();
					cliente.SetID(result.getInt(1));
					cliente.SetDNI(result.getString(2));
					cliente.SetNombre(result.getString(3));
					cliente.SetCorreo(result.getString(4));
					cliente.SetActivo(result.getBoolean(5));
					cliente.SetDescuento(result.getInt(7));
					cliente.SetAntiguedad(result.getInt(8));

					result.close();
					statement.close();
					return cliente;
				} else {
					result.close();
					statement.close();
					return null;
				}
			}
		} catch (Exception e) {
			return new TClienteNormal(-1);
		}
		
	}

	@Override
	public ArrayList<TCliente> ReadAll() {
		
		ArrayList<TCliente> list = new ArrayList<TCliente>();

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
			PreparedStatement statement = c.prepareStatement("SELECT * FROM Cliente FOR UPDATE");
			ResultSet result = statement.executeQuery();
			while (result.next()) {

				try {
					PreparedStatement statementCN = c.prepareStatement(
							"SELECT c.facturacion FROM ClienteNormal AS c WHERE c.cliente=? FOR UPDATE");
					statementCN.setInt(1, result.getInt(1));
					ResultSet resultCN = statementCN.executeQuery();

					if (resultCN.next()) {
						TClienteNormal clienteNormal = new TClienteNormal();
						clienteNormal.SetID(result.getInt(1));
						clienteNormal.SetDNI(result.getString(2));
						clienteNormal.SetNombre(result.getString(3));
						clienteNormal.SetCorreo(result.getString(4));
						clienteNormal.SetActivo(result.getBoolean(5));
						clienteNormal.SetFacturacion(resultCN.getInt(1));

						statementCN.close();
						resultCN.close();

						list.add(clienteNormal);

					} else {
						PreparedStatement statementCV = c.prepareStatement(
								"SELECT cv.descuento, cv.antiguedad FROM ClienteVip AS cv WHERE cv.cliente=? FOR UPDATE");
						statementCV.setInt(1, result.getInt(1));
						ResultSet resultCV = statementCV.executeQuery();

						if (resultCV.next()) {
							TClienteVip clienteVip = new TClienteVip();
							clienteVip.SetID(result.getInt(1));
							clienteVip.SetDNI(result.getString(2));
							clienteVip.SetNombre(result.getString(3));
							clienteVip.SetCorreo(result.getString(4));
							clienteVip.SetActivo(result.getBoolean(5));
							clienteVip.SetDescuento(resultCV.getInt(1));
							clienteVip.SetAntiguedad(resultCV.getInt(2));

							statementCV.close();
							resultCV.close();
							list.add(clienteVip);
						}
					}

				} catch (Exception e) {
					list = new ArrayList<TCliente>();
					list.add(new TClienteNormal(-1));
					return list;
				}
			}

			statement.close();
			result.close();

			return list;
		} catch (Exception e) {
			list = new ArrayList<TCliente>();
			list.add(new TClienteNormal(-1));
			return list;
		}

	}

}