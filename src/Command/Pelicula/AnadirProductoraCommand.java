
// @author Beatriz

package Presentacion.Command.Pelicula;

import Negocio.ASFactory.ASFactory;
import Negocio.Pelicula.ASPelicula;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import utilities.Pair;


public class AnadirProductoraCommand implements Command {
	
	public Context Execute(Object input) {
		ASPelicula asPelicula = ASFactory.getInstance().GetASPelicula();
		Integer idProductora = ((Pair<Integer,Integer>)input).getFirst();
		Integer idPelicula = ((Pair<Integer,Integer>)input).getSecond();
		return new Context(ContextEnum.ANADIRPRODUCTORA, asPelicula.AnadirProductora(idProductora, idPelicula));
	}
	
}