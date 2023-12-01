
package Presentacion.Gui.Panels.Sala;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import Negocio.Sala.TSala;
import Negocio.Sala.TSalaEspecial;
import Negocio.Sala.TSalaEstandar;
import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;

public class AltaSalaPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;

	private JButton ok;
	private JComboBox<String> typeSala;
	private static AltaSalaPanel instance;
	private Panel_Main_Sala parent;
	private JTextField nomF;
	private JSpinner aforoS;
	private JCheckBox aseosC, _VOC, _3DC;
	private JPanel estandar, especial;

	public AltaSalaPanel() {
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		// ____ CABECERA _____
		JLabel altaSala = new JLabel("ALTA SALA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		altaSala.setFont(f);
		altaSala.setHorizontalAlignment(SwingConstants.CENTER);
		altaSala.setForeground(Color.DARK_GRAY);
		altaSala.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(altaSala, BorderLayout.PAGE_START);

		// DATOS (CENTRO)
		JPanel datos = new JPanel(new GridLayout(1, 2));

		// DATOS COMUNES
		JPanel datosC = new JPanel(null);

		// NOMBRE
		JPanel nombre = new JPanel(new FlowLayout());
		nombre.setBounds(10, 38, 400, 30);
		JLabel nom = new JLabel("Nombre:");
		nomF = new JTextField(10);
		nombre.add(nom);
		nombre.add(nomF);

		// AFORO
		JPanel aforo = new JPanel(new FlowLayout());
		aforo.setBounds(0, 80, 400, 30);
		JLabel aforoLabel = new JLabel("Aforo:");
		SpinnerModel model = new SpinnerNumberModel(10, 10, 100, 10);
		aforoS = new JSpinner(model);
		aforoS.setPreferredSize(new Dimension(45, 25));
		aforo.add(aforoLabel);
		aforo.add(aforoS);

		ok = new JButton("Aceptar");
		ok.setBounds(190, 190, 100, 30);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (validation()) {
					TSala sala;
					if (typeSala.getSelectedItem() == "ESTANDAR") {
						sala = new TSalaEstandar(null, nomF.getText(), (Integer) aforoS.getValue(), true,aseosC.isSelected());
						Context request = new Context(ContextEnum.ALTASALA, sala);
						ApplicationController.GetInstance().ManageRequest(request);
					} else if(typeSala.getSelectedItem() == "ESPECIAL"){
						sala = new TSalaEspecial(null, nomF.getText(), (Integer) aforoS.getValue(), true,_3DC.isSelected(), _VOC.isSelected());
						Context request = new Context(ContextEnum.ALTASALA, sala);
						ApplicationController.GetInstance().ManageRequest(request);
					}
					else
					{
						JOptionPane.showMessageDialog(parent, "No se ha introducido el tipo de la sala", "Error",JOptionPane.ERROR_MESSAGE);
					}
				} else
					JOptionPane.showMessageDialog(parent, "Los datos introduccidos son sintacticamente erroneos", "Error",JOptionPane.ERROR_MESSAGE);

			}
		});

		datosC.add(nombre);
		datosC.add(aforo);
		datosC.add(ok);

		datos.add(datosC);

		// DATOS ESPECIFICOS
		JPanel datosE = new JPanel(null);

		// ESTÁNDAR
		estandar = new JPanel();
		estandar.setBounds(9, 75, 200, 50);
		estandar.setVisible(false);

		// ASEOS
		JPanel aseos = new JPanel(new FlowLayout());
		aseosC = new JCheckBox("Aseos");
		aseos.add(aseosC);

		estandar.add(aseos);

		// ESPECIAL
		especial = new JPanel();
		especial.setBounds(25, 75, 200, 50);
		especial.setVisible(false);

		// 3D
		JPanel _3D = new JPanel(new FlowLayout());
		_3DC = new JCheckBox("3D");
		_3D.add(_3DC);

		// VO
		JPanel _VO = new JPanel(new FlowLayout());
		_VO.setBounds(10, 30, 100, 30);
		_VOC = new JCheckBox("VO");
		_VO.add(_VOC);

		especial.add(_3D);
		especial.add(_VO);

		// COMBO TIPOS
		JPanel tipos = new JPanel(new FlowLayout());
		tipos.setBounds(10, 35, 200, 50);
		JLabel tipo = new JLabel("Tipo:");
		typeSala = new JComboBox<String>();
		typeSala.addItem("ESTANDAR");
		typeSala.addItem("ESPECIAL");
		typeSala.setSelectedItem(null);
		typeSala.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (typeSala.getSelectedItem() == null) {
					estandar.setVisible(false);
					especial.setVisible(false);
					aseosC.setSelected(false);
					_VOC.setSelected(false);
					_3DC.setSelected(false);
				} else if (typeSala.getSelectedItem().equals("ESTANDAR")) {
					estandar.setVisible(true);
					especial.setVisible(false);
					_VOC.setSelected(false);
					_3DC.setSelected(false);
				} else {
					especial.setVisible(true);
					estandar.setVisible(false);
					aseosC.setSelected(false);
				}

			}

		});
		tipos.add(tipo);
		tipos.add(typeSala);
		datosE.add(tipos);
		datosE.add(estandar);
		datosE.add(especial);

		datos.add(datosE);

		this.add(datos, BorderLayout.CENTER);

	}

	public static AltaSalaPanel getInstance() {
		if (instance == null) {
			instance = new AltaSalaPanel();
		}
		return instance;
	}

	public void setParent(Panel_Main_Sala parent) {
		this.parent = parent;
	}

	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.SALA, ((Integer) o));
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			parent.showPanel(this);
			clear();
		}
		else {
			JOptionPane.showMessageDialog(parent, "La sala se ha dado de alta con exito con ID " + ((Integer)o).toString());
			clear();
		}
	}

	public boolean hasError(Object response) {
		return (Integer)response < 0;

	}

	public void clear() {
		nomF.setText("");
		aforoS.setValue(10);
		typeSala.setSelectedItem(null);
		aseosC.setSelected(false);
		_VOC.setSelected(false);
		_3DC.setSelected(false);
		estandar.setVisible(false);
		especial.setVisible(false);
	}

	@Override
	public boolean validation() {
		try {
			if (nomF.getText().equals(""))
				throw new Exception();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}