package Presentacion.Gui.Panels.Pase;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import Negocio.Pase.TPase;
import java.util.ArrayList;
import java.util.List;

/*
 * Autores: Nico y Axel
 */

public class ListarPorCompraTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] columnNames = { "ID", "Activo", "Horario", "Precio actual", "Pelicula", "Sala", "Asientos libres" };
	private List<TPase> pases;

	public ListarPorCompraTableModel(List<TPase> pases) {
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
		SwingUtilities.invokeLater(() -> {
			pases = list;
			fireTableDataChanged();
		});

	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex){
		case 0:
			return pases.get(rowIndex).GetID().toString();
		case 1:
			if (pases.get(rowIndex).GetActivo()) return "SI";
			else return "NO";
		case 2:
			return pases.get(rowIndex).GetHorario().toString();
		case 3:
			return pases.get(rowIndex).GetPrecioActual().toString();
		case 4:
			return pases.get(rowIndex).GetIDPelicula().toString();
		case 5:
			return pases.get(rowIndex).GetIDSala().toString();
		case 6:
			return pases.get(rowIndex).GetAsientosLibres().toString();
		}
		return null;
	}
}