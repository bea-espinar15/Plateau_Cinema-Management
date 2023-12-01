package Presentacion.Gui.Panels.Cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import Negocio.Cliente.TCliente;
import Negocio.Cliente.TClienteVip;
import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;

public class ModificarClientePanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;

	private static ModificarClientePanel instance;
	private Panel_Main_Cliente parent;
	private JButton modificar, ok;
	private JTextField idF, nomF, dniF, correoF;
	private JSpinner antiguedadS;
	private JPanel vip, antiguedad;
	private Boolean buscado;

	public ModificarClientePanel() {
		buscado = false;
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		// CABECERA
		JLabel modificarCliente = new JLabel("MODIFICAR CLIENTE");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		modificarCliente.setFont(f);
		modificarCliente.setHorizontalAlignment(SwingConstants.CENTER);
		modificarCliente.setForeground(Color.DARK_GRAY);
		modificarCliente.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(modificarCliente, BorderLayout.PAGE_START);

		// ______ DATOS (CENTRE) _______
		JPanel datos = new JPanel(new GridLayout(1, 2));

		// _______ DATOS COMUNES ________
		JPanel datosC = new JPanel(null);

		JPanel idPanel = new JPanel(new FlowLayout());
		idPanel.setBackground(Color.LIGHT_GRAY);
		idPanel.setBounds(100, 35, 170, 30);
		JLabel idLabel = new JLabel("ID:");
		this.idF = new JTextField(10);
		idPanel.add(idLabel);
		idPanel.add(idF);

		// ACEPTAR ID
		ok = new JButton("Buscar");
		ok.setBounds(280,35,80,30);
		ok.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				if(validation()){
					buscado = true;
					
					Context request = new Context(ContextEnum.MOSTRARCLIENTEPARAMODIFICAR, Integer.parseInt(idF.getText()));
					
					ApplicationController.GetInstance().ManageRequest(request);
					
				}else{
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error", 
							JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		// NOMBRE
		JPanel nombre = new JPanel(new FlowLayout());
		nombre.setBounds(0, 130, 400, 30);
		JLabel nom = new JLabel("Nombre:");
		nomF = new JTextField(10);
		nomF.setEditable(false);
		nombre.add(nom);
		nombre.add(nomF);

		// DNI
		JPanel dni = new JPanel(new FlowLayout());
		dni.setBounds(11, 170, 400, 30);
		JLabel dniLabel = new JLabel("DNI:");
		dniF = new JTextField(10);
		dniF.setEditable(false);
		dni.add(dniLabel);
		dni.add(dniF);

		// CORREO
		JPanel correo = new JPanel(new FlowLayout());
		correo.setBounds(1, 210, 400, 30);
		JLabel correoLabel = new JLabel("Correo:");
		correoF = new JTextField(10);
		correoF.setEditable(false);
		correo.add(correoLabel);
		correo.add(correoF);

		modificar = new JButton("Modificar");
		modificar.setEnabled(false);
		modificar.setBounds(180, 285, 100, 30);
		modificar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				if (validation()) {
					Context request;
					if(antiguedadS.isEnabled()){
						TClienteVip cliente_vip = new TClienteVip();
						cliente_vip.SetAntiguedad((Integer)antiguedadS.getValue());
						cliente_vip.SetID(Integer.parseInt(idF.getText()));
						cliente_vip.SetNombre(nomF.getText());
						cliente_vip.SetDNI(dniF.getText().toUpperCase());
						cliente_vip.SetCorreo(correoF.getText());
						request = new Context(ContextEnum.MODIFICARCLIENTE, cliente_vip);
						
					}
					else{
						TCliente cliente = new TCliente();
						cliente.SetID(Integer.parseInt(idF.getText()));
						cliente.SetNombre(nomF.getText());
						cliente.SetDNI(dniF.getText().toUpperCase());
						cliente.SetCorreo(correoF.getText());
						request = new Context(ContextEnum.MODIFICARCLIENTE, cliente);
					}
					buscado = false;
					ApplicationController.GetInstance().ManageRequest(request);
					
					
				} else {
					JOptionPane.showMessageDialog(parent, "Los datos introduccidos son sintacticamente erroneos", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		});
		datosC.add(idPanel);
		datosC.add(ok);
		datosC.add(nombre);
		datosC.add(dni);
		datosC.add(correo);
		datosC.add(modificar);

		datos.add(datosC);

		// DATOS ESPECIFICOS
		JPanel datosE = new JPanel(null);

		// VIP
		vip = new JPanel();
		vip.setBounds(7, 120, 200, 40);

		// ANTIGUEDAD
		antiguedad = new JPanel(new FlowLayout());
		JLabel antig = new JLabel("Antiguedad:");
		SpinnerModel model = new SpinnerNumberModel(0,0,99,1);
		antiguedadS = new JSpinner(model);
		antiguedadS.setPreferredSize(new Dimension(36,25));
		antiguedadS.setEnabled(false);
		antiguedad.add(antig);
		antiguedad.add(antiguedadS);
		antiguedad.setVisible(false);

		vip.add(antiguedad);	
		datosE.add(vip);

		datos.add(datosE);

		this.add(datos, BorderLayout.CENTER);
	}

	public static ModificarClientePanel getInstance() {
		if (instance == null)
			instance = new ModificarClientePanel();
		return instance;
	}

	public void setParent(Panel_Main_Cliente parent) {
		this.parent = parent;
	}

	
	public void update(Object o) {
		
		if(!buscado){
			if (hasError(o)) {
				
				Message msg = ErrorHandlerManager.getMessage(EntityEnum.CLIENTE, (Integer)o);
				JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
				clear();
			}else{
				JOptionPane.showMessageDialog(parent, "El cliente se ha modificado con exito.");
				parent.showPanel(this);
				clear();
			}
		}else{
			if (hasError(o)) {
				buscado = false;
				Message msg = ErrorHandlerManager.getMessage(EntityEnum.CLIENTE,((TCliente)o).GetID());
				JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
				clear();
			}else{
				
				nomF.setText(((TCliente) o).GetNombre());
				dniF.setText(((TCliente) o).GetDNI());
				correoF.setText(((TCliente) o).GetCorreo());
				if(o instanceof TClienteVip){
					antiguedad.setVisible(true);
					antiguedadS.setEnabled(true);
					antiguedadS.setValue((((TClienteVip) o).GetAntiguedad()));
				}
				
				idF.setEditable(false);
				nomF.setEditable(true);
				dniF.setEditable(true);
				correoF.setEditable(true);
				modificar.setEnabled(true);
				ok.setEnabled(false);
			}
		}
	}

	public boolean hasError(Object response) {
		
		if(response instanceof Integer){
			if(!buscado && ((Integer)response) < 0)
				return true;
			else if(buscado && ((Integer)response) < 0)
				return true;
		}else{
			if(!buscado && ((TCliente)response).GetID() < 0)
				return true;
			else if(buscado && ((TCliente)response).GetID() < 0)
				return true;
		}
		
		return false;
	}

	public boolean validation() {
		try {
			if(!buscado){
				if (idF.getText().equals(""))
					throw new Exception();
				if(Integer.parseInt(idF.getText()) < 0){
					throw new Exception();
				}
				return true;
			}else{
				Pattern pat = Pattern.compile("[0-9]{8}[A-Za-z]");
				Matcher mat;
				if(nomF.getText().equals(""))
					throw new Exception();
				mat = pat.matcher(dniF.getText());
				if (dniF.getText().equals("") || !mat.matches())
					throw new Exception();
				pat = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
				mat = pat.matcher(correoF.getText());
				if (correoF.getText().equals("") || !mat.matches())
					throw new Exception();
				
				return true;
			}
			
		} catch (Exception e) {
			return false;
		}
	}

	public void clear() {
		idF.setText("");
		nomF.setText("");
		dniF.setText("");
		correoF.setText("");
		antiguedadS.setValue(0);
		
		nomF.setEditable(false);
		dniF.setEditable(false);
		correoF.setEditable(false);
		idF.setEditable(true);
		ok.setEnabled(true);
		modificar.setEnabled(false);
		antiguedad.setVisible(false);
		buscado = false;
	}
}