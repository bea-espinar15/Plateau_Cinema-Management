
package Presentacion.Gui.Panels.Compra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import Negocio.Compra.TCompra;
import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;


public class DevolverCompraPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;
	
	private static DevolverCompraPanel instance;
	private Panel_Main_Compra parent;
	private JTextField idCompraField, idPaseField;
	private JSpinner nEntradasS;
	private JButton ok;

	public DevolverCompraPanel() {
		initGUI();	
	}
	
	public static DevolverCompraPanel getInstance() {
		if (instance == null) instance = new DevolverCompraPanel();
		return instance;
	}

	public void setParent(Panel_Main_Compra parent) {
		this.parent = parent;
	}
	
	private void initGUI() {

		this.setLayout(new BorderLayout());
		
		// _______ CABECERA (PAGE_START) ________
		JLabel devolverCompra = new JLabel("DEVOLVER COMPRA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		devolverCompra.setFont(f);
		devolverCompra.setHorizontalAlignment(SwingConstants.CENTER);
		devolverCompra.setForeground(Color.DARK_GRAY);
		devolverCompra.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		this.add(devolverCompra, BorderLayout.PAGE_START);
		
		//______ DATOS _______
		JPanel datos = new JPanel(null);
				
		//_______ CAMPOS ________				
		JPanel idCompra = new JPanel(new FlowLayout());
		idCompra.setBounds(100, 45, 200, 30);
		JLabel idCompraLabel = new JLabel("ID Compra:");
		idCompraField = new JTextField(10);
		idCompra.add(idCompraLabel);
		idCompra.add(idCompraField);
		
		JPanel idPase = new JPanel(new FlowLayout());
		idPase.setBounds(107, 87, 200, 30);
		JLabel idPaseLabel = new JLabel("ID Pase:");
		idPaseField = new JTextField(10);
		idPase.add(idPaseLabel);
		idPase.add(idPaseField);
		
		JPanel nEntradas = new JPanel(new FlowLayout());
		nEntradas.setBounds(23, 130, 280, 50);
		JLabel nEntradasLabel = new JLabel("Entradas:");
		SpinnerModel model = new SpinnerNumberModel(1, 1, 100, 1);
		nEntradasS = new JSpinner(model);
		nEntradasS.setPreferredSize(new Dimension(35, 22));
		nEntradas.add(nEntradasLabel);
		nEntradas.add(nEntradasS);
		
		ok = new JButton("Devolver");
		ok.setBounds(390, 50, 90, 25);
		ok.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					ArrayList<Integer> devolucion = new ArrayList<Integer>();
					devolucion.add(Integer.parseInt(idCompraField.getText()));
					devolucion.add(Integer.parseInt(idPaseField.getText()));
					devolucion.add((Integer)nEntradasS.getValue());
					Context request = new Context(ContextEnum.DEVOLVERCOMPRA, devolucion);
					ApplicationController.GetInstance().ManageRequest(request);
				}
				else {
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error", JOptionPane.ERROR_MESSAGE);
				}
				clear();
			}
			
		});
		
		datos.add(idCompra);
		datos.add(idPase);
		datos.add(nEntradas);
		datos.add(ok);
		
		this.add(datos, BorderLayout.CENTER);
		
	}

	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.COMPRA, (Integer)o);
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		}
		else {
			JOptionPane.showMessageDialog(parent, "La compra ha sido devuelta con exito");
			clear();
		}
	}

	public boolean hasError(Object response) {
		if ((Integer)response != 0)
			return true;
		return false;
	}

	public boolean validation() {
		try {
			if (idCompraField.getText().equals("") || idPaseField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(idCompraField.getText()) < 0 || Integer.parseInt(idPaseField.getText()) < 0)
				throw new Exception();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public void clear() {
		idCompraField.setText("");
		idPaseField.setText("");
		nEntradasS.setValue(1);
	}
	
}