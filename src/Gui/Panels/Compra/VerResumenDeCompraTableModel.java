
package Presentacion.Gui.Panels.Compra;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import Negocio.Compra.TLineaCompra;


public class VerResumenDeCompraTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"IDCompra","IDPase","Entradas","Precio"};
	private ArrayList<TLineaCompra> lineasCompra;
	
	public VerResumenDeCompraTableModel (ArrayList<TLineaCompra> lineasCompra) {
		this.lineasCompra = lineasCompra;
	}
	
	@Override
	public int getRowCount() {
		return lineasCompra.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex){
		case 0:
			return lineasCompra.get(rowIndex).GetIDCompra();
		case 1:
			return lineasCompra.get(rowIndex).GetIDPase();
		case 2:
			return lineasCompra.get(rowIndex).GetNEntradas();
		case 3:
			return lineasCompra.get(rowIndex).GetPrecio();
	}
	return null;
	}

	public void updateList(ArrayList<TLineaCompra> lineasCompra) {
		this.lineasCompra = lineasCompra;
	}
	
}
