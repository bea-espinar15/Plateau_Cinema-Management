
package Presentacion.Command.Sala;

import Negocio.ASFactory.ASFactory;
import Negocio.Sala.ASSala;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;


public class BajaSalaCommand implements Command {
	
	public Context Execute(Object input) {
		ASSala asSala = ASFactory.getInstance().GetASSala();
 		return new Context(ContextEnum.BAJASALA, asSala.Baja((Integer) input));
	}
	
}