
// @author Santi

package Negocio.Productora;


public class TProductora {
	
	private Integer id;
	private Boolean activo;
	private String nombre;
	private String cif;
	private String direccion;
	private Integer telefono;
	
	public TProductora (Integer id){
		this.id = id;
	}
	
	public TProductora(String nombre, String cif, Integer telefono,String direccion) {
		this.nombre = nombre;
		this.cif = cif;
		this.telefono = telefono;
		this.direccion = direccion;
	}
	
	public TProductora(Integer id, String nombre, String cif, Integer telefono,String direccion, Boolean activo) {
		this.id = id;
		this.nombre = nombre;
		this.cif = cif;
		this.telefono = telefono;
		this.direccion = direccion;
		this.activo = activo;
	}
	
	public Integer GetID() {
		return id;
	}
	
	public void SetID(Integer id) {
		this.id = id;
	}
	
	public Boolean GetActivo() {
		return activo;
	}
	
	public void SetActivo(Boolean activo) {
		this.activo = activo;
	}
	
	public String GetNombre() {
		return nombre;
	}
	
	public void SetNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String GetCIF() {
		return cif;
	}
	
	public void SetCIF(String cif) {
		this.cif = cif;
	}
	
	public String GetDireccion() {
		return direccion;
	}
	
	public void SetDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	public Integer GetTelefono() {
		return telefono;
	}
	
	public void SetTelefono(Integer telefono) {
		this.telefono = telefono;
	}
}