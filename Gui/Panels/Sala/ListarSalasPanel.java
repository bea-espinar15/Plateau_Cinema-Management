
package Presentacion.Gui.Panels.Sala;

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

import Negocio.Sala.TSala;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListarSalasPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;

	private static ListarSalasPanel instance;
	private Panel_Main_Sala parent;
	private JTable salasTable;
	private ArrayList<TSala> salas;
	private ListarSalasTableModel TableModel;

	public ListarSalasPanel() {
		this.salas = new ArrayList<TSala>();
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());

		// CABECERA

		JLabel listarSalas = new JLabel("LISTAR SALAS");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		listarSalas.setFont(f);
		listarSalas.setHorizontalAlignment(SwingConstants.CENTER);
		listarSalas.setForeground(Color.DARK_GRAY);
		listarSalas.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(listarSalas, BorderLayout.PAGE_START);

		JPanel botonListar = new JPanel();
		botonListar.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		JButton listar = new JButton("Listar");
		listar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Context request = new Context(ContextEnum.LISTARSALAS, null);
				ApplicationController.GetInstance().ManageRequest(request);
			}

		});
		botonListar.add(listar);

		this.add(botonListar, BorderLayout.SOUTH);

		// CENTERPANEL
		this.add(crearTabla(salas), BorderLayout.CENTER);

	}

	public static ListarSalasPanel getInstance() {

		if (instance == null) {
			instance = new ListarSalasPanel();
		}
		return instance;

	}

	public void setParent(Panel_Main_Sala parent) {
		this.parent = parent;
	}

	public Component crearTabla(ArrayList<TSala> aux) {
		TableModel = new ListarSalasTableModel(aux);
		salasTable = new JTable(TableModel);
		JScrollPane scrollpane = new JScrollPane(salasTable);
		scrollpane.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
		return scrollpane;
	}

	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.SALA, ((ArrayList<TSala>)o).get(0).GetId());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			parent.showPanel(this);
			clear();
		}
		else {
			parent.showPanel(this);
			this.salas = (ArrayList<TSala>) o;
			TableModel.updateList(salas);
		}
	}

	public void clear() {
		TableModel.updateList(new ArrayList<TSala>());
	}

	@Override
	public boolean validation() {
		return true;
	}

	@Override
	public boolean hasError(Object response) {
		if (((ArrayList<TSala>)response).size() > 0 && ((ArrayList<TSala>)response).get(0).GetId() < 0)
			return true;
		return false;
	}
}