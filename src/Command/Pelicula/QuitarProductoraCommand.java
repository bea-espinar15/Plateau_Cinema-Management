
// @author Beatriz

package Presentacion.Command.Pelicula;

import Negocio.ASFactory.ASFactory;
import Negocio.Pelicula.ASPelicula;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import utilities.Pair;


public class QuitarProductoraCommand implements Command {
	
	public Context Execute(Object input) {
		ASPelicula asPelicula = ASFactory.getInstance().GetASPelicula();
		@SuppressWarnings("unchecked")
		Integer idProductora = ((Pair<Integer,Integer>)input).getFirst();
		@SuppressWarnings("unchecked")
		Integer idPelicula = ((Pair<Integer,Integer>)input).getSecond();
		return new Context(ContextEnum.QUITARPRODUCTORA, asPelicula.QuitarProductora(idProductora, idPelicula));
	}
	
}