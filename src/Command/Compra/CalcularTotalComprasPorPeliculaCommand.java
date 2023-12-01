
package Presentacion.Command.Compra;

import Negocio.ASFactory.ASFactory;
import Negocio.Compra.ASCompra;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;


public class CalcularTotalComprasPorPeliculaCommand implements Command {

	public Context Execute(Object input) {
		ASCompra asCompra = ASFactory.getInstance().GetASCompra();
		return new Context(ContextEnum.CALCULARTOTALCOMPRASPORPELICULA, asCompra.CalcularTotalComprasPorPelicula((Integer)input));
	}
	
}