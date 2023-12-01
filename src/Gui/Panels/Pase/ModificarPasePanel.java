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
import javax.swing.SwingUtilities;

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

public class ModificarPasePanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;

	private static ModificarPasePanel instance;
	private Panel_Main_Pase parent;
	private JTextField idField, precioField, peliculaField, salaField;
	JSpinner horarioSpinner;
	private JButton modificar, ok;
	private static boolean buscado = false;
	private TPase pase;

	public ModificarPasePanel() {
		initGUI();
	}

	public static ModificarPasePanel getInstance() {
		if (instance == null) {
			instance = new ModificarPasePanel();
		}
		return instance;
	}

	public void setParent(Panel_Main_Pase parent) {
		this.parent = parent;
	}

	public void update(Object o) {
		if (hasError(o)) {
			int errorCode;
			if (buscado)
				errorCode = ((TPase) o).GetID();
			else
				errorCode = (Integer) o;

			this.clear();

			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.PASE, errorCode);
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		} else {
			if (buscado) {
				pase = (TPase) o;
				updateMostrarPanel();
			} else {
				JOptionPane.showMessageDialog(parent, "El pase se ha modificado con exito");
				clear();
			}
		}
	}

	public void updateMostrarPanel() {
		horarioSpinner.setValue(pase.GetHorario());
		precioField.setText(pase.GetPrecioActual().toString());
		peliculaField.setText(pase.GetIDPelicula().toString());
		salaField.setText(pase.GetIDSala().toString());

		precioField.setEditable(true);
		peliculaField.setEditable(true);
		salaField.setEditable(true);
		horarioSpinner.setEnabled(true);
		modificar.setEnabled(true);
		ok.setEnabled(false);
		idField.setEditable(false);
	}

	public boolean hasError(Object response) {
		if (buscado) {
			if (((TPase) response).GetID() < 0)
				return true;
			return false;
		} else if (((Integer) response) < 0)
			return true;
		return false;
	}

	public void clear() {
		buscado = false;

		idField.setText("");
		precioField.setText("");
		peliculaField.setText("");
		salaField.setText("");
		horarioSpinner.setValue(new Date());

		precioField.setEditable(false);
		peliculaField.setEditable(false);
		salaField.setEditable(false);
		horarioSpinner.setEnabled(false);
		idField.setEditable(true);
		ok.setEnabled(true);
		modificar.setEnabled(false);
	}

	@Override
	public boolean validation() {
		try {
			if (precioField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(precioField.getText()) <= 0)
				throw new Exception();
			if (peliculaField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(peliculaField.getText()) < 0)
				throw new Exception();
			if (salaField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(salaField.getText()) < 0)
				throw new Exception();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean idValidation() {
		try {
			Integer aux = Integer.parseInt(idField.getText());
			if(aux < 0) return false;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		// __________ CABECERA _________
		JLabel titleLbl = new JLabel("MODIFICAR PASE");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		titleLbl.setFont(f);
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setForeground(Color.DARK_GRAY);
		titleLbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(titleLbl, BorderLayout.PAGE_START);

		// ___________ CENTER ______________
		JPanel centerPanel = new JPanel(null);

		// ID
		JPanel idPanel = new JPanel(new FlowLayout());
		idPanel.setBackground(Color.LIGHT_GRAY);
		idPanel.setBounds(100, 35, 170, 30);
		JLabel idLabel = new JLabel("ID:");
		this.idField = new JTextField(10);
		idPanel.add(idLabel);
		idPanel.add(idField);

		centerPanel.add(idPanel);

		// ACEPTAR ID
		ok = new JButton("Buscar");
		ok.setBounds(280, 35, 80, 30);
		ok.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				buscado = true;

				if (idValidation()) {
					Context request = new Context(ContextEnum.MOSTRARPASEPARAMODIFICAR,
							Integer.parseInt(idField.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
				} else {
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		centerPanel.add(ok);

		JPanel horarioPanel = new JPanel(new FlowLayout());
		horarioPanel.setBounds(44, 130, 250, 30);
		JLabel horarioLbl = new JLabel("Horario: ");
		horarioSpinner = new JSpinner(new SpinnerDateModel());
		horarioSpinner.setPreferredSize(new Dimension(113, 20));
		horarioSpinner.setEnabled(false);
		horarioPanel.add(horarioLbl);
		horarioPanel.add(horarioSpinner);

		centerPanel.add(horarioPanel);

		JPanel precioPanel = new JPanel(new FlowLayout());
		precioPanel.setBounds(28, 170, 250, 30);
		JLabel precioLbl = new JLabel("Precio actual: ");
		precioField = new JTextField(10);
		precioField.setEditable(false);
		precioPanel.add(precioLbl);
		precioPanel.add(precioField);

		centerPanel.add(precioPanel);

		JPanel salaPanel = new JPanel(new FlowLayout());
		salaPanel.setBounds(52, 210, 250, 30);
		JLabel salaLbl = new JLabel("Sala: ");
		salaField = new JTextField(10);
		salaField.setEditable(false);
		salaPanel.add(salaLbl);
		salaPanel.add(salaField);

		centerPanel.add(salaPanel);

		JPanel peliculaPanel = new JPanel(new FlowLayout());
		peliculaPanel.setBounds(42, 250, 250, 30);
		JLabel peliculaLbl = new JLabel("Pelicula: ");
		peliculaField = new JTextField(10);
		peliculaField.setEditable(false);
		peliculaPanel.add(peliculaLbl);
		peliculaPanel.add(peliculaField);

		centerPanel.add(peliculaPanel);

		modificar = new JButton("Modificar");
		modificar.setBounds(150, 330, 100, 30);
		modificar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					buscado = false;
					pase.SetHorario((Date) horarioSpinner.getValue());
					pase.SetIDPelicula(Integer.parseInt(peliculaField.getText()));
					pase.SetIDSala(Integer.parseInt(salaField.getText()));
					pase.SetPrecioActual(Integer.parseInt(precioField.getText()));
					Context request = new Context(ContextEnum.MODIFICARPASE, pase);
					ApplicationController.GetInstance().ManageRequest(request);
				} else {
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		modificar.setEnabled(false);

		centerPanel.add(modificar);

		this.add(centerPanel, BorderLayout.CENTER);
	}
}