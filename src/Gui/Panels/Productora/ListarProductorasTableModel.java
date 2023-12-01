
package Presentacion.Gui.Panels.Productora;

import javax.swing.table.AbstractTableModel;
import Negocio.Productora.TProductora;
import java.util.ArrayList;


public class ListarProductorasTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String [] columnNames = {"ID","Activo","Nombre","Telefono","Direccion", "CIF"};
	private ArrayList<TProductora> productoras;
	
	public ListarProductorasTableModel(ArrayList<TProductora> aux) {
		this.productoras = aux;
	}
	
	public int getRowCount() {
		int fila=0;
		if (this.productoras != null) {
			fila = this.productoras.size();
		}
		return fila;
	}
	public void updateList(ArrayList<TProductora> k){
		this.productoras = k;
	}
	public int getColumnCount() {
		return this.columnNames.length;
	}
	
	public String getColumnName(int i) {
		return this.columnNames[i];
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex){
		case 0:
			return productoras.get(rowIndex).GetID().toString();
		case 1:
			if (productoras.get(rowIndex).GetActivo()) return "SI";
			else return "NO";
		case 2:
			return productoras.get(rowIndex).GetNombre().toString();
		case 3:
			return productoras.get(rowIndex).GetTelefono().toString();
		case 4:
			return productoras.get(rowIndex).GetDireccion().toString();
		case 5:
			return productoras.get(rowIndex).GetCIF().toString();
		}
		return null;
	}
	
}