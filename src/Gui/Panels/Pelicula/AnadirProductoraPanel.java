
package Presentacion.Gui.Panels.Pelicula;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;
import utilities.Pair;


public class AnadirProductoraPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;

	private static AnadirProductoraPanel instance;
	private Panel_Main_Pelicula parent;
	JTextField idPeliculaField, idProductoraField;

	public AnadirProductoraPanel() {
		initGUI();
	}

	private void initGUI() {
		
		this.setLayout(new BorderLayout());

		// __________ CABECERA _________
		JLabel titleLbl = new JLabel("ANYADIR PRODUCTORA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		titleLbl.setFont(f);
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setForeground(Color.DARK_GRAY);
		titleLbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(titleLbl, BorderLayout.PAGE_START);

		// ___________ CENTER ______________
		JPanel centerPanel = new JPanel(null);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

		// ___________ CENTER - ID FIELDS  ______________
		JPanel idPanel = new JPanel();
		idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.X_AXIS));
		idPanel.setBorder(BorderFactory.createEmptyBorder(42, 0, 0, 0));
		idPanel.setMinimumSize(new Dimension(600,100));
		idPanel.setMaximumSize(new Dimension(600,100));
		idPanel.setPreferredSize(new Dimension(600,100));
		
		// ID PELICULA
		JPanel peliculaPanel = new JPanel(new FlowLayout());
		JLabel idPeliLbl = new JLabel("ID Pelicula: ");
		idPeliculaField = new JTextField(10);
		peliculaPanel.add(idPeliLbl);
		peliculaPanel.add(idPeliculaField);

		idPanel.add(peliculaPanel);
		idPanel.add(Box.createRigidArea(new Dimension(5,0)));
		
		// ID PRODUCTORA
		JPanel productoraPanel = new JPanel(new FlowLayout());
		JLabel idProdLbl = new JLabel("ID Productora: ");
		idProductoraField = new JTextField(10);
		productoraPanel.add(idProdLbl);
		productoraPanel.add(idProductoraField);

		idPanel.add(productoraPanel);
		
		centerPanel.add(idPanel);

		JButton annadirBtn = new JButton("Aceptar");
		annadirBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					Context request = new Context(ContextEnum.ANADIRPRODUCTORA, new Pair(Integer.parseInt(idProductoraField.getText()), Integer.parseInt(idPeliculaField.getText())));
					ApplicationController.GetInstance().ManageRequest(request);
				} else 
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error",
							JOptionPane.ERROR_MESSAGE);
			}
		});
		annadirBtn.setAlignmentX(CENTER_ALIGNMENT);
		
		centerPanel.add(annadirBtn);

		this.add(centerPanel, BorderLayout.CENTER);
		
	}

	public static AnadirProductoraPanel getInstance() {
		if (instance == null) {
			instance = new AnadirProductoraPanel();
		}
		return instance;
	}

	public void setParent(Panel_Main_Pelicula parent) {
		this.parent = parent;
	}

	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.PELICULA, (Integer)o);
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		}
		else {
			JOptionPane.showMessageDialog(parent, "La productora se ha asociado a la pelicula con exito");
			clear();
		}
	}

	public boolean hasError(Object response) {
		if ((Integer)response < 0)
			return true;
		return false;
	}

	public void clear() {
		idPeliculaField.setText("");
		idProductoraField.setText("");
	}

	@Override
	public boolean validation() {
		try {
			if (idPeliculaField.getText().equals("") || idProductoraField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(idPeliculaField.getText()) < 0 || Integer.parseInt(idProductoraField.getText()) < 0)
				throw new Exception();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}