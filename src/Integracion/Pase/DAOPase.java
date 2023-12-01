
package Integracion.Pase;

import Negocio.Pase.TPase;
import java.util.ArrayList;
import java.util.Date;


public interface DAOPase {

	public Integer Create(TPase pase);
	public TPase Read(Integer id);
	public Integer Update(TPase pase);
	public Integer Delete(Integer id);
	public ArrayList<TPase> ReadAll();
	public ArrayList<TPase> ReadAllBySala(Integer id_sala);
	public ArrayList<TPase> ReadAllByPelicula(Integer id_pelicula);
	public ArrayList<TPase> ReadAllByCompra(Integer id_compra);
	public TPase ReadByKey(Integer id_sala, Date horario);

}