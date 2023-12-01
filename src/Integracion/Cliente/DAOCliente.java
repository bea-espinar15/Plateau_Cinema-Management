
package Integracion.Cliente;

import Negocio.Cliente.TClienteVip;

import java.util.ArrayList;

import Negocio.Cliente.TCliente;
import Negocio.Cliente.TClienteNormal;


public interface DAOCliente {
	
	public abstract Integer Create(TCliente cliente);
	public abstract TCliente Read(Integer id);
	public abstract Integer Update(TCliente cliente);
	public abstract Integer Delete(Integer id);
	public abstract ArrayList<TClienteVip> ReadAllVIP();
	public abstract ArrayList<TClienteNormal> readAllNormal();
	public abstract TCliente ReadByDNI(String dni);
	public abstract ArrayList<TCliente> ReadAll();
	
}