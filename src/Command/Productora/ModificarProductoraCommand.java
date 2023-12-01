
// @author Beatriz

package Presentacion.Command.Productora;

import Negocio.ASFactory.ASFactory;
import Negocio.Productora.ASProductora;
import Negocio.Productora.TProductora;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;


public class ModificarProductoraCommand implements Command {
	
	public Context Execute(Object input) {
		ASProductora asProductora = ASFactory.getInstance().GetASProductora();
		return new Context(ContextEnum.MODIFICARPRODUCTORA, asProductora.Modificar((TProductora)input));
	}
	
}