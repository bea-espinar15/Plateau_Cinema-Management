
// @author Beatriz

package Negocio.Compra;

import java.util.ArrayList;
import java.util.HashMap;


public interface ASCompra {

	public Integer Cerrar(TCompra compra, HashMap<Integer, TLineaCompra> compraPases);
	public TCompra Mostrar(Integer id);
	public ArrayList<TCompra> Listar();
	public TCompraEnDetalle MostrarEnDetalle(Integer id);
	public ArrayList<TCompra> ListarPorCliente(Integer idCliente);
	public ArrayList<TCompra> ListarPorPase(Integer idPase);
	public Integer CalcularTotalComprasPorPelicula(Integer idPelicula);
	public Integer DevolverCompra(Integer idCompra, Integer idPase, Integer entradasDevueltas);
	public ArrayList<TLineaCompra> VerResumenDeCompra(Integer idCompra);
	
}