
package Presentacion.Command.Compra;

import java.util.HashMap;

import Negocio.ASFactory.ASFactory;
import Negocio.Compra.ASCompra;
import Negocio.Compra.TCompra;
import Negocio.Compra.TLineaCompra;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import utilities.Pair;


public class CerrarCompraCommand implements Command {

	public Context Execute(Object input) {
		ASCompra asCompra = ASFactory.getInstance().GetASCompra();
		TCompra tCompra = ((Pair<TCompra, HashMap<Integer, TLineaCompra>>) input).getFirst();
		HashMap<Integer, TLineaCompra> carrito = ((Pair<TCompra, HashMap<Integer, TLineaCompra>>) input).getSecond();
		return new Context(ContextEnum.CERRARCOMPRA, asCompra.Cerrar(tCompra, carrito));
	}
	
}