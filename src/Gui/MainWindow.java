
package Presentacion.Gui;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Presentacion.Gui.Panels.Cliente.Panel_Main_Cliente;
import Presentacion.Gui.Panels.Compra.Panel_Main_Compra;
import Presentacion.Gui.Panels.Pase.Panel_Main_Pase;
import Presentacion.Gui.Panels.Pelicula.Panel_Main_Pelicula;
import Presentacion.Gui.Panels.Productora.Panel_Main_Productora;
import Presentacion.Gui.Panels.Sala.Panel_Main_Sala;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private static final Color boton1 = new Color(255,203,114);
	private static final Color boton2 = new Color(255,218,154);
	private static final Color pulsado = new Color(255,240,215);
	
	private JButton gClientes;
	private JButton gSalas;
	private JButton gPases;
	private JButton gProductoras;
	private JButton gPelicula;
	private JButton gCompra;
	
	private JPanel contentPanel, mainPanel;
	private Panel_Main_Cliente ClientesPanel;
	private Panel_Main_Pase PasesPanel;
	private Panel_Main_Sala SalasPanel;
	private Panel_Main_Productora ProductorasPanel;
	private Panel_Main_Pelicula PeliculasPanel;
	private Panel_Main_Compra ComprasPanel;

	private MainWindow(){
		super("Plateau - Gestion de Cine");
		initGUI();
		this.setLocationRelativeTo(null);
	}
	
	private void initGUI() {
		this.setMinimumSize(new Dimension(900,850));
		mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		mainPanel.setPreferredSize(new Dimension(800, 800));
		mainPanel.setMinimumSize(new Dimension(525, 800));
		mainPanel.setMaximumSize(new Dimension(525, 800));
		initContainer();
		
		//____________ CABECERA ____________
		JPanel header = new JPanel();
		header.setBackground(new Color(41,51,72));
		header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
		
		JPanel logoPanel = new JPanel(new BorderLayout());
		logoPanel.setBackground(new Color(41,51,72));
		JLabel logoLabel = new JLabel();
		Image aux = new ImageIcon("images/logo.png").getImage();
		ImageIcon logo = new ImageIcon(aux.getScaledInstance(290, 150, Image.SCALE_SMOOTH));
		logoLabel.setIcon(logo);
		logoLabel.setHorizontalAlignment(JLabel.CENTER);
		logoPanel.add(logoLabel, BorderLayout.CENTER);
		
		header.add(logoPanel);
		
		//- - - - - - Botones Panel - - - - - - -
		
		JPanel buttons = new JPanel();
		buttons.setBackground(Color.black);
		buttons.setLayout(new GridLayout(1,7));
		
		// G. CLIENTES
		gClientes = new JButton("Clientes");
		gClientes.setBackground(boton1);
		gClientes.setBounds(100,450,70,40);
		gClientes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				activateButtons();
				gClientes.setEnabled(false);
				resetColors();
				gClientes.setBackground(pulsado);
				
				hidePanels();				
				contentPanel.add(ClientesPanel, BorderLayout.CENTER);				
				ClientesPanel.setVisible(true);		
				ClientesPanel.hidePanels();
			}
															
		});
		buttons.add(gClientes);	
		
		// G. PASES
		gPases = new JButton("Pases");
		gPases.setBackground(boton2);
		gPases.setBounds(100,450,70,40);
		gPases.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				activateButtons();
				gPases.setBackground(new Color(255,240,215));
				gPases.setEnabled(false);
				resetColors();
				gPases.setBackground(pulsado);
				
				hidePanels();				
				contentPanel.add(PasesPanel, BorderLayout.CENTER);
				PasesPanel.setVisible(true);
				PasesPanel.hidePanels();
			}
															
		});
		buttons.add(gPases);
		
		// G. PELICULAS
		gPelicula = new JButton("Peliculas");
		gPelicula.setBackground(boton1);
		gPelicula.setBounds(100,450,70,40);
		gPelicula.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				activateButtons();
				gPelicula.setEnabled(false);
				resetColors();
				gPelicula.setBackground(pulsado);
				
				hidePanels();
				contentPanel.add(PeliculasPanel, BorderLayout.CENTER);
				PeliculasPanel.setVisible(true);	
				PeliculasPanel.hidePanels();
			}
															
		});
		buttons.add(gPelicula);	

		// G. PRODUCTORAS
		gProductoras = new JButton("Productoras");
		gProductoras.setBackground(boton2);
		gProductoras.setBounds(100,450,70,40);
		gProductoras.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				activateButtons();
				gProductoras.setEnabled(false);
				resetColors();
				gProductoras.setBackground(pulsado);
				
				hidePanels();
				contentPanel.add(ProductorasPanel, BorderLayout.CENTER);
				ProductorasPanel.setVisible(true);	
				ProductorasPanel.hidePanels();
			}
															
		});
		buttons.add(gProductoras);	

		// G. COMPRAS
		gCompra = new JButton("Compras");
		gCompra.setBackground(boton1);
		gCompra.setBounds(100,450,70,40);
		gCompra.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				activateButtons();
				gCompra.setEnabled(false);
				resetColors();
				gCompra.setBackground(pulsado);
				
				hidePanels();
				contentPanel.add(ComprasPanel, BorderLayout.CENTER);
				ComprasPanel.setVisible(true);	
				ComprasPanel.hidePanels();
			}
															
		});
		buttons.add(gCompra);	
		
		// G. SALAS
		gSalas = new JButton("Salas");
		gSalas.setBackground(boton2);
		gSalas.setBounds(100, 450,70,40);
		gSalas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				activateButtons();
				gSalas.setEnabled(false);
				resetColors();
				gSalas.setBackground(pulsado);
				
				hidePanels();
				contentPanel.add(SalasPanel, BorderLayout.CENTER);
				SalasPanel.setVisible(true);	
				SalasPanel.hidePanels();
			}
															
		});
		buttons.add(gSalas);
		
		header.add(buttons);
		mainPanel.add(header,BorderLayout.NORTH);
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	private void initContainer(){
		contentPanel = new JPanel(new BorderLayout());
		ClientesPanel = new Panel_Main_Cliente();
		PasesPanel = new Panel_Main_Pase();
		SalasPanel = new Panel_Main_Sala();
		ProductorasPanel = new Panel_Main_Productora();
		PeliculasPanel = new Panel_Main_Pelicula();
		ComprasPanel = new Panel_Main_Compra();

	}
	
	private void hidePanels(){	
		ClientesPanel.setVisible(false);
		PasesPanel.setVisible(false);
		SalasPanel.setVisible(false);
		ProductorasPanel.setVisible(false);
		PeliculasPanel.setVisible(false);
		ComprasPanel.setVisible(false);
	}
	
	private void activateButtons(){
		gClientes.setEnabled(true);
		gCompra.setEnabled(true);
		gPases.setEnabled(true);
		gPelicula.setEnabled(true);
		gProductoras.setEnabled(true);
		gSalas.setEnabled(true);
		
	}
	
	private void resetColors(){
		gClientes.setBackground(boton1);
		gPases.setBackground(boton2);
		gPelicula.setBackground(boton1);
		gProductoras.setBackground(boton2);
		gCompra.setBackground(boton1);
		gSalas.setBackground(boton2);
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new MainWindow();
			}
		});
	}
}