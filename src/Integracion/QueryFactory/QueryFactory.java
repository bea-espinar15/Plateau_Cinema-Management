
package Integracion.QueryFactory;


public abstract class QueryFactory {
	
	private static QueryFactory instance;
	
	public static QueryFactory getInstance() {
		if(instance == null)
			instance = new QueryFactoryImp();
		return instance;
	}
	
	public abstract Query generateQuery(Integer id);
	
}