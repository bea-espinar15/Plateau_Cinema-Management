
package Integracion.Transactions;

public class TransactionFactoryImp extends TransactionFactory {

	public Transaction createTransaction() {
		return new TransactionMySql();
	}
	
}