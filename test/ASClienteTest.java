package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import Negocio.ASFactory.ASFactory;
import Negocio.Cliente.ASCliente;
import Negocio.Cliente.TCliente;
import Negocio.Cliente.TClienteNormal;
import Negocio.Cliente.TClienteVip;

public class ASClienteTest {
	private static ASCliente as;
	private static Random random;

	@BeforeClass
	public static void beforeClass() {
		as = ASFactory.getInstance().GetASCliente();
		random = new Random();
	}

	private boolean equals(TCliente a, TCliente b) {
		boolean subClassEquals = false;
		if(a instanceof TClienteNormal && b instanceof TClienteNormal)
			subClassEquals = equals((TClienteNormal) a, (TClienteNormal) b);
		else if(a instanceof TClienteVip && b instanceof TClienteVip)
			subClassEquals = equals((TClienteVip) a, (TClienteVip) b);
		return subClassEquals && a.GetID().equals(b.GetID()) && a.GetDNI().equals(b.GetDNI()) && a.GetNombre().equals(b.GetNombre())
				&& a.GetCorreo().equals(b.GetCorreo()) && a.GetActivo().equals(b.GetActivo());
	}

	private boolean equals(TClienteNormal a, TClienteNormal b) {
		return a.GetFacturacion() == b.GetFacturacion();
	}
	
	private boolean equals(TClienteVip a, TClienteVip b) {
		return a.GetDescuento() == b.GetDescuento() && a.GetAntiguedad().equals(b.GetAntiguedad());
	}

	@Test
	public void testAlta() {
		TClienteNormal a = new TClienteNormal();
		a.SetNombre("Normal");
		a.SetCorreo("correo");
		a.SetDNI("dni " + random.nextInt());

		TClienteVip b = new TClienteVip();
		b.SetNombre("vip");
		b.SetCorreo("correo");
		b.SetDNI("dni " + random.nextInt());
		b.SetAntiguedad(6);

		// Test - alta cliente normal no existente sin errores
		Integer idNormal = as.Alta(a);
		if (idNormal <= 0)
			fail("Error: Alta() cliente normal no existente no deberia retornar error");

		// Test - alta cliente vip no existente sin errores
		Integer idVip = as.Alta(b);
		if (idVip <= 0)
			fail("Error: Alta() cliente VIP no existente no deberia retornar error");

		// Test - alta cliente existente activo
		if (as.Alta(a) != -3)
			fail("Error: Alta() cliente existente deberia retornar -3");
	}

	@Test
	public void testBaja() {
		// Creamos cliente para tener cliente para desactivar
		TClienteVip b = new TClienteVip();
		b.SetNombre("vip");
		b.SetCorreo("correo");
		b.SetDNI("dni " + random.nextInt());
		b.SetAntiguedad(6);
		Integer idVip = as.Alta(b);
		if (idVip <= 0)
			fail("Error: Alta() es requisito de Baja()");
		b.SetID(idVip);
		b.SetActivo(true);
		b.SetAntiguedad(6);
		b.SetDescuento(25);

		// Test - baja cliente no existente
		if (as.Baja(-1) != -2)
			fail("Error: Baja() cliente no existente deberia retornar -2");

		// Test - baja sin errores
		if (as.Baja(idVip) <= 0)
			fail("Error: Baja() cliente sin errores deberia retornar id > 0");

		// Test - baja cliente no activo
		if (as.Baja(idVip) != -5)
			fail("Error: Baja() cliente no activo deberia retornar -5");
		b.SetActivo(false);
	}

	@Test
	public void testMostrar() {
		// Creamos cliente para tener un cliente para mostrar
		TClienteNormal a = new TClienteNormal();
		a.SetNombre("Normal");
		a.SetCorreo("correo");
		a.SetDNI("dni " + random.nextInt());
		Integer idNormal = as.Alta(a);
		if (idNormal <= 0)
			fail("Error: Alta() es requisito de Mostrar()");
		a.SetID(idNormal);
		a.SetActivo(true);
		a.SetFacturacion(0);

		// Test - mostrar cliente no existente
		if (as.Mostrar(-1).GetID() != -2)
			fail("Error: Mostrar() cliente no existente deberia retornar entidad con ID -2");

		// Test - mostrar cliente sin errores
		if (!equals(a, as.Mostrar(idNormal)))
			fail("Error: Mostrar() cliente sin errores deberia retornar entidad identica");
	}

	@Test
	public void testMostrarParaModificar() {
		// Solo tenemos que testear el error -5 (cliente no activo)
		// Otros codigos de error se testean en testMostrar()

		// Creamos cliente para tener cliente para desactivar
		TClienteVip b = new TClienteVip();
		b.SetNombre("vip");
		b.SetCorreo("correo");
		b.SetDNI("dni " + random.nextInt());
		b.SetAntiguedad(6);
		Integer idVip = as.Alta(b);
		if (idVip <= 0)
			fail("Error: Alta() es requisito de MostrarParaModificar()");
		b.SetID(idVip);
		b.SetActivo(true);
		b.SetAntiguedad(6);
		b.SetDescuento(25);

		if (as.Baja(idVip) <= 0)
			fail("Error: Baja() es requisito de MostrarParaModificar()");

		// Test - mostrarParaModificar cliente inactivo
		if (as.MostrarParaModificar(idVip).GetID() != -5)
			fail("Error: MostrarParaModificar() cliente no activo deberia retornar -5");
	}

	@Test
	public void testModificar() {
		// Test - modificar cliente no válido
		if (as.Modificar(new TCliente(-1)) != -4)
			fail("Error: Modificar() cliente no válido deberia retornar -4");

		// Creamos cliente para modificar
		TClienteNormal a = new TClienteNormal();
		a.SetNombre("Normal");
		a.SetCorreo("correo");
		a.SetDNI("dni " + random.nextInt());
		Integer idNormal = as.Alta(a);
		if (idNormal <= 0)
			fail("Error: Alta() es requisito de Modificar()");
		a.SetID(idNormal);

		// Test - modificar sin errores
		a.SetNombre("Normal " + random.nextInt());
		a.SetCorreo("correo " + random.nextInt());
		if (as.Modificar(a) != 0)
			fail("Error: Modificar() sin errores deberia retornar 0");
	}

	@Test
	public void testListar() {
		// Creamos clientes para tener lista no vacia
		TClienteNormal a = new TClienteNormal();
		a.SetNombre("Normal");
		a.SetCorreo("correo");
		a.SetDNI("dni " + random.nextInt());

		TClienteVip b = new TClienteVip();
		b.SetNombre("vip");
		b.SetCorreo("correo");
		b.SetDNI("dni " + random.nextInt());
		b.SetAntiguedad(6);

		Integer idNormal = as.Alta(a);
		Integer idVip = as.Alta(b);
		if (idNormal <= 0 || idVip <= 0)
			fail("Error: Alta() es requisito de Listar()");
		a.SetID(idNormal);
		a.SetActivo(true);
		a.SetFacturacion(0);
		b.SetID(idVip);
		b.SetActivo(true);
		b.SetAntiguedad(6);
		b.SetDescuento(25);

		ArrayList<TCliente> listaAll = as.Listar();

		// Test - retorna lista no vacia
		if (listaAll.get(0).GetID() == -4)
			fail("Error: listar() deberia retornar lista no vacia");

		// Test - lista contiene entidades creadas en testAlta()
		boolean normalFound = false, vipFound = false;
		for (TCliente c : listaAll) {
			if (equals(c, a))
				normalFound = true;
			else if (equals(c, b))
				vipFound = true;
		}
		if (!normalFound || !vipFound)
			fail("Error: Listar() deberia contener todas las entidades creadas");
	}

	@Test
	public void testListarNormal() {
		// Creamos cliente para tener lista no vacia
		TClienteNormal a = new TClienteNormal();
		a.SetNombre("Normal");
		a.SetCorreo("correo");
		a.SetDNI("dni " + random.nextInt());
		Integer idNormal = as.Alta(a);
		if (idNormal <= 0)
			fail("Error: Alta() es requisito de ListarNormal()");
		a.SetID(idNormal);
		a.SetActivo(true);
		a.SetFacturacion(0);

		ArrayList<TClienteNormal> listaNormal = as.ListarNormal();

		// Test - retorna lista no vacia
		if (listaNormal.get(0).GetID() == -4)
			fail("Error: listar() deberia retornar lista no vacia");

		// Test - lista contiene entidades creadas en testAlta()
		boolean normalFound = false;
		for (TCliente c : listaNormal) {
			if (equals(c, a))
				normalFound = true;
		}
		if (!normalFound)
			fail("Error: Listar() deberia contener todas las entidades Normales creadas");
	}

	@Test
	public void testListarVIP() {
		// Creamos cliente para tener lista no vacia
		TClienteVip b = new TClienteVip();
		b.SetNombre("vip");
		b.SetCorreo("correo");
		b.SetDNI("dni " + random.nextInt());
		b.SetAntiguedad(6);
		Integer idVip = as.Alta(b);
		if (idVip <= 0)
			fail("Error: Alta() es requisito de ListarVIP()");
		b.SetID(idVip);
		b.SetActivo(true);
		b.SetAntiguedad(6);
		b.SetDescuento(25);

		ArrayList<TClienteVip> listaVip = as.ListarVIP();

		// Test - retorna lista no vacia
		if (listaVip.get(0).GetID() == -4)
			fail("Error: listar() deberia retornar lista no vacia");

		// Test - lista contiene entidades creadas en testAlta()
		boolean vipFound = false;
		for (TCliente c : listaVip) {
			if (equals(c, b))
				vipFound = true;
		}
		if (!vipFound)
			fail("Error: ListarVip() deberia contener todas las entidades VIP creadas");
	}

}
