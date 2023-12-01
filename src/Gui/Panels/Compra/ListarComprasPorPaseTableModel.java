
package Presentacion.Gui.Panels.Compra;

import javax.swing.table.AbstractTableModel;
import Negocio.Compra.TCompra;

import java.text.SimpleDateFormat;
import java.util.List;


public class ListarComprasPorPaseTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] columnNames = {"ID","IDCliente","Precio Total","Fecha"};
	private List<TCompra> compras;
	
	public ListarComprasPorPaseTableModel(List<TCompra> a){
		this.compras = a;
	}
	
	public String getColumnName(int i) {
		return this.columnNames[i];
	}
	public void updateList(List<TCompra> list) {
		this.compras = list;
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
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object o = null;
		if(columnIndex == 0){
			o = compras.get(rowIndex).GetID().toString();
		}
		else if(columnIndex == 1){
			o = compras.get(rowIndex).GetIDCliente().toString();
		}
		else if(columnIndex == 2){
			o = compras.get(rowIndex).GetPrecioTotal().toString();
		}
		else if(columnIndex == 3){
			o = new SimpleDateFormat("dd-MM-yyyy").format(compras.get(rowIndex).GetFecha());
		}
	
	return o;
	}

}