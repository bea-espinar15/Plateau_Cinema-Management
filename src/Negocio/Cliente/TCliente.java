package Negocio.Cliente;


public class TCliente {
	
	private Integer id;	
	private String dni;	
	private String nombre;	
	private String correo;	
	protected Boolean activo;
	
	public TCliente() {}
	
	public TCliente(Integer id) {
		this.id = id;
	}
	
	public TCliente(Integer id, String dni, String nombre, String correo, Boolean activo) {
		this.id = id;
		this.dni = dni;
		this.nombre = nombre;
		this.correo = correo;
		this.activo = activo;
	}
	
	public Integer GetID() {
		return id;
	}

	public void SetID(Integer id) {
		this.id = id;
	}

	public String GetDNI() {
		return dni;
	}

	public void SetDNI(String dni) {
		this.dni = dni;
	}
	
	public String GetNombre() {
		return nombre;
	}

	public void SetNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String GetCorreo() {
		return correo;
	}

	public void SetCorreo(String correo) {
		this.correo = correo;
	}
	
	public Boolean GetActivo() {
		return activo;
	}

	public void SetActivo(Boolean activo) {
		this.activo = activo;
	}
}