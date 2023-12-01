package Presentacion.Gui.Panels.Cliente;

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
import javax.swing.SwingConstants;

import java.util.ArrayList;
import Negocio.Cliente.TCliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ListarClientesPanel extends GeneralPanel {
	
	private static  final long serialVersionUID =1L;
	private static ListarClientesPanel instance;
	private Panel_Main_Cliente parent;
	private JTable clientesTable;
	private ArrayList<TCliente> clientes;
	private ListarClientesTableModel TableModel;
	
	public ListarClientesPanel(){
		clientes = new ArrayList<>();
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		
		//CABECERA
		
		JLabel listarClientes = new JLabel("LISTAR CLIENTES");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		listarClientes.setFont(f);
		listarClientes.setHorizontalAlignment(SwingConstants.CENTER);
		listarClientes.setForeground(Color.DARK_GRAY);
		listarClientes.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		this.add(listarClientes, BorderLayout.PAGE_START);
		
		JPanel botonListar = new JPanel();
		botonListar.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		JButton listar = new JButton("Listar");
		listar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Context request = new Context(ContextEnum.LISTARCLIENTES, null);
				ApplicationController.GetInstance().ManageRequest(request);
			}
			
		});
		botonListar.add(listar);
		
		this.add(botonListar, BorderLayout.SOUTH);
		
		// CENTERPANEL		
		this.add(crearTabla(clientes), BorderLayout.CENTER);
	}


	public static ListarClientesPanel getInstance() {
		if (instance == null) instance = new ListarClientesPanel();
		return instance;

	}

	public void setParent(Panel_Main_Cliente parent) {
		this.parent = parent;
	}


	public Component crearTabla(ArrayList<TCliente> aux) {
		TableModel = new ListarClientesTableModel(aux);
		clientesTable = new JTable(TableModel);
	    JScrollPane scrollpane = new JScrollPane(clientesTable);
	    scrollpane.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
	    return scrollpane;
	}

	@SuppressWarnings("unchecked")
	public void update(Object o) {
		if(hasError(o)){
			Message msg = ErrorHandlerManager.getMessage(EntityEnum.CLIENTE,((ArrayList<TCliente>) o).get(0).GetID());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			parent.showPanel(this);
			clear();
		}else{
			this.parent.showPanel(this);
			clientes = (ArrayList<TCliente>) o;
			TableModel.updateList(clientes);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasError(Object response) {
		return ((ArrayList<TCliente>) response).size() > 0 && ((ArrayList<TCliente>) response).get(0).GetID() < 0;
	}

	@Override
	public boolean validation() {
		return true;
	}

	@Override
	public void clear() {
		TableModel.updateList(new ArrayList<TCliente>());
	}


}