
package Presentacion.Gui.Panels.Compra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;

import Negocio.Compra.TCompra;
import Negocio.Compra.TLineaCompra;
import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;
import utilities.Pair;

public class CerrarCompraPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;
	private static CerrarCompraPanel instance;
	private CrearCompraPanel parent;
	private TCompra TCompra;
	private HashMap<Integer, TLineaCompra> carrito;
	private JLabel idPaseLabel, canLabel;
	private JTextField idPaseField;
	private JButton anadir, quitar, cerrar;
	private JSpinner nEntradasS;
	private Boolean quitarPase, anyadirPase;

	private CerrarCompraPanel() {
		quitarPase = false;
		anyadirPase = false;
		initGUI();
	}

	public static CerrarCompraPanel getInstance() {
		if (instance == null)
			instance = new CerrarCompraPanel();
		return instance;
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		// __________ CABECERA _________
		JLabel cerrarCompra = new JLabel("REALIZAR COMPRA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		cerrarCompra.setFont(f);
		cerrarCompra.setHorizontalAlignment(SwingConstants.CENTER);
		cerrarCompra.setForeground(Color.DARK_GRAY);
		cerrarCompra.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(cerrarCompra, BorderLayout.PAGE_START);

		// ______ DATOS (CENTER) _______
		JPanel data = new JPanel();
		data.setLayout(new BoxLayout(data, BoxLayout.Y_AXIS));

		JPanel inputsPanel = new JPanel();
		inputsPanel.setLayout(new BoxLayout(inputsPanel, BoxLayout.X_AXIS));
		inputsPanel.setBorder(BorderFactory.createEmptyBorder(42, 0, 0, 0));
		inputsPanel.setMinimumSize(new Dimension(600, 100));
		inputsPanel.setMaximumSize(new Dimension(600, 100));
		inputsPanel.setPreferredSize(new Dimension(600, 100));

		JPanel idPPanel = new JPanel(new FlowLayout());
		idPaseLabel = new JLabel("ID Pase:");
		idPaseField = new JTextField(10);
		idPPanel.add(idPaseLabel);
		idPPanel.add(idPaseField);

		inputsPanel.add(idPPanel);
		inputsPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		JPanel canPanel = new JPanel(new FlowLayout());
		canLabel = new JLabel("Nº Entradas:");
		SpinnerModel model = new SpinnerNumberModel(1, 1, 100, 1);
		nEntradasS = new JSpinner(model);
		nEntradasS.setPreferredSize(new Dimension(35, 22));
		canPanel.add(canLabel);
		canPanel.add(nEntradasS);

		inputsPanel.add(canPanel);
		inputsPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		data.add(inputsPanel);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.setPreferredSize(new Dimension(350, 100));
		buttonsPanel.setMaximumSize(new Dimension(350, 100));
		buttonsPanel.setMinimumSize(new Dimension(350, 100));

		anadir = new JButton("Anyadir");
		anadir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				anyadirPase = true;
				if (validation()) {
					if (carrito.containsKey(Integer.parseInt(idPaseField.getText()))) {
						Integer entradasAct = carrito.get(Integer.parseInt(idPaseField.getText())).GetNEntradas()
								+ (Integer) nEntradasS.getValue();
						carrito.get(Integer.parseInt(idPaseField.getText())).SetNEntradas(entradasAct);
					} else {
						TLineaCompra lineaCompra = new TLineaCompra(TCompra.GetID());
						lineaCompra.SetIDPase(Integer.parseInt(idPaseField.getText()));
						lineaCompra.SetNEntradas((Integer) nEntradasS.getValue());
						carrito.put(Integer.parseInt(idPaseField.getText()), lineaCompra);
					}
				} else
					JOptionPane.showMessageDialog(parent, "Los datos son sintacticamente erroneos", "Error",
							JOptionPane.ERROR_MESSAGE);
				anyadirPase = false;
				clear();
			}

		});

		buttonsPanel.add(anadir);

		buttonsPanel.add(Box.createHorizontalGlue());

		quitar = new JButton("Quitar");
		quitar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				quitarPase = true;
				if (validation()) {
					if (carrito.containsKey(Integer.parseInt(idPaseField.getText()))) {
						Integer entradasAct = carrito.get(Integer.parseInt(idPaseField.getText())).GetNEntradas()
								- (Integer) nEntradasS.getValue();
						if (entradasAct <= 0)
							carrito.remove(Integer.parseInt(idPaseField.getText()));
						else
							carrito.get(Integer.parseInt(idPaseField.getText())).SetNEntradas(entradasAct);
					} else {
						JOptionPane.showMessageDialog(parent, "El pase no estaba anyadido al carrito", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else
					JOptionPane.showMessageDialog(parent, "Los datos son sintacticamente erroneos", "Error",
							JOptionPane.ERROR_MESSAGE);
				clear();
				quitarPase = false;
			}

		});

		buttonsPanel.add(quitar);

		buttonsPanel.add(Box.createHorizontalGlue());

		cerrar = new JButton("Cerrar");
		cerrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					Context request = new Context(ContextEnum.CERRARCOMPRA,
							new Pair<TCompra, HashMap<Integer, TLineaCompra>>(TCompra, carrito));
					ApplicationController.GetInstance().ManageRequest(request);
				} else
					JOptionPane.showMessageDialog(parent, "Los datos son sintacticamente erroneos", "Error",
							JOptionPane.ERROR_MESSAGE);
				parent.showAbrirPanel();
			}
		});

		buttonsPanel.add(cerrar);

		data.add(buttonsPanel);

		this.add(data, BorderLayout.CENTER);
	}

	public void setTCompra(TCompra TCompra) {
		this.TCompra = TCompra;
	}

	public void setCarrito(HashMap<Integer, TLineaCompra> carrito) {
		this.carrito = carrito;
	}

	public void setParent(CrearCompraPanel parent) {
		this.parent = parent;
	}

	public void showCerrarPanel(TCompra TCompra, HashMap<Integer, TLineaCompra> carrito) {
		this.setVisible(true);
		setTCompra(TCompra);
		setCarrito(carrito);
	}

	@Override
	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.COMPRA, (Integer) o);
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
			parent.showAbrirPanel();
		} else {
			JOptionPane.showMessageDialog(parent, "La compra se ha realizado con exito, ID: " + o);
			clear();
			parent.showAbrirPanel();
		}
	}

	@Override
	public boolean hasError(Object response) {
		if ((Integer) response < 0)
			return true;
		return false;
	}

	@Override
	public boolean validation() {
		try {
			if (quitarPase) {
				if (idPaseField.getText().equals(""))
					throw new Exception();
				if (Integer.parseInt(idPaseField.getText()) < 0)
					throw new Exception();
			} else if (anyadirPase) {
				if (idPaseField.getText().equals(""))
					throw new Exception();
				if (Integer.parseInt(idPaseField.getText()) < 0)
					throw new Exception();
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void clear() {
		idPaseField.setText("");
		nEntradasS.setValue(1);
	}

}