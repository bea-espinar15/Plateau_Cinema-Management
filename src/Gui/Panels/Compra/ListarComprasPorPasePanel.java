
package Presentacion.Gui.Panels.Compra;

import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralMainPanel;
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

import Negocio.Compra.TCompra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ListarComprasPorPasePanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;
	
	private ListarComprasPorPaseTableModel listarComprasPorPaseTableModel;
	private static ListarComprasPorPasePanel instance;
	private Panel_Main_Compra parent;
	private JTable comprasTable;
	private JTextField idField;
	private ArrayList<TCompra> compras;
	private Integer id;

	public ListarComprasPorPasePanel(){
		compras = new ArrayList<>();
		initGUI();
	}
	
	public static ListarComprasPorPasePanel getInstance() {
		if(instance == null)
			  instance = new ListarComprasPorPasePanel();
		return instance;
	}

	public void setParent(Panel_Main_Compra parent) {
		this.parent = parent;
	}
	
	private void initGUI() {

		setLayout(new BorderLayout());
		
		//_______ CABECERA (PAGE_START) ________
		JLabel listarPorPase = new JLabel("LISTAR POR PASE");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		listarPorPase.setFont(f);
		listarPorPase.setHorizontalAlignment(SwingConstants.CENTER);
		listarPorPase.setForeground(Color.DARK_GRAY);
		listarPorPase.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		this.add(listarPorPase, BorderLayout.PAGE_START);
		
		// _______ CAMPOS ENTRADA _______
		JPanel idPase = new JPanel(new FlowLayout());
		idPase.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		JLabel idPaseLabel = new JLabel("ID Pase:");
		idField = new JTextField(10);
		JButton listar = new JButton("Listar");
		listar.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(validation()){
					id = Integer.parseInt(idField.getText());
					Context request = new Context(ContextEnum.LISTARCOMPRASPORPASE, Integer.parseInt(idField.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
				}else{
					JOptionPane.showMessageDialog(parent,"Los datos introducidos son sintacticamente erroneos", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		idPase.add(idPaseLabel);
		idPase.add(idField);
		idPase.add(listar);
		
		this.add(idPase, BorderLayout.CENTER);
		
		//______ DATOS (CENTER) _______
		this.add(crearTabla(compras), BorderLayout.AFTER_LAST_LINE);
		
	}

	public Component crearTabla(List<TCompra> aux) {
		listarComprasPorPaseTableModel = new ListarComprasPorPaseTableModel(aux);
		comprasTable = new JTable(listarComprasPorPaseTableModel);
		JScrollPane scrollpane = new JScrollPane(comprasTable);
		scrollpane.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
		return scrollpane;
	}

	public void update(Object o) {
		if(hasError(o)){
			Message msg = ErrorHandlerManager.getInstance().getMessage(EntityEnum.COMPRA, ((ArrayList<TCompra>) o).get(0).GetID());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			parent.showPanel(this);
			clear();
		}else{
			parent.showPanel(this);
			idField.setText(id.toString());
			compras = (ArrayList<TCompra>) o;
			listarComprasPorPaseTableModel.updateList(compras);
		}
	}

	public boolean hasError(Object response) {
		return ((ArrayList<TCompra>) response).size() > 0 && ((ArrayList<TCompra>) response).get(0).GetID() < 0;
	}

	public boolean validation() {
		try {
			if (idField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(idField.getText()) < 0)
				throw new Exception();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public void clear() {
		idField.setText("");
		this.listarComprasPorPaseTableModel.updateList(new ArrayList<TCompra>());
	}
	
}