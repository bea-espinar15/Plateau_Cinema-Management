
package Presentacion.Command.Productora;

import java.util.Date;

import Negocio.ASFactory.ASFactory;
import Negocio.Productora.ASProductora;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import utilities.Pair;


public class CalcularProductoraMasIngresosCommand implements Command {

	@SuppressWarnings("unchecked")
	public Context Execute(Object input) {
		ASProductora asProductora = ASFactory.getInstance().GetASProductora();
		return new Context(ContextEnum.CALCULARPRODUCTORAMASINGRESOS, asProductora.ProductoraMasIngresos((Pair<Date, Date>) input));
	}

}
