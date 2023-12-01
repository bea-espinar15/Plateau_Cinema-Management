package test;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import Negocio.ASFactory.ASFactory;
import Negocio.Productora.ASProductora;
import Negocio.Productora.TProductora;

public class ASProductoraTest {

	private static ASProductora as;
	private static Random random;

	@BeforeClass
	public static void beforeClass() {
		as = ASFactory.getInstance().GetASProductora();
		random = new Random();
	}

	private boolean equals(TProductora a, TProductora b) {
		if (!a.GetCIF().equals(b.GetCIF()) || !a.GetNombre().equals(b.GetNombre())
				|| !a.GetTelefono().equals(b.GetTelefono()) || !a.GetDireccion().equals(b.GetDireccion()))
			return false;
		return true;
	}

	private TProductora crearProductora() {
		TProductora p = new TProductora("Test " + random.nextInt(), "cif " + random.nextInt(),
				random.nextInt(999999999 - 100000000) + 100000000, "dir test");
		Integer idP = as.Alta(p);
		if (idP <= 0) {
			fail("Error: Alta() la productora no existente, no deberia devolver error, ha devuelto :" + idP);
		}
		p.SetActivo(true);
		p.SetID(idP);
		return p;
	}

	@Test
	public void testAlta() {

		// Test - alta de una productora no existente en la BBDD, sin errores
		TProductora p1 = new TProductora("Test " + random.nextInt(), "cif " + random.nextInt(),
				random.nextInt(999999999 - 100000000) + 100000000, "dir test");
		Integer idP = as.Alta(p1);
		if (idP <= 0) {
			fail("Error: Alta() la productora no existente, no deberia devolver error, ha devuelto :" + idP);
		}

		// Test - alta productora: ver si se cumplen los errores de las
		// comprobaciones
		// teléfono de longitud incorrecta
		TProductora p2 = new TProductora("Test " + random.nextInt(), "cif " + random.nextInt(), 13, "dir test");
		Integer x = as.Alta(p2);
		if (x != -4) {
			fail("debe devolver -4, y ha devuelto :" + x);
		}

		// verificamos que no se puede dar de alta dos veces la misma productora

		Integer aux = as.Alta(p1);
		if (aux >= 0)
			fail("Ya habia una productora con mismo nombre y CIF en la BBDD");

		// verificamos que no se puede dar de alta una productora con el mismo
		// nombre que otra que ya exista en la BBDD
		p2.SetTelefono(457961358);
		p2.SetNombre(p1.GetNombre());
		x = as.Alta(p2);
		if (x != -2) {
			fail("debe devolver -2, y ha devuelto :" + x);
		}

		// verificamos que no se puede dar de alta una productora con el mismo
		// CIF que otra que ya exista en la BBDD

		p2.SetNombre("Test " + random.nextInt());
		p2.SetCIF(p1.GetCIF());
		x = as.Alta(p2);
		if (x != -3) {
			fail("debe devolver -3, y ha devuelto :" + x);
		}

	}

	@Test
	public void testBaja() {
		// Creamos una productora para desactivarla

		TProductora p = crearProductora();

		// probamos a dar de baja una productora que no existe
		Integer res = as.Baja(-1);
		if (res != -5) {
			fail("Error: Baja() productora no existente deberia retornar -5 y devuelve:" + res);
		}

		// baja sin errores
		res = as.Baja(p.GetID());
		if (res != 0) {
			fail("Error: Baja() productora sin errores, deberia retornar id > 0 y devuelve:" + res);
		}

		// intentar dar de baja a una productora que ya está inactiva
		res = as.Baja(p.GetID());
		if (res != -6) {
			fail("Error: Baja() productora no activa, deberia retornar -5 y devuelve:" + res);
		}

	}

	@Test
	public void testMostrar() {

		// Creamos productora para tener una para mostrar
		TProductora p = crearProductora();

		// intentamos mostrar una productora que no existe
		Integer res = as.Mostrar(-1).GetID();
		if (res != -5) {
			fail("Error: Mostrar() productora no existente, deberia retornar entidad con ID -5 y devuelve:" + res);
		}

		// prueba: mostar una productora sin errores
		if (!equals(p, as.Mostrar(p.GetID()))) {
			fail("Error: Mostrar() productora sin errores deberia retornar una entidad identica a la que habíamos creado");
		}
	}

	@Test
	public void testMostrarParaModificar() {
		// Solo tenemos que testear el error -6 (productora no activa)
		// Otros codigos de error se testean en testMostrar()

		// Creamos una productora para tener una para desactivar

		TProductora p = crearProductora();

		if (as.Baja(p.GetID()) != 0) {
			fail("Error: Baja() es requisito de MostrarParaModificar()");
		}

		// prueba de mostrarParaModificar en el caso de que la productora este
		// inactiva
		Integer res = as.MostrarParaModificar(p.GetID()).GetID();
		if (res != -6) {
			fail("Error: MostrarParaModificar() productora no activa deberia retornar -6 y devuelve:" + res);
		}

	}

	@Test
	public void testModificar() {

		// intentamos modificar una productora que no existe
		Integer res = as.Modificar(new TProductora(-1));
		if (res != -5) {
			fail("Error: Modificar() productora inexistente deberia retornar -5 y devuelve:" + res);
		}

		// creamos productora para tener una que modificar
		TProductora p = crearProductora();
		
		// Solo tenemos que testear el error -6 (productora no activa)
		// Otros codigos de error se testean en Alta()

		// Test - modificar sin errores
		p.SetNombre("Test " + random.nextInt());
		p.SetDireccion("Calle Azúcar 24");
		res = as.Modificar(p);
		if (res <= 0) {
			fail("Error: Modificar() sin errores deberia retornar ID > 0 y devuelve:" + res);
		}

		// Test - modificar productora inactiva
		// Desactivamos p
		if (as.Baja(p.GetID()) != 0) {
			fail("Error: Baja() es requisito de Modificar()");
		}
		res = as.Modificar(p);
		if (res != -6) {
			fail("Error: Modificar() cliente inactivo deberia retornar -6 y devuelve: " + res);
		}
	}

	@Test
	public void testListar() {
		// Creamos productoras para tener lista no vacia

		TProductora p1 = crearProductora();
		TProductora p2 = crearProductora();
		TProductora p3 = crearProductora();

		ArrayList<TProductora> listaAll = as.Listar();

		// Test - retorna lista no vacia
		if (listaAll.get(0).GetID() == -1) {
			fail("Error: listar() deberia retornar lista no vacia");
		}

		// Test - lista contiene entidades creadas en testAlta()
		boolean p1Found = false, p2Found = false, p3Found = false;
		for (TProductora p : listaAll) {
			if (equals(p, p1))
				p1Found = true;
			else if(equals(p, p2))
				p2Found = true;
			else if(equals(p, p3))
				p3Found = true;
		}
		if (!p1Found || !p2Found || !p3Found) {
			fail("Error: Listar() deberia contener todas las entidades creadas");
		}
	}
}
