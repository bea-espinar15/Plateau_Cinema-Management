
package Presentacion.Gui.Panels.Pelicula;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import Negocio.Pelicula.TPelicula;
import Negocio.Productora.TProductora;
import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;

public class ModificarPeliculaPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;

	private static ModificarPeliculaPanel instance;
	private Panel_Main_Pelicula parent;
	private JTextField idField, tituloField, directorField, generoField;
	private JLabel duracionLbl;
	private JSpinner duracionSpinner;
	private JButton modificar, ok;
	private Boolean buscado;
//	private Integer idStored;

	public ModificarPeliculaPanel() {
		buscado = false;
		initGUI();
	}

	private void initGUI() {

		this.setLayout(new BorderLayout());

		// __________ CABECERA _________
		JLabel titleLbl = new JLabel("MODIFICAR PELICULA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		titleLbl.setFont(f);
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setForeground(Color.DARK_GRAY);
		titleLbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(titleLbl, BorderLayout.PAGE_START);

		// ___________ CENTER ______________
		JPanel centerPanel = new JPanel(null);

		JPanel idPanel = new JPanel(new FlowLayout());
		idPanel.setBackground(Color.LIGHT_GRAY);
		idPanel.setBounds(100, 35, 170, 30);
		JLabel idLabel = new JLabel("ID:");
		this.idField = new JTextField(10);
		idPanel.add(idLabel);
		idPanel.add(idField);

		// ACEPTAR ID
		ok = new JButton("Buscar");
		ok.setBounds(280, 35, 80, 30);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (validation()) {
					Context request = new Context(ContextEnum.MOSTRARPELICULAPARAMODIFICAR,Integer.parseInt(idField.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
				} else {
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		centerPanel.add(idPanel);
		centerPanel.add(ok);

		// ___________ CENTER - OTHER FIELDS ______________

		// TITULO
		JPanel tituloPanel = new JPanel(new FlowLayout());
		tituloPanel.setBounds(48, 95, 250, 30);
		JLabel tituloLbl = new JLabel("Titulo: ");
		this.tituloField = new JTextField(10);
		tituloField.setEditable(false);
		tituloPanel.add(tituloLbl);
		tituloPanel.add(tituloField);

		centerPanel.add(tituloPanel);

		// DIRECTOR
		JPanel directorPanel = new JPanel(new FlowLayout());
		directorPanel.setBounds(41, 120, 250, 30);
		JLabel directorLbl = new JLabel("Director: ");
		this.directorField = new JTextField(10);
		directorField.setEditable(false);
		directorPanel.add(directorLbl);
		directorPanel.add(directorField);

		centerPanel.add(directorPanel);

		// GENERO
		JPanel generoPanel = new JPanel(new FlowLayout());
		generoPanel.setBounds(43, 145, 250, 30);
		JLabel generoLbl = new JLabel("Genero: ");
		this.generoField = new JTextField(10);
		generoField.setEditable(false);
		generoPanel.add(generoLbl);
		generoPanel.add(generoField);

		centerPanel.add(generoPanel);

		// DURACION
		JPanel duracionPanel = new JPanel(new FlowLayout());
		duracionPanel.setBounds(40, 170, 250, 30);
		this.duracionLbl = new JLabel("Duracion: ");
		this.duracionSpinner = new JSpinner(new SpinnerNumberModel(10, 10, 600, 10));
		this.duracionSpinner.setPreferredSize(new Dimension(115, 20));
		duracionSpinner.setEnabled(false);
		duracionPanel.add(duracionLbl);
		duracionPanel.add(duracionSpinner);

		centerPanel.add(duracionPanel);

		modificar = new JButton("Modificar");
		modificar.setBounds(150, 235, 100, 30);
		modificar.setEnabled(false);
		modificar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					// request
					Context c = new Context(ContextEnum.MODIFICARPELICULA,new TPelicula(Integer.parseInt(idField.getText()),
						true,tituloField.getText(), directorField.getText(), generoField.getText(), (Integer)duracionSpinner.getValue()));
					ApplicationController.GetInstance().ManageRequest(c);
				
				} else 
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error",JOptionPane.ERROR_MESSAGE);
			}
			
		});

		centerPanel.add(modificar);

		this.add(centerPanel, BorderLayout.CENTER);
	}

	public static ModificarPeliculaPanel getInstance() {
		if (instance == null) {
			instance = new ModificarPeliculaPanel();
		}
		return instance;
	}

	public void setParent(Panel_Main_Pelicula parent) {
		this.parent = parent;
	}

	public void update(Object o) {
		if (!buscado) { // se ha pulsado mostrar
			if (hasError(o)) {
				ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
				Message msg = ehm.getMessage(EntityEnum.PELICULA, ((TPelicula)o).GetID());
				JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
				clear();
			}
			else {
				TPelicula aux = (TPelicula)o;
				tituloField.setText(aux.GetTitulo());
				directorField.setText(aux.GetDirector());
				generoField.setText(aux.GetGenero());
				this.duracionSpinner.setValue(aux.GetDuracion());
				buscado = true;
				tituloField.setEditable(true);
				directorField.setEditable(true);
				generoField.setEditable(true);
				duracionSpinner.setEnabled(true);
				modificar.setEnabled(true);
				idField.setEditable(false);
				ok.setEnabled(false);
			}
		}
		else {
			if (hasError(o)) {
				ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
				Message msg = ehm.getMessage(EntityEnum.PELICULA, (Integer)o);
				JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
				clear();
			}
			else{
				JOptionPane.showMessageDialog(parent,"La pelicula se ha modificado con exito ");
				clear();
			}
		}
	}

	public boolean hasError(Object response) {
		if (buscado) {
			return(Integer)response < 0;
		} else {
			return ((TPelicula)response).GetID() < 0;
		}
	}

	public void clear() {
		idField.setText("");
		tituloField.setText("");
		tituloField.setEditable(false);
		directorField.setText("");
		directorField.setEditable(false);
		generoField.setText("");
		generoField.setEditable(false);
		duracionSpinner.setValue(10);
		duracionSpinner.setEnabled(false);
		idField.setEditable(true);
		ok.setEnabled(true);
		modificar.setEnabled(false);
		buscado = false;
	}

	@Override
	public boolean validation() {
		try {
			if (!buscado) {
				if (idField.getText().equals(""))
					throw new Exception();
				if (Integer.parseInt(idField.getText()) < 0)
					throw new Exception();
				return true;
			}
			else {
				if (tituloField.getText().equals(""))
					throw new Exception();
				if (directorField.getText().equals(""))
					throw new Exception();
				if (generoField.getText().equals(""))
					throw new Exception();
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}
}