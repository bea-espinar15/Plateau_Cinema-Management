
package Integracion.Transactions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import Integracion.Util.PropertiesUtil;


public class TransactionMySql implements Transaction {
	
	private static final String DB_NAME_PROP = "dbname";
	private static final String DB_HOST_PROP = "host";
	private static final String DB_PASSWORD_PROP = "password";
	private static final String DB_PORT_PROP = "port";
	private static final String DB_USER_PROP = "user";
	
	private Connection connection;
	private String db_properties;
	
	public TransactionMySql(){
		try{
			db_properties = "src/META-INF/dbconfig.properties";
			Properties prop = PropertiesUtil.loadProperty(db_properties);
			String host = prop.getProperty(DB_HOST_PROP);
			String port = prop.getProperty(DB_PORT_PROP);
			String db = prop.getProperty(DB_NAME_PROP);
			String user = prop.getProperty(DB_USER_PROP);
			String password = prop.getProperty(DB_PASSWORD_PROP);
			
			String connectionString = "jdbc:mysql://" + host +":" + port +"/" + db +"?user=" + user + "&password=" + password +"&useSSL=false"+"&serverTimezone=UTC";         
			connection = DriverManager.getConnection(connectionString);
			
		}catch(SQLException e){
			connection = null;
			System.out.println("Error al establecer la conexión");
			System.out.println(e.getMessage());
		}
	}
	
	public void start() {
		if(connection != null){
			try{
				connection.setAutoCommit(false);
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	public void commit() {
		if(connection != null){
			try {
				connection.commit();
				connection.close();
				TransactionManager.getInstance().deleteTransaction();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void rollback() {
		if(connection != null){
			try {
				connection.rollback();
				connection.close();
				TransactionManager.getInstance().deleteTransaction();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Object getResource() {
		return connection;
	}
	
}