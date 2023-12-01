package Presentacion.Gui.Panels.Pase;

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
import java.util.List;
import Negocio.Pase.TPase;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * Autores: Nico y Axel
 */

public class ListarPorPeliculaPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;

	private static ListarPorPeliculaPanel instance;
	private Panel_Main_Pase parent;
	private JTable pasesTable;
	private JTextField idPeliculaField;
	private ArrayList<TPase> pases;
	private ListarPorPeliculaTableModel listarPorPeliculaTableModel;
	private Integer id;

	public ListarPorPeliculaPanel() {
		this.pases = new ArrayList<TPase>();
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		
		//_______ CABECERA (PAGE_START) ________
		JLabel listarClientes = new JLabel("LISTAR POR PELICULA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		listarClientes.setFont(f);
		listarClientes.setHorizontalAlignment(SwingConstants.CENTER);
		listarClientes.setForeground(Color.DARK_GRAY);
		listarClientes.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		this.add(listarClientes, BorderLayout.PAGE_START);
		
		JPanel idPanel = new JPanel(new FlowLayout());
		idPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		JLabel idPeliculaLbl = new JLabel("ID Pelicula:");
		this.idPeliculaField = new JTextField(10);
		JButton listar = new JButton("Listar");
		listar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(validation()){	
					id = Integer.parseInt(idPeliculaField.getText());
					Context request = new Context(ContextEnum.LISTARPASESPORPELICULA, Integer.parseInt(idPeliculaField.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
  				}else{
  					JOptionPane.showMessageDialog(parent,"Los datos introducidos son sintacticamente erroneos","Error", JOptionPane.ERROR_MESSAGE);
  				}
			}
			
		});
		idPanel.add(idPeliculaLbl);
		idPanel.add(idPeliculaField);
		idPanel.add(listar);
		
		this.add(idPanel, BorderLayout.CENTER);
		
		//______ DATOS (CENTER) _______
		this.add(crearTabla(pases), BorderLayout.AFTER_LAST_LINE);
	}

	public static ListarPorPeliculaPanel getInstance() {
		if (instance == null) {
			instance = new ListarPorPeliculaPanel();
		}
		return instance;
	}

	public void setParent(Panel_Main_Pase parent) {
		this.parent = parent;
	}

	public Component crearTabla(List<TPase> pases) {
		listarPorPeliculaTableModel = new ListarPorPeliculaTableModel(pases);
		this.pasesTable = new JTable(listarPorPeliculaTableModel);
		JScrollPane scroll = new JScrollPane(this.pasesTable);
		scroll.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
		return scroll;
	}

	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.PASE, ((ArrayList<TPase>)o).get(0).GetID());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			parent.showPanel(this);
			clear();
		}
		else {
			parent.showPanel(this);
			idPeliculaField.setText(id.toString());
			pases = (ArrayList<TPase>) o;
			listarPorPeliculaTableModel.updateList(pases);
		}
	}

	public boolean hasError(Object response) {
		return(((ArrayList<TPase>)response).size() > 0 && ((ArrayList<TPase>)response).get(0).GetID() < 0);

	}

	public void clear() {
		idPeliculaField.setText("");
		listarPorPeliculaTableModel.updateList(new ArrayList<TPase>());
	}

	@Override
	public boolean validation() {
		try{
			if (idPeliculaField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(idPeliculaField.getText()) < 0)
				throw new Exception();
			return true;
		} catch(Exception e){
			return false;
		}
	}
}