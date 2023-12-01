
package Integracion.Compra;

import java.util.ArrayList;
import Negocio.Compra.TCompra;
import Negocio.Compra.TLineaCompra;


public interface DAOCompra {

	public abstract Integer Create(TCompra compra);
	public abstract TCompra Read(Integer id);
	public abstract Integer Update(TCompra compra);
	public abstract Integer Delete(Integer id);
	public abstract ArrayList<TCompra> ReadAll();
	public abstract ArrayList<TCompra> ReadAllByCliente(Integer idCliente);
	public abstract ArrayList<TCompra> ReadAllByPase(Integer idPase);
	
}