package Presentacion.Gui.Panels.Cliente;

import javax.swing.table.AbstractTableModel;
import Negocio.Cliente.TClienteVip;

import java.util.ArrayList;
import java.util.List;

public class ListarClientesVIPTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"ID","Activo","Nombre", "DNI","Correo", "Antiguedad", "Descuento"};
	private List<TClienteVip> clientes;
	
	public ListarClientesVIPTableModel(ArrayList<TClienteVip> clientes){
		this.clientes = clientes;
	}

	public int getRowCount() {
		return clientes.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];

	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex){
			case 0:
				return clientes.get(rowIndex).GetID();
			case 1:
				if (clientes.get(rowIndex).GetActivo()) return "SI";
				else return "NO";
			case 2:
				return clientes.get(rowIndex).GetNombre();
			case 3:
				return clientes.get(rowIndex).GetDNI();
			case 4:
				return clientes.get(rowIndex).GetCorreo();
			case 5:
				return clientes.get(rowIndex).GetAntiguedad();
			case 6:
				return clientes.get(rowIndex).GetDescuento();
		}

	return null;
	}
	
	public void updateList(ArrayList<TClienteVip> clientes) {
		this.clientes = clientes;
	}
}