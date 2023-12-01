/**
 * 
 */
package Presentacion.Gui.Panels.Cliente;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Presentacion.Gui.Panels.GeneralMainPanel;
import Presentacion.Gui.Panels.GeneralPanel;
public class Panel_Main_Cliente extends GeneralMainPanel {

	private static final long serialVersionUID = 1L;
	
	private JButton alta;
	private JButton baja;
	private JButton mostrar;
	private JButton modificar;
	private JButton listar;
	private JButton listarNormal;
	private JButton listarVIP;
	
	private JPanel contentPanel;
	private AltaClientePanel altaPanel;
	private BajaClientePanel bajaPanel;
	private MostrarClientePanel mostrarPanel;
	private ModificarClientePanel modificarPanel;
	private ListarClientesPanel listarPanel;
	private ListarClientesNormalPanel listarNormalPanel;
	private ListarClientesVIPPanel listarVIPPanel;
	
	public Panel_Main_Cliente(){
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

		
		listarNormal = new JButton("Listar Normales");
		listarNormal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(listarNormalPanel);
				listarNormalPanel.setVisible(true);
			}
			
		});
		buttonsPanel.add(listarNormal);
		
		listarVIP = new JButton("Listar VIP");
		listarVIP.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hidePanels();
				contentPanel.add(listarVIPPanel);
				listarVIPPanel.setVisible(true);
			}
			
		});
		buttonsPanel.add(listarVIP);

		
		this.add(buttonsPanel, BorderLayout.WEST);
		this.add(contentPanel, BorderLayout.CENTER);
	}

	public void initPanels() {
		contentPanel = new JPanel(new BorderLayout());
		altaPanel = AltaClientePanel.getInstance();
		altaPanel.setParent(this);
		bajaPanel = BajaClientePanel.getInstance();
		bajaPanel.setParent(this);
		mostrarPanel = MostrarClientePanel.getInstance();
		mostrarPanel.setParent(this);
		modificarPanel = ModificarClientePanel.getInstance();
		modificarPanel.setParent(this);
		listarPanel = ListarClientesPanel.getInstance();
		listarPanel.setParent(this);
		listarNormalPanel = ListarClientesNormalPanel.getInstance();
		listarNormalPanel.setParent(this);
		listarVIPPanel = ListarClientesVIPPanel.getInstance();
		listarVIPPanel.setParent(this);
	}

	public void hidePanels() {
		altaPanel.setVisible(false);
		altaPanel.clear();
		bajaPanel.setVisible(false);
		bajaPanel.clear();
		mostrarPanel.setVisible(false);
		mostrarPanel.clear();
		modificarPanel.clear();
		modificarPanel.setVisible(false);
		listarPanel.setVisible(false);
		listarPanel.clear();
		listarNormalPanel.setVisible(false);
		listarNormalPanel.clear();
		listarVIPPanel.setVisible(false);
		listarVIPPanel.clear();
	}

	public void showPanel(GeneralPanel panel) {
		hidePanels();
		contentPanel.add(panel);
		panel.setVisible(true);
	}
}