
// @author Beatriz

package Presentacion.Command.Compra;

import Negocio.ASFactory.ASFactory;
import Negocio.Compra.ASCompra;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;


public class ListarComprasPorPaseCommand implements Command {

	public Context Execute(Object idPase) {
		ASCompra asCompra = ASFactory.getInstance().GetASCompra();
		return new Context(ContextEnum.LISTARCOMPRASPORPASE, asCompra.ListarPorPase((Integer)idPase));
	}
	
}