package Negocio.Pase;

import java.util.Date;

public class TPase {

	private Integer id;
	private Boolean activo;
	private Date horario;
	private Integer precioActual;
	private Integer asientosLibres;
	private Integer idPelicula;
	private Integer idSala;
	
	public TPase() {}
	
	public TPase(Integer id) {
		this.id = id;
	}
	
	public TPase(Date horario, Integer precioActual,
			Integer idPelicula, Integer idSala) {
		this.horario = horario;
		this.precioActual = precioActual;
		this.idPelicula = idPelicula;
		this.idSala = idSala;
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

	public Date GetHorario() {
		return horario;
	}

	public void SetHorario(Date horario) {
		this.horario = horario;
	}

	public Integer GetPrecioActual() {
		return precioActual;
	}

	public void SetPrecioActual(Integer precioActual) {
		this.precioActual = precioActual;
	}

	public Integer GetAsientosLibres() {
		return asientosLibres;
	}

	public void SetAsientosLibres(Integer asientosLibres) {
		this.asientosLibres = asientosLibres;
	}

	public Integer GetIDPelicula() {
		return idPelicula;
	}

	public void SetIDPelicula(Integer idPelicula) {
		this.idPelicula = idPelicula;
	}

	public Integer GetIDSala() {
		return idSala;
	}

	public void SetIDSala(Integer idSala) {
		this.idSala = idSala;
	}
}