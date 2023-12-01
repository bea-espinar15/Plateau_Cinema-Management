
package Presentacion.Gui.Panels.Compra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Negocio.Compra.TCompra;
import Negocio.Compra.TLineaCompra;
import Presentacion.Gui.Panels.GeneralPanel;

public class AbrirCompraPanel extends GeneralPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static AbrirCompraPanel instance;	
	private CrearCompraPanel parent;
	private JTextField idClienteField;
	private JButton accept;


	public AbrirCompraPanel(){
		initGUI();
	}
	
	public static AbrirCompraPanel getInstance() {
		if(instance == null)
			instance = new AbrirCompraPanel();
		return instance;
	}


	private void initGUI() {
		
		this.setLayout(new BorderLayout());
		
		//_______ CABECERA (PAGE_START) ________
		JLabel abrirCompra = new JLabel("ABRIR COMPRA");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		abrirCompra.setFont(f);
		abrirCompra.setHorizontalAlignment(SwingConstants.CENTER);
		abrirCompra.setForeground(Color.DARK_GRAY);
		abrirCompra.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		this.add(abrirCompra, BorderLayout.PAGE_START);
		
		JPanel data = new JPanel();
		JPanel idPanel = new JPanel();
		idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.X_AXIS));
		idPanel.setBorder(BorderFactory.createEmptyBorder(35, 0, 0, 0));
		JLabel idCliente = new JLabel("ID Cliente:");
		this.idClienteField = new JTextField(10);
		idPanel.add(idCliente);
		idPanel.add(Box.createRigidArea(new Dimension(5,0)));
		idPanel.add(idClienteField);
		
		idPanel.add(Box.createRigidArea(new Dimension(15,0)));
		
		accept = new JButton("Aceptar");
		accept.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (validation()) {
					TCompra compra = new TCompra();
					compra.SetIDCliente(Integer.parseInt(idClienteField.getText()));				
					// HashMap<IdPase, TLineaCompra>
					HashMap<Integer, TLineaCompra> carrito = new HashMap<Integer, TLineaCompra>();
					parent.showCerrarPanel(compra, carrito);
				}
				else
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error", JOptionPane.ERROR_MESSAGE);
				clear();
			}
			
		});
		idPanel.add(accept);
		
		data.add(idPanel);
		this.add(data, BorderLayout.CENTER);
		
	}
	
	public void setParent(CrearCompraPanel parent) {
		this.parent = parent;
	}

	@Override
	public void update(Object o) {}

	@Override
	public boolean hasError(Object response) {
		return false;
	}

	@Override
	public boolean validation() {
		try {
			if (idClienteField.getText().equals(""))
				throw new Exception();
			if (Integer.parseInt(idClienteField.getText()) < 0)
				throw new Exception();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	@Override
	public void clear() {
		idClienteField.setText("");
	}

}