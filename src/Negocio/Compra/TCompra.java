
// @author Beatriz

package Negocio.Compra;

import java.util.Date;


public class TCompra {

	private Integer id;
	private Integer precio_total;
	private Date fecha;
	private Integer idCliente;

	public TCompra() {}
	
	public TCompra(Integer id) {
		this.id = id;
	}
	
	public TCompra(Integer id, Integer precio_total, Date fecha, Integer idCliente) {
		this.id = id;
		this.precio_total = precio_total;
		this.fecha = fecha;
		this.idCliente = idCliente;
	}

	public Integer GetID() {
		return id;
	}

	public void SetID(Integer id) {
		this.id = id;
	}

	public Integer GetPrecioTotal() {
		return precio_total;
	}

	public void SetPrecioTotal(Integer precio_total) {
		this.precio_total = precio_total;
	}

	public Date GetFecha() {
		return fecha;
	}

	public void SetFecha(Date fechaActual) {
		this.fecha = fechaActual;
	}

	public Integer GetIDCliente() {
		return idCliente;
	}

	public void SetIDCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
}