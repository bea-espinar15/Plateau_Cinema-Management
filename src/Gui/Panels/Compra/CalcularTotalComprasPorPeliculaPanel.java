/**
 * 
 */
package Presentacion.Gui.Panels.Compra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;

public class CalcularTotalComprasPorPeliculaPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;
	
	private static CalcularTotalComprasPorPeliculaPanel instance;
	private Panel_Main_Compra parent;
	private JTextField idField, nComprasField;
	private JButton ok;

	public CalcularTotalComprasPorPeliculaPanel() {
		initGUI();	
	}
	
	public static CalcularTotalComprasPorPeliculaPanel getInstance() {
		if (instance == null) instance = new CalcularTotalComprasPorPeliculaPanel();
		return instance;
	}

	public void setParent(Panel_Main_Compra parent) {
		this.parent = parent;
	}
	
	private void initGUI() {

		this.setLayout(new BorderLayout());
		
		// _______ CABECERA (PAGE_START) ________
		JLabel calcularComprasPorPelicula = new JLabel("COMPRAS POR PELICULA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		calcularComprasPorPelicula.setFont(f);
		calcularComprasPorPelicula.setHorizontalAlignment(SwingConstants.CENTER);
		calcularComprasPorPelicula.setForeground(Color.DARK_GRAY);
		calcularComprasPorPelicula.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		this.add(calcularComprasPorPelicula, BorderLayout.PAGE_START);
		
		//______ DATOS _______
		JPanel datos = new JPanel(null);
				
		//_______ CAMPOS ENTRADA ________				
		JPanel idPelicula = new JPanel(new FlowLayout());
		idPelicula.setBackground(Color.LIGHT_GRAY);
		idPelicula.setBounds(200, 45, 210, 30);
		JLabel idPeliculaLabel = new JLabel("ID Pelicula:");
		idField = new JTextField(10);
		idPelicula.add(idPeliculaLabel);
		idPelicula.add(idField);
		
		ok = new JButton("Calcular");
		ok.setBounds(418, 45, 100, 30);
		ok.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(validation()){
					Context request = new Context(ContextEnum.CALCULARTOTALCOMPRASPORPELICULA, Integer.parseInt(idField.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
				}else{
					JOptionPane.showMessageDialog(parent,"Los datos introducidos son sintacticamente erroneos","Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		// ______ CAMPOS SALIDA _______
		JPanel nCompras = new JPanel(new FlowLayout());
		nCompras.setBounds(60, 130, 400, 50);
		JLabel nComprasLabel = new JLabel("Numero de compras:");
		nComprasField = new JTextField(10);
		nComprasField.setEditable(false);;
		nCompras.add(nComprasLabel);
		nCompras.add(nComprasField);
		
		datos.add(idPelicula);
		datos.add(nCompras);
		datos.add(ok);
		
		this.add(datos, BorderLayout.CENTER );
		
	}


	public void update(Object o) {
		if(hasError(o)){
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.COMPRA, (Integer)o);
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		}else{
			this.nComprasField.setText(((Integer)o).toString());
		}
	}

	public boolean hasError(Object response) {
		if ((Integer)response < 0)
			return true;
		return false;
	}

	public boolean validation() {
		try {
			if (idField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(idField.getText()) < 0)
				throw new Exception();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public void clear() {
		idField.setText("");
		nComprasField.setText("");
	}
}