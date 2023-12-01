
package Presentacion.Command;

import Presentacion.Controller.Context;


public abstract interface Command {
	public abstract Context Execute(Object input);
}