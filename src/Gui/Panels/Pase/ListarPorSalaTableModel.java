package Presentacion.Gui.Panels.Pase;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import Negocio.Pase.TPase;
import java.util.ArrayList;
import java.util.List;

/*
 * Autores: Nico y Axel
 */

public class ListarPorSalaTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] columnNames = { "ID", "Horario", "Precio actual", "Pelicula", "Asientos libres" };
	private List<TPase> pases;

	public ListarPorSalaTableModel(List<TPase> pases) {
		this.pases = pases;
	}

	public int getRowCount() {
		int fila = 0;
		if (this.pases != null) {
			fila = this.pases.size();
		}

		return fila;
	}

	public int getColumnCount() {
		return this.columnNames.length;
	}

	public String getColumnName(int i) {
		return this.columnNames[i];
	}

	public void updateList(ArrayList<TPase> list) {
		pases = list;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex){
		case 0:
			return pases.get(rowIndex).GetID().toString();
		case 1:
			return pases.get(rowIndex).GetHorario().toString();
		case 2:
			return pases.get(rowIndex).GetPrecioActual().toString();
		case 3:
			return pases.get(rowIndex).GetIDPelicula().toString();
		case 4:
			return pases.get(rowIndex).GetAsientosLibres().toString();
		}
		return null;
	}
}