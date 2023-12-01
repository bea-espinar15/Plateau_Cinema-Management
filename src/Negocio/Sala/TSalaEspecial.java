
// @author Beatriz

package Negocio.Sala;

public class TSalaEspecial extends TSala {
	
	private Boolean _3D;
	private Boolean _VO;
	
	public TSalaEspecial(){}
	
	public TSalaEspecial(Integer id) {
		super(id);
	}
	
	public TSalaEspecial(Integer id, String nombre, Integer aforo, Boolean activo, Boolean _3D, Boolean _VO) {
		super(id, nombre, aforo, activo);
		this._3D = _3D;
		this._VO = _VO;
	}
	
	public Boolean Get3D() {
		return _3D;
	}

	public void Set3D(Boolean _3D) {
		this._3D = _3D;
	}

	public Boolean GetVO() {
		return _VO;
	}

	public void SetVO(Boolean _VO) {
		this._VO = _VO;
	}
}