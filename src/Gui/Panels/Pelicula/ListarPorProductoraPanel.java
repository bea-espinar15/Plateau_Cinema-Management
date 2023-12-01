package Presentacion.Gui.Panels.Pelicula;

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
import Negocio.Pelicula.TPelicula;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListarPorProductoraPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;
	
	private static ListarPorProductoraPanel instance;
	private Panel_Main_Pelicula parent;
	private JTable peliculasTable;
	private JTextField idProductoraField;
	private ListarPorProductoraTableModel listarPorProductoraTableModel;
	private ArrayList<TPelicula> peliculas;
	private Integer id;
	
	public ListarPorProductoraPanel() {
		peliculas = new ArrayList<TPelicula>();
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		
		//_______ CABECERA (PAGE_START) ________
		JLabel listarClientes = new JLabel("LISTAR POR PRODUCTORA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		listarClientes.setFont(f);
		listarClientes.setHorizontalAlignment(SwingConstants.CENTER);
		listarClientes.setForeground(Color.DARK_GRAY);
		listarClientes.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		this.add(listarClientes, BorderLayout.PAGE_START);
		
		JPanel idPanel = new JPanel(new FlowLayout());
		idPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		JLabel idSalaLbl = new JLabel("ID Productora:");
		this.idProductoraField = new JTextField(10);
		JButton listar = new JButton("Listar");
		listar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()){	
					id = Integer.parseInt(idProductoraField.getText());
					Context request = new Context(ContextEnum.LISTARPELICULASPORPRODUCTORA, Integer.parseInt(idProductoraField.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
  				}
				else
  					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		});
		idPanel.add(idSalaLbl);
		idPanel.add(idProductoraField);
		idPanel.add(listar);
		
		this.add(idPanel, BorderLayout.CENTER);
		
		//______ DATOS (CENTER) _______
		this.add(crearTabla(peliculas), BorderLayout.AFTER_LAST_LINE);
	}

	public static ListarPorProductoraPanel getInstance() {
		if(instance == null)
		{
			instance = new ListarPorProductoraPanel();
		}
		return instance;
	}


	public void setParent(Panel_Main_Pelicula parent) {
		this.parent = parent;
	}

	public Component crearTabla(ArrayList<TPelicula> peliculas) {
		listarPorProductoraTableModel = new ListarPorProductoraTableModel(peliculas);
		this.peliculasTable = new JTable(listarPorProductoraTableModel);
        JScrollPane scroll = new JScrollPane(this.peliculasTable);
        scroll.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
		return scroll;
	}

	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.PELICULA, ((ArrayList<TPelicula>)o).get(0).GetID());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			parent.showPanel(this);
			clear();
		}
		else {
			parent.showPanel(this);
			idProductoraField.setText(id.toString());
			peliculas = (ArrayList<TPelicula>) o;
			listarPorProductoraTableModel.updateList(peliculas);
		}
	}

	public boolean hasError(Object response) {
		return(((((ArrayList<TCompra>) response).size() > 0)) && (((ArrayList<TPelicula>)response).get(0).GetID() < 0));
	
	}

	public void clear() {
		this.idProductoraField.setText("");
		listarPorProductoraTableModel.updateList(new ArrayList<TPelicula>());
	}

	@Override
	public boolean validation() {
		try{
			if (idProductoraField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(idProductoraField.getText()) < 0)
				throw new Exception();
			return true;
		} 
		catch(Exception e){
			return false;
		}
	}
	
}