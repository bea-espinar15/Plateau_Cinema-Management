package Negocio.Cliente;


public class TClienteVip extends TCliente {
	
	private Integer descuento;	
	private Integer antiguedad;

	public TClienteVip() {
		super();
	}
	
	public TClienteVip(Integer id){
		super(id);
	}
	
	public TClienteVip(Integer id, String dni, String nombre, String correo, Integer descuento, Integer antiguedad, Boolean activo){
		super(id, dni, nombre, correo, activo);
		this.antiguedad = antiguedad;
		this.descuento = descuento;
	}
	
	public Integer GetDescuento() {
		return descuento;
	}

	public void SetDescuento(Integer descuento) {
		this.descuento = descuento;
	}

	public Integer GetAntiguedad() {
		return antiguedad;
	}

	public void SetAntiguedad(Integer antiguedad) {
		this.antiguedad = antiguedad;
	}
}