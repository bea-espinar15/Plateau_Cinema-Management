package Presentacion.Gui.ErrorHandler;



public class ClienteErrorHandler {

	static Message getMessage(int reasonDigit) {
		switch (reasonDigit) {
		case -2:
			return new Message("No existe ningun cliente con el ID introducido", "Cliente inexistente");
		case -3:
			return new Message("Ya existe un cliente activo con el DNI introducido", "Cliente existente activo");
		case -4:
			return new Message("Se ha producido un error con la base de datos", "Error base de datos");
		case -5: 
			return new Message("El cliente esta dado de baja", "Cliente inactivo");
		case -6:
			return new Message("Ya existe un cliente con el DNI indicado","DNI repetido");
		case -7:
			return new Message("No se ha seleccionado ningún tipo de Cliente", "Cliente sin tipo");
		default:
			return new Message("Ha habido un error desconocido en el tratamiento de datos del cliente.", "Error desconocido");
		}
	}
	
}
