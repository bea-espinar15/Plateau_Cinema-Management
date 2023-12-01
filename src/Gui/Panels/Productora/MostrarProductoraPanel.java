
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

public class MostrarProductoraPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;
	private static MostrarProductoraPanel instance;
	private Panel_Main_Productora parent;
	private JTextField idField, activoField, cifField, nombreField, direccionField, tlfField;

	public MostrarProductoraPanel() {
		initGUI();
	}

	public static MostrarProductoraPanel getInstance() {
		if (instance == null)
			instance = new MostrarProductoraPanel();
		return instance;
	}

	public void setParent(Panel_Main_Productora parent) {
		this.parent = parent;
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		// __________ CABECERA _________
		JLabel titleLbl = new JLabel("MOSTRAR PRODUCTORA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		titleLbl.setFont(f);
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setForeground(Color.DARK_GRAY);
		titleLbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(titleLbl, BorderLayout.PAGE_START);

		// ___________ CENTER ______________
		JPanel centerPanel = new JPanel(null);

		// ___________ CENTER - ID FIELD + BUTTON ______________
		JPanel idPanel = new JPanel(new FlowLayout());
		idPanel.setBackground(Color.LIGHT_GRAY);
		idPanel.setBounds(200, 35, 200, 30);
		JLabel idLbl = new JLabel("ID:");
		this.idField = new JTextField(10);
		idPanel.add(idLbl);
		idPanel.add(idField);

		centerPanel.add(idPanel);

		JButton mostrarBtn = new JButton("Mostrar");
		mostrarBtn.setBounds(400, 35, 100, 30);
		mostrarBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {

					Context request = new Context(ContextEnum.MOSTRARPRODUCTORA, Integer.parseInt(idField.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
				} else {
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		centerPanel.add(mostrarBtn);

		// ___________ CENTER - OTHER FIELDS ______________

		// ACTIVO
		JPanel activoPanel = new JPanel(new FlowLayout());
		activoPanel.setBounds(46, 120, 250, 30);
		JLabel activoLbl = new JLabel("Activo: ");
		activoField = new JTextField(10);
		activoField.setEditable(false);
		activoPanel.add(activoLbl);
		activoPanel.add(activoField);

		centerPanel.add(activoPanel);

		// CIF
		JPanel cifPanel = new JPanel(new FlowLayout());
		cifPanel.setBounds(55, 145, 250, 30);
		JLabel cifLbl = new JLabel("CIF: ");
		this.cifField = new JTextField(10);
		cifField.setEditable(false);
		cifPanel.add(cifLbl);
		cifPanel.add(cifField);

		centerPanel.add(cifPanel);

		// NOMBRE
		JPanel nombrePanel = new JPanel(new FlowLayout());
		nombrePanel.setBounds(41, 170, 250, 30);
		JLabel nombreLbl = new JLabel("Nombre: ");
		this.nombreField = new JTextField(10);
		nombreField.setEditable(false);
		nombrePanel.add(nombreLbl);
		nombrePanel.add(nombreField);

		centerPanel.add(nombrePanel);

		// TELEFONO
		JPanel tlfPanel = new JPanel(new FlowLayout());
		tlfPanel.setBounds(39, 195, 250, 30);
		JLabel tlfLbl = new JLabel("Telefono: ");
		this.tlfField = new JTextField(10);
		tlfField.setEditable(false);
		tlfPanel.add(tlfLbl);
		tlfPanel.add(tlfField);

		centerPanel.add(tlfPanel);

		// DIRECCION
		JPanel direccionPanel = new JPanel(new FlowLayout());
		direccionPanel.setBounds(37, 220, 250, 30);
		JLabel direccionLbl = new JLabel("Direccion: ");
		this.direccionField = new JTextField(10);
		direccionField.setEditable(false);
		direccionPanel.add(direccionLbl);
		direccionPanel.add(direccionField);

		centerPanel.add(direccionPanel);

		this.add(centerPanel, BorderLayout.CENTER);

	}
	
	//no sé si está bien
	@Override
	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.PRODUCTORA, ((TProductora)o).GetID());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		}
		else {
			idField.setText(((TProductora)o).GetID().toString());
			cifField.setText(((TProductora)o).GetCIF());
			nombreField.setText(((TProductora)o).GetNombre());
			direccionField.setText(((TProductora)o).GetDireccion());
			tlfField.setText(((TProductora)o).GetTelefono().toString());
			Boolean act = ((TProductora)o).GetActivo();
			if (act) activoField.setText("SI");
			else activoField.setText("NO");
		}		
	}
	
	@Override
	//no se si está bien
	public boolean hasError(Object response) {
		return (((TProductora) response).GetID()) < 0;
	}



	@Override
	public void clear() {
		idField.setText("");
		activoField.setText("");
		cifField.setText(""); 
		nombreField.setText(""); 
		direccionField.setText("");
		tlfField.setText("");
	}

	@Override
	public boolean validation() {
		try {
			if (idField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(idField.getText()) < 0)
				throw new Exception();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}