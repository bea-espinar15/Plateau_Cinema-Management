
package Presentacion.Gui.Panels.Productora;

import javax.swing.table.AbstractTableModel;
import Negocio.Productora.TProductora;
import utilities.Pair;

import java.util.ArrayList;


public class CalcularProductoraMasIngresosTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private String [] columnNames = {"ID","Nombre","Telefono","Direccion", "CIF","Ingresos"};
	private Pair<ArrayList<TProductora>,Integer> productoras;
	public CalcularProductoraMasIngresosTableModel(Pair<ArrayList<TProductora>, Integer> aux) {
		this.productoras = aux;
	}
	@Override
	public int getRowCount() {
		return this.productoras.getFirst().size();
	}
	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(columnIndex == 0) {
			return this.productoras.getFirst().get(rowIndex).GetID().toString();
		}
		else if(columnIndex == 1) {
			return this.productoras.getFirst().get(rowIndex).GetNombre();
		}
		else if(columnIndex == 2) {
			return this.productoras.getFirst().get(rowIndex).GetTelefono().toString();
		}
		else if(columnIndex == 3) {
			return this.productoras.getFirst().get(rowIndex).GetDireccion();
		}
		else if(columnIndex == 4) {
			return this.productoras.getFirst().get(rowIndex).GetCIF();
		}
		else if(columnIndex == 5){
			return this.productoras.getSecond().toString();
		}
		else return null;
	}
	@Override
	public String getColumnName(int i) {
		return this.columnNames[i];
	}

	public void updateList(ArrayList<TProductora> list, Integer i ) {
		this.productoras.setObj1(list);
		this.productoras.setObj2(i);
		
	}

}