
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
import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;

public class AltaPeliculaPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;
	private static AltaPeliculaPanel instance;
	private Panel_Main_Pelicula parent;

	private JTextField tituloField, directorField, generoField;
	private JSpinner duracionSpinner;
	private Integer idPelicula;

	public AltaPeliculaPanel() {
		initGUI();
	}

	public static AltaPeliculaPanel getInstance() {
		if (instance == null)
			instance = new AltaPeliculaPanel();
		return instance;
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		// __________ CABECERA _________
		JLabel titleLbl = new JLabel("ALTA PELICULA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		titleLbl.setFont(f);
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setForeground(Color.DARK_GRAY);
		titleLbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(titleLbl, BorderLayout.PAGE_START);

		// ___________ CENTER ______________
		JPanel centerPanel = new JPanel(null);

		// ___________ CENTER - ALL FIELDS ______________
		JPanel tituloPanel = new JPanel(new FlowLayout());
		tituloPanel.setBounds(57, 55, 250, 30);
		JLabel tituloLbl = new JLabel("Titulo: ");
		this.tituloField = new JTextField(10);
		tituloPanel.add(tituloLbl);
		tituloPanel.add(tituloField);

		centerPanel.add(tituloPanel);

		JPanel directorPanel = new JPanel(new FlowLayout());
		directorPanel.setBounds(50, 80, 250, 30);
		JLabel directorLbl = new JLabel("Director: ");
		this.directorField = new JTextField(10);
		directorPanel.add(directorLbl);
		directorPanel.add(directorField);

		centerPanel.add(directorPanel);

		JPanel generoPanel = new JPanel(new FlowLayout());
		generoPanel.setBounds(52, 105, 250, 30);
		JLabel generoLbl = new JLabel("Genero: ");
		this.generoField = new JTextField(10);
		generoPanel.add(generoLbl);
		generoPanel.add(generoField);

		centerPanel.add(generoPanel);

		JPanel duracionPanel = new JPanel(new FlowLayout());
		duracionPanel.setBounds(32, 130, 250, 30);
		JLabel duracionLbl = new JLabel("Duracion (min): ");
		this.duracionSpinner = new JSpinner(new SpinnerNumberModel(10, 10, 600, 10));
		this.duracionSpinner.setPreferredSize(new Dimension(115, 18));
		duracionPanel.add(duracionLbl);
		duracionPanel.add(duracionSpinner);

		centerPanel.add(duracionPanel);

		JButton aceptarBtn = new JButton("Aceptar");
		aceptarBtn.setBounds(160, 195, 100, 30);
		aceptarBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					TPelicula peli = new TPelicula(null, true, tituloField.getText(), directorField.getText(),
							generoField.getText(), (Integer) duracionSpinner.getValue());
					Context request = new Context(ContextEnum.ALTAPELICULA, peli);
					ApplicationController.GetInstance().ManageRequest(request);

				} else
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error",
							JOptionPane.ERROR_MESSAGE);
			}
		});

		centerPanel.add(aceptarBtn);

		this.add(centerPanel, BorderLayout.CENTER);
	}

	public void setParent(Panel_Main_Pelicula parent) {
		this.parent = parent;
	}

	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.PELICULA, ((Integer) o));
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		} else {
			idPelicula = (Integer) o;
			String mensaje = "La pelicula se ha dado de alta con exito con ID: " + idPelicula.toString();
			JOptionPane.showMessageDialog(parent, mensaje);
			clear();
		}
	}

	public boolean hasError(Object response) {
		if (((Integer) response) < 0)
			return true;
		return false;
	}

	public void clear() {
		tituloField.setText("");
		directorField.setText("");
		generoField.setText("");
		duracionSpinner.setValue(10);
	}

	@Override
	public boolean validation() {
		if (tituloField.getText().equals(""))
			return false;
		if (directorField.getText().equals(""))
			return false;
		if (generoField.getText().equals(""))
			return false;
		return true;
	}

}