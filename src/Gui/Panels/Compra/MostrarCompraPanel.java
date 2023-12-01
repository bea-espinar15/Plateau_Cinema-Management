
package Presentacion.Gui.Panels.Compra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Negocio.Compra.TCompra;
import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;

public class MostrarCompraPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;
	
	private static MostrarCompraPanel instance;
	private Panel_Main_Compra parent;
	private JTextField idField, precioTotalField, fechaField, idClienteField;
	private JButton ok;
	
	public MostrarCompraPanel(){
		initGUI();
	}
	
	public static MostrarCompraPanel getInstance() {
		if (instance == null) instance = new MostrarCompraPanel();
		return instance;
	}	
	
	public void setParent(Panel_Main_Compra parent) {
		this.parent = parent;
	}
	
	private void initGUI() {
		
		this.setLayout(new BorderLayout());
		
		// _______ CABECERA (PAGE_START) ________
		JLabel mostrarCompra = new JLabel("MOSTRAR COMPRA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		mostrarCompra.setFont(f);
		mostrarCompra.setHorizontalAlignment(SwingConstants.CENTER);
		mostrarCompra.setForeground(Color.DARK_GRAY);
		mostrarCompra.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		this.add(mostrarCompra, BorderLayout.PAGE_START);
		
		//______ DATOS _______
		JPanel datos = new JPanel(null);
				
		//_______ CAMPOS ENTRADA ________				
		JPanel idPanel = new JPanel(new FlowLayout());
		idPanel.setBackground(Color.LIGHT_GRAY);
		idPanel.setBounds(228, 35, 200, 30);
		JLabel id = new JLabel("ID:");
		this.idField = new JTextField(10);
		idPanel.add(id);
		idPanel.add(idField);
		
		ok = new JButton("Mostrar");
		ok.setBounds(438, 35, 100, 30);
		ok.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					Context request = new Context(ContextEnum.MOSTRARCOMPRA, Integer.parseInt(idField.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
				}
				else {
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		// ______ CAMPOS SALIDA _______
		JPanel precioTotal = new JPanel(new FlowLayout());
		precioTotal.setBounds(100, 125, 200, 30);
		JLabel precioTotalLabel = new JLabel("Precio total:");
		precioTotalField = new JTextField(10);
		precioTotalField.setEditable(false);
		precioTotal.add(precioTotalLabel);
		precioTotal.add(precioTotalField);
		
		JPanel fecha = new JPanel(new FlowLayout());
		fecha.setBounds(75, 165, 280, 30);
		JLabel fechaLabel = new JLabel("Fecha:");
		fechaField = new JTextField(10);
		fechaField.setEditable(false);
		fecha.add(fechaLabel);
		fecha.add(fechaField);
		
		JPanel idCliente = new JPanel(new FlowLayout());
		idCliente.setBounds(62, 205, 300, 30);
		JLabel idClienteLabel = new JLabel("Cliente:");
		idClienteField = new JTextField(10);
		idClienteField.setEditable(false);
		idCliente.add(idClienteLabel);
		idCliente.add(idClienteField);
		
		datos.add(idPanel);
		datos.add(precioTotal);
		datos.add(fecha);
		datos.add(idCliente);
		datos.add(ok);
		
		this.add(datos, BorderLayout.CENTER);
		
	}	

	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.COMPRA, ((TCompra)o).GetID());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		}
		else {
			precioTotalField.setText(((TCompra)o).GetPrecioTotal().toString()); 
			fechaField.setText(new SimpleDateFormat("dd-MM-yyyy").format(((TCompra)o).GetFecha())); 
			idClienteField.setText(((TCompra)o).GetIDCliente().toString());
		}
	}

	public boolean hasError(Object response) {
		if (((TCompra)response).GetID() < 0)
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
		precioTotalField.setText(""); 
		fechaField.setText(""); 
		idClienteField.setText("");
	}
	
}