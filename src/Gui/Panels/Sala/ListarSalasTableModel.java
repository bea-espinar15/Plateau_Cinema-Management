
package Presentacion.Gui.Panels.Sala;

import javax.swing.table.AbstractTableModel;
import Negocio.Sala.TSala;
import Negocio.Sala.TSalaEstandar;

import java.util.ArrayList;

public class ListarSalasTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnNames = { "ID", "Activo", "Nombre", "Aforo", "Tipo" };
	private ArrayList<TSala> salas;

	public ListarSalasTableModel(ArrayList<TSala> salas) {
		this.salas = salas;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public int getRowCount() {
		return salas.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return salas.get(rowIndex).GetId();
		case 1:
			if (salas.get(rowIndex).GetActivo()) return "SI";
			else return "NO";
		case 2:
			return salas.get(rowIndex).GetNombre();
		case 3:
			return salas.get(rowIndex).GetAforo();
		case 4:
			if (salas.get(rowIndex) instanceof TSalaEstandar) return "Estandar";
			else return "Especial";
		}

		return null;
	}

	public void updateList(ArrayList<TSala> salas) {
		this.salas = salas;
	}
}