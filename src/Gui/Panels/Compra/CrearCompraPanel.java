/**
 * 
 */
package Presentacion.Gui.Panels.Compra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import Negocio.Compra.TCompra;
import Negocio.Compra.TLineaCompra;
import Presentacion.Gui.Panels.GeneralPanel;


public class CrearCompraPanel extends GeneralPanel {
	
	
	private static final long serialVersionUID = 1L;
	
	private static CrearCompraPanel instance;
	private Panel_Main_Compra parent;
	private AbrirCompraPanel abrirCompraPanel;
	private CerrarCompraPanel cerrarCompraPanel;

	
	public CrearCompraPanel(){
		initGUI();
	}
	
	public static CrearCompraPanel getInstance() {
		if(instance == null)
			instance = new CrearCompraPanel();
		return instance;
	}


	public void setParent(Panel_Main_Compra parent) {
		this.parent = parent;
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());
		initPanels();
		showAbrirPanel();
	}
	
	public void showCerrarPanel(TCompra compra, HashMap<Integer,TLineaCompra>carrito){
		abrirCompraPanel.setVisible(false);
		cerrarCompraPanel.setTCompra(compra);
		cerrarCompraPanel.setCarrito(carrito);
		this.add(cerrarCompraPanel, BorderLayout.CENTER);
		cerrarCompraPanel.setVisible(true);
	}
	
	public void showAbrirPanel(){
		cerrarCompraPanel.setVisible(false);
		this.add(abrirCompraPanel, BorderLayout.CENTER);
		abrirCompraPanel.setVisible(true);
	}
	
	private void initPanels(){
		abrirCompraPanel = AbrirCompraPanel.getInstance();
		abrirCompraPanel.setParent(this);
		cerrarCompraPanel = CerrarCompraPanel.getInstance();
		cerrarCompraPanel.setParent(this);
	}
	@Override
	public void update(Object o) {}
	
	@Override
	public boolean validation() {
		return true;
	}

	@Override
	public boolean hasError(Object response) {
		return false;
	}

	@Override
	public void clear() {
		abrirCompraPanel.setVisible(true);
		abrirCompraPanel.clear();
		cerrarCompraPanel.setVisible(false);
		cerrarCompraPanel.clear();
	}
	
}