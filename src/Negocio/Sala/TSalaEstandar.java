
// @author Beatriz

package Negocio.Sala;


public class TSalaEstandar extends TSala {
	
	private Boolean aseos;
	
	public TSalaEstandar(){}
	
	public TSalaEstandar(Integer id) {
		super(id);
	}
	
	public TSalaEstandar(Integer id, String nombre, Integer aforo, Boolean activo, Boolean aseos) {
		super(id, nombre, aforo, activo);
		this.aseos = aseos;
	}
	
	public Boolean GetAseos() {
		return aseos;
	}

	public void SetAseos(Boolean aseos) {
		this.aseos = aseos;
	}
}