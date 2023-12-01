/**
 * 
 */
package Presentacion.Gui.Panels.Pelicula;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Presentacion.Gui.Panels.GeneralMainPanel;
import Presentacion.Gui.Panels.GeneralPanel;

public class Panel_Main_Pelicula extends GeneralMainPanel {

	private static final long serialVersionUID = 1L;

	private JButton alta;
	private JButton baja;
	private JButton mostrar;
	private JButton modificar;
	private JButton listar;
	private JButton listarPorProductora;
	private JButton anadirProductora;
	private JButton quitarProductora;

	private JPanel contentPanel;
	private AltaPeliculaPanel altaPanel;
	private BajaPeliculaPanel bajaPanel;
	private MostrarPeliculaPanel mostrarPanel;
	private ModificarPeliculaPanel modificarPanel;
	private ListarPeliculaPanel listarPanel;
	private ListarPorProductoraPanel listarPorProductoraPanel;
	private AnadirProductoraPanel annadirProductoraPanel;
	private QuitarProductoraPanel quitarProductoraPanel;

	public Panel_Main_Pelicula() {
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		initPanels();

		// __________ CASOS DE USO BUTTONS (WEST)_____________

		JPanel buttonsPanel = new JPanel(new GridLayout(8, 1));

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

		listarPorProductora = new JButton("Listar por Productora");
		listarPorProductora.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(listarPorProductoraPanel);
				listarPorProductoraPanel.setVisible(true);
			}

		});
		buttonsPanel.add(listarPorProductora);

		anadirProductora = new JButton("Anyadir Productora");
		anadirProductora.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(annadirProductoraPanel);
				annadirProductoraPanel.setVisible(true);
			}

		});
		buttonsPanel.add(anadirProductora);

		quitarProductora = new JButton("Quitar Productora");
		quitarProductora.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(quitarProductoraPanel);
				quitarProductoraPanel.setVisible(true);
			}

		});
		buttonsPanel.add(quitarProductora);

		this.add(buttonsPanel, BorderLayout.WEST);
		this.add(contentPanel, BorderLayout.CENTER);
	}

	public void initPanels() {
		contentPanel = new JPanel(new BorderLayout());

		altaPanel = AltaPeliculaPanel.getInstance();
		altaPanel.setParent(this);

		bajaPanel = BajaPeliculaPanel.getInstance();
		bajaPanel.setParent(this);

		mostrarPanel = MostrarPeliculaPanel.getInstance();
		mostrarPanel.setParent(this);

		listarPanel = ListarPeliculaPanel.getInstance();
		listarPanel.setParent(this);

		modificarPanel = ModificarPeliculaPanel.getInstance();
		modificarPanel.setParent(this);

		listarPorProductoraPanel = ListarPorProductoraPanel.getInstance();
		listarPorProductoraPanel.setParent(this);

		annadirProductoraPanel = AnadirProductoraPanel.getInstance();
		annadirProductoraPanel.setParent(this);

		quitarProductoraPanel = QuitarProductoraPanel.getInstance();
		quitarProductoraPanel.setParent(this);
	}

	public void hidePanels() {
		altaPanel.setVisible(false);
		altaPanel.clear();
		bajaPanel.setVisible(false);
		bajaPanel.clear();
		mostrarPanel.setVisible(false);
		mostrarPanel.clear();
		modificarPanel.setVisible(false);
		modificarPanel.clear();
		listarPanel.setVisible(false);
		listarPanel.clear();
		listarPorProductoraPanel.setVisible(false);
		listarPorProductoraPanel.clear();
		annadirProductoraPanel.setVisible(false);
		annadirProductoraPanel.clear();
		quitarProductoraPanel.setVisible(false);
		quitarProductoraPanel.clear();
	}

	public void showPanel(GeneralPanel panel) {
		hidePanels();
		contentPanel.add(panel);
		panel.setVisible(true);
	}
}