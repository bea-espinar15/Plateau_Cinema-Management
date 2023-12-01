package Presentacion.Gui.ErrorHandler;

public class ProductoraErrorHandler {
	
	static Message getMessage(int reasonDigit) {
		switch (reasonDigit) {
		case -1:
			return new Message("Se ha producido un error con la base de datos", "Error base de datos");
		case -2:
			return new Message("Ya existe una productora activa con el nombre introducido", "Productora activa");
		case -3:
			return new Message("Ya existe una productora activa con el CIF introducido", "Productora activa");
		case -4: 
			return new Message("El telefono debe ser un numero de 9 dígitos", "Telefono erroneo");
		case -5:
			return new Message("No existe ninguna productora con el ID introducido", "Productora inexistente");
		case -6:
			return new Message("La productora esta dada de baja", "Productora inactiva");
		case -7:
			return new Message("No existe ninguna pelicula con el ID introducido", "Pelicula inexistente");
		case -8: 
			return new Message("La pelicula con el ID introducido esta dada de baja", "Pelicula inactiva");
		case -9:
			return new Message("Las fechas deben ser anteriores a la fecha actual", "Fechas incorrectas");
		case -10:
			return new Message("La fecha final debe ser posterior a la inicial", "Fechas incompatibles");
		case -11:
			return new Message("No existe ninguna productora activa", "No hay productoras");
		case -12:
			return new Message("Ya existe una productora con el nombre introducido", "Nombre repetido");
		case -13:
			return new Message("Ya existe una productora con el CIF introducido", "CIF repetido");
		default:
			return new Message("Ha habido un error desconocido en el tratamiento de datos de la productora.", "Error desconocido");
		}
	}
}
