
package Negocio.Pase;

import java.util.ArrayList;


public interface ASPase {
	
	public Integer Alta(TPase pase);
	public Integer Baja(Integer id);
	public Integer Modificar(TPase pase);
	public TPase Mostrar(Integer id);
	public TPase MostrarParaModificar(Integer id);
	public ArrayList<TPase> Listar();
	public ArrayList<TPase> ListarPorSala(Integer idSala);
	public ArrayList<TPase> ListarPorPelicula(Integer idPelicula);
	public ArrayList<TPase> ListarPorCompra(Integer idCompra);
	
}