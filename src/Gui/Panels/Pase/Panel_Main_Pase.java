package Presentacion.Gui.Panels.Pase;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Presentacion.Gui.Panels.GeneralMainPanel;
import Presentacion.Gui.Panels.GeneralPanel;

/*
 * Autores: Nico y Axel
 */

public class Panel_Main_Pase extends GeneralMainPanel {

	private static final long serialVersionUID = 1L;
	
	private JButton alta;
	private JButton baja;
	private JButton modificar;
	private JButton mostrar;
	private JButton listar;
	private JButton listarPorSala;
	private JButton listarPorPelicula;
	private JButton listarPorCompra;
	
	private JPanel contentPanel;
	private AltaPasePanel altaPanel;
	private BajaPasePanel bajaPanel;
	private ModificarPasePanel modificarPanel;
	private MostrarPasePanel mostrarPanel;
	private ListarPasesPanel listarPanel;
	private ListarPorCompraPanel listarPorCompraPanel;
	private ListarPorPeliculaPanel listarPorPeliculaPanel;
	private ListarPorSalaPanel listarPorSalaPanel;

	
	public Panel_Main_Pase(){
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		initPanels();
		
		//__________ CASOS DE USO BUTTONS (WEST)_____________
		
		JPanel buttonsPanel = new JPanel(new GridLayout(8,1));
		
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

		
		listarPorSala = new JButton("Listar por Sala");
		listarPorSala.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(listarPorSalaPanel);
				listarPorSalaPanel.setVisible(true);
			}
			
		});
		buttonsPanel.add(listarPorSala);
		
		listarPorCompra = new JButton("Listar por Compra");
		listarPorCompra.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(listarPorCompraPanel);
				listarPorCompraPanel.setVisible(true);
			}
			
		});
		buttonsPanel.add(listarPorCompra);

		listarPorPelicula = new JButton("Listar por Pelicula");
		listarPorPelicula.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(listarPorPeliculaPanel);
				listarPorPeliculaPanel.setVisible(true);
			}
			
		});
		buttonsPanel.add(listarPorPelicula);
		
		this.add(buttonsPanel, BorderLayout.WEST);
		this.add(contentPanel, BorderLayout.CENTER);
	}

	public void initPanels() {
		contentPanel = new JPanel(new BorderLayout());
		altaPanel = AltaPasePanel.getInstance();
		altaPanel.setParent(this);
		bajaPanel = BajaPasePanel.getInstance();
		bajaPanel.setParent(this);
		mostrarPanel = MostrarPasePanel.getInstance();
		mostrarPanel.setParent(this);
		modificarPanel = ModificarPasePanel.getInstance();
		modificarPanel.setParent(this);
		listarPanel = ListarPasesPanel.getInstance();
		listarPanel.setParent(this);
		listarPorCompraPanel = ListarPorCompraPanel.getInstance();
		listarPorCompraPanel.setParent(this);
		listarPorPeliculaPanel = ListarPorPeliculaPanel.getInstance();
		listarPorPeliculaPanel.setParent(this);
		listarPorSalaPanel = ListarPorSalaPanel.getInstance();
		listarPorSalaPanel.setParent(this);
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
		listarPorCompraPanel.setVisible(false);
		listarPorCompraPanel.clear();
		listarPorPeliculaPanel.setVisible(false);
		listarPorPeliculaPanel.clear();
		listarPorSalaPanel.setVisible(false);
		listarPorSalaPanel.clear();
	}

	public void showPanel(GeneralPanel panel) {
		hidePanels();
		contentPanel.add(panel);
		panel.setVisible(true);
	}
}