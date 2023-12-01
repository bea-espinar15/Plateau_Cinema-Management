package Presentacion.Gui.Panels.Compra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Negocio.Compra.TLineaCompra;
import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;

public class VerResumenDeCompraPanel extends GeneralPanel{

	private static final long serialVersionUID = 1L;
	private VerResumenDeCompraTableModel VerResumenCompraTableModel;
	private static VerResumenDeCompraPanel instance;
	private Panel_Main_Compra parent;
	private JTable lineaComprasTable;
	private JTextField idCompraField;
	private ArrayList<TLineaCompra> lineaCompras;
	private Integer id;

	public VerResumenDeCompraPanel(){
		lineaCompras = new ArrayList<>();
		initGUI();
	}
	
	public static VerResumenDeCompraPanel getInstance() {
		if(instance == null)
			  instance = new VerResumenDeCompraPanel();
		return instance;
	}

	public void setParent(Panel_Main_Compra parent) {
		this.parent = parent;
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		
		//_______ CABECERA (PAGE_START) ________
		JLabel verResumen = new JLabel("VER RESUMEN DE COMPRA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		verResumen.setFont(f);
		verResumen.setHorizontalAlignment(SwingConstants.CENTER);
		verResumen.setForeground(Color.DARK_GRAY);
		verResumen.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		this.add(verResumen, BorderLayout.PAGE_START);
		
		// _______ CAMPOS ENTRADA _______
		JPanel idCompra = new JPanel(new FlowLayout());
		idCompra.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		JLabel idCompraLabel = new JLabel("ID Compra:");
		idCompraField = new JTextField(10);
		JButton listar = new JButton("Listar");
		listar.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(validation()){
					id = Integer.parseInt(idCompraField.getText());
					Context request = new Context(ContextEnum.VERRESUMENDECOMPRA, Integer.parseInt(idCompraField.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
				}else{
					JOptionPane.showMessageDialog(parent,"Los datos introducidos son sintacticamente erroneos", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		idCompra.add(idCompraLabel);
		idCompra.add(idCompraField);
		idCompra.add(listar);
		
		this.add(idCompra, BorderLayout.CENTER);
		
		//______ DATOS (CENTER) _______
		this.add(crearTabla(lineaCompras), BorderLayout.AFTER_LAST_LINE);
	}

	private Component crearTabla(ArrayList<TLineaCompra> aux) {
		VerResumenCompraTableModel = new VerResumenDeCompraTableModel(aux);
		lineaComprasTable = new JTable(VerResumenCompraTableModel);
		JScrollPane scrollpane = new JScrollPane(lineaComprasTable);
		scrollpane.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
		return scrollpane;
	}

	@Override
	public void update(Object o) {
		if(hasError(o)){
			Message msg = ErrorHandlerManager.getInstance().getMessage(EntityEnum.COMPRA, ((ArrayList<TLineaCompra>) o).get(0).GetIDCompra());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			parent.showPanel(this);
			clear();
		}else{
			parent.showPanel(this);
			idCompraField.setText(id.toString());
			lineaCompras = (ArrayList<TLineaCompra>) o;
			VerResumenCompraTableModel.updateList(lineaCompras);
		}
	}

	@Override
	public boolean hasError(Object response) {
		return ((ArrayList<TLineaCompra>) response).size() > 0 && ((ArrayList<TLineaCompra>) response).get(0).GetIDCompra() < 0;
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
		VerResumenCompraTableModel.updateList(new ArrayList<TLineaCompra>());
	}

}
