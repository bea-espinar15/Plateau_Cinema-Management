/**
 * 
 */
package Presentacion.Gui.Panels;

import javax.swing.JPanel;

public abstract class GeneralPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public abstract void update(Object o);
	public abstract boolean hasError(Object response);
	public abstract boolean validation();
	public abstract void clear();
}