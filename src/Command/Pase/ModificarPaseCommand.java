
package Presentacion.Command.Pase;

import Negocio.ASFactory.ASFactory;
import Negocio.Pase.ASPase;
import Negocio.Pase.TPase;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;


public class ModificarPaseCommand implements Command {

	public Context Execute(Object input) {
		ASPase asPase = ASFactory.getInstance().GetASPase();
		return new Context(ContextEnum.MODIFICARPASE, asPase.Modificar((TPase)input));
	}
}