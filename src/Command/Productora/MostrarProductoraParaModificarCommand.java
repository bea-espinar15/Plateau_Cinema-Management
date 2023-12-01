package Presentacion.Command.Productora;

import Negocio.ASFactory.ASFactory;
import Negocio.Productora.ASProductora;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;

public class MostrarProductoraParaModificarCommand implements Command{

	@Override
	public Context Execute(Object input) {
		ASProductora asProductora = ASFactory.getInstance().GetASProductora();
		return new Context(ContextEnum.MOSTRARPRODUCTORAPARAMODIFICAR, asProductora.MostrarParaModificar((Integer)input));
	}

}
