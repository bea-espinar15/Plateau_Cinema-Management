
package Presentacion.Command.Cliente;

import Negocio.ASFactory.ASFactory;
import Negocio.Cliente.ASCliente;
import Negocio.Cliente.TCliente;
import Presentacion.Command.Command;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;


public class AltaClienteCommand implements Command {
	
	public Context Execute(Object input) {
		ASCliente asCliente = ASFactory.getInstance().GetASCliente();
		return new Context(ContextEnum.ALTACLIENTE,asCliente.Alta((TCliente) input));
	}
	
}