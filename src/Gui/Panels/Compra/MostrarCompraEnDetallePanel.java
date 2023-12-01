
package Presentacion.Gui.Panels.Compra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Negocio.Compra.TCompra;
import Negocio.Compra.TCompraEnDetalle;
import Negocio.Pase.TPase;
import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;
import Presentacion.Gui.Panels.Pase.ListarPasesTableModel;

public class MostrarCompraEnDetallePanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;
	private static MostrarCompraEnDetallePanel instance;
	private Panel_Main_Compra parent;
	private JTextField idCompraField, precioTotalField, fechaField, idClienteField, dniField, nombreField, correoField;
	private JLabel idCompraLabel, precioTotalLabel, fechaLabel, dniLabel, nombreLabel, correoLabel;
	private JTable pasesTable;
	private ArrayList<TPase> pases;
	private ListarPasesTableModel listarPasesTableModel;

	public MostrarCompraEnDetallePanel() {
		pases = new ArrayList<TPase>();
		initGUI();
	}

	public static MostrarCompraEnDetallePanel getInstance() {
		if (instance == null) {
			instance = new MostrarCompraEnDetallePanel();
		}
		return instance;
	}

	public void setParent(Panel_Main_Compra parent) {
		this.parent = parent;
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());
		// __________ CABECERA _________

		JLabel mostrarEnDetalleCompra = new JLabel("MOSTRAR COMPRA EN DETALLE");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		mostrarEnDetalleCompra.setFont(f);
		mostrarEnDetalleCompra.setHorizontalAlignment(SwingConstants.CENTER);
		mostrarEnDetalleCompra.setForeground(Color.DARK_GRAY);
		mostrarEnDetalleCompra.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(mostrarEnDetalleCompra, BorderLayout.PAGE_START);

		JPanel centerPanel = new JPanel();

		// ENTRADA
		JPanel entrada = new JPanel(new FlowLayout());
		entrada.setMinimumSize(new Dimension(800, 80));
		entrada.setMaximumSize(new Dimension(800, 80));
		entrada.setPreferredSize(new Dimension(800, 80));
		entrada.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		JPanel idPanel = new JPanel(new FlowLayout());
		idPanel.setBackground(Color.LIGHT_GRAY);
		idPanel.setMinimumSize(new Dimension(160, 30));
		idPanel.setMaximumSize(new Dimension(160, 30));
		idPanel.setPreferredSize(new Dimension(160, 30));
		idCompraLabel = new JLabel("ID:");
		idCompraField = new JTextField(10);
		idPanel.add(idCompraLabel);
		idPanel.add(idCompraField);
		entrada.add(idPanel);

		JButton mostrar = new JButton("Mostrar");
		mostrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(validation()){
					Context request = new Context(ContextEnum.MOSTRARCOMPRAENDETALLE, idCompraField.getText());
					ApplicationController.GetInstance().ManageRequest(request);
				}
				else{
					JOptionPane.showMessageDialog(parent,"Los datos introducidos son sintacticamente erroneos", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		});
		entrada.add(mostrar);
		centerPanel.add(entrada);

		// ______ DATOS (CENTER) _______
		JPanel data = new JPanel(new GridLayout(1, 2));

		// ____ COMPRA Y CLIENTE PANEL - - - - -
		JPanel compraYCliente = new JPanel();
		compraYCliente.setLayout(new BoxLayout(compraYCliente, BoxLayout.Y_AXIS));
		compraYCliente.setPreferredSize(new Dimension(360, 700));
		compraYCliente.setMaximumSize(new Dimension(360, 700));
		compraYCliente.setMinimumSize(new Dimension(360, 700));

		// COMPRA
		JPanel tituloCompra = new JPanel(new FlowLayout());
		tituloCompra.setPreferredSize(new Dimension(400, 30));
		tituloCompra.setMaximumSize(new Dimension(400, 30));
		tituloCompra.setMinimumSize(new Dimension(400, 30));
		JLabel compraLabel = new JLabel("COMPRA");
		Font c = new Font("Monospaced", Font.ITALIC, 20);
		compraLabel.setFont(c);
		compraLabel.setForeground(Color.DARK_GRAY);

		tituloCompra.add(compraLabel);
		compraYCliente.add(tituloCompra);

		// PRECIO TOTAL
		JPanel precioTotalPanel = new JPanel(new FlowLayout());
		precioTotalPanel.setPreferredSize(new Dimension(250, 50));
		precioTotalPanel.setMaximumSize(new Dimension(250, 50));
		precioTotalPanel.setMinimumSize(new Dimension(250, 50));
		precioTotalPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		precioTotalLabel = new JLabel("Precio total: ");
		precioTotalField = new JTextField(10);
		precioTotalField.setEditable(false);
		precioTotalPanel.add(precioTotalLabel);
		precioTotalPanel.add(precioTotalField);

		compraYCliente.add(precioTotalPanel);

		// FECHA
		JPanel fechaPanel = new JPanel(new FlowLayout());
		fechaPanel.setPreferredSize(new Dimension(250, 50));
		fechaPanel.setMaximumSize(new Dimension(250, 50));
		fechaPanel.setMinimumSize(new Dimension(250, 50));
		fechaLabel = new JLabel("Fecha: ");
		fechaField = new JTextField(10);
		fechaField.setEditable(false);
		fechaPanel.add(fechaLabel);
		fechaPanel.add(fechaField);

		compraYCliente.add(fechaPanel);

		// CLIENTE
		JPanel tituloCliente = new JPanel(new FlowLayout());
		tituloCliente.setPreferredSize(new Dimension(400, 50));
		tituloCliente.setMaximumSize(new Dimension(400, 50));
		tituloCliente.setMinimumSize(new Dimension(400, 50));
		tituloCliente.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		JLabel clienteLabel = new JLabel("CLIENTE");
		clienteLabel.setFont(c);
		clienteLabel.setForeground(Color.DARK_GRAY);

		tituloCliente.add(clienteLabel);
		compraYCliente.add(tituloCliente);

		// ID CLIENTE
		JPanel idClientePanel = new JPanel(new FlowLayout());
		idClientePanel.setPreferredSize(new Dimension(250, 50));
		idClientePanel.setMaximumSize(new Dimension(250, 50));
		idClientePanel.setMinimumSize(new Dimension(250, 50));
		idClientePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		clienteLabel = new JLabel("ID: ");
		idClienteField = new JTextField(10);
		idClienteField.setEditable(false);
		idClientePanel.add(clienteLabel);
		idClientePanel.add(idClienteField);

		compraYCliente.add(idClientePanel);

		// DNI
		JPanel dniClientePanel = new JPanel(new FlowLayout());
		dniClientePanel.setPreferredSize(new Dimension(250, 30));
		dniClientePanel.setMaximumSize(new Dimension(250, 30));
		dniClientePanel.setMinimumSize(new Dimension(250, 30));
		dniLabel = new JLabel("DNI: ");
		dniField = new JTextField(10);
		dniField.setEditable(false);
		dniClientePanel.add(dniLabel);
		dniClientePanel.add(dniField);

		compraYCliente.add(dniClientePanel);

		// NOMBRE
		JPanel nombreClientePanel = new JPanel(new FlowLayout());
		nombreClientePanel.setPreferredSize(new Dimension(250, 30));
		nombreClientePanel.setMaximumSize(new Dimension(250, 30));
		nombreClientePanel.setMinimumSize(new Dimension(250, 30));
		nombreLabel = new JLabel("Nombre: ");
		nombreField = new JTextField(10);
		nombreField.setEditable(false);
		nombreClientePanel.add(nombreLabel);
		nombreClientePanel.add(nombreField);

		compraYCliente.add(nombreClientePanel);

		// CORREO
		JPanel correoClientePanel = new JPanel(new FlowLayout());
		correoClientePanel.setPreferredSize(new Dimension(250, 30));
		correoClientePanel.setMaximumSize(new Dimension(250, 30));
		correoClientePanel.setMinimumSize(new Dimension(250, 30));
		correoLabel = new JLabel("Correo: ");
		correoField = new JTextField(10);
		correoField.setEditable(false);
		correoClientePanel.add(correoLabel);
		correoClientePanel.add(correoField);

		compraYCliente.add(correoClientePanel);

		data.add(compraYCliente);

		// - - - - - - TPASES PANEL - - - - -
		JPanel TPases = new JPanel();
		TPases.setLayout(new BoxLayout(TPases, BoxLayout.Y_AXIS));
		TPases.setMinimumSize(new Dimension(360,700));
		TPases.setMaximumSize(new Dimension(360,700));
		TPases.setPreferredSize(new Dimension(360,700));

		JPanel tituloPases = new JPanel(new FlowLayout());
		tituloPases.setPreferredSize(new Dimension(100, 30));
		tituloPases.setMaximumSize(new Dimension(100, 30));
		tituloPases.setMinimumSize(new Dimension(100, 30));
		JLabel paseLabel = new JLabel("PASES");
		paseLabel.setFont(c);
		paseLabel.setForeground(Color.DARK_GRAY);

		tituloPases.add(paseLabel);
		tituloPases.setAlignmentX(Component.LEFT_ALIGNMENT);
		TPases.add(tituloPases);

		TPases.add(crearTabla(pases), BorderLayout.CENTER);
		
		data.add(TPases);
		centerPanel.add(data);

		this.add(centerPanel, BorderLayout.CENTER);

	}

	public Component crearTabla(ArrayList<TPase> aux) {
		listarPasesTableModel = new ListarPasesTableModel(aux);
		pasesTable = new JTable(listarPasesTableModel);
		JScrollPane scrollpane = new JScrollPane(pasesTable);
		scrollpane.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 20));
		return scrollpane;
	}
	
	@Override
	public void update(Object o) {
		if(hasError(o)){
			Message msg = ErrorHandlerManager.getInstance().getMessage(EntityEnum.COMPRA, ((TCompraEnDetalle) o).GetCompra().GetID());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			parent.showPanel(this);
			clear();
		}else{
			parent.showPanel(this);
			
			idCompraField.setText(((TCompraEnDetalle) o).GetCompra().GetID().toString());
			precioTotalField.setText(((TCompraEnDetalle) o).GetCompra().GetPrecioTotal().toString());;
			fechaField.setText(new SimpleDateFormat("dd-MM-yyyy").format(((TCompraEnDetalle) o).GetCompra().GetFecha()));
			idClienteField.setText(((TCompraEnDetalle) o).GetCliente().GetID().toString());;
			dniField.setText(((TCompraEnDetalle) o).GetCliente().GetDNI());;
			nombreField.setText(((TCompraEnDetalle) o).GetCliente().GetNombre());;
			correoField.setText(((TCompraEnDetalle) o).GetCliente().GetCorreo());;
			
			listarPasesTableModel.updateList(((TCompraEnDetalle) o).GetPases());
		}
	}

	@Override
	public boolean hasError(Object response) {
		return ((TCompraEnDetalle) response).GetCompra().GetID() < 0;
	}

	@Override
	public boolean validation() {
		try {
			if (idCompraField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(idCompraField.getText()) < 0)
				throw new Exception();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	@Override
	public void clear() {
		idCompraField.setText(""); 
		precioTotalField.setText(""); 
		fechaField.setText(""); 
		idClienteField.setText(""); 
		dniField.setText(""); 
		nombreField.setText(""); 
		correoField.setText("");
		listarPasesTableModel.updateList(new ArrayList<TPase>());
	}
	
}