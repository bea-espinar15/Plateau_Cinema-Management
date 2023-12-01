/**
 * 
 */
package Presentacion.Gui.Panels.Pelicula;

import javax.swing.table.AbstractTableModel;
import Negocio.Pelicula.TPelicula;
import java.util.ArrayList;
import java.util.List;

public class ListarPorProductoraTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = { "ID", "Titulo", "Director", "Genero", "Duracion" };
	private List<TPelicula> peliculas;

	public ListarPorProductoraTableModel(List<TPelicula> peliculas) {
		this.peliculas = peliculas;
	}

	public int getRowCount() {
		int fila = 0;
		if (this.peliculas != null) {
			fila = this.peliculas.size();
		}
		return fila;
	}

	public int getColumnCount() {
		return this.columnNames.length;
	}

	public String getColumnName(int i) {
		return this.columnNames[i];
	}

	public void updateList(ArrayList<TPelicula> list) {
		peliculas = list;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex){
		case 0:
			return peliculas.get(rowIndex).GetID().toString();
		case 1:
			return peliculas.get(rowIndex).GetTitulo().toString();
		case 2:
			return peliculas.get(rowIndex).GetDirector().toString();
		case 3:
			return peliculas.get(rowIndex).GetGenero().toString();
		case 4:
			return peliculas.get(rowIndex).GetDuracion().toString();
		}
		return null;
	}
}