
// @author Josema

package Presentacion.Command.Sala;

import Negocio.ASFactory.ASFactory;
import Negocio.Sala.ASSala;
import Negocio.Sala.TSala;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;


public class AltaSalaCommand implements Command {
	
	public Context Execute(Object input) {
		ASSala asSala = ASFactory.getInstance().GetASSala();
		return new Context(ContextEnum.ALTASALA, asSala.Alta((TSala) input));
	}
	
}