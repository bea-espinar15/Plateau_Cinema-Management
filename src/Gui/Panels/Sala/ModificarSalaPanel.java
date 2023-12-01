
package Presentacion.Gui.Panels.Sala;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import Negocio.Pelicula.TPelicula;
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

public class ModificarSalaPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;

	private static ModificarSalaPanel instance;
	private Panel_Main_Sala parent;
	private JButton modificar, ok;
	private JTextField idF, nomF;
	private JSpinner aforoS;
	private String type;
	private JPanel estandar, especial;
	private JCheckBox aseosC, _VOC, _3DC;
	private Boolean buscado;

	public ModificarSalaPanel() {
		type = "";
		buscado = false;
		initGUI();
	}

	public static ModificarSalaPanel getInstance() {
		if (instance == null)
			instance = new ModificarSalaPanel();
		return instance;
	}

	public void setParent(Panel_Main_Sala parent) {
		this.parent = parent;
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		// CABECERA
		JLabel modificarSala = new JLabel("MODIFICAR SALA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		modificarSala.setFont(f);
		modificarSala.setHorizontalAlignment(SwingConstants.CENTER);
		modificarSala.setForeground(Color.DARK_GRAY);
		modificarSala.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(modificarSala, BorderLayout.PAGE_START);

		// ______ DATOS (CENTRE) _______
		JPanel datos = new JPanel(new GridLayout(1, 2));

		// _______ DATOS COMUNES ________
		JPanel datosC = new JPanel(null);

		JPanel idPanel = new JPanel(new FlowLayout());
		idPanel.setBackground(Color.LIGHT_GRAY);
		idPanel.setBounds(100, 35, 170, 30);
		JLabel idLabel = new JLabel("ID:");
		this.idF = new JTextField(10);
		idPanel.add(idLabel);
		idPanel.add(idF);

		// ACEPTAR ID
		ok = new JButton("Buscar");
		ok.setBounds(280, 35, 80, 30);
		ok.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					Context request = new Context(ContextEnum.MOSTRARSALAPARAMODIFICAR, Integer.parseInt(idF.getText()));
					ApplicationController.GetInstance().ManageRequest(request);
				
				} else{
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// NOMBRE
		JPanel nombre = new JPanel(new FlowLayout());
		nombre.setBounds(30, 130, 400, 30);
		JLabel nom = new JLabel("Nombre:");
		nomF = new JTextField(10);
		nomF.setEditable(false);
		nombre.add(nom);
		nombre.add(nomF);

		// AFORO
		JPanel aforo = new JPanel(new FlowLayout());
		aforo.setBounds(0, 180, 400, 30);
		JLabel aforoLabel = new JLabel("Aforo:");
		SpinnerModel model = new SpinnerNumberModel(10, 10, 100, 10);
		aforoS = new JSpinner(model);
		aforoS.setPreferredSize(new Dimension(45, 25));
		aforo.add(aforoLabel);
		aforo.add(aforoS);

		modificar = new JButton("Modificar");
		modificar.setEnabled(false);
		modificar.setBounds(142, 260, 100, 30);
		modificar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					TSala sala;
					if (type == "ESTANDAR") {
						sala = new TSalaEstandar(Integer.parseInt(idF.getText()), nomF.getText(),
								(Integer) aforoS.getValue(), true, aseosC.isSelected());
					} else {
						sala = new TSalaEspecial(Integer.parseInt(idF.getText()), nomF.getText(),
								(Integer)aforoS.getValue(), true, _3DC.isSelected(), _VOC.isSelected());
					}
					Context request = new Context(ContextEnum.MODIFICARSALA, sala);
					ApplicationController.GetInstance().ManageRequest(request);
				} else{
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error",
							JOptionPane.ERROR_MESSAGE);
				}	
			}
		});
		datosC.add(idPanel);
		datosC.add(ok);
		datosC.add(nombre);
		datosC.add(aforo);
		datosC.add(modificar);

		datos.add(datosC);

		// DATOS ESPECIFICOS
		JPanel datosE = new JPanel(null);

		// ESTANDAR
		estandar = new JPanel();
		estandar.setBounds(7, 120, 200, 40);
		estandar.setVisible(false);

		// ASEOS
		JPanel aseos = new JPanel(new FlowLayout());
		aseosC = new JCheckBox("Aseos");
		aseosC.setEnabled(false);
		aseos.add(aseosC);

		estandar.add(aseos);
		datosE.add(estandar);

		// ESPECIAL
		especial = new JPanel();
		especial.setBounds(7, 120, 200, 40);
		especial.setVisible(false);

		// 3D
		JPanel _3D = new JPanel(new FlowLayout());
		_3DC = new JCheckBox("3D");
		_3DC.setEnabled(false);
		_3D.add(_3DC);

		// VO
		JPanel _VO = new JPanel(new FlowLayout());
		_VO.setBounds(10, 30, 100, 30);
		_VOC = new JCheckBox("VO");
		_VOC.setEnabled(false);
		_VO.add(_VOC);

		especial.add(_3D);
		especial.add(_VO);
		datosE.add(especial);

		datos.add(datosE);

		this.add(datos, BorderLayout.CENTER);
	}

	@Override
	public void update(Object o) {
		if (!buscado) {
			if (hasError(o)) {
				ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
				Message msg = ehm.getMessage(EntityEnum.SALA, ((TSala) o).GetId());
				JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
				clear();
			}
			else {

				nomF.setText(((TSala)o).GetNombre());
				aforoS.setValue(((TSala)o).GetAforo());
				if ((TSala)o instanceof TSalaEstandar) 
				{
					type = "ESTANDAR";
					aseosC.setSelected(((TSalaEstandar)o).GetAseos());
				}
				else {
					type = "ESPECIAL";
					_3DC.setSelected(((TSalaEspecial)o).Get3D());
					_VOC.setSelected(((TSalaEspecial)o).GetVO());
				}
				
				nomF.setEditable(true);
				aforoS.setEnabled(true);
				modificar.setEnabled(true);
				idF.setEditable(false);
				ok.setEnabled(false);
				if (type == "ESTANDAR") {
					estandar.setVisible(true);
					aseosC.setEnabled(true);
				} else if (type == "ESPECIAL") {
					especial.setVisible(true);
					_3DC.setEnabled(true);
					_VOC.setEnabled(true);
				}
				buscado = true;
				
			}
		
		}
		else {
			
			if (hasError(o)) {
				ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
				Message msg = ehm.getMessage(EntityEnum.SALA, (Integer)o);
				JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
				clear();
			}
			else {
				JOptionPane.showMessageDialog(parent, "Sala modificada correctamente");
				clear();
			}
		}
	}

	@Override
	public boolean hasError(Object response) {
		if (buscado && (Integer)response < 0) 
			return true;
		else if (!buscado && ((TSala)response).GetId() < 0)
			return true;
		return false;
	}

	@Override
	public boolean validation() {
		try {
			if (!buscado) {
				if (idF.getText().equals(""))
					throw new Exception();
				if (Integer.parseInt(idF.getText()) < 0)
					throw new Exception();
				return true;
			}
			else {
				if (nomF.getText().equals(""))
					throw new Exception();
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void clear() {
		idF.setText("");
		nomF.setText("");
		aforoS.setValue(10);
		aseosC.setSelected(false);
		_VOC.setSelected(false);
		_3DC.setSelected(false);

		nomF.setEditable(false);
		aforoS.setEnabled(false);
		aseosC.setEnabled(false);
		_VOC.setEnabled(false);
		_3DC.setEnabled(false);
		idF.setEditable(true);

		ok.setEnabled(true);
		modificar.setEnabled(false);

		estandar.setVisible(false);
		especial.setVisible(false);
		
		buscado = false;
	}

}
