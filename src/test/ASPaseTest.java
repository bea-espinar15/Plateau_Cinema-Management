package test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import Negocio.ASFactory.ASFactory;
import Negocio.Cliente.ASCliente;
import Negocio.Cliente.TClienteNormal;
import Negocio.Compra.TCompra;
import Negocio.Compra.TLineaCompra;
import Negocio.Pase.ASPase;
import Negocio.Pase.TPase;
import Negocio.Pelicula.ASPelicula;
import Negocio.Pelicula.TPelicula;
import Negocio.Sala.ASSala;
import Negocio.Sala.TSalaEstandar;

public class ASPaseTest {
	private static ASPase as;
	private static long sysTime, time;
	private static Random random;

	private boolean equals(TPase a, TPase b) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		String aHorario = format.format(a.GetHorario()), bHorario = format.format(b.GetHorario());

		return a.GetIDPelicula().equals(b.GetIDPelicula()) && a.GetIDSala().equals(b.GetIDSala())
				&& a.GetAsientosLibres().equals(b.GetAsientosLibres())
				&& a.GetPrecioActual().equals(b.GetPrecioActual()) && aHorario.equals(bHorario);
	}

	private Integer crearSala() {
		ASSala asSala = ASFactory.getInstance().GetASSala();
		TSalaEstandar sala = new TSalaEstandar();
		sala.SetNombre("ASPase test sala" + random.nextInt());
		sala.SetAforo(50);
		sala.SetAseos(false);
		Integer idSala = asSala.Alta(sala);
		if (idSala < 0)
			fail("Error: Alta() sala es requisito para poder testear");
		return idSala;
	}

	private Integer crearPeli() {
		ASPelicula asPeli = ASFactory.getInstance().GetASPelicula();
		TPelicula peli = new TPelicula();
		peli.SetTitulo("ASPase test peli" + random.nextInt());
		peli.SetDirector("Director");
		peli.SetDuracion(1000);
		peli.SetGenero("Genero");
		Integer idPeli = asPeli.Alta(peli);
		if (idPeli < 0)
			fail("Error: Alta() pelicula es requisito para poder testear");
		return idPeli;
	}
	
	// function to generate a random string of length n
	private String getAlphaNumericString(int n) {

		// chose a Character random from this String
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

		// create StringBuffer size of AlphaNumericString
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {

			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int) (AlphaNumericString.length() * Math.random());

			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}

	private Integer crearCliente() {
		
		ASCliente asCliente = ASFactory.getInstance().GetASCliente();
		TClienteNormal cliente = new TClienteNormal();
		cliente.SetNombre(getAlphaNumericString(4));
		cliente.SetCorreo(getAlphaNumericString(4));
		cliente.SetDNI(getAlphaNumericString(8));
		Integer idCliente = asCliente.Alta(cliente);
		if (idCliente < 0)
			fail("Error: Alta() cliente es requisito para poder testear");
		cliente.SetID(idCliente);
		cliente.SetActivo(true);
		return idCliente;

	}
	
	private Integer crearPase() {
		Integer idPeli = crearPeli();
		Integer idSala = crearSala();
		TPase pase = new TPase(new Date(time), 1, idPeli, idSala);
		Integer idPase = as.Alta(pase);
		if (idPase < 0)
			fail("Error: Alta() pase es requisito para poder testear");
		return idPase;
	}

	private TCompra crearCompra() {
		Integer idCliente = crearCliente();
		TCompra compra = new TCompra(null, null, null, idCliente);
		Integer idPase = crearPase();
		TLineaCompra lc = new TLineaCompra(null, idPase, 3, null);
		HashMap<Integer, TLineaCompra> compraPases = new HashMap<>();
		compraPases.put(0, lc);
		Integer idCompra = ASFactory.getInstance().GetASCompra().Cerrar(compra, compraPases);
		if(idCompra < 0)
			fail("Error: Cerrar() compra es requisito para poder testear");
		compra.SetID(idCompra);
		return compra;
	}

	@BeforeClass
	public static void beforeClass() {
		as = ASFactory.getInstance().GetASPase();

		// Tiempo random para que se puede ejecutar varias veces seguidas
		random = new Random();
		sysTime = System.currentTimeMillis();
		time = sysTime + (long) ((random.nextDouble() + 1) * sysTime);
	}

	private TPase crearPaseSinDarAlta() {
		Integer idPeli = crearPeli();
		Integer idSala = crearSala();

		return new TPase(new Date(time), 1, idPeli, idSala);
	}

	@Test
	public void testAlta() {
		// Cumpliendo todos los requisitos
		TPase p1 = crearPaseSinDarAlta();
		Integer p1Id = as.Alta(p1);

		if (p1Id < 0) {
			fail("Error: Alta() de pase que cumple todos los requisitos");
		}

		// Test código de error 3 - sala no existe
		TPase error3 = new TPase(new Date(time), 1, p1.GetIDPelicula(), -1);

		if (as.Alta(error3) != -3) {
			fail("Error: Alta() de pase con sala inexistente debería devolver -3");
		}

		// Test código de error 9 - pase ya existe (mismo horario, misma sala) y esta activo
		TPase error9 = new TPase(p1.GetHorario(), 1, 5, p1.GetIDSala());

		if (as.Alta(error9) != -9) {
			fail("Error: Alta() de pase que ya existente debería devolver -9");
		}

		// Test código de error 12 - horario en conflicto
		TPase error12 = new TPase(new Date(time + 1000000), 1, p1.GetIDPelicula(), p1.GetIDSala());

		if (as.Alta(error12) != -12) {
			fail("Error: Alta() de pase con conflicto de horario debería devolver -12");
		}

		// Cambiamos time para generar una nueva fecha y evitar conflicto
		// horario
		time = sysTime + (long) ((random.nextDouble() + 1) * sysTime);

		// Test código de error 5 - pelicula no existe
		TPase error5 = new TPase(new Date(time), 1, -1, p1.GetIDSala());

		if (as.Alta(error5) != -5) {
			fail("Error: Alta() de pase con pelicula inexistente debería devolver -5");
		}

		// Test código de error 10 - pase en pasado
		TPase error10 = new TPase(new Date(1), 1, p1.GetIDPelicula(), p1.GetIDSala());

		if (as.Alta(error10) != -10) {
			fail("Error: Alta() de pase con horario en pasado debería devolver -10");
		}

		// Test código de error 11 - precio <= 0
		TPase error11 = new TPase(new Date(time), 0, p1.GetIDPelicula(), p1.GetIDSala());

		if (as.Alta(error11) != -11) {
			fail("Error: Alta() de pase con precio <= 0 debería devolver -11");
		}

		// Test código de error 4 - sala inactiva
		// Primero desactivamos sala
		ASFactory.getInstance().GetASSala().Baja(p1.GetIDSala());
		TPase error4 = new TPase(new Date(time), 1, p1.GetIDPelicula(), p1.GetIDSala());

		if (as.Alta(error4) != -4) {
			fail("Error: Alta() de pase con sala inactiva debería devolver -4");
		}

		// Test código de error 6 - pelicula inactiva
		// Primero desactivamos pelicula y creamos sala otra vez
		ASFactory.getInstance().GetASPelicula().Baja(p1.GetIDPelicula());
		Integer idSala = crearSala();
		TPase error6 = new TPase(new Date(time), 1, p1.GetIDPelicula(), idSala);

		if (as.Alta(error6) != -6) {
			fail("Error: Alta() de pase con pelicula inactiva debería devolver -6");
		}
		
		// Test de reactivación
		TPase oldpase = crearPaseSinDarAlta();
		Integer oldpaseid = as.Alta(oldpase);
		as.Baja(oldpaseid);
		if(as.Alta(oldpase) < 0)
			fail("Error: Alta() de pase inactivo con mismo horario y sala, debería reactivarse");
		if (as.Alta(oldpase) != -9) {
			fail("Error: Alta() de pase que ya existente debería devolver -9");
		}
	}

	@Test
	public void testBaja() {
		Integer idPeli = crearPeli();
		Integer idSala = crearSala();

		TPase p1 = new TPase(new Date(time), 1, idPeli, idSala);

		Integer p1Id = as.Alta(p1);

		// Baja - normal
		if (as.Baja(p1Id) != 0)
			fail("Error: Baja() sin error debería retornar 0");

		// Baja - pase inexistente
		if (as.Baja(-1) != -2)
			fail("Error: Baja() pase inexistente debería retornar -2");

		// Baja - pase no activo
		if (as.Baja(p1Id) != -8)
			fail("Error: Baja() pase no activo debería retornar -8");
	}

	@Test
	public void testModificar() {
		TPase p1 = crearPaseSinDarAlta();
		Integer p1Id = as.Alta(p1);
		if (p1Id < 0)
			fail("Error: Alta() es un requisito de Modificar()");

		// Test - modificar pase no existe
		TPase noExistente = new TPase(-1);
		if (as.Modificar(noExistente) != -2)
			fail("Error: Modificar() pase no existente deberia retornar -2");
		
		as.Baja(p1Id);
		//Test - modifcar - pase inactivo
		if(as.MostrarParaModificar(p1Id).GetID() != -8)
			fail("Error: Modificar() pase inactivo debería retornar -8");
		as.Alta(p1);

		// Test - modificar a pase con mismo <horario, idSala>
		TPase p2 = crearPaseSinDarAlta();
		Integer p2Id = as.Alta(p2);
		if (p2Id < 0)
			fail("Error: Alta() es un requisito de Modificar()");
		p2.SetID(p2Id);
		p2.SetIDSala(p1.GetIDSala());
		p2.SetHorario(p1.GetHorario());

		if (as.Modificar(p2) != -9)
			fail("Error: Modificar() pase a horario, idSala existentes deberia retornar -9");

		// Test - modificar pase sin errores (modificamos todo)
		p1.SetPrecioActual(99);
		p1.SetHorario(new Date(sysTime + (long) ((random.nextDouble() + 1) * sysTime)));
		p1.SetID(p1Id);
		p1.SetActivo(true);

		if (as.Modificar(p1) < 0)
			fail("Error: Modificar() pase sin errores deberia retornar el id del pase");
	}

	@Test
	public void testMostrar() {
		Integer idPeli = crearPeli();
		Integer idSala = crearSala();

		TPase p1 = new TPase(new Date(time), 1, idPeli, idSala);

		Integer p1Id = as.Alta(p1);

		// Mostrar - normal
		if (as.Mostrar(p1Id).GetID() < 0)
			fail("Error: Mostrar() sin error debería retornar un pase con id positivo");

		// Mostrar - pase inexistente
		if (as.Mostrar(-1).GetID() != -2)
			fail("Error: Mostrar() pase inexistente debería retornar -2");

	}
	
	@Test
	public void testMostrarParaModificar() {
		Integer idPeli = crearPeli();
		Integer idSala = crearSala();

		TPase p1 = new TPase(new Date(time), 1, idPeli, idSala);

		Integer p1Id = as.Alta(p1);

		// MostrarParaModificar - normal
		if (as.MostrarParaModificar(p1Id).GetID() < 0)
			fail("Error: MostrarParaModificar() sin error debería retornar un pase con id positivo");

		// MostrarParaModificar - pase inexistente
		if (as.MostrarParaModificar(-1).GetID() != -2)
			fail("Error: MostrarParaModificar() pase inexistente debería retornar -2");
		
		as.Baja(p1Id);
		//MostrarParaModifcar - pase inactivo
		if(as.MostrarParaModificar(p1Id).GetID() != -8)
			fail("Error: MostrarParaModificar() pase inactivo debería retornar -8");

	}

	@Test
	public void testListar() {
		TPase p1 = crearPaseSinDarAlta();
		Integer p1Id = as.Alta(p1);
		if (p1Id <= 0)
			fail("Error: Alta() se usa para testear Listar()");
		
		// Listar - normal
		// Buscar en lista
		boolean found = false;
		ArrayList<TPase> listaAll = as.Listar();
		for (TPase p : listaAll) {
			if (equals(p, p1))
				found = true;
		}

		if (!found)
			fail("Error: Listar() sin error debería contener el pase creado");

	}

	@Test
	public void testListarPorPelicula() {
		TPase p1 = crearPaseSinDarAlta();
		Integer p1Id = as.Alta(p1);
		if (p1Id <= 0)
			fail("Error: Alta() se usa para testear ListarPorPelicula()");

		// ListarPorPelicula - normal
		if (as.ListarPorPelicula(p1.GetIDPelicula()).get(0).GetID() < 0)
			fail("Error: ListarPorPelicula() sin error debería retornar un pase con id positivo en la primera posicion de la lista");

		// Test código de error 5 - pelicula no existe
		if (as.ListarPorPelicula(-1).get(0).GetID() != -5) {
			fail("Error: ListarPorPelicula() de pase con pelicula inexistente debería devolver -5");
		}
		
		//Test pase inactivo
		as.Baja(p1Id);
		if(as.ListarPorPelicula(p1.GetIDPelicula()).contains(p1))
			fail("Error: ListarPorPelicula() no debería tener un pase inactivo ");
		as.Alta(p1); //Reactivamos pase

		// Test código de error 6 - pelicula inactiva
		ASFactory.getInstance().GetASPelicula().Baja(p1.GetIDPelicula());

		if (as.ListarPorPelicula(p1.GetIDPelicula()).get(0).GetID() != -6) {
			fail("Error: ListarPorPelicula() de pase con pelicula inactiva debería devolver -6");
		}
	}

	@Test
	public void testListarPorSala() {
		TPase p1 = crearPaseSinDarAlta();
		Integer p1Id = as.Alta(p1);
		if (p1Id <= 0)
			fail("Error: Alta() se usa para testear ListarPorSala()");

		// ListarPorSala - normal
		if (as.ListarPorSala(p1.GetIDSala()).get(0).GetID() < 0)
			fail("Error: ListarPorSala() sin error debería retornar un pase con id positivo en la primera posicion de la lista");

		// Test código de error 5 - sala no existe

		if (as.ListarPorSala(-1).get(0).GetID() != -3) {
			fail("Error: ListarPorSala() de pase con sala inexistente debería devolver -3");
		}
		
		//Test pase inactivo
		as.Baja(p1Id);
		if(as.ListarPorSala(p1.GetIDSala()).contains(p1))
			fail("Error: ListarPorSala() no debería tener un pase inactivo ");
		as.Alta(p1); //Reactivamos pase

		// Test código de error 6 - sala inactiva
		ASFactory.getInstance().GetASSala().Baja(p1.GetIDSala());

		if (as.ListarPorSala(p1.GetIDSala()).get(0).GetID() != -4) {
			fail("Error: ListarPorSala() de pase con sala inactiva debería devolver -4");
		}

	}

	@Test
	public void testListarPorCompra() {
		TCompra compra = crearCompra();
		// ListarPorCompra - normal
		if (as.ListarPorCompra(compra.GetID()).get(0).GetID() < 0)
			fail("Error: ListarPorCompra() sin error debería retornar un pase con id positivo en la primera posicion de la lista");

		// Test código de error 7 - compra no existe

		if (as.ListarPorCompra(-1).get(0).GetID() != -7) {
			fail("Error: ListarPorCompra() de pase con compra inexistente debería devolver -7");
		}
	}
}
