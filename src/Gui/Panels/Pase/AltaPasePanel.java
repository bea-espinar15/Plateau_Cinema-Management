	package Presentacion.Gui.Panels.Pase;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;

import Negocio.Pase.TPase;
import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;

/*
 * Autores: Nico y Axel
 */

public class AltaPasePanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;

	private static AltaPasePanel instance;
	private Panel_Main_Pase parent;
	private JTextField precioField, peliculaField, salaField;
	JSpinner horarioSpinner;
	private Integer id;

	public AltaPasePanel() {
		initGUI();
	}

	public static AltaPasePanel getInstance() {
		if (instance == null) {
			instance = new AltaPasePanel();
		}
		return instance;
	}

	public void setParent(Panel_Main_Pase parent) {
		this.parent = parent;
	}

	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.PASE, ((Integer) o));
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		} else {
			id = (Integer) o;
			String mensaje = "El pase se ha dado de alta con exito con ID " + id.toString();
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
		precioField.setText("");
		peliculaField.setText("");
		salaField.setText("");
		horarioSpinner.setValue(new Date());
	}

	@Override
	public boolean validation() {
		try {
			if (precioField.getText().equals("") || peliculaField.getText().equals("") || salaField.equals(""))
				throw new Exception();
			if (Integer.parseInt(precioField.getText()) <= 0)
				throw new Exception();
			if (Integer.parseInt(peliculaField.getText()) < 0)
				throw new Exception();
			if (Integer.parseInt(salaField.getText()) < 0)
				throw new Exception();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		// __________ CABECERA _________
		JLabel titleLbl = new JLabel("ALTA PASE");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		titleLbl.setFont(f);
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setForeground(Color.DARK_GRAY);
		titleLbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(titleLbl, BorderLayout.PAGE_START);

		// ___________ CENTER ______________
		JPanel centerPanel = new JPanel(null);

		// ___________ CENTER - ALL FIELDS ______________
		JPanel horarioPanel = new JPanel(new FlowLayout());
		horarioPanel.setBounds(44, 55, 250, 30);
		JLabel horarioLbl = new JLabel("Horario: ");
		this.horarioSpinner = new JSpinner(new SpinnerDateModel());
		this.horarioSpinner.setPreferredSize(new Dimension(113, 20));
		horarioSpinner.setValue(new Date());
		horarioPanel.add(horarioLbl);
		horarioPanel.add(horarioSpinner);

		centerPanel.add(horarioPanel);

		JPanel precioPanel = new JPanel(new FlowLayout());
		precioPanel.setBounds(46, 80, 250, 30);
		JLabel precioLbl = new JLabel("Precio: ");
		this.precioField = new JTextField(10);
		precioPanel.add(precioLbl);
		precioPanel.add(precioField);

		centerPanel.add(precioPanel);

		JPanel salaPanel = new JPanel(new FlowLayout());
		salaPanel.setBounds(52, 105, 250, 30);
		JLabel salaLbl = new JLabel("Sala: ");
		this.salaField = new JTextField(10);
		salaPanel.add(salaLbl);
		salaPanel.add(salaField);

		centerPanel.add(salaPanel);

		JPanel peliculaPanel = new JPanel(new FlowLayout());
		peliculaPanel.setBounds(42, 130, 250, 30);
		JLabel peliculaLbl = new JLabel("Pelicula: ");
		this.peliculaField = new JTextField(10);
		peliculaPanel.add(peliculaLbl);
		peliculaPanel.add(peliculaField);

		centerPanel.add(peliculaPanel);

		JButton aceptarBtn = new JButton("Aceptar");
		aceptarBtn.setBounds(150, 205, 90, 30);
		aceptarBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					TPase pase = new TPase((Date) horarioSpinner.getValue(), Integer.parseInt(precioField.getText()),
							Integer.parseInt(peliculaField.getText()), Integer.parseInt(salaField.getText()));
					pase.SetActivo(true);
					Context request = new Context(ContextEnum.ALTAPASE, pase);
					ApplicationController.GetInstance().ManageRequest(request);
				} else {
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		centerPanel.add(aceptarBtn);

		this.add(centerPanel, BorderLayout.CENTER);
	}
}