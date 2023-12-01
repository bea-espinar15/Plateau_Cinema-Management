
package Presentacion.Gui.Panels.Sala;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

public class BajaSalaPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;

	private static BajaSalaPanel instance;
	private Panel_Main_Sala parent;
	private JTextField field;

	BajaSalaPanel() {
		initGUI();
	}

	public static BajaSalaPanel getInstance() {
		if (instance == null)
			instance = new BajaSalaPanel();
		return instance;
	}

	public void setParent(Panel_Main_Sala parent) {
		this.parent = parent;
	}

	private void initGUI() {
		setLayout(new BorderLayout());

		// CABECERA
		JLabel bajaSala = new JLabel("BAJA SALA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		bajaSala.setFont(f);
		bajaSala.setHorizontalAlignment(SwingConstants.CENTER);
		bajaSala.setForeground(Color.DARK_GRAY);
		bajaSala.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(bajaSala, BorderLayout.PAGE_START);

		JPanel centerPanel = new JPanel();
		JPanel idPanel = new JPanel();
		idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.X_AXIS));
		idPanel.setBorder(BorderFactory.createEmptyBorder(35, 0, 0, 0));
		JLabel id = new JLabel("ID:");
		this.field = new JTextField(10);
		idPanel.add(id);
		idPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		idPanel.add(field);

		idPanel.add(Box.createRigidArea(new Dimension(15, 0)));

		JButton ok = new JButton("Aceptar");
		idPanel.add(ok);
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					Context request = new Context(ContextEnum.BAJASALA, Integer.parseInt(field.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
				} else {
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		centerPanel.add(idPanel);

		this.add(centerPanel, BorderLayout.CENTER);

	}

	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.SALA, ((Integer) o));
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		}
		else {
			JOptionPane.showMessageDialog(parent, "La sala se ha dado de baja con exito");
			clear();
		}
	}

	public boolean hasError(Object response) {
		return (Integer) response < 0;
	}

	public boolean validation() {
		try {
			if (field.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(field.getText()) < 0)
				throw new Exception();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public void clear() {
		field.setText("");
	}
}