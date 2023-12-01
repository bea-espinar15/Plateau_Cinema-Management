
package Integracion.Transactions;


public interface Transaction {
	
	public void start();
	public void commit();
	public void rollback();
	public Object getResource();
	
}