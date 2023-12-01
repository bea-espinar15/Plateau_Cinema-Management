
package Integracion.Transactions;

import java.util.concurrent.ConcurrentHashMap;


public class TransactionManagerImp extends TransactionManager {
	
	private ConcurrentHashMap<Thread, Transaction> transactionMap;
	
	public TransactionManagerImp(){
		transactionMap = new ConcurrentHashMap<>();
	}
	
	@Override
	public void deleteTransaction() throws Exception {
		Thread th = Thread.currentThread();
		if (th != null) {
			Transaction t = transactionMap.get(th);
			if (t != null) {
				transactionMap.remove(th);
			}
			else
				throw new Exception("La hebra actual no tiene transaccion asociada");
		}
		else
			throw new Exception("Hebra de ejecucion nula");
	}
	
	@Override
	public void newTransaction() throws Exception {
		Thread th = Thread.currentThread();
		if (th != null) {
			Transaction t = transactionMap.get(th);
			if(t == null){
				t = TransactionFactory.getInstance().createTransaction();
				transactionMap.put(th, t);
			}
			else
				throw new Exception("Ya existe transaccion para esta hebra");
		}	
		else
			throw new Exception("Hebra de ejecucion nula");
	}
	
	@Override
	public Transaction getTransaction() throws Exception {
		Thread th = Thread.currentThread();
		if (th != null) {
			Transaction t = transactionMap.get(th);
			if(t != null)
				return t;
			else
				throw new Exception("No existe transaccion para esta hebra");
		}	
		else
			throw new Exception("Hebra de ejecucion nula");
	}
	
}