
package Presentacion.Gui.Panels.Productora;

import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;
import utilities.Pair;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Negocio.Productora.TProductora;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;


public class CalcularProductoraMasIngresosPanel extends GeneralPanel {
	
	private static final long serialVersionUID = 1L;
	private static CalcularProductoraMasIngresosPanel instance;
	private Panel_Main_Productora parent;
	private JTable productorasTable;
	private Pair<ArrayList<TProductora>,Integer> productorasIngreso;
	private CalcularProductoraMasIngresosTableModel calcularProductorasMasIngresosTableModel;
	private JSpinner fechaIni, fechaFin;
	private JLabel fechaIniLabel, fechaFinLabel;

	public CalcularProductoraMasIngresosPanel() {
		this.productorasIngreso = new Pair<ArrayList<TProductora>, Integer>(new ArrayList<TProductora>(), 0);
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());

		// _______ CABECERA (PAGE_START) ________
		JLabel listarPorPelicula = new JLabel("PRODUCTORA TOP VENTAS");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		listarPorPelicula.setFont(f);
		listarPorPelicula.setHorizontalAlignment(SwingConstants.CENTER);
		listarPorPelicula.setForeground(Color.DARK_GRAY);
		listarPorPelicula.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		this.add(listarPorPelicula, BorderLayout.PAGE_START);

		// _______ CAMPOS ENTRADA _______
		JPanel listarBtnPanel = new JPanel(new FlowLayout());
		listarBtnPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		JButton listar = new JButton("Calcular");
		listar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(validation()){
					Date d1 = (Date)fechaIni.getValue();
					Date d2 = (Date)fechaFin.getValue();
					Pair<Date,Date> aux = new Pair<Date,Date>(d1,d2);
					
					Context request = new Context(ContextEnum.CALCULARPRODUCTORAMASINGRESOS, aux);
					ApplicationController.GetInstance().ManageRequest(request);
					
					calcularProductorasMasIngresosTableModel.updateList(productorasIngreso.getFirst(),productorasIngreso.getSecond());

				}
				else{
					JOptionPane.showMessageDialog(parent, "Los datos introducidos son sintacticamente erroneos", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}

		});
		listarBtnPanel.add(listar, BorderLayout.EAST);
		fechaFinLabel = new JLabel("Fecha Fin:");
		fechaIniLabel = new JLabel("Fecha Inicio:");
		SimpleDateFormat model = new SimpleDateFormat("yyyy-MM-dd");
		fechaIni = new JSpinner(new SpinnerDateModel(new Date(0), new Date(0), new Date(), Calendar.DAY_OF_MONTH));
		fechaIni.setEditor(new JSpinner.DateEditor(fechaIni, model.toPattern()));
		fechaIni.setPreferredSize(new Dimension(85, 20));
		fechaFin = new JSpinner(new SpinnerDateModel());
		fechaFin = new JSpinner(new SpinnerDateModel(new Date(), new Date(0), new Date(), Calendar.DAY_OF_MONTH));
		fechaFin.setEditor(new JSpinner.DateEditor(fechaFin, model.toPattern()));
		fechaFin.setPreferredSize(new Dimension(85, 20));
		listarBtnPanel.add(fechaIniLabel, BorderLayout.WEST);
		listarBtnPanel.add(fechaIni, BorderLayout.WEST);
		listarBtnPanel.add(fechaFinLabel, BorderLayout.WEST);
		listarBtnPanel.add(fechaFin, BorderLayout.WEST);
		this.add(listarBtnPanel, BorderLayout.CENTER);

		// ______ DATOS (CENTER) _______
		this.add(crearTabla(this.productorasIngreso),BorderLayout.AFTER_LAST_LINE);
	}

	public static CalcularProductoraMasIngresosPanel getInstance() {
		if (instance == null)
			instance = new CalcularProductoraMasIngresosPanel();
		return instance;
	}

	public void setParent(Panel_Main_Productora parent) {
		this.parent = parent;
	}

	public Component crearTabla(Pair<ArrayList<TProductora>, Integer> aux) {
		calcularProductorasMasIngresosTableModel = new CalcularProductoraMasIngresosTableModel(aux);
		productorasTable = new JTable(calcularProductorasMasIngresosTableModel);
		JScrollPane scrollpane = new JScrollPane(productorasTable);
		scrollpane.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
		return scrollpane;
	}
	
	@Override
	public void update(Object o) {
		if (hasError(o)) {
			Message msg = ErrorHandlerManager.getInstance().getMessage(EntityEnum.PRODUCTORA, ((Pair<ArrayList<TProductora>,Integer>) o).getSecond());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			parent.showPanel(this);
			clear();
		} else {
			parent.showPanel(this);
			this.productorasIngreso = (Pair<ArrayList<TProductora>, Integer>) o;
			this.calcularProductorasMasIngresosTableModel.updateList(productorasIngreso.getFirst(),productorasIngreso.getSecond());			
		}

	}
	@Override
	public boolean hasError(Object response) {
		return ((Pair<ArrayList<TProductora>, Integer>) response).getSecond() < 0;
	}
	@Override
	public void clear() {
		fechaIni.setValue(new Date(0));
		fechaFin.setValue(new Date());
		calcularProductorasMasIngresosTableModel.updateList(new ArrayList<TProductora>(),0);
	}

	@Override
	public boolean validation() {
		return true;
	}

}