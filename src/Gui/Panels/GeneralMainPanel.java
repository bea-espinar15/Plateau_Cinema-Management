/**
 * 
 */
package Presentacion.Gui.Panels;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class GeneralMainPanel extends JPanel {

	public abstract void initPanels();
	public abstract void hidePanels();
	public abstract void showPanel(GeneralPanel panel);
}