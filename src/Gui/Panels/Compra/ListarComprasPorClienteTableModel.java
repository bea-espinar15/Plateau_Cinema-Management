
package Presentacion.Gui.Panels.Compra;

import javax.swing.table.AbstractTableModel;
import Negocio.Compra.TCompra;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListarComprasPorClienteTableModel extends AbstractTableModel {
	

	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"ID","Precio Total","Fecha"};
	private ArrayList<TCompra> compras;


	public ListarComprasPorClienteTableModel(ArrayList<TCompra> compras) {
		this.compras = compras;
	}


	@Override
	public int getRowCount() {
		return compras.size();
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
			return compras.get(rowIndex).GetID();
		case 1:
			return compras.get(rowIndex).GetPrecioTotal();
		case 2:
			return new SimpleDateFormat("dd-MM-yyyy").format(compras.get(rowIndex).GetFecha());
		}
		return null;
	}


	public void updateList(ArrayList<TCompra> compras){
		this.compras = compras;
	}
	
}