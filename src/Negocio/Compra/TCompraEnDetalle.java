
// @author Steven

package Negocio.Compra;

import Negocio.Cliente.TCliente;
import java.util.ArrayList;
import Negocio.Pase.TPase;


public class TCompraEnDetalle {

	private TCompra tCompra;
	private TCliente tCliente;
	private ArrayList<TPase> tPases;
	
	public TCompraEnDetalle(){
	}

	public TCompra GetCompra() {
		return tCompra;
	}

	public void SetCompra(TCompra tCompra) {
		this.tCompra = tCompra;
	}

	public TCliente GetCliente() {
		return tCliente;
	}

	public void SetCliente(TCliente tCliente) {
		this.tCliente = tCliente;
	}

	public ArrayList<TPase> GetPases() {
		return tPases;
	}

	public void SetPase(ArrayList<TPase> tPases) {
		this.tPases = tPases;
	}
	
}