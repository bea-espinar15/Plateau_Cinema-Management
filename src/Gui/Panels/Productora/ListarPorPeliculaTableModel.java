
package Presentacion.Gui.Panels.Productora;

import javax.swing.table.AbstractTableModel;
import Negocio.Productora.TProductora;
import java.util.ArrayList;


public class ListarPorPeliculaTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String [] columnNames = {"ID","Nombre","Telefono","Direccion", "CIF"};
	private ArrayList<TProductora> productoras;
	
	
	public ListarPorPeliculaTableModel(ArrayList<TProductora> productoras) {
		this.productoras = productoras;
	}
	
	public int getRowCount() {
		int fila=0;
		if (this.productoras != null) {
			fila = this.productoras.size();
		}
		return fila;
	}
	
	public int getColumnCount() {
		return this.columnNames.length;
	}
	public String getColumnName(int i) {
		return this.columnNames[i];
	}
	public void updateList(ArrayList<TProductora> list) {
		this.productoras = list;
	}
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object o = null;
		if(columnIndex == 0) {
			o= this.productoras.get(rowIndex).GetID().toString();
		}
		else if(columnIndex == 1) {
			o= this.productoras.get(rowIndex).GetNombre();
		}
		else if(columnIndex == 2) {
			o= this.productoras.get(rowIndex).GetTelefono().toString();
		}
		else if(columnIndex == 3) {
			o= this.productoras.get(rowIndex).GetDireccion();
		}
		else if(columnIndex == 4) {
			o= this.productoras.get(rowIndex).GetCIF();
		}
		return o;
	}
}