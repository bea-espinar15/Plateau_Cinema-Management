package Presentacion.Gui.ErrorHandler;

public class PeliculaErrorHandler {
	
	static Message getMessage(int reasonDigit) {
		switch (reasonDigit) {
		case -1:
			return new Message("Se ha producido un error con la base de datos", "Error base de datos");
		case -2:
			return new Message("No existe ninguna pelicula con el ID introducido", "Pelicula inexistente");
		case -3:
			return new Message("La pelicula esta dada de baja", "Pelicula inactiva");
		case -4: 
			return new Message("Ya existe una pelicula activa con el titulo introducido", "Pelicula activa");
		case -5:
			return new Message("No existe ninguna productora con el ID introducido", "Productora inexistente");
		case -6:
			return new Message("La productora esta dada de baja", "Productora inactiva");
		case -7: 
			return new Message("La pelicula ya tenia asociada la productora introducida", "Vinculacion existente");
		case -8:
			return new Message("La pelicula y la productora no estaban asociadas", "Vinculacion inexistente");
		case -9:
			return new Message("Ya existe una pelicula con el titulo introducido", "Titulo repetido");
		default:
			return new Message("Ha habido un error desconocido en el tratamiento de datos de la pelicula.", "Error desconocido");
		}
	}
}
