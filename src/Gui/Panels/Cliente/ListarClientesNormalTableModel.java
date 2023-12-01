package Presentacion.Gui.Panels.Cliente;

import javax.swing.table.AbstractTableModel;
import Negocio.Cliente.TClienteNormal;

import java.util.ArrayList;
import java.util.List;

public class ListarClientesNormalTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"ID","Activo","Nombre", "DNI","Correo","Facturacion"};
	private List<TClienteNormal> clientes;
	
	public ListarClientesNormalTableModel(ArrayList<TClienteNormal> clientes){
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
				return clientes.get(rowIndex).GetFacturacion();
		}

	return null;
	}
	
	public void updateList(ArrayList<TClienteNormal> clientes) {
		this.clientes = clientes;
	}
}