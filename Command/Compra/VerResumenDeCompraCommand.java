
package Presentacion.Command.Compra;

import Negocio.ASFactory.ASFactory;
import Negocio.Compra.ASCompra;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;


public class VerResumenDeCompraCommand implements Command {

	@Override
	public Context Execute(Object idCompra) {
		ASCompra asCompra = ASFactory.getInstance().GetASCompra();
		return new Context(ContextEnum.VERRESUMENDECOMPRA, asCompra.VerResumenDeCompra((Integer)idCompra));
	}

}
