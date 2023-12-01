package Presentacion.Gui.ErrorHandler;

public class SalaErrorHandler {
	
	static Message getMessage(int reasonDigit) {
		switch (reasonDigit) {
		case -1:
			return new Message("Se ha producido un error con la base de datos", "Error base de datos");
		case -2:
			return new Message("Ya existe una sala activa con el nombre introducido", "Sala activa");
		case -3:
			return new Message("No existe ninguna sala con el ID introducido", "Sala inexistente");
		case -4: 
			return new Message("La sala esta dada de baja", "Sala inactiva");
		case -5:
			return new Message("Ya existe una sala con el nombre introducido", "Nombre repetido");
		case -6:
			return new Message("No se puede cambiar el aforo de una sala donde se emite un pase del que se han vendido entradas", "Pase comprado");
		default:
			return new Message("Ha habido un error desconocido en el tratamiento de datos de la sala.", "Error desconocido");
		}
	}
}
