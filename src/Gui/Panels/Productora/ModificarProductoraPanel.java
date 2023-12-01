
package Presentacion.Gui.Panels.Productora;

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

import Negocio.Productora.TProductora;


import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;


public class ModificarProductoraPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;
	private JTextField idField, tlfField, direccionField, cifField, nombreField;
	private static ModificarProductoraPanel instance;
	private Panel_Main_Productora parent;
	private JButton ok, modificar;
	private boolean buscado;

	public ModificarProductoraPanel() {
		buscado = false;
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		// __________ CABECERA _________
		JLabel titleLbl = new JLabel("MODIFICAR PRODUCTORA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		titleLbl.setFont(f);
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setForeground(Color.DARK_GRAY);
		titleLbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(titleLbl, BorderLayout.PAGE_START);

		// ___________ CENTER ______________
		JPanel centerPanel = new JPanel(null);

		JPanel idPanel = new JPanel(new FlowLayout());
		idPanel.setBackground(Color.LIGHT_GRAY);
		idPanel.setBounds(100, 35, 170, 30);
		JLabel idLabel = new JLabel("ID:");
		this.idField = new JTextField(10);
		idPanel.add(idLabel);
		idPanel.add(idField);

		// ACEPTAR ID
		ok = new JButton("Buscar");
		ok.setBounds(280, 35, 80, 30);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (validation()) {
					Context c = new Context(ContextEnum.MOSTRARPRODUCTORAPARAMODIFICAR, Integer.parseInt(idField.getText()));
					ApplicationController.GetInstance().ManageRequest(c);
					
				} else{
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error",JOptionPane.ERROR_MESSAGE);
				}
					
			}
		});

		centerPanel.add(idPanel);
		centerPanel.add(ok);

		// ___________ CENTER - OTHER FIELDS ______________
		JPanel cifPanel = new JPanel(new FlowLayout());
		cifPanel.setBounds(55, 105, 250, 30);
		JLabel cifLbl = new JLabel("CIF: ");
		this.cifField = new JTextField(10);
		cifField.setEditable(false);
		cifPanel.add(cifLbl);
		cifPanel.add(cifField);

		centerPanel.add(cifPanel);

		JPanel nombrePanel = new JPanel(new FlowLayout());
		nombrePanel.setBounds(41, 130, 250, 30);
		JLabel nombreLbl = new JLabel("Nombre: ");
		this.nombreField = new JTextField(10);
		nombreField.setEditable(false);
		nombrePanel.add(nombreLbl);
		nombrePanel.add(nombreField);

		centerPanel.add(nombrePanel);

		JPanel tlfPanel = new JPanel(new FlowLayout());
		tlfPanel.setBounds(39, 155, 250, 30);
		JLabel tlfLbl = new JLabel("Telefono: ");
		this.tlfField = new JTextField(10);
		tlfField.setEditable(false);
		tlfPanel.add(tlfLbl);
		tlfPanel.add(tlfField);

		centerPanel.add(tlfPanel);

		JPanel direccionPanel = new JPanel(new FlowLayout());
		direccionPanel.setBounds(37, 180, 250, 30);
		JLabel direccionLbl = new JLabel("Direccion: ");
		this.direccionField = new JTextField(10);
		direccionField.setEditable(false);
		direccionPanel.add(direccionLbl);
		direccionPanel.add(direccionField);

		centerPanel.add(direccionPanel);

		modificar = new JButton("Modificar");
		modificar.setBounds(150, 250, 100, 30);
		modificar.setEnabled(false);
		modificar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (validation()) {
					TProductora p = new TProductora(Integer.parseInt(idField.getText()),nombreField.getText(),cifField.getText(),(int)Integer.parseInt(tlfField.getText()),direccionField.getText(),true);
  					Context request = new Context(ContextEnum.MODIFICARPRODUCTORA, p);
  					ApplicationController.GetInstance().ManageRequest(request);
				} else {
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		centerPanel.add(modificar);

		this.add(centerPanel, BorderLayout.CENTER);
	}

	public static ModificarProductoraPanel getInstance() {
		if (instance == null) {
			instance = new ModificarProductoraPanel();
		}
		return instance;
	}

	public void setParent(Panel_Main_Productora parent) {
		this.parent = parent;
	}

	public void update(Object o) {
		if(!buscado){
			if(hasError(o)){
				ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
				Message msg = ehm.getMessage(EntityEnum.PRODUCTORA, ((TProductora)o).GetID());
				JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
				clear();
			}else{
				TProductora aux = (TProductora)o;
				tlfField.setText(aux.GetTelefono().toString());
				tlfField.setEditable(true);
				direccionField.setText(aux.GetDireccion());
				direccionField.setEditable(true);
				cifField.setText(aux.GetCIF());
				cifField.setEditable(true);
				nombreField.setText(aux.GetNombre());
				nombreField.setEditable(true);
				modificar.setEnabled(true);
				idField.setEditable(false);
				ok.setEnabled(false);
				buscado = true;
			}
		}else{
			if (hasError(o)) {	
				Message msg = ErrorHandlerManager.getMessage(EntityEnum.PRODUCTORA, (Integer)o);
				JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
				clear();
			}
			else{
				JOptionPane.showMessageDialog(parent,"La Productora se ha modificado con exito ");
				clear();
			}
		}
		
	}

	public boolean hasError(Object response) {
		if (!buscado) {
			return ((TProductora)response).GetID() < 0;
		} else {
			return (Integer)response < 0;
		}
	}

	public void clear() {
		idField.setText("");
		tlfField.setText("");
		direccionField.setText("");
		cifField.setText("");
		nombreField.setText("");
		idField.setEditable(true);
		tlfField.setEditable(false);
		direccionField.setEditable(false);
		cifField.setEditable(false);
		nombreField.setEditable(false);
		ok.setEnabled(true);
		modificar.setEnabled(false);
		this.buscado = false;
	}

	@Override
	public boolean validation() {
		
		try{
			if(!buscado){
				if(idField.getText().isEmpty()) return false;
				Integer aux = Integer.parseInt(idField.getText());
				if(aux < 0) return false;
			}else{
				if(nombreField.getText().isEmpty()) return false;
				if(cifField.getText().isEmpty()) return false;
				if(direccionField.getText().isEmpty()) return false;
				if(tlfField.getText().isEmpty()) return false;
				Integer aux = Integer.parseInt(tlfField.getText());
				if(aux < 0) return false;
			}
		
			return true;
		}catch(Exception e){
			return false;
		}
	}
}