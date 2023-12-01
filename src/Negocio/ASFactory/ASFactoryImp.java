
package Negocio.ASFactory;

import Negocio.Cliente.ASCliente;
import Negocio.Cliente.ASClienteImp;
import Negocio.Compra.ASCompra;
import Negocio.Compra.ASCompraImp;
import Negocio.Pase.ASPase;
import Negocio.Pase.ASPaseImp;
import Negocio.Pelicula.ASPelicula;
import Negocio.Pelicula.ASPeliculaImp;
import Negocio.Productora.ASProductora;
import Negocio.Productora.ASProductoraImp;
import Negocio.Sala.ASSala;
import Negocio.Sala.ASSalaImp;


public class ASFactoryImp extends ASFactory {

	@Override
	public ASCliente GetASCliente() {
		return new ASClienteImp();
	}

	@Override
	public ASCompra GetASCompra() {
		return new ASCompraImp();
	}

	@Override
	public ASPase GetASPase() {
		return new ASPaseImp();
	}

	@Override
	public ASPelicula GetASPelicula() {
		return new ASPeliculaImp();
	}

	@Override
	public ASProductora GetASProductora() {
		return new ASProductoraImp();
	}

	@Override
	public ASSala GetASSala() {
		return new ASSalaImp();
	}
	
}