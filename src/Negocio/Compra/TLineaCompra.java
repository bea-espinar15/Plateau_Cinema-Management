
// @author Steven

package Negocio.Compra;

public class TLineaCompra {

	private Integer idCompra;
	private Integer idPase;
	private Integer nEntradas;
	private Integer precio;

	public TLineaCompra() {}
	
	public TLineaCompra(Integer idCompra) {
		this.idCompra = idCompra;
	}
	
	public TLineaCompra(Integer idCompra, Integer idPase, Integer nEntradas, Integer precio) {
		this.idCompra = idCompra;
		this.idPase = idPase;
		this.nEntradas = nEntradas;
		this.precio = precio;
	}
	
	public Integer GetIDCompra() {
		return idCompra;
	}

	public void SetIDCompra(Integer idCompra) {
		this.idCompra = idCompra;
	}

	public Integer GetIDPase() {
		return idPase;
	}

	public void SetIDPase(Integer idPase) {
		this.idPase = idPase;
	}

	public Integer GetNEntradas() {
		return nEntradas;
	}

	public void SetNEntradas(Integer nEntradas) {
		this.nEntradas = nEntradas;
	}

	public Integer GetPrecio() {
		return precio;
	}

	public void SetPrecio(Integer precio) {
		this.precio = precio;
	}	
}