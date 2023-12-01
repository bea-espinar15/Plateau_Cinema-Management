
package Integracion.QueryFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;
import Negocio.Productora.TProductora;
import utilities.Pair;

public class CalcularProductoraConMasIngresos implements Query {

	// le llega un Pair con 2 Date
	public Object execute(Object param) {

		Pair<Date, Date> aux = (Pair<Date, Date>) param;

		Date d1 = aux.getFirst();
		Date d2 = aux.getSecond();

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
			String sqlQuery = "select idProductora, cif, nombre, direccion, telefono, activo, ingresos "
					+ "from (select productora.idproductora, productora.cif, productora.nombre, productora.direccion, productora.telefono, productora.activo, sum(nEntradas * precio) as ingresos "
					+ "from productora join peliculaproductora on productora.idProductora = peliculaproductora.idProductora "
					+ "join pase on peliculaproductora.idPelicula = pase.idPelicula "
					+ "join lineacompra on pase.id = lineacompra.idPase "
					+ "join compra on compra.id = lineacompra.idCompra "
					+ "where fecha between ? and ?"
					+ "group by productora.idProductora) as ingresosProductoras "
					+ "where ingresos >= ALL (select sum(nEntradas * precio) as ingresos "
					+ "from productora join peliculaproductora on productora.idProductora = peliculaproductora.idProductora "
					+ "join pase on peliculaproductora.idPelicula = pase.idPelicula "
					+ "join lineacompra on pase.id = lineacompra.idPase "
					+ "join compra on compra.id = lineacompra.idCompra "
					+ "where fecha between ? and ?"
					+ "group by productora.idProductora) for update";
			PreparedStatement stmt = c.prepareStatement(sqlQuery);
			stmt.setString(1, dateToTimestampString(d1));
			stmt.setString(2, dateToTimestampString(d2));
			stmt.setString(3, dateToTimestampString(d1));
			stmt.setString(4, dateToTimestampString(d2));
			ResultSet rs = stmt.executeQuery();
			
			ArrayList<TProductora> productoras = new ArrayList<TProductora>();
			Integer ingresos = 0;
			
			while (rs.next()) {
				Integer id = rs.getInt("idProductora");
				String CIF = rs.getString("cif");
				String nombre = rs.getString("nombre");
				String direccion = rs.getString("direccion");
				Integer telefono = rs.getInt("telefono");
				Boolean activo = rs.getBoolean("activo");
				ingresos = rs.getInt("ingresos");
				productoras.add(new TProductora(id, nombre, CIF, telefono, direccion, activo));
			}
			
			rs.close();
			stmt.close();
			return new Pair<ArrayList<TProductora>, Integer>(productoras, ingresos);
			
		} catch (Exception e) {
			return new Pair<List<TProductora>, Integer>(new ArrayList<TProductora>(), -1);
		}

	}

	private String dateToTimestampString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		return format.format(date.getTime());
	}
}