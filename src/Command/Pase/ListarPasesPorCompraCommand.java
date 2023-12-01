
package Presentacion.Command.Pase;

import Negocio.ASFactory.ASFactory;
import Negocio.Pase.ASPase;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;


public class ListarPasesPorCompraCommand implements Command {

	public Context Execute(Object input) {
		ASPase asPase = ASFactory.getInstance().GetASPase();
		return new Context(ContextEnum.LISTARPASESPORCOMPRA, asPase.ListarPorCompra((Integer)input));
	}
	
}