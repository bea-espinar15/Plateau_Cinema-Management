
package Presentacion.Command.Cliente;

import Negocio.ASFactory.ASFactory;
import Negocio.Cliente.ASCliente;
import Negocio.Cliente.TCliente;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;


public class ModificarClienteCommand implements Command {
	
	public Context Execute(Object input) {
		ASCliente asCliente = ASFactory.getInstance().GetASCliente();
		return new Context(ContextEnum.MODIFICARCLIENTE, asCliente.Modificar((TCliente) input));
	}
	
}