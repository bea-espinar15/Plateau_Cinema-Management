
package Integracion.Productora;

import Negocio.Productora.TProductora;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author Santi,Sara
 * @generated "UML a Java
 *            (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class DAOProductoraImp implements DAOProductora {

	public Integer Create(TProductora productora) {

		Transaction t = null;
		Integer result = -1;

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
					"INSERT INTO PRODUCTORA(nombre,cif,direccion,telefono,activo) VALUES (" + "'"
							+ productora.GetNombre() + "','" + productora.GetCIF() + "'	," + "'"
							+ productora.GetDireccion() + "'," + "" + productora.GetTelefono() + "," + true + ")",
					Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				result = rs.getInt(1);
			}
			stmt.close();
			rs.close();
			return result;
		} catch (Exception e) {
			return -1;
		}

	}

	public TProductora Read(Integer id) {

		TProductora p = null;
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
			PreparedStatement stmt = c
					.prepareStatement("SELECT * FROM PRODUCTORA WHERE IDPRODUCTORA = " + id + " FOR UPDATE");
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {// se agrego a la bbdd
				p = new TProductora(rs.getInt("idproductora"), rs.getString("nombre"), rs.getString("cif"),
						rs.getInt("telefono"), rs.getString("direccion"), rs.getBoolean("activo"));
			}
			else{
				p = null;
			}
			stmt.close();
			rs.close();
			return p;
		} catch (Exception e) {
			return new TProductora(-1);
		}

	}

	public Integer Update(TProductora productora) {

		Transaction t = null;
		Integer ok = -1;

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
					"UPDATE PRODUCTORA SET NOMBRE='" + productora.GetNombre() + "' , cif='" + productora.GetCIF()
							+ "' , telefono=" + productora.GetTelefono() + " , direccion='" + productora.GetDireccion()
							+ "' , activo=" + productora.GetActivo() + " WHERE idProductora = " + productora.GetID(),
					Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			ok = productora.GetID();
			stmt.close();
			rs.close();
			return ok;

		} catch (Exception e) {
			return -1;
		}

	}

	public Integer Delete(Integer id) {

		Transaction t = null;
		Integer ok = -1;

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
					"UPDATE PRODUCTORA SET activo = " + false + " WHERE idProductora = " + id,
					Statement.RETURN_GENERATED_KEYS);
			Integer affectedRows = stmt.executeUpdate();
			if (affectedRows == 0)
				return -1;
			ok = 0;
			stmt.close();
			return ok;

		} catch (Exception e) {
			return -1;
		}

	}

	public ArrayList<TProductora> ReadAll() {

		ArrayList<TProductora> productoras = new ArrayList<TProductora>();
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
			PreparedStatement stmt = c.prepareStatement("SELECT * FROM PRODUCTORA FOR UPDATE");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {

				TProductora p = new TProductora(rs.getInt("idproductora"), rs.getString("nombre"), rs.getString("cif"),
						rs.getInt("telefono"), rs.getString("direccion"), rs.getBoolean("activo"));
				productoras.add(p);
			}

			stmt.close();
			rs.close();
			return productoras;
		} catch (Exception e) {
			productoras = new ArrayList<TProductora>();
			productoras.add(new TProductora(-1));
			return productoras;
		}

	}

	public ArrayList<TProductora> ReadAllByPelicula(Integer idPelicula) {

		ArrayList<TProductora> productoras = new ArrayList<TProductora>();
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
					"SELECT productora.idproductora, nombre, cif, telefono, direccion, productora.activo FROM PRODUCTORA JOIN PELICULAPRODUCTORA ON PRODUCTORA.idProductora = PeliculaProductora.idProductora WHERE PeliculaProductora.IDPELICULA = "
							+ idPelicula + " FOR UPDATE");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {

				TProductora p = new TProductora(rs.getInt("idproductora"), rs.getString("nombre"), rs.getString("cif"),
						rs.getInt("telefono"), rs.getString("direccion"), rs.getBoolean("activo"));
				productoras.add(p);
			}

			stmt.close();
			rs.close();
			return productoras;
		} catch (Exception e) {
			productoras = new ArrayList<TProductora>();
			productoras.add(new TProductora(-1));
			return productoras;
		}

	}

	public TProductora ReadByCIF(String cif) {

		TProductora p = null;
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
			PreparedStatement stmt = c.prepareStatement("SELECT * FROM PRODUCTORA WHERE CIF = '" + cif + "' FOR UPDATE",
					Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {// se agrego a la bbdd
				p = new TProductora(rs.getInt("idproductora"), rs.getString("nombre"), rs.getString("cif"),
						rs.getInt("telefono"), rs.getString("direccion"), rs.getBoolean("activo"));
			}

			stmt.close();
			rs.close();
			return p;
		} catch (Exception e) {
			return new TProductora(-1);
		}

	}

	public TProductora ReadByNombre(String nombre) {

		TProductora p = null;
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
					"SELECT * FROM PRODUCTORA WHERE NOMBRE = '" + nombre + "' FOR UPDATE",
					Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {// se agrego a la bbdd
				p = new TProductora(rs.getInt("idproductora"), rs.getString("nombre"), rs.getString("cif"),
						rs.getInt("telefono"), rs.getString("direccion"), rs.getBoolean("activo"));
			} 
			
			stmt.close();
			rs.close();
			return p;
		} catch (Exception e) {
			return new TProductora(-1);
		}

	}
}