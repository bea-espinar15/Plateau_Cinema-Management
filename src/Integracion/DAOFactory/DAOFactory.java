
package Integracion.DAOFactory;

import Integracion.Pase.DAOPase;
import Integracion.Cliente.DAOCliente;
import Integracion.Compra.DAOCompra;
import Integracion.Pelicula.DAOPelicula;
import Integracion.Productora.DAOProductora;
import Integracion.Sala.DAOSala;
import Integracion.Compra.DAOLineaCompra;


public abstract class DAOFactory {
	
	private static DAOFactory instance;
	
	public static DAOFactory getInstance() {
		if(instance == null)
			instance = new DAOFactoryImp();
		return instance;
	}
	
	public abstract DAOPase getDAOPase();
	public abstract DAOCliente getDAOCliente();
	public abstract DAOCompra getDAOCompra();
	public abstract DAOPelicula getDAOPelicula();
	public abstract DAOProductora getDAOProductora();
	public abstract DAOSala getDAOSala();
	public abstract DAOLineaCompra getDAOLineaCompra();
	
}