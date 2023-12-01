
package Integracion.QueryFactory;

public class QueryFactoryImp extends QueryFactory {

	// las queries tienen asignadas un entero
	public Query generateQuery(Integer id) {
		switch (id) {
		case 1:
			return new CalcularNumComprasPorPelicula();
		case 2:
			return new CalcularProductoraConMasIngresos();
		default:
			return null;
		}
	}

}