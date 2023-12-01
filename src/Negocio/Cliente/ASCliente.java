package Negocio.Cliente;

import java.util.ArrayList;


public interface ASCliente {
	
	public Integer Alta(TCliente cliente);
	public Integer Baja(Integer id);
	public Integer Modificar(TCliente cliente);
	public TCliente Mostrar(Integer id);
	public TCliente MostrarParaModificar(Integer id);
	public ArrayList<TCliente> Listar();
	public ArrayList<TClienteNormal> ListarNormal();
	public ArrayList<TClienteVip> ListarVIP();
	
}