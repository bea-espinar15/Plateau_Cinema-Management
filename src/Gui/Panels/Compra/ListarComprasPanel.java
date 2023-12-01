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
import javax.swing.SwingConstants;

import java.util.ArrayList;
import Negocio.Compra.TCompra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListarComprasPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;
	private ListarComprasTableModel listarComprasTableModel;
	private static ListarComprasPanel instance;
	private Panel_Main_Compra parent;
	private ArrayList<TCompra> compras;
	private JTable comprasTable;

	public ListarComprasPanel() {
		compras = new ArrayList<>();
		initGUI();
	}

	public static ListarComprasPanel getInstance() {
		if (instance == null) {
			instance = new ListarComprasPanel();
		}
		return instance;
	}

	public void setParent(Panel_Main_Compra parent) {
		this.parent = parent;
	}

	private void initGUI() {
		setLayout(new BorderLayout());

		// __________ CABECERA _________

		JLabel listarCompras = new JLabel("LISTAR COMPRAS");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		listarCompras.setFont(f);
		listarCompras.setHorizontalAlignment(SwingConstants.CENTER);
		listarCompras.setForeground(Color.DARK_GRAY);
		listarCompras.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(listarCompras, BorderLayout.PAGE_START);

		// ______________ BOTON ____________________
		JPanel botonListar = new JPanel();
		botonListar.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		JButton listar = new JButton("Listar");
		listar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Context request = new Context(ContextEnum.LISTARCOMPRAS, null);
				ApplicationController.GetInstance().ManageRequest(request);
			}

		});
		botonListar.add(listar);

		this.add(botonListar, BorderLayout.SOUTH);

		// CENTERPANEL
		this.add(crearTabla(compras), BorderLayout.CENTER);

	}

	public Component crearTabla(ArrayList<TCompra> aux) {
		listarComprasTableModel = new ListarComprasTableModel(aux);
		comprasTable = new JTable(listarComprasTableModel);
		JScrollPane scrollpane = new JScrollPane(comprasTable);
		scrollpane.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
		return scrollpane;
	}

	@Override
	public void update(Object o) {
		if(hasError(o)){
			Message msg = ErrorHandlerManager.getInstance().getMessage(EntityEnum.COMPRA, ((ArrayList<TCompra>) o).get(0).GetID());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		}else{
			parent.showPanel(this);
			this.compras = (ArrayList<TCompra>) o;
			listarComprasTableModel.updateList(compras);
		}
	}

	@Override
	public boolean hasError(Object response) {
		return ((ArrayList<TCompra>) response).size() > 0 && ((ArrayList<TCompra>) response).get(0).GetID() < 0 ;
	}

	@Override
	public boolean validation() {
		return true;
	}

	@Override
	public void clear() {
		this.listarComprasTableModel.updateList(new ArrayList<TCompra>());

	}
}