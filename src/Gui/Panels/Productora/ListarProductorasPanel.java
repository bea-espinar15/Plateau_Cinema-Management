
package Presentacion.Gui.Panels.Productora;

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

import Negocio.Productora.TProductora;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListarProductorasPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;
	private ListarProductorasTableModel listarProductorasTableModel;
	private static ListarProductorasPanel instance;
	private Panel_Main_Productora parent;
	private JTable productorasTable;
	private ArrayList<TProductora> productoras;

	ListarProductorasPanel() {
		productoras = new ArrayList<TProductora>();
		initGUI();
	}

	public static ListarProductorasPanel getInstance() {
		if (instance == null) {
			instance = new ListarProductorasPanel();
		}
		return instance;
	}

	public void setParent(Panel_Main_Productora parent) {
		this.parent = parent;
	}

	private void initGUI() {
		setLayout(new BorderLayout());

		// __________ CABECERA _________

		JLabel listarPases = new JLabel("LISTAR PRODUCTORAS");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		listarPases.setFont(f);
		listarPases.setHorizontalAlignment(SwingConstants.CENTER);
		listarPases.setForeground(Color.DARK_GRAY);
		listarPases.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(listarPases, BorderLayout.PAGE_START);

		// ______________ BOTON ____________________

		JPanel listarBtnPanel = new JPanel();
		listarBtnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		JButton listar = new JButton("Listar");
		listar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Context request = new Context(ContextEnum.LISTARPRODUCTORAS, null);
				ApplicationController.GetInstance().ManageRequest(request);
			}

		});
		listarBtnPanel.add(listar);

		this.add(listarBtnPanel, BorderLayout.SOUTH);

		// _____________ CENTERPANEL ______________

		this.add(crearTabla(productoras), BorderLayout.CENTER);
	}

	public Component crearTabla(ArrayList<TProductora> aux) {
		listarProductorasTableModel = new ListarProductorasTableModel(aux);
		productorasTable = new JTable(listarProductorasTableModel);
		JScrollPane scrollpane = new JScrollPane(productorasTable);
		scrollpane.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
		return scrollpane;
	}

	@Override
	public void update(Object o) {
		if (hasError(o)) {
			Message msg = ErrorHandlerManager.getInstance().getMessage(EntityEnum.PRODUCTORA,
					((ArrayList<TProductora>) o).get(0).GetID());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			parent.showPanel(this);
			clear();
		} else {
			parent.showPanel(this);
			this.productoras = (ArrayList<TProductora>) o;
			this.listarProductorasTableModel.updateList(productoras);
		}

	}

	@Override
	public boolean hasError(Object response) {
		return ((ArrayList<TProductora>) response).size() > 0 && ((ArrayList<TProductora>) response).get(0).GetID() < 0;
	}

	@Override
	public boolean validation() {
		return true;
	}

	// no se si está bien
	@Override
	public void clear() {
		listarProductorasTableModel.updateList(new ArrayList<TProductora>());
		// this.crearTabla(new ArrayList<TProductora>());
	}

}