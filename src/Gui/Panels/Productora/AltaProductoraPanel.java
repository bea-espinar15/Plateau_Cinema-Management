 	 
package Presentacion.Gui.Panels.Productora;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;

import Negocio.Pase.TPase;
import Negocio.Productora.TProductora;
import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;


public class AltaProductoraPanel extends GeneralPanel {

	// cambiamos serialversionUID por el creado por defecto
	private static final long serialVersionUID = 1L;
	private static AltaProductoraPanel instance;
	private Panel_Main_Productora parent;
	// atributos usados para interaccion usuario:
	private JTextField tlfField, direccionField, cifField, nombreField;

	public AltaProductoraPanel() {
		// finished
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		// __________ CABECERA _________
		JLabel titleLbl = new JLabel("ALTA PRODUCTORA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		titleLbl.setFont(f);
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setForeground(Color.DARK_GRAY);
		titleLbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(titleLbl, BorderLayout.PAGE_START);

		// ___________ CENTER ______________
		JPanel centerPanel = new JPanel(null);

		// ___________ CENTER - ALL FIELDS ______________
		JPanel cifPanel = new JPanel(new FlowLayout());
		cifPanel.setBounds(65, 55, 250, 30);
		JLabel cifLbl = new JLabel("CIF: ");
		this.cifField = new JTextField(10);
		cifPanel.add(cifLbl);
		cifPanel.add(cifField);

		centerPanel.add(cifPanel);

		JPanel nombrePanel = new JPanel(new FlowLayout());
		nombrePanel.setBounds(51, 80, 250, 30);
		JLabel nombreLbl = new JLabel("Nombre: ");
		this.nombreField = new JTextField(10);
		nombrePanel.add(nombreLbl);
		nombrePanel.add(nombreField);

		centerPanel.add(nombrePanel);

		JPanel tlfPanel = new JPanel(new FlowLayout());
		tlfPanel.setBounds(49, 105, 250, 30);
		JLabel tlfLbl = new JLabel("Telefono: ");
		this.tlfField = new JTextField(10);
		tlfPanel.add(tlfLbl);
		tlfPanel.add(tlfField);

		centerPanel.add(tlfPanel);

		JPanel direccionPanel = new JPanel(new FlowLayout());
		direccionPanel.setBounds(47, 130, 250, 30);
		JLabel direccionLbl = new JLabel("Direccion: ");
		this.direccionField = new JTextField(10);
		direccionPanel.add(direccionLbl);
		direccionPanel.add(direccionField);

		centerPanel.add(direccionPanel);

		JButton aceptarBtn = new JButton("Aceptar");
		aceptarBtn.setBounds(158, 205, 100, 30);
		aceptarBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					TProductora p = new TProductora(1,nombreField.getText(),cifField.getText(),(int)Integer.parseInt(tlfField.getText()),direccionField.getText(),true);
					//Integer id, String nombre, String cif, Integer telefono,String direccion, Boolean activo
  					Context request = new Context(ContextEnum.ALTAPRODUCTORA, p);
  					ApplicationController.GetInstance().ManageRequest(request);
				} else {
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		centerPanel.add(aceptarBtn);

		this.add(centerPanel, BorderLayout.CENTER);
	}

	public static AltaProductoraPanel getInstance() {
		if (instance == null) {
			instance = new AltaProductoraPanel();
		}
		return instance;
	}

	public void setParent(Panel_Main_Productora parent) {
		this.parent = parent;
	}

	@Override
	public void update(Object o) {
		if(hasError(o)){
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.PRODUCTORA, (Integer)o);
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		}
		else{
			JOptionPane.showMessageDialog(parent,"La Productora se ha creado con exito con ID: " + (Integer)o);
			clear();
		}
	}

	@Override
	public boolean hasError(Object response) {
		return (Integer)response < 0;
	}

	@Override
	public boolean validation() {
		boolean ok = false;
		try{
			if(Integer.parseInt(this.tlfField.getText()) > 0
					&& !this.cifField.getText().equals("") 
					&& !this.nombreField.getText().equals("") 
					&& !this.direccionField.getText().equals("")){
						ok = true;
			}
			return ok;
		}
		catch(Exception e){
			return false;
		}
	}

	@Override
	public void clear() {
		tlfField.setText("");
		direccionField.setText("");
		cifField.setText("");
		nombreField.setText("");
	}
}