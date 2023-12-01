package Negocio.Cliente;


public class TClienteNormal extends TCliente {
	
	private Integer facturacion;

	public TClienteNormal() {
		super();
	}
	
	public TClienteNormal(Integer id){
		super(id);
	}
	
	public TClienteNormal(Integer id, String dni, String nombre, String correo, Integer facturacion, Boolean activo) {
		super(id, dni, nombre, correo, activo);
		this.facturacion = facturacion;
	}

	public Integer GetFacturacion() {
		return facturacion;
	}

	public void SetFacturacion(Integer facturacion) {
		this.facturacion = facturacion;
	}
}