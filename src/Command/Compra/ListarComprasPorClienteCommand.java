
// @author Beatriz

package Presentacion.Command.Compra;

import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Negocio.ASFactory.ASFactory;
import Negocio.Compra.ASCompra;


public class ListarComprasPorClienteCommand implements Command {
	
	public Context Execute(Object idCliente) {
		ASCompra asCompra = ASFactory.getInstance().GetASCompra();
		return new Context(ContextEnum.LISTARCOMPRASPORCLIENTE, asCompra.ListarPorCliente((Integer)idCliente));
	}
	
}