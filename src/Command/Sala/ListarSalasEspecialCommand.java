
// @author Beatriz

package Presentacion.Command.Sala;

import Negocio.ASFactory.ASFactory;
import Negocio.Sala.ASSala;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;


public class ListarSalasEspecialCommand implements Command {
	
	public Context Execute(Object input) {
		ASSala asSala = ASFactory.getInstance().GetASSala();
		return new Context(ContextEnum.LISTARSALASESPECIALES, asSala.ListarEspecial());
	}
	
}