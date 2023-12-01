
package Presentacion.Gui.Panels.Sala;

import javax.swing.table.AbstractTableModel;

import Negocio.Sala.TSalaEspecial;

import java.util.ArrayList;

public class ListarSalasEspecialTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnNames = { "ID", "Activo", "Nombre", "Aforo", "3D", "VO" };
	private ArrayList<TSalaEspecial> salas;

	public ListarSalasEspecialTableModel(ArrayList<TSalaEspecial> salas) {
		this.salas = salas;
	}

	public int getRowCount() {
		return salas.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

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
			if (salas.get(rowIndex).Get3D()) return "SI";
			else return "NO";
		case 5:
			if (salas.get(rowIndex).GetVO()) return "SI";
			else return "NO";
		}
		return null;
	}

	public void updateList(ArrayList<TSalaEspecial> salas) {
		this.salas = salas;
	}

}