
package Integracion.QueryFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;

public class CalcularNumComprasPorPelicula implements Query {

	public Object execute(Object param) {

		int index = (int) param;
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
			String sqlQuery = "select count(distinct compra.id) as nTotal from "
					+ "compra join lineacompra on compra.id = lineacompra.idCompra join pase on lineacompra.idPase = pase.id "
					+ "where pase.idPelicula = ? for update";
			PreparedStatement stmt = c.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, index);
			ResultSet rs = stmt.executeQuery();

			Integer n = -1;
			if (rs.next())
				n = rs.getInt("nTotal");
			
			stmt.close();
			rs.close();
			return n;
			
		} catch (Exception e) {
			return -1;
		}
	}

}