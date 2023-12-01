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
import java.util.Date;
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

public class ListarPorCompraPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;

	private static ListarPorCompraPanel instance;
	private Panel_Main_Pase parent;
	private JTable pasesTable;
	private JTextField idCompraField;
	private ArrayList<TPase> pases;
	private ListarPorCompraTableModel listarPorCompraTableModel;
	private Integer id;

	public ListarPorCompraPanel() {
		this.pases = new ArrayList<TPase>();
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		
		//_______ CABECERA (PAGE_START) ________
		JLabel listarPases = new JLabel("LISTAR POR COMPRA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		listarPases.setFont(f);
		listarPases.setHorizontalAlignment(SwingConstants.CENTER);
		listarPases.setForeground(Color.DARK_GRAY);
		listarPases.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		this.add(listarPases, BorderLayout.PAGE_START);
		
		JPanel idPanel = new JPanel(new FlowLayout());
		idPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		JLabel idCompraLbl = new JLabel("ID Compra:");
		this.idCompraField = new JTextField(10);
		JButton listarBtn = new JButton("Listar");
		listarBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(validation()){	
					id = Integer.parseInt(idCompraField.getText());
					Context request = new Context(ContextEnum.LISTARPASESPORCOMPRA, Integer.parseInt(idCompraField.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
  				}else{
  					JOptionPane.showMessageDialog(parent,"Los datos introducidos son sintacticamente erroneos","Error", JOptionPane.ERROR_MESSAGE);
  				}
			}
			
		});
		idPanel.add(idCompraLbl);
		idPanel.add(idCompraField);
		idPanel.add(listarBtn);
		
		this.add(idPanel, BorderLayout.CENTER);
		
		//______ DATOS (CENTER) _______
		this.add(crearTabla(pases), BorderLayout.SOUTH);
	}

	public static ListarPorCompraPanel getInstance() {
		if (instance == null) {
			instance = new ListarPorCompraPanel();
		}
		return instance;
	}

	public void setParent(Panel_Main_Pase parent) {
		this.parent = parent;
	}

	public Component crearTabla(List<TPase> pases) {
		listarPorCompraTableModel = new ListarPorCompraTableModel(pases);
		this.pasesTable = new JTable(listarPorCompraTableModel);
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
			idCompraField.setText(id.toString());
			pases = (ArrayList<TPase>) o;
			listarPorCompraTableModel.updateList(pases);
		}
	}

	public boolean hasError(Object response) {
		if(((ArrayList<TPase>)response).size() > 0 && ((ArrayList<TPase>)response).get(0).GetID() < 0)
			return true;
		return false;
	}

	public void clear() {
		idCompraField.setText("");
		listarPorCompraTableModel.updateList(new ArrayList<TPase>());
	}

	@Override
	public boolean validation() {
		try{
			if (idCompraField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(idCompraField.getText()) < 0)
				throw new Exception();
			return true;
		} catch(Exception e){
			return false;
		}
	}
}