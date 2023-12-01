
// @author Santi

package Negocio.Productora;

import java.util.Date;
import java.util.ArrayList;
import utilities.Pair;


public interface ASProductora {
	
	public Integer Alta(TProductora productora);
	public Integer Baja(Integer id);
	public Integer Modificar(TProductora productora);
	public TProductora Mostrar(Integer id);
	public ArrayList<TProductora> Listar();
	public ArrayList<TProductora> ListarPorPelicula(Integer idPelicula);
	public Pair<ArrayList<TProductora>,Integer> ProductoraMasIngresos(Pair<Date, Date> input);
	public TProductora MostrarParaModificar(Integer id);
	
}