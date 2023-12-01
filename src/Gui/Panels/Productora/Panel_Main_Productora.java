/**
 * 
 */
package Presentacion.Gui.Panels.Productora;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Presentacion.Gui.Panels.GeneralMainPanel;
import Presentacion.Gui.Panels.GeneralPanel;

public class Panel_Main_Productora extends GeneralMainPanel {
	

	private static final long serialVersionUID = 1L;
	
	private JButton alta;
	private JButton baja;
	private JButton modificar;
	private JButton mostrar;
	private JButton listar;
	private JButton listarPorPelicula;
	private JButton calcularProductoraMasIngresos;
	
	private JPanel contentPanel;
	private AltaProductoraPanel altaPanel;
	private BajaProductoraPanel bajaPanel;
	private ModificarProductoraPanel modificarPanel;
	private MostrarProductoraPanel mostrarPanel;
	private ListarProductorasPanel listarPanel;
	private ListarPorPeliculaPanel listarPorPeliculaPanel;
	private CalcularProductoraMasIngresosPanel calcularProductoraMasIngresosPanel;

	public Panel_Main_Productora(){
		initGUI();
	}
	
	private void initGUI() {

		setLayout(new BorderLayout());
		initPanels();
		
		//__________ CASOS DE USO BUTTONS (WEST)_____________
		
		JPanel buttonsPanel = new JPanel(new GridLayout(7,1));
		
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
		
		calcularProductoraMasIngresos = new JButton("Productora TOP ventas");
		calcularProductoraMasIngresos.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(calcularProductoraMasIngresosPanel);
				calcularProductoraMasIngresosPanel.setVisible(true);
			}
			
		});
		buttonsPanel.add(calcularProductoraMasIngresos);

		this.add(buttonsPanel, BorderLayout.WEST);
		this.add(contentPanel, BorderLayout.CENTER);
		
	}

	public void initPanels() {
		contentPanel = new JPanel(new BorderLayout());
		
		altaPanel = AltaProductoraPanel.getInstance();
		altaPanel.setParent(this);
		
		bajaPanel = BajaProductoraPanel.getInstance();
		bajaPanel.setParent(this);
		modificarPanel = ModificarProductoraPanel.getInstance();
		modificarPanel.setParent(this);
		listarPanel = ListarProductorasPanel.getInstance();
		listarPanel.setParent(this);
		
		mostrarPanel = MostrarProductoraPanel.getInstance();
		mostrarPanel.setParent(this);
		
	
		
		listarPorPeliculaPanel = ListarPorPeliculaPanel.getInstance();
		listarPorPeliculaPanel.setParent(this);
		calcularProductoraMasIngresosPanel = CalcularProductoraMasIngresosPanel.getInstance();
		calcularProductoraMasIngresosPanel.setParent(this);
	
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
		listarPorPeliculaPanel.setVisible(false);
		listarPorPeliculaPanel.clear();
		calcularProductoraMasIngresosPanel.setVisible(false);
		calcularProductoraMasIngresosPanel.clear();
	}

	public void showPanel(GeneralPanel panel) {
		hidePanels();
		contentPanel.add(panel);
		panel.setVisible(true);
	}
}