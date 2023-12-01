
package Presentacion.Command.Pase;

import Negocio.ASFactory.ASFactory;
import Negocio.Pase.ASPase;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;

public class BajaPaseCommand implements Command {

	public Context Execute(Object input) {
		ASPase asPase = ASFactory.getInstance().GetASPase();
		return new Context(ContextEnum.BAJAPASE, asPase.Baja((Integer)input));
	}
	
}