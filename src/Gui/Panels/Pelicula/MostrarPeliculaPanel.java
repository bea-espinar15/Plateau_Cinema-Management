/**
 * 
 */
package Presentacion.Gui.Panels.Pelicula;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import Negocio.Pelicula.TPelicula;
import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;

public class MostrarPeliculaPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;
	private static MostrarPeliculaPanel instance;
	private Panel_Main_Pelicula parent;
	private JTextField idField, activoField, tituloField, directorField, generoField, duracionField;
	private TPelicula pelicula;
	
	public MostrarPeliculaPanel() {
		initGUI();
	}

	public static MostrarPeliculaPanel getInstance() {
		if (instance == null)
			instance = new MostrarPeliculaPanel();
		return instance;
	}

	public void setParent(Panel_Main_Pelicula parent) {
		this.parent = parent;
	}

	private void initGUI() {
		
		this.setLayout(new BorderLayout());

		// __________ CABECERA _________
		JLabel titleLbl = new JLabel("MOSTRAR PELICULA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		titleLbl.setFont(f);
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setForeground(Color.DARK_GRAY);
		titleLbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(titleLbl, BorderLayout.PAGE_START);

		// ___________ CENTER ______________
		JPanel centerPanel = new JPanel(null);

		// ___________ CENTER - ID FIELD + BUTTON ______________
		JPanel idPanel = new JPanel(new FlowLayout());
		idPanel.setBackground(Color.LIGHT_GRAY);
		idPanel.setBounds(200, 35, 200, 30);
		JLabel idLbl = new JLabel("ID:");
		this.idField = new JTextField(10);
		idPanel.add(idLbl);
		idPanel.add(idField);

		centerPanel.add(idPanel);

		JButton mostrarBtn = new JButton("Mostrar");
		mostrarBtn.setBounds(400, 35, 100, 30);
		mostrarBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					Context request = new Context(ContextEnum.MOSTRARPELICULA, Integer.parseInt(idField.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
				} 
				else 
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error",
							JOptionPane.ERROR_MESSAGE);
			}
		});

		centerPanel.add(mostrarBtn);

		// ___________ CENTER - DATA SHOWN ______________

		// ACTIVO
		JPanel activoPanel = new JPanel(new FlowLayout());
		activoPanel.setBounds(64, 120, 250, 30);
		JLabel activoLbl = new JLabel("Activo: ");
		activoField = new JTextField(10);
		activoField.setEditable(false);
		activoPanel.add(activoLbl);
		activoPanel.add(activoField);

		centerPanel.add(activoPanel);

		// TITULO
		JPanel tituloPanel = new JPanel(new FlowLayout());
		tituloPanel.setBounds(66, 145, 250, 30);
		JLabel tituloLbl = new JLabel("Titulo: ");
		this.tituloField = new JTextField(10);
		tituloField.setEditable(false);
		tituloPanel.add(tituloLbl);
		tituloPanel.add(tituloField);

		centerPanel.add(tituloPanel);

		// DIRECTOR
		JPanel directorPanel = new JPanel(new FlowLayout());
		directorPanel.setBounds(59, 170, 250, 30);
		JLabel directorLbl = new JLabel("Director: ");
		this.directorField = new JTextField(10);
		directorField.setEditable(false);
		directorPanel.add(directorLbl);
		directorPanel.add(directorField);

		centerPanel.add(directorPanel);

		// GENERO
		JPanel generoPanel = new JPanel(new FlowLayout());
		generoPanel.setBounds(61, 195, 250, 30);
		JLabel generoLbl = new JLabel("Genero: ");
		this.generoField = new JTextField(10);
		generoField.setEditable(false);
		generoPanel.add(generoLbl);
		generoPanel.add(generoField);

		centerPanel.add(generoPanel);

		// DURACION
		JPanel duracionPanel = new JPanel(new FlowLayout());
		duracionPanel.setBounds(40, 220, 250, 30);
		JLabel duracionLbl = new JLabel("Duracion (min): ");
		this.duracionField = new JTextField(10);
		duracionField.setEditable(false);
		duracionPanel.add(duracionLbl);
		duracionPanel.add(duracionField);

		centerPanel.add(duracionPanel);

		this.add(centerPanel, BorderLayout.CENTER);

	}

	@Override
	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.PELICULA, ((TPelicula) o).GetID());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		}
		else {
			pelicula = (TPelicula) o;
			boolean activo = pelicula.GetActivo();
			if(activo)
				activoField.setText("SI");
			else
				activoField.setText("NO");
			tituloField.setText(pelicula.GetTitulo());
			directorField.setText(pelicula.GetDirector());				
			generoField.setText(pelicula.GetGenero());
			duracionField.setText(pelicula.GetDuracion().toString());
		}
	}

	@Override
	public boolean hasError(Object response) {
		if(((TPelicula) response).GetID() < 0)
			return true;
		return false;
	}

	@Override
	public boolean validation() {
		try{
			if (idField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(idField.getText()) < 0)
				throw new Exception();
			return true;
		} 
		catch(Exception e){
			return false;
		}
	}

	@Override
	public void clear() {
		idField.setText("");
		activoField.setText("");
		tituloField.setText("");
		directorField.setText("");
		generoField.setText("");
		duracionField.setText("");
	}

}