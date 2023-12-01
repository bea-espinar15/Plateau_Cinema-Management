
package Negocio.ASFactory;

import Negocio.Cliente.ASCliente;
import Negocio.Compra.ASCompra;
import Negocio.Pase.ASPase;
import Negocio.Pelicula.ASPelicula;
import Negocio.Productora.ASProductora;
import Negocio.Sala.ASSala;


public abstract class ASFactory {
	
	private static ASFactory instance;

	public static synchronized ASFactory getInstance() {
		if (ASFactory.instance == null)
			return new ASFactoryImp();
		else return ASFactory.instance;
	}
	
	public abstract ASCliente GetASCliente();	
	public abstract ASCompra GetASCompra();
	public abstract ASPase GetASPase();	
	public abstract ASPelicula GetASPelicula();
	public abstract ASProductora GetASProductora();	
	public abstract ASSala GetASSala();

}