
// @author Beatriz

package Presentacion.Command.Productora;

import Negocio.ASFactory.ASFactory;
import Negocio.Productora.ASProductora;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;


public class ListarProductorasPorPeliculaCommand implements Command {

	public Context Execute(Object input) {
		ASProductora asProductora = ASFactory.getInstance().GetASProductora();
		return new Context(ContextEnum.LISTARPRODUCTORASPORPELICULA, asProductora.ListarPorPelicula((Integer)input));
	}
	
}