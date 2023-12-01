
package Integracion.Sala;

import Negocio.Sala.TSala;

import java.util.ArrayList;
import Negocio.Sala.TSalaEstandar;
import Negocio.Sala.TSalaEspecial;


public interface DAOSala {
	
	public Integer Create(TSala sala);
	public TSala Read(Integer id);
	public Integer Update(TSala sala);
	public Integer Delete(Integer id);
	public ArrayList<TSala> ReadAll();
	public ArrayList<TSalaEstandar> ReadAllEstandar();
	public ArrayList<TSalaEspecial> ReadAllEspecial();
	public TSala ReadByNombre(String nombre);
	
}