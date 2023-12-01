/**
 * 
 */
package Presentacion.Gui.Panels.Sala;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Presentacion.Gui.Panels.GeneralMainPanel;
import Presentacion.Gui.Panels.GeneralPanel;

public class Panel_Main_Sala extends GeneralMainPanel {

	private static final long serialVersionUID = 1L;

	private JButton alta;
	private JButton baja;
	private JButton mostrar;
	private JButton modificar;
	private JButton listar;
	private JButton listarEstandar;
	private JButton listarEspecial;

	private JPanel contentPanel;
	private AltaSalaPanel altaPanel;
	private BajaSalaPanel bajaPanel;
	private MostrarSalaPanel mostrarPanel;
	private ModificarSalaPanel modificarPanel;
	private ListarSalasPanel listarPanel;
	private ListarSalasEstandarPanel listarEstandarPanel;
	private ListarSalasEspecialPanel listarEspecialPanel;

	public Panel_Main_Sala() {
		initGUI();
	}

	private void initGUI() {

		setLayout(new BorderLayout());
		initPanels();

		// __________ CASOS DE USO BUTTONS (WEST)_____________

		JPanel buttonsPanel = new JPanel(new GridLayout(7, 1));

		alta = new JButton("Alta");
		alta.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(altaPanel);
				altaPanel.setVisible(true);

			}

		});
		buttonsPanel.add(alta);

		baja = new JButton("Baja");
		baja.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(bajaPanel);
				bajaPanel.setVisible(true);
			}

		});
		buttonsPanel.add(baja);

		modificar = new JButton("Modificar");
		modificar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(modificarPanel);
				modificarPanel.setVisible(true);
			}

		});
		buttonsPanel.add(modificar);

		mostrar = new JButton("Mostrar");
		mostrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(mostrarPanel);
				mostrarPanel.setVisible(true);
			}

		});
		buttonsPanel.add(mostrar);

		listar = new JButton("Listar");
		listar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(listarPanel);
				listarPanel.setVisible(true);
			}

		});
		buttonsPanel.add(listar);

		listarEstandar = new JButton("Listar Estandar");
		listarEstandar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(listarEstandarPanel);
				listarEstandarPanel.setVisible(true);
			}

		});
		buttonsPanel.add(listarEstandar);

		listarEspecial = new JButton("Listar Especiales");
		listarEspecial.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(listarEspecialPanel);
				listarEspecialPanel.setVisible(true);
			}

		});
		buttonsPanel.add(listarEspecial);

		this.add(buttonsPanel, BorderLayout.WEST);
		this.add(contentPanel, BorderLayout.CENTER);

	}

	public void initPanels() {
		contentPanel = new JPanel(new BorderLayout());
		bajaPanel = BajaSalaPanel.getInstance();
		bajaPanel.setParent(this);
		mostrarPanel = MostrarSalaPanel.getInstance();
		mostrarPanel.setParent(this);
		listarPanel = ListarSalasPanel.getInstance();
		listarPanel.setParent(this);
		altaPanel = AltaSalaPanel.getInstance();
		altaPanel.setParent(this);
		modificarPanel = ModificarSalaPanel.getInstance();
		modificarPanel.setParent(this);
		listarEstandarPanel = ListarSalasEstandarPanel.getInstance();
		listarEstandarPanel.setParent(this);
		listarEspecialPanel = ListarSalasEspecialPanel.getInstance();
		listarEspecialPanel.setParent(this);
	}

	public void hidePanels() {
		bajaPanel.setVisible(false);
		bajaPanel.clear();
		mostrarPanel.setVisible(false);
		mostrarPanel.clear();
		listarPanel.setVisible(false);
		listarPanel.clear();
		altaPanel.setVisible(false);
		altaPanel.clear();
		modificarPanel.setVisible(false);
		modificarPanel.clear();
		listarEstandarPanel.setVisible(false);
		listarEstandarPanel.clear();
		listarEspecialPanel.setVisible(false);
		listarEspecialPanel.clear();
	}

	public void showPanel(GeneralPanel panel) {
		hidePanels();
		contentPanel.add(panel);
		panel.setVisible(true);
	}

}