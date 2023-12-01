
package Presentacion.Gui.Panels.Sala;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Negocio.Sala.TSala;
import Negocio.Sala.TSalaEspecial;
import Negocio.Sala.TSalaEstandar;

import javax.swing.JOptionPane;

import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;

public class MostrarSalaPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;

	private String type;
	private static MostrarSalaPanel instance;
	private Panel_Main_Sala parent;
	private JTextField idF, actF, nomF, aforoF, aseosF, _3DF, _VOF, noTypeF, estandarF, especialF;
	private JPanel typeEmpty, estandar, especial, aseos, _3D, _VO;

	public MostrarSalaPanel() {
		type = "";
		initGUI();
	}

	public static MostrarSalaPanel getInstance() {
		if (instance == null)
			instance = new MostrarSalaPanel();
		return instance;
	}

	public void setParent(Panel_Main_Sala parent) {
		this.parent = parent;
	}

	private void initGUI() {
		setLayout(new BorderLayout());

		// CABECERA
		JLabel mostrarSala = new JLabel("MOSTRAR SALA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		mostrarSala.setFont(f);
		mostrarSala.setHorizontalAlignment(SwingConstants.CENTER);
		mostrarSala.setForeground(Color.DARK_GRAY);
		mostrarSala.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(mostrarSala, BorderLayout.PAGE_START);

		// CENTER

		JPanel centerPanel = new JPanel(null);

		JPanel idPanel = new JPanel(new FlowLayout());
		idPanel.setBackground(Color.LIGHT_GRAY);
		idPanel.setBounds(228, 35, 200, 30);
		JLabel id = new JLabel("ID:");
		this.idF = new JTextField(10);
		idPanel.add(id);
		idPanel.add(idF);

		centerPanel.add(idPanel);

		JButton mostrar = new JButton("Mostrar");
		mostrar.setBounds(438, 35, 100, 30);
		mostrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					Context request = new Context(ContextEnum.MOSTRARSALA, Integer.parseInt(idF.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
				} else {
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		centerPanel.add(mostrar);

		// ACTIVO
		JPanel activo = new JPanel(new FlowLayout());
		activo.setBounds(47, 110, 250, 30);
		JLabel act = new JLabel("Activo:");
		actF = new JTextField(10);
		actF.setEditable(false);
		activo.add(act);
		activo.add(actF);

		centerPanel.add(activo);

		// NOMBRE
		JPanel nombre = new JPanel(new FlowLayout());
		nombre.setBounds(42, 150, 250, 30);
		JLabel nom = new JLabel("Nombre:");
		this.nomF = new JTextField(10);
		nomF.setEditable(false);
		nombre.add(nom);
		nombre.add(nomF);

		centerPanel.add(nombre);

		// AFORO
		JPanel aforo = new JPanel(new FlowLayout());
		aforo.setBounds(49, 190, 250, 30);
		JLabel aforoLabel = new JLabel("Aforo:");
		aforoF = new JTextField(10);
		aforoF.setEditable(false);
		aforo.add(aforoLabel);
		aforo.add(aforoF);

		centerPanel.add(aforo);

		// SIN TIPO
		typeEmpty = new JPanel(new FlowLayout());
		typeEmpty.setBounds(350, 110, 250, 30);
		JLabel noType = new JLabel("Tipo:");
		noTypeF = new JTextField(10);
		noTypeF.setEditable(false);
		typeEmpty.add(noType);
		typeEmpty.add(noTypeF);

		centerPanel.add(typeEmpty);

		// TIPO ESTANDAR
		estandar = new JPanel(new FlowLayout());
		estandar.setBounds(350, 110, 250, 30);
		JLabel estandarL = new JLabel("Tipo:");
		estandarF = new JTextField("ESTANDAR", 10);
		estandarF.setEditable(false);
		estandar.add(estandarL);
		estandar.add(estandarF);

		centerPanel.add(estandar);

		// ASEOS
		aseos = new JPanel(new FlowLayout());
		aseos.setBounds(342, 150, 250, 30);
		JLabel aseosL = new JLabel("Aseos:");
		aseosF = new JTextField(10);
		aseosF.setEditable(false);
		aseos.add(aseosL);
		aseos.add(aseosF);

		centerPanel.add(aseos);

		// TIPO ESPECIAL
		especial = new JPanel(new FlowLayout());
		especial.setBounds(350, 110, 250, 30);
		JLabel especialL = new JLabel("Tipo:");
		especialF = new JTextField("ESPECIAL", 10);
		especialF.setEditable(false);
		especial.add(especialL);
		especial.add(especialF);

		centerPanel.add(especial);

		// 3D
		_3D = new JPanel(new FlowLayout());
		_3D.setBounds(354, 150, 250, 30);
		JLabel _3DL = new JLabel("3D:");
		_3DF = new JTextField(10);
		_3DF.setEditable(false);
		_3D.add(_3DL);
		_3D.add(_3DF);

		centerPanel.add(_3D);

		// VO
		_VO = new JPanel(new FlowLayout());
		_VO.setBounds(352, 190, 250, 30);
		JLabel _VOL = new JLabel("VO:");
		_VOF = new JTextField(10);
		_VOF.setEditable(false);
		_VO.add(_VOL);
		_VO.add(_VOF);

		centerPanel.add(_VO);

		updatePanel(type);

		this.add(centerPanel, BorderLayout.CENTER);

	}

	@Override
	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.SALA, ((TSala) o).GetId());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		} else {
			TSala s = (TSala)o;
			if (s.GetActivo())
				actF.setText("SI");
			else
				actF.setText("NO");
			nomF.setText(s.GetNombre());
			aforoF.setText(s.GetAforo().toString());
			if (s instanceof TSalaEstandar) {
				updatePanel("ESTANDAR");
				estandarF.setText("ESTANDAR");
				if (((TSalaEstandar)s).GetAseos())
					aseosF.setText("SI");
				else
					aseosF.setText("NO");
			}
			else {
				updatePanel("ESPECIAL");
				especialF.setText("ESPECIAL");
				if (((TSalaEspecial)s).Get3D())
					_3DF.setText("SI");
				else
					_3DF.setText("NO");
				if (((TSalaEspecial)s).GetVO())
					_VOF.setText("SI");
				else
					_VOF.setText("NO");
			}
		}
	}

	@Override
	public boolean hasError(Object response) {
		if (((TSala)response).GetId() < 0)
			return true;
		return false;
	}

	@Override
	public boolean validation() {
		try {
			 if (idF.getText().equals(""))
				 throw new Exception();
			 if (Integer.parseInt(idF.getText()) < 0)
				 throw new Exception();
			 return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void clear() {
		idF.setText("");
		actF.setText("");
		nomF.setText("");
		aforoF.setText("");
		aseosF.setText("");
		_3DF.setText("");
		_VOF.setText("");
		noTypeF.setText("");
		estandarF.setText("");
		especialF.setText("");
	}

	private void updatePanel(String type) {
		if (type == "") {
			estandar.setVisible(false);
			aseos.setVisible(false);
			especial.setVisible(false);
			_3D.setVisible(false);
			_VO.setVisible(false);
			typeEmpty.setVisible(true);
		} else if (type == "ESTANDAR") {
			typeEmpty.setVisible(false);
			especial.setVisible(false);
			_3D.setVisible(false);
			_VO.setVisible(false);
			estandar.setVisible(true);
			aseos.setVisible(true);
		} else {
			typeEmpty.setVisible(false);
			estandar.setVisible(false);
			aseos.setVisible(false);
			especial.setVisible(true);
			_3D.setVisible(true);
			_VO.setVisible(true);
		}
	}

}
