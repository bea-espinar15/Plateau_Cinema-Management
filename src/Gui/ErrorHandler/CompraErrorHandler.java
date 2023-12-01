package Presentacion.Gui.ErrorHandler;

public class CompraErrorHandler {
	
	static Message getMessage(int reasonDigit) {
		switch (reasonDigit) {
		case -1:
			return new Message("Se ha producido un error con la base de datos", "Error base de datos");
		case -2:
			return new Message("No existe ningun pase con el ID introducido", "Pase inexistente");
		case -3:
			return new Message("El pase con el ID introducido esta dado de baja", "Pase inactivo");
		case -4: 
			return new Message("No existe ninguna compra con el ID introducido", "Compra inexistente");
		case -5:
			return new Message("El pase que se quiere devolver no estaba incluido en la compra", "Pase no incluido");
		case -6:
			return new Message("No existe ningun cliente con el ID introducido", "Cliente inexistente");
		case -7:
			return new Message("El cliente con el ID introducido esta dado de baja", "Cliente inactivo");
		case -8: 
			return new Message("El carrito de la compra esta vacio", "Compra vacia");
		case -9:
			return new Message("No se puede adquirir un pase que ya se emitio", "Pase obsoleto");
		case -10:
			return new Message("No hay suficientes asientos libres para las entradas que se desea comprar", "Espacio limitado");
		case -11:
			return new Message("No existe ninguna pelicula con el ID introducido", "Pelicula inexistente");
		case -12: 
			return new Message("La pelicula con el ID introducido esta dada de baja", "Pelicula inactiva");
		default:
			return new Message("Ha habido un error desconocido en el tratamiento de datos de la compra.", "Error desconocido");
		}
	}
	
}
