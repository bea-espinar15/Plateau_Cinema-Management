
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
import javax.swing.SwingConstants;

import java.util.ArrayList;

import Negocio.Compra.TCompra;
import Negocio.Pelicula.TPelicula;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ListarPeliculaPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;

	private static ListarPeliculaPanel instance;
	private Panel_Main_Pelicula parent;
	private JTable peliculasTable;
	private ListarPeliculasTableModel listarPeliculasTableModel;
	private ArrayList<TPelicula> peliculas;

	public ListarPeliculaPanel() {
		this.peliculas = new ArrayList<TPelicula>();
		initGUI();
		
	}

	private void initGUI() {
		
		setLayout(new BorderLayout());

		// __________ CABECERA _________

		JLabel listarPases = new JLabel("LISTAR PELICULAS");
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
					Context request = new Context(ContextEnum.LISTARPELICULAS, null);
					ApplicationController.GetInstance().ManageRequest(request);
			}

		});
		listarBtnPanel.add(listar);

		this.add(listarBtnPanel, BorderLayout.SOUTH);

		// _____________ CENTERPANEL ______________

		this.add(crearTabla(this.peliculas), BorderLayout.CENTER);
	}

	public static ListarPeliculaPanel getInstance() {
		if (instance == null) {
			instance = new ListarPeliculaPanel();
		}
		return instance;
	}

	public void setParent(Panel_Main_Pelicula parent) {
		this.parent = parent;
	}

	public Component crearTabla(ArrayList<TPelicula> aux) {
		listarPeliculasTableModel = new ListarPeliculasTableModel(aux);
		this.peliculasTable = new JTable(listarPeliculasTableModel);
		JScrollPane scroll = new JScrollPane(this.peliculasTable);
		scroll.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
		return scroll;
	}

	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.PASE, ((ArrayList<TPelicula>)o).get(0).GetID());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		}
		else {
			parent.showPanel(this);
			this.peliculas = (ArrayList<TPelicula>) o;
			listarPeliculasTableModel.updateList(peliculas);
		}
	}

	public boolean hasError(Object response) {
		return(((((ArrayList<TCompra>) response).size() > 0)) && (((ArrayList<TPelicula>)response).get(0).GetID() < 0));

	}

	public void clear() {
		listarPeliculasTableModel.updateList(new ArrayList<TPelicula>());
	}

	@Override
	public boolean validation() {
		return true;
	}
	
}