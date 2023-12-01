
package Presentacion.Gui.Panels.Cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import Negocio.Cliente.TClienteNormal;
import Negocio.Cliente.TClienteVip;
import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;
import java.util.regex.*;

public class AltaClientePanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;
	
	private JButton ok;
	private JComboBox<String> typeCliente;
	private static AltaClientePanel instance;
	private Panel_Main_Cliente parent;
	private JTextField nomF,dniF , correoF;
	private JSpinner antiguedadS;
	private JPanel vip;
	
	public AltaClientePanel(){
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());
		
		// ____ CABECERA _____		
		JLabel altaCliente = new JLabel("ALTA CLIENTE");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		altaCliente.setFont(f);
		altaCliente.setHorizontalAlignment(SwingConstants.CENTER);
		altaCliente.setForeground(Color.DARK_GRAY);
		altaCliente.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		this.add(altaCliente, BorderLayout.PAGE_START);
		
		// DATOS (CENTRO)
		JPanel datos = new JPanel(new GridLayout(1,2));
		
		// DATOS COMUNES
		JPanel datosC = new JPanel(null);
		
		// NOMBRE
		JPanel nombre = new JPanel(new FlowLayout());
		nombre.setBounds(10, 35, 400, 30);
		JLabel nom = new JLabel("Nombre:");
		this.nomF = new JTextField(10);
		nombre.add(nom);
		nombre.add(nomF);
		
		// DNI
		JPanel dni = new JPanel(new FlowLayout());
		dni.setBounds(21, 75, 400, 30);
		JLabel dniLabel = new JLabel("DNI:");
		this.dniF = new JTextField(10);
		dni.add(dniLabel);
		dni.add(dniF);
		
		// CORREO
		JPanel correo = new JPanel(new FlowLayout());
		correo.setBounds(11, 115, 400, 30);
		JLabel correoLabel = new JLabel("Correo:");
		this.correoF = new JTextField(10);
		correo.add(correoLabel);
		correo.add(correoF);
		
		ok = new JButton("Aceptar");
		ok.setBounds(190,190,100,30);
		ok.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				if(validation()){
					if(typeCliente.getSelectedItem() == "NORMAL"){
						TClienteNormal cliente = new TClienteNormal();
						cliente.SetNombre(nomF.getText());
						cliente.SetDNI(dniF.getText().toUpperCase());
						cliente.SetCorreo(correoF.getText());
						
						Context request = new Context(ContextEnum.ALTACLIENTE, cliente);
						ApplicationController.GetInstance().ManageRequest(request);
					}else if (typeCliente.getSelectedItem() == "VIP"){
						TClienteVip cliente = new TClienteVip();
						cliente.SetNombre(nomF.getText());
						cliente.SetDNI(dniF.getText().toUpperCase());
						cliente.SetCorreo(correoF.getText());
						cliente.SetAntiguedad((Integer)antiguedadS.getValue());
						
						Context request = new Context(ContextEnum.ALTACLIENTE, cliente);
						ApplicationController.GetInstance().ManageRequest(request);
					}
					else {
						JOptionPane.showMessageDialog(parent, "No se ha introducido el tipo del cliente", "Error",JOptionPane.ERROR_MESSAGE);
					}
				}
				else{
					JOptionPane.showMessageDialog(parent,"Los datos introduccidos son sintacticamente erroneos", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		
		datosC.add(nombre);
		datosC.add(dni);
		datosC.add(correo);
		datosC.add(ok);
		
		datos.add(datosC);
		
		// DATOS ESPECIFICOS
		JPanel datosE = new JPanel(null);
		
		// VIP		
		vip = new JPanel();
		vip.setBounds(7, 110, 200, 40);
		vip.setVisible(false);
		
		// ANTIGUEDAD		
		JPanel antiguedad = new JPanel(new FlowLayout());
		JLabel antig = new JLabel("Antiguedad:");
		SpinnerModel model = new SpinnerNumberModel(0,0,99,1);
		antiguedadS = new JSpinner(model);
		antiguedadS.setPreferredSize(new Dimension(36,25));
		antiguedad.add(antig);
		antiguedad.add(antiguedadS);
		

		vip.add(antiguedad);
		
		// COMBO TIPOS		
		JPanel tipos = new JPanel(new FlowLayout());
		tipos.setBounds(10, 35, 200, 50);
		JLabel tipo = new JLabel("Tipo:");
		typeCliente = new JComboBox<String>();
		typeCliente.addItem("NORMAL");
		typeCliente.addItem("VIP");
		typeCliente.setSelectedItem(null);
		typeCliente.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if(typeCliente.getSelectedItem() == null || typeCliente.getSelectedItem().equals("NORMAL"))
					vip.setVisible(false);
				else
					vip.setVisible(true);				
			}
			
		});
		tipos.add(tipo);
		tipos.add(typeCliente);
		datosE.add(tipos);
		datosE.add(vip);
		datos.add(datosE);
		
		this.add(datos, BorderLayout.CENTER);	
	}


	public static AltaClientePanel getInstance() {
		if(instance == null)
			instance = new AltaClientePanel();
		return instance;
	}


	public void setParent(Panel_Main_Cliente parent) {
		this.parent = parent;
	}


	public void update(Object o) {
		if(hasError(o)){
			Message msg = ErrorHandlerManager.getMessage(EntityEnum.CLIENTE,(Integer) o);
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		}else{
			JOptionPane.showMessageDialog(parent,"El cliente se ha creado con exito. ID: " + o);
			clear();
		}
	}


	public boolean hasError(Object response) {
		return (Integer) response < 0;
	}


	public boolean validation() {
		try{
			Pattern pat = Pattern.compile("[0-9]{8}[A-Za-z]");
			Matcher mat;
			if(nomF.getText().equals("")) return false;
			mat = pat.matcher(dniF.getText());
			if(dniF.getText().equals("") || !mat.matches()) return false;
			pat = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
			mat = pat.matcher(correoF.getText());
			if(correoF.getText().equals("") || !mat.matches()) return false;
			return true;
		}catch(Exception e){
			return false;
		}
	}


	public void clear() {
		nomF.setText("");
		dniF.setText("");
		correoF.setText("");
		antiguedadS.setValue(0);
		typeCliente.setSelectedItem(null);
		vip.setVisible(false);
	}
	
}