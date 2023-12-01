
package Presentacion.Command.Compra;

import java.util.ArrayList;

import Negocio.ASFactory.ASFactory;
import Negocio.Compra.ASCompra;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;


public class DevolucionCompraCommand implements Command {

	public Context Execute(Object input) {
		ASCompra asCompra = ASFactory.getInstance().GetASCompra();
		Integer idCompra = ((ArrayList<Integer>)input).get(0);
		Integer idPase = ((ArrayList<Integer>)input).get(1);
		Integer entradasDevueltas = ((ArrayList<Integer>)input).get(2);
		return new Context(ContextEnum.DEVOLVERCOMPRA, asCompra.DevolverCompra(idCompra, idPase, entradasDevueltas));
	}
	
}