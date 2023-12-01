/**
 * 
 */
package Presentacion.Gui.Panels.Compra;

import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.util.ArrayList;
import Negocio.Compra.TCompra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListarComprasPorClientePanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;
	private ListarComprasPorClienteTableModel listarComprasPorClienteTableModel;
	private static ListarComprasPorClientePanel instance;
	private Panel_Main_Compra parent;
	private JTable comprasTable;
	private JLabel clienteLabel;
	private JTextField clienteField;
	private ArrayList<TCompra> compras;
	private Integer id;

	public ListarComprasPorClientePanel(){
		compras = new ArrayList<>();
		initGUI();
	}

	public static ListarComprasPorClientePanel getInstance() {
		if(instance == null) {
			instance = new ListarComprasPorClientePanel();
		}
		return instance;
	}

	public void setParent(Panel_Main_Compra parent) {
		this.parent = parent;
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		
		//_______ CABECERA (PAGE_START) ________
		JLabel listarClientes = new JLabel("LISTAR POR CLIENTE");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		listarClientes.setFont(f);
		listarClientes.setHorizontalAlignment(SwingConstants.CENTER);
		listarClientes.setForeground(Color.DARK_GRAY);
		listarClientes.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		this.add(listarClientes, BorderLayout.PAGE_START);
		
		JPanel cliente = new JPanel(new FlowLayout());
		cliente.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		this.clienteLabel = new JLabel("ID Cliente:");
		this.clienteField = new JTextField(10);
		JButton listar = new JButton("Listar");
		listar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					id = Integer.parseInt(clienteField.getText());
					Context request = new Context(ContextEnum.LISTARCOMPRASPORCLIENTE, Integer.parseInt(clienteField.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
				}
				else
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		});
		cliente.add(clienteLabel);
		cliente.add(clienteField);
		cliente.add(listar);
		
		this.add(cliente, BorderLayout.CENTER);
		
		//______ DATOS (CENTER) _______
		this.add(crearTabla(compras), BorderLayout.AFTER_LAST_LINE);
	}

	public Component crearTabla(ArrayList<TCompra> aux) {
		listarComprasPorClienteTableModel = new ListarComprasPorClienteTableModel(aux);
		comprasTable = new JTable(listarComprasPorClienteTableModel);
		JScrollPane scrollpane = new JScrollPane(comprasTable);
		scrollpane.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
		return scrollpane;
	}

	@Override
	public void update(Object compras) {
		if (hasError(compras)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.COMPRA, ((ArrayList<TCompra>)compras).get(0).GetID());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			parent.showPanel(this);
			clear();
		}
		else {
			parent.showPanel(this);
			clienteField.setText(id.toString());
			this.compras = (ArrayList<TCompra>)compras;
			listarComprasPorClienteTableModel.updateList(this.compras);
		}		
	}

	@Override
	public boolean hasError(Object response) {
		if (((ArrayList<TCompra>)response).size() > 0 && ((ArrayList<TCompra>)response).get(0).GetID() < 0 )
			return true;		
		return false;
	}

	@Override
	public boolean validation() {
		try {
			if (clienteField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(clienteField.getText()) < 0)
				throw new Exception();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	@Override
	public void clear() {
		clienteField.setText("");
		listarComprasPorClienteTableModel.updateList(new ArrayList<TCompra>());
	}

}