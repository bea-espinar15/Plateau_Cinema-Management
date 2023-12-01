
package Integracion.Compra;

import Negocio.Compra.TLineaCompra;

import java.util.ArrayList;

public interface DAOLineaCompra {
	
	public Integer Create(TLineaCompra lineaCompra);
	public TLineaCompra Read(Integer idPase, Integer idCompra);
	public Integer Update(TLineaCompra lineaCompra);
	public Integer Delete(Integer idCompra, Integer idPase);
	public ArrayList<TLineaCompra> ReadAll();
	public ArrayList<TLineaCompra> ReadAllByCompra(Integer idCompra);

}