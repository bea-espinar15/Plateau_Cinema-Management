/**
 * 
 */
package Presentacion.Gui.Panels.Cliente;

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

import Negocio.Cliente.TCliente;
import Negocio.Cliente.TClienteNormal;
import Negocio.Cliente.TClienteVip;
import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;

public class MostrarClientePanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;
	private String type;
	private static MostrarClientePanel instance;
	private Panel_Main_Cliente parent;
	private JTextField idF, actF, nomF, dniF, correoF, facturacionF, descuentoF, antiguedadF, noTypeF, normF, vipF;
	private JPanel typeEmpty, normal, vip, facturacion, descuento, antiguedad;

	public MostrarClientePanel() {
		type = "";
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());

		// CABECERA

		JLabel mostrarCliente = new JLabel("MOSTRAR CLIENTE");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		mostrarCliente.setFont(f);
		mostrarCliente.setHorizontalAlignment(SwingConstants.CENTER);
		mostrarCliente.setForeground(Color.DARK_GRAY);
		mostrarCliente.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(mostrarCliente, BorderLayout.PAGE_START);

		// CENTER

		JPanel centerPanel = new JPanel(null);

		JPanel idPanel = new JPanel(new FlowLayout());
		idPanel.setBackground(Color.LIGHT_GRAY);
		idPanel.setBounds(228, 35, 200, 30);
		JLabel id = new JLabel("ID:");
		this.idF = new JTextField(10);
		idPanel.add(id);
		idPanel.add(idF);

		centerPanel.add(idPanel);

		JButton mostrar = new JButton("Mostrar");
		mostrar.setBounds(438, 35, 100, 30);
		mostrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					Context request = new Context(ContextEnum.MOSTRARCLIENTE, Integer.parseInt(idF.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
				} else {
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		centerPanel.add(mostrar);

		// ACTIVO
		JPanel activo = new JPanel(new FlowLayout());
		activo.setBounds(47, 110, 250, 30);
		JLabel act = new JLabel("Activo:");
		this.actF = new JTextField(10);
		actF.setEditable(false);
		activo.add(act);
		activo.add(actF);

		centerPanel.add(activo);

		// NOMBRE
		JPanel nombre = new JPanel(new FlowLayout());
		nombre.setBounds(42, 150, 250, 30);
		JLabel nom = new JLabel("Nombre:");
		this.nomF = new JTextField(10);
		nomF.setEditable(false);
		nombre.add(nom);
		nombre.add(nomF);

		centerPanel.add(nombre);

		// DNI
		JPanel dni = new JPanel(new FlowLayout());
		dni.setBounds(54, 190, 250, 30);
		JLabel dniLabel = new JLabel("DNI:");
		this.dniF = new JTextField(10);
		dniF.setEditable(false);
		dni.add(dniLabel);
		dni.add(dniF);

		centerPanel.add(dni);

		// CORREO
		JPanel correo = new JPanel(new FlowLayout());
		correo.setBounds(45, 230, 250, 30);
		JLabel correoLabel = new JLabel("Correo:");
		this.correoF = new JTextField(10);
		correoF.setEditable(false);
		correo.add(correoLabel);
		correo.add(correoF);

		centerPanel.add(correo);

		// SIN TIPO
		typeEmpty = new JPanel(new FlowLayout());
		typeEmpty.setBounds(350, 110, 250, 30);
		JLabel noType = new JLabel("Tipo:");
		noTypeF = new JTextField(10);
		noTypeF.setEditable(false);
		typeEmpty.add(noType);
		typeEmpty.add(noTypeF);

		centerPanel.add(typeEmpty);

		// TIPO NORMAL
		normal = new JPanel(new FlowLayout());
		normal.setBounds(350, 110, 250, 30);
		JLabel norm = new JLabel("Tipo:");
		normF = new JTextField("NORMAL", 10);
		normF.setEditable(false);
		normal.add(norm);
		normal.add(normF);

		centerPanel.add(normal);

		// FACTURACION
		facturacion = new JPanel(new FlowLayout());
		facturacion.setBounds(328, 150, 250, 30);
		JLabel fac = new JLabel("Facturacion:");
		this.facturacionF = new JTextField(10);
		facturacionF.setEditable(false);
		facturacion.add(fac);
		facturacion.add(facturacionF);

		centerPanel.add(facturacion);

		// TIPO VIP
		vip = new JPanel(new FlowLayout());
		vip.setBounds(350, 110, 250, 30);
		JLabel vipLabel = new JLabel("Tipo:");
		this.vipF = new JTextField("VIP", 10);
		vipF.setEditable(false);
		vip.add(vipLabel);
		vip.add(vipF);

		centerPanel.add(vip);

		// DESCUENTO
		descuento = new JPanel(new FlowLayout());
		descuento.setBounds(331, 150, 250, 30);
		JLabel desc = new JLabel("Descuento:");
		this.descuentoF = new JTextField(10);
		descuentoF.setEditable(false);
		descuento.add(desc);
		descuento.add(descuentoF);

		centerPanel.add(descuento);

		// antiguedad
		antiguedad = new JPanel(new FlowLayout());
		antiguedad.setBounds(329, 190, 250, 30);
		JLabel ant = new JLabel("Antiguedad:");
		this.antiguedadF = new JTextField(10);
		antiguedadF.setEditable(false);
		antiguedad.add(ant);
		antiguedad.add(antiguedadF);

		centerPanel.add(antiguedad);

		updatePanel(type);

		this.add(centerPanel, BorderLayout.CENTER);

	}

	public static MostrarClientePanel getInstance() {
		if (instance == null)
			instance = new MostrarClientePanel();
		return instance;
	}

	public void setParent(Panel_Main_Cliente parent) {
		this.parent = parent;
	}

	public void update(Object o) {
		if (hasError(o)) {
			Message msg = ErrorHandlerManager.getMessage(EntityEnum.CLIENTE,((TCliente) o).GetID());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		} else {
			parent.showPanel(this);
			if (o instanceof TClienteNormal) {
				this.type = "NORMAL";
				updatePanel(type);
				this.facturacionF.setText(((TClienteNormal) o).GetFacturacion().toString());
				this.normF.setText("NORMAL");
			} else {
				this.type = "VIP";
				updatePanel(type);
				this.descuentoF.setText(((TClienteVip) o).GetDescuento().toString() + "%");
				this.antiguedadF.setText(((TClienteVip) o).GetAntiguedad().toString());
				this.vipF.setText("VIP");
			}
			this.idF.setText(((TCliente) o).GetID().toString());
			this.dniF.setText(((TCliente) o).GetDNI());
			this.nomF.setText(((TCliente) o).GetNombre());
			this.correoF.setText(((TCliente) o).GetCorreo());
			Boolean act = ((TCliente) o).GetActivo();
			if (act) actF.setText("SI");
			else actF.setText("NO");
		}
	}

	public boolean hasError(Object response) {
		return ((TCliente) response).GetID() < 0;
	}

	public boolean validation() {
		try {
			if (idF.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(idF.getText()) < 0)
				throw new Exception();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void clear() {
		idF.setText("");
		actF.setText("");
		nomF.setText("");
		dniF.setText("");
		correoF.setText("");
		antiguedadF.setText("");
		facturacionF.setText("");
		descuentoF.setText("");
		vipF.setText("");
		normF.setText("");
	}

	private void updatePanel(String type) {
		if (type == "") {
			vip.setVisible(false);
			descuento.setVisible(false);
			antiguedad.setVisible(false);
			normal.setVisible(false);
			facturacion.setVisible(false);
			typeEmpty.setVisible(true);
		} else if (type == "NORMAL") {
			typeEmpty.setVisible(false);
			vip.setVisible(false);
			descuento.setVisible(false);
			antiguedad.setVisible(false);
			normal.setVisible(true);
			facturacion.setVisible(true);
		} else {
			typeEmpty.setVisible(false);
			normal.setVisible(false);
			facturacion.setVisible(false);
			vip.setVisible(true);
			descuento.setVisible(true);
			antiguedad.setVisible(true);

		}
	}
}