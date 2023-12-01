
package Integracion.Productora;

import Negocio.Productora.TProductora;

import java.util.ArrayList;


public interface DAOProductora {
	
	public Integer Create(TProductora productora);
	public TProductora Read(Integer id);
	public Integer Update(TProductora productora);
	public Integer Delete(Integer id);
	public ArrayList<TProductora> ReadAll();
	public ArrayList<TProductora> ReadAllByPelicula(Integer idPelicula);
	public TProductora ReadByCIF(String cif);
	public TProductora ReadByNombre(String nombre);
	
}