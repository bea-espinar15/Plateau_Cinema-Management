
package Presentacion.Gui.Panels.Compra;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Presentacion.Gui.Panels.GeneralMainPanel;
import Presentacion.Gui.Panels.GeneralPanel;

public class Panel_Main_Compra extends GeneralMainPanel {
	

	private static final long serialVersionUID = 1L;
	
	private JButton crear;
	private JButton mostrar;
	private JButton mostrarEnDetalle;
	private JButton listar;
	private JButton listarPorCliente;
	private JButton listarPorPase;
	private JButton calcularTotalPorPelicula;
	private JButton devolver;
	private JButton verResumen;
	
	private JPanel contentPanel;
	private CrearCompraPanel crearPanel;
	private MostrarCompraPanel mostrarPanel;
	private MostrarCompraEnDetallePanel mostrarEnDetallePanel;
	private ListarComprasPanel listarPanel;
	private ListarComprasPorClientePanel listarPorClientePanel;
	private ListarComprasPorPasePanel listarPorPasePanel;
	private CalcularTotalComprasPorPeliculaPanel calcularTotalPorPeliculaPanel;
	private DevolverCompraPanel devolverPanel;
	private VerResumenDeCompraPanel verResumenPanel;

	public Panel_Main_Compra(){
		initGUI();
	}
	
	private void initGUI() {

		setLayout(new BorderLayout());
		initPanels();
		
		//__________ CASOS DE USO BUTTONS (WEST)_____________
		
		JPanel buttonsPanel = new JPanel(new GridLayout(9,1));
		
		crear = new JButton("Crear");
		crear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(crearPanel);
				crearPanel.setVisible(true);
				
			}
			
		});
		buttonsPanel.add(crear);
		
		
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
		
		
		mostrarEnDetalle = new JButton("Mostrar en detalle");
		mostrarEnDetalle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(mostrarEnDetallePanel);
				mostrarEnDetallePanel.setVisible(true);
			}
			
		});
		buttonsPanel.add(mostrarEnDetalle);	
		
		
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

		
		listarPorCliente = new JButton("Listar por Cliente");
		listarPorCliente.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(listarPorClientePanel);
				listarPorClientePanel.setVisible(true);
			}
			
		});
		buttonsPanel.add(listarPorCliente);
		
		
		listarPorPase = new JButton("Listar por Pase");
		listarPorPase.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(listarPorPasePanel);
				listarPorPasePanel.setVisible(true);
			}
			
		});
		buttonsPanel.add(listarPorPase);
		
		
		calcularTotalPorPelicula = new JButton("Compras por Pelicula");
		calcularTotalPorPelicula.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(calcularTotalPorPeliculaPanel);
				calcularTotalPorPeliculaPanel.setVisible(true);
			}
			
		});
		buttonsPanel.add(calcularTotalPorPelicula);
		
		
		devolver = new JButton("Devolver");
		devolver.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(devolverPanel);
				devolverPanel.setVisible(true);
			}
			
		});
		buttonsPanel.add(devolver);
		
		
		verResumen = new JButton("Ver Resumen");
		verResumen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(verResumenPanel);
				verResumenPanel.setVisible(true);
			}
			
		});
		buttonsPanel.add(verResumen);
		

		this.add(buttonsPanel, BorderLayout.WEST);
		this.add(contentPanel, BorderLayout.CENTER);
		
	}

	public void initPanels() {
		contentPanel = new JPanel(new BorderLayout());
		
		crearPanel = CrearCompraPanel.getInstance();
		crearPanel.setParent(this);
		listarPanel = ListarComprasPanel.getInstance();
		listarPanel.setParent(this);
		mostrarPanel = MostrarCompraPanel.getInstance();
		mostrarPanel.setParent(this);
		devolverPanel = DevolverCompraPanel.getInstance();
		devolverPanel.setParent(this);
		calcularTotalPorPeliculaPanel = CalcularTotalComprasPorPeliculaPanel.getInstance();
		calcularTotalPorPeliculaPanel.setParent(this);
		listarPorPasePanel = ListarComprasPorPasePanel.getInstance();
		listarPorPasePanel.setParent(this);	
		listarPorClientePanel = ListarComprasPorClientePanel.getInstance();
		listarPorClientePanel.setParent(this);	
		mostrarEnDetallePanel = MostrarCompraEnDetallePanel.getInstance();
		mostrarEnDetallePanel.setParent(this);
		verResumenPanel = VerResumenDeCompraPanel.getInstance();
		verResumenPanel.setParent(this);
		
	}

	public void hidePanels() {
		crearPanel.setVisible(false);
		crearPanel.clear();
		mostrarPanel.setVisible(false);
		mostrarPanel.clear();
		listarPanel.setVisible(false);
		listarPanel.clear();
		devolverPanel.setVisible(false);
		devolverPanel.clear();
		calcularTotalPorPeliculaPanel.setVisible(false);
		calcularTotalPorPeliculaPanel.clear();
		listarPorPasePanel.setVisible(false);
		listarPorPasePanel.clear();
		listarPorClientePanel.setVisible(false);
		listarPorClientePanel.clear();
		mostrarEnDetallePanel.setVisible(false);	
		mostrarEnDetallePanel.clear();
		verResumenPanel.setVisible(false);
		verResumenPanel.clear();
	}

	public void showPanel(GeneralPanel panel) {
		hidePanels();
		contentPanel.add(panel);
		panel.setVisible(true);
	}
}