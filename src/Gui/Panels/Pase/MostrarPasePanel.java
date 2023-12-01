package Presentacion.Gui.Panels.Pase;

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

public class MostrarPasePanel extends GeneralPanel {
	private static final long serialVersionUID = 1L;

	private static MostrarPasePanel instance;
	private Panel_Main_Pase parent;
	private JTextField idField, activoField, horarioField, precioField, peliculaField, salaField, asientosField;
	private TPase pase;

	public MostrarPasePanel() {
		initGUI();
	}

	public static MostrarPasePanel getInstance() {
		if (instance == null) {
			instance = new MostrarPasePanel();
		}
		return instance;
	}

	public void setParent(Panel_Main_Pase parent) {
		this.parent = parent;
	}

	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.PASE, ((TPase) o).GetID());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		}
		else {
			pase = (TPase) o;
			updatePanel();
		}
	}
	
	
	public void updatePanel()
	{
		SwingUtilities.invokeLater(() -> {
			boolean activo = pase.GetActivo();
			if(activo)
				activoField.setText("SI");
			else
				activoField.setText("NO");
			horarioField.setText(pase.GetHorario().toString());
			precioField.setText(pase.GetPrecioActual().toString());
			peliculaField.setText(pase.GetIDPelicula().toString());
			salaField.setText(pase.GetIDSala().toString());
			asientosField.setText(pase.GetAsientosLibres().toString());
		});
	}

	public boolean hasError(Object response) {
		if(((TPase) response).GetID() < 0)
			return true;
		return false;
	}

	public void clear() {
		idField.setText("");
		activoField.setText("");
		precioField.setText("");
		peliculaField.setText("");
		salaField.setText("");
		asientosField.setText("");
		horarioField.setText("");
	}

	@Override
	public boolean validation() {
		try{
			if (idField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(idField.getText()) < 0)
				throw new Exception();
			return true;
		} catch(Exception e){
			return false;
		}
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		// __________ CABECERA _________
		JLabel titleLbl = new JLabel("MOSTRAR PASE");
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
		idPanel.setBounds(200, 50, 200, 30);
		JLabel idLbl = new JLabel("ID:");
		this.idField = new JTextField(10);
		idPanel.add(idLbl);
		idPanel.add(idField);

		centerPanel.add(idPanel);

		JButton mostrarBtn = new JButton("Mostrar");
		mostrarBtn.setBounds(400, 50, 100, 30);
		mostrarBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					Context request = new Context(ContextEnum.MOSTRARPASE, Integer.parseInt(idField.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
				} else {
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		centerPanel.add(mostrarBtn);

		// ___________ CENTER - DATA SHOWN ______________

		// ACTIVO
		JPanel activoPanel = new JPanel(new FlowLayout());
		activoPanel.setBounds(50, 100, 250, 30);
		JLabel activoLbl = new JLabel("Activo: ");
		activoField = new JTextField(10);
		activoField.setEditable(false);
		activoPanel.add(activoLbl);
		activoPanel.add(activoField);

		centerPanel.add(activoPanel);
		
		// HORARIO
		JPanel horarioPanel = new JPanel(new FlowLayout());
		horarioPanel.setBounds(46, 125, 250, 30);
		JLabel horarioLbl = new JLabel("Horario: ");
		this.horarioField = new JTextField(10);
		horarioField.setEditable(false);
		horarioPanel.add(horarioLbl);
		horarioPanel.add(horarioField);

		centerPanel.add(horarioPanel);

		// PRECIO ACTUAL
		JPanel precioPanel = new JPanel(new FlowLayout());
		precioPanel.setBounds(28, 150, 250, 30);
		JLabel precioLbl = new JLabel("Precio actual: ");
		this.precioField = new JTextField(10);
		precioField.setEditable(false);
		precioPanel.add(precioLbl);
		precioPanel.add(precioField);

		centerPanel.add(precioPanel);

		// ASIENTOS LIBRES
		JPanel asientosPanel = new JPanel(new FlowLayout());
		asientosPanel.setBounds(22, 175, 250, 30);
		JLabel asientosLbl = new JLabel("Asientos libres: ");
		this.asientosField = new JTextField(10);
		asientosField.setEditable(false);
		asientosPanel.add(asientosLbl);
		asientosPanel.add(asientosField);

		centerPanel.add(asientosPanel);

		// ID SALA
		JPanel salaPanel = new JPanel(new FlowLayout());
		salaPanel.setBounds(52, 200, 250, 30);
		JLabel salaLbl = new JLabel("Sala: ");
		this.salaField = new JTextField(10);
		salaField.setEditable(false);
		salaPanel.add(salaLbl);
		salaPanel.add(salaField);

		centerPanel.add(salaPanel);

		// ID PELICULA
		JPanel peliculaPanel = new JPanel(new FlowLayout());
		peliculaPanel.setBounds(42, 225, 250, 30);
		JLabel peliculaLbl = new JLabel("Pelicula: ");
		this.peliculaField = new JTextField(10);
		peliculaField.setEditable(false);
		peliculaPanel.add(peliculaLbl);
		peliculaPanel.add(peliculaField);

		centerPanel.add(peliculaPanel);

		this.add(centerPanel, BorderLayout.CENTER);

	}

}