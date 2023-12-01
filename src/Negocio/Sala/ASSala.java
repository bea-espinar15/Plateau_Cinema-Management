
package Negocio.Sala;

import java.util.ArrayList;


public interface ASSala {

	public Integer Alta(TSala sala);
	public Integer Baja(Integer id);
	public Integer Modificar(TSala sala);
	public TSala MostrarParaModificar(Integer id);
	public TSala Mostrar(Integer id);
	public ArrayList<TSala> Listar();
	public ArrayList<TSalaEstandar> ListarEstandar();
	public ArrayList<TSalaEspecial> ListarEspecial();
	
}