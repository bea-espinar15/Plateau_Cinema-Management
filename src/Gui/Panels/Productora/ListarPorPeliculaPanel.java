
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.util.ArrayList;

import Negocio.Productora.TProductora;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListarPorPeliculaPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;

	private ListarPorPeliculaTableModel listarPorPeliculaTableModel;
	private static ListarPorPeliculaPanel instance;
	private Panel_Main_Productora parent;
	private JTable productoraTable;
	private ArrayList<TProductora> productoras;
	private JTextField idField;
	private Integer id;

	public ListarPorPeliculaPanel() {
		productoras = new ArrayList<TProductora>();
		initGUI();
	}

	public static ListarPorPeliculaPanel getInstance() {
		if (instance == null) {
			instance = new ListarPorPeliculaPanel();
		}

		return instance;
	}

	public void setParent(Panel_Main_Productora parent) {
		this.parent = parent;
	}

	private void initGUI() {

		setLayout(new BorderLayout());

		// _______ CABECERA (PAGE_START) ________
		JLabel listarPorPelicula = new JLabel("LISTAR POR PELICULA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		listarPorPelicula.setFont(f);
		listarPorPelicula.setHorizontalAlignment(SwingConstants.CENTER);
		listarPorPelicula.setForeground(Color.DARK_GRAY);
		listarPorPelicula.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(listarPorPelicula, BorderLayout.PAGE_START);

		// _______ CAMPOS ENTRADA _______
		JPanel idPelicula = new JPanel(new FlowLayout());
		idPelicula.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		JLabel idPeliculaLabel = new JLabel("ID Pelicula:");
		idField = new JTextField(10);
		JButton listar = new JButton("Listar");
		listar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					id = Integer.parseInt(idField.getText());
					Context request = new Context(ContextEnum.LISTARPRODUCTORASPORPELICULA,
							Integer.parseInt(idField.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
				}
				else
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos",
							"Error", JOptionPane.ERROR_MESSAGE);

			}

		});
		idPelicula.add(idPeliculaLabel);
		idPelicula.add(idField);
		idPelicula.add(listar);

		this.add(idPelicula, BorderLayout.CENTER);

		// ______ DATOS (CENTER) _______
		this.add(crearTabla(productoras), BorderLayout.AFTER_LAST_LINE);

	}

	public Component crearTabla(ArrayList<TProductora> aux) {
		listarPorPeliculaTableModel = new ListarPorPeliculaTableModel(aux);
		productoraTable = new JTable(listarPorPeliculaTableModel);
		JScrollPane scrollpane = new JScrollPane(productoraTable);
		scrollpane.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
		return scrollpane;
	}

	@Override
	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.PRODUCTORA, ((ArrayList<TProductora>) o).get(0).GetID());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			parent.showPanel(this);
			clear();
		} else {
			parent.showPanel(this);
			idField.setText(id.toString());
			this.productoras = (ArrayList<TProductora>) o;
			listarPorPeliculaTableModel.updateList(productoras);
		}

	}

	@Override
	public boolean hasError(Object response) {
		return ((ArrayList<TProductora>) response).size() > 0 && ((ArrayList<TProductora>) response).get(0).GetID() < 0;

	}

	@Override
	public void clear() {
		idField.setText("");
		listarPorPeliculaTableModel.updateList(new ArrayList<TProductora>());
	}

	@Override
	public boolean validation() {
		try {
			if (idField.getText() == "")
				throw new Exception();
			if (Integer.parseInt(idField.getText()) < 0)
				throw new Exception();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}