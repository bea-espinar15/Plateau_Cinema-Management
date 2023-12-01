package Presentacion.Gui.ErrorHandler;

public class PaseErrorHandler {
	
	static Message getMessage(int reasonDigit) {
		switch (reasonDigit) {
		case -1:
			return new Message("Se ha producido un error con la base de datos", "Error base de datos");
		case -2:
			return new Message("No existe ningun pase con el ID introducido", "Pase inexistente");
		case -3:
			return new Message("No existe ninguna sala con el ID introducido", "Sala inexistente");
		case -4: 
			return new Message("La sala con el ID introducido esta inactiva", "Sala inactiva");
		case -5:
			return new Message("No existe ninguna pelicula con el ID introducido", "Pelicula inexistente");
		case -6:
			return new Message("La pelicula con el ID introducido esta inactiva", "Pelicula inactiva");
		case -7:
			return new Message("No existe ninguna compra con el ID introducido", "Compra inexistente");
		case -8: 
			return new Message("El pase esta dado de baja", "Pase inactivo");
		case -9:
			return new Message("Ya se realiza un pase en la sala durante el horario introducido", "Pase activo");
		case -10:
			return new Message("No se puede emitir un pase en una fecha anterior a la actual", "Pase obsoleto");
		case -11:
			return new Message("El precio actual del pase debe ser positivo", "Pase negativo");
		case -12: 
			return new Message("El horario del pase se superpone con algun pase activo", "Horario incompatible");		
		case -13:
			return new Message("No se puede modificar la sala de un pase para el que se hayan vendido entradas", "Pase incluido en compra");
		default:
			return new Message("Ha habido un error desconocido en el tratamiento de datos del pase.", "Error desconocido");
		}
	}
}
