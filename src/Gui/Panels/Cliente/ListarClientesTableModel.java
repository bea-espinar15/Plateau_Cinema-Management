package Presentacion.Gui.Panels.Cliente;

import javax.swing.table.AbstractTableModel;
import Negocio.Cliente.TCliente;
import Negocio.Cliente.TClienteNormal;

import java.util.ArrayList;
import java.util.List;

public class ListarClientesTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"ID","Activo","Nombre", "DNI","Correo","Tipo"};
	private List<TCliente> clientes;
	
	public ListarClientesTableModel(ArrayList<TCliente> clientes){
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
				if (clientes.get(rowIndex) instanceof TClienteNormal) return "Normal";
				else return "VIP";
		}

	return null;
	}
	
	public void updateList(ArrayList<TCliente> clientes) {
		this.clientes = clientes;
	}
}