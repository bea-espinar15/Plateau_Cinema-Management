
package Integracion.DAOFactory;

import Integracion.Cliente.DAOCliente;
import Integracion.Cliente.DAOClienteImp;
import Integracion.Compra.DAOCompra;
import Integracion.Compra.DAOCompraImp;
import Integracion.Compra.DAOLineaCompra;
import Integracion.Compra.DAOLineaCompraImp;
import Integracion.Pase.DAOPase;
import Integracion.Pase.DAOPaseImp;
import Integracion.Pelicula.DAOPelicula;
import Integracion.Pelicula.DAOPeliculaImp;
import Integracion.Productora.DAOProductora;
import Integracion.Productora.DAOProductoraImp;
import Integracion.Sala.DAOSala;
import Integracion.Sala.DAOSalaImp;

public class DAOFactoryImp extends DAOFactory {

	@Override
	public DAOPase getDAOPase() {
		return new DAOPaseImp();
	}

	@Override
	public DAOCliente getDAOCliente() {
		return new DAOClienteImp();
	}

	@Override
	public DAOCompra getDAOCompra() {
		return new DAOCompraImp();
	}

	@Override
	public DAOPelicula getDAOPelicula() {
		return new DAOPeliculaImp();
	}

	@Override
	public DAOProductora getDAOProductora() {
		return new DAOProductoraImp();
	}

	@Override
	public DAOSala getDAOSala() {
		return new DAOSalaImp();
	}

	@Override
	public DAOLineaCompra getDAOLineaCompra() {
		return new DAOLineaCompraImp();
	}

}