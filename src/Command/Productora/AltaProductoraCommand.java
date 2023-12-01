
// @author Santi

package Presentacion.Command.Productora;

import Negocio.ASFactory.ASFactory;
import Negocio.Productora.ASProductora;
import Negocio.Productora.TProductora;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;


public class AltaProductoraCommand implements Command {
	
	public Context Execute(Object input) {
		ASProductora asProductora = ASFactory.getInstance().GetASProductora();
		return new Context(ContextEnum.ALTAPRODUCTORA, asProductora.Alta((TProductora)input));
	}
	
}