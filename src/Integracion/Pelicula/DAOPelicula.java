
package Integracion.Pelicula;

import Negocio.Pelicula.TPelicula;

import java.util.ArrayList;


public interface DAOPelicula {
	public Integer Create(TPelicula pelicula);
	public TPelicula Read(Integer id);
	public Integer Update(TPelicula pelicula);
	public Integer Delete(Integer id);
	public ArrayList<TPelicula> ReadAll();
	public ArrayList<TPelicula> ReadAllByProductora(Integer id_productora);
	public TPelicula ReadByTitulo(String titulo);
	public Integer AddProductora(Integer idProductora, Integer idPelicula);
	public Integer RemoveProductora(Integer idProductora, Integer idPelicula);
	public Integer LinkedProductora(Integer idProductora, Integer idPelicula);
}