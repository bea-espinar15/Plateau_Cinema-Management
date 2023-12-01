package Presentacion.Command.Cliente;

import Negocio.ASFactory.ASFactory;
import Negocio.Cliente.ASCliente;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;

public class MostrarClienteParaModificarCommand implements Command {

	@Override
	public Context Execute(Object input) {
		ASCliente asCliente = ASFactory.getInstance().GetASCliente();
		return new Context(ContextEnum.MOSTRARCLIENTEPARAMODIFICAR, asCliente.MostrarParaModificar((Integer) input));
	}

}
