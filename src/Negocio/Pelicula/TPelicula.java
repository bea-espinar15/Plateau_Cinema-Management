
package Negocio.Pelicula;


public class TPelicula {
	
	private Integer id;
	private Boolean activo;
	private String titulo;
	private String director;	
	private String genero;	
	private Integer duracion;
	
	public TPelicula(){}
	
	public TPelicula(Integer id){
		this.id = id;
	}
	
	public TPelicula(String titulo, String director, String genero, Integer duracion) {
		this.titulo = titulo;
		this.director = director;
		this.genero = genero;
		this.duracion = duracion;
	}
	
	public TPelicula(Integer id, Boolean activo, String titulo, String director, String genero, Integer duracion) {
		this.id = id;
		this.activo = activo;
		this.titulo = titulo;
		this.director = director;
		this.genero = genero;
		this.duracion = duracion;
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

	public String GetTitulo() {
		return titulo;
	}

	public void SetTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String GetDirector() {
		return director;
		
	}

	public void SetDirector(String director) {
		this.director = director;
	}

	public String GetGenero() {
		return genero;
	}

	public void SetGenero(String genero) {
		this.genero = genero;
	}

	public Integer GetDuracion() {
		return duracion;
	}

	public void SetDuracion(Integer duracion) {
		this.duracion = duracion;
	}
}