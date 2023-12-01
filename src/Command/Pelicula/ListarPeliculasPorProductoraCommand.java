
// @author Beatriz

package Presentacion.Command.Pelicula;

import Negocio.ASFactory.ASFactory;
import Negocio.Pelicula.ASPelicula;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;


public class ListarPeliculasPorProductoraCommand implements Command {
	
	public Context Execute(Object input) {
		ASPelicula asPelicula = ASFactory.getInstance().GetASPelicula();
		return new Context(ContextEnum.LISTARPELICULASPORPRODUCTORA, asPelicula.ListarPorProductora((Integer)input));
	}
	
}