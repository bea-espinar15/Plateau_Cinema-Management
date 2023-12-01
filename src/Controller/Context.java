
package Presentacion.Controller;


public class Context {
	
	private ContextEnum CommandEnum;
	private Object Data;
	
	public Context(ContextEnum CommandEnum, Object Data) {
		this.CommandEnum = CommandEnum;
		this.Data = Data;
	}
	
	public ContextEnum getContext() {
		return this.CommandEnum;
	}
	
	public Object getData() {
		return this.Data;
	}
	
}