
package Negocio.Sala;


public class TSala {
	
	private Integer id;
	private String nombre;
	private Integer aforo;
	private Boolean activo;
	
	public TSala(){}
	
	public TSala(Integer id) {
		this.id = id;
	}
	
	public TSala(Integer id, String nombre, Integer aforo, Boolean activo) {
		this.id=id;
		this.nombre=nombre;
		this.aforo=aforo;
		this.activo=activo;	
	}
	
	public Integer GetId() {
		return id;
	}

	public void SetId(Integer id) {
		this.id = id;
	}

	public String GetNombre() {
		return nombre;
	}

	public void SetNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer GetAforo() {
		return aforo;		
	}

	public void SetAforo(Integer aforo) {
		this.aforo = aforo;
	}
	
	public Boolean GetActivo() {
		return activo;
	}

	public void SetActivo(Boolean activo) {
		this.activo = activo;
	}
}