package Presentacion.Gui.ErrorHandler;

public class ErrorHandlerManager {
	
	private static ErrorHandlerManager instance;
	
	public ErrorHandlerManager(){}
	
	public static ErrorHandlerManager getInstance(){
		if(instance == null){
			instance = new ErrorHandlerManager();
		}
		return instance;
	}
	
	public static Message getMessage(EntityEnum entity, int errorCode) {
		
		switch(entity) {
			case CLIENTE:
				return ClienteErrorHandler.getMessage(errorCode);
			case COMPRA:
				return CompraErrorHandler.getMessage(errorCode);
			case PASE:
				return PaseErrorHandler.getMessage(errorCode);
			case PELICULA:
				return PeliculaErrorHandler.getMessage(errorCode);
			case PRODUCTORA:
				return ProductoraErrorHandler.getMessage(errorCode);
			case SALA:
				return SalaErrorHandler.getMessage(errorCode);
			default:
				return new Message("Error desconocido",
						"Ha habido un error desconocido en el tratamiento de datos.");
		}
	}
}
