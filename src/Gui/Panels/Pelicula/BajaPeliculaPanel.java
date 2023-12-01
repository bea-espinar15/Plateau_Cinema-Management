
package Presentacion.Gui.Panels.Pelicula;

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


public class BajaPeliculaPanel extends GeneralPanel {
	
	private static final long serialVersionUID = 1L;
	private static BajaPeliculaPanel instance;
	private Panel_Main_Pelicula parent;
	private JTextField idField;
	
	public BajaPeliculaPanel() {
		initGUI();
	}
	
	public static BajaPeliculaPanel getInstance() {
		if(instance == null)
			instance = new BajaPeliculaPanel();
		return instance;
	}

	private void initGUI() {
		
		this.setLayout(new BorderLayout());
		
		//__________ CABECERA _________
		JLabel titleLbl = new JLabel("BAJA PELICULA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		titleLbl.setFont(f);
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setForeground(Color.DARK_GRAY);
		titleLbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		this.add(titleLbl, BorderLayout.PAGE_START);
		
		//___________ CENTER ______________
		JPanel centerPanel = new JPanel(null);
		
		//___________ CENTER - ID FIELD + BUTTON ______________
		JPanel idPanel = new JPanel(new FlowLayout());
		idPanel.setBackground(Color.LIGHT_GRAY);
		idPanel.setBounds(200, 50, 200, 30);
		JLabel idLbl = new JLabel("ID:");
		this.idField = new JTextField(10);
		idPanel.add(idLbl);
		idPanel.add(idField);

		centerPanel.add(idPanel);
		
		JButton mostrarBtn = new JButton("Aceptar");
		mostrarBtn.setBounds(400, 50, 100, 30);
		mostrarBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(validation()){
					Context request = new Context(ContextEnum.BAJAPELICULA, Integer.parseInt(idField.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
				}
				else
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		centerPanel.add(mostrarBtn);
		
		this.add(centerPanel,BorderLayout.CENTER);
	}
	
	public void setParent(Panel_Main_Pelicula parent) {
		this.parent=parent;
	}

	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.PELICULA, ((Integer) o));
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		}
		else {
			String mensaje = "Pelicula dada de baja correctamente";
			JOptionPane.showMessageDialog(parent, mensaje);
			clear();
		}
	}

	public boolean hasError(Object response) {
		if(((Integer)response) < 0)
			return true;
		return false;
	}
	
	@Override
	public boolean validation() {
		try{
			if (idField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(idField.getText()) < 0)
				throw new Exception();
			return true;
		} 
		catch(Exception e){
			return false;
		}
	}
	
	public void clear() {
		idField.setText("");
	}	

}