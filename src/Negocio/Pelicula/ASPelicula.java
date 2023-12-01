
package Negocio.Pelicula;

import java.util.ArrayList;;


public interface ASPelicula {
	
	public Integer Alta(TPelicula pelicula);
	public Integer Baja(Integer id);
	public TPelicula MostrarParaModificar(Integer id);
	public Integer Modificar(TPelicula pelicula);
	public TPelicula Mostrar(Integer id);	
	public ArrayList<TPelicula> Listar();	
	public ArrayList<TPelicula> ListarPorProductora(Integer idProductora);
	public Integer AnadirProductora(Integer idProductora, Integer idPelicula);
	public Integer QuitarProductora(Integer idProductora, Integer idPelicula);
	
}