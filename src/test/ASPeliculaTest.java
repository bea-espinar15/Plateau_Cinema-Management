package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import Negocio.ASFactory.ASFactory;
import Negocio.Pelicula.ASPelicula;
import Negocio.Pelicula.TPelicula;
import Negocio.Productora.ASProductora;
import Negocio.Productora.TProductora;

public class ASPeliculaTest {
	private static ASPelicula as;
	private static ASProductora asProd;
	private static Random casetest;

	@BeforeClass
	public static void beforeClass() {
		as = ASFactory.getInstance().GetASPelicula();
		asProd = ASFactory.getInstance().GetASProductora();
		casetest = new Random();
	}
	
	private boolean equals(TPelicula a, TPelicula b){
		if(!a.GetTitulo().equals(b.GetTitulo()) || 
				!a.GetDirector().equals(b.GetDirector()) || 
				!a.GetDuracion().equals(b.GetDuracion()) ||
				!a.GetGenero().equals(b.GetGenero()))
			return false;
		return true;
	}

	private TPelicula crearPeli() {
		TPelicula peli = new TPelicula();
		peli.SetTitulo("ASPase test pelicula" + casetest.nextInt());
		peli.SetDirector("test");
		peli.SetDuracion(100);
		peli.SetGenero("test");
		Integer idPeli = as.Alta(peli);
		if (idPeli < 0)
			fail("Error: Alta() pelicula es requisito para poder testear");
		peli.SetID(idPeli);
		peli.SetActivo(true);
		return peli;
	}
	
	private TProductora crearProductora(){
		TProductora productora = new TProductora("Nombre " + casetest.nextInt(), "CIF " + casetest.nextInt(), casetest.nextInt(999999999 - 100000000) + 100000000, "direccion");
		Integer idProd = asProd.Alta(productora);
		if(idProd <= 0)
			fail("Error: Alta() productora es requisito de este test");
		productora.SetID(idProd);
		productora.SetActivo(true);
		return productora;
	}

	@Test
	public void testAlta() {
		TPelicula peli = new TPelicula();
		peli.SetTitulo("Peli " + casetest.nextInt());
		peli.SetDirector("ASPelicula");
		peli.SetGenero("test");
		peli.SetDuracion(50);

		// Test codigo de error 1 - pelicula no creada
		Integer idPeli = as.Alta(peli);
		if (idPeli < 0)
			fail("Error: 1 - Alta() pelicula es requisito para poder testear");

		TPelicula p = new TPelicula("Peli " + casetest.nextInt(), "aux", "aux", 100);

		// Test - pelicula dada de alta correctamente
		if (as.Alta(p) <= 0) {
			fail("Error: Alta() de pelicula exitosa deberia devolver ID > 0");
		}

		// Test codigo de error 4 - pelicula repetida
		if (as.Alta(p) != -4) {
			fail("Error: 4 - Alta() de pelicula con pelicula existente deberia devolver -4");
		}
	}

	@Test
	public void testBaja() {
		TPelicula peli = crearPeli();

		// Test codigo de error 2 - pelicula repetida
		if (as.Baja(-1) != -2) {
			fail("Error: 2 - Baja() de pelicula con pelicula inexistente deberia devolver -2");
		}

		// Test codigo de error 0 - pelicula dada de baja correctamente
		if (as.Baja(peli.GetID()) != 0) {
			fail("Error: 0 - Baja() de pelicula exitosa debería devolver 0");
		}

		// Test codigo de error 2 - pelicula desactivada
		if (as.Baja(peli.GetID()) != -3) {
			fail("Error: 3 - Baja() de pelicula con pelicula desactivada deberia devolver -3");
		}
	}

	@Test
	public void testMostrar() {
		TPelicula peli = crearPeli();

		// Test codigo de error ID2 - pelicula no encontrada
		TPelicula error2 = new TPelicula();
		error2.SetID(-1);
		if (as.Mostrar(error2.GetID()).GetID() != -2) {
			fail("Error: ID2 - Mostrar() de pelicula con pelicula no encontrada deberia establecer su id en -2");
		}

		// Test codigo de error incorrecta - pelicula mostrada correctamente
		if (!equals(peli, as.Mostrar(peli.GetID()))) {
			fail("Error: incorrecta - Mostrar() de pelicula exitosa deberia devolver pelicula correcta");
		}

	}

	@Test
	public void testListar() {
		TPelicula peli = crearPeli();

		boolean found = false;

		for (TPelicula p : as.Listar()) {
			if (equals(p, peli))
				found = true;
		}

		if (!found)
			fail("Error: Listar() sin error deberia contener la pelicula creada");
	}

	@Test
	public void testMostrarParaModificar() {
		TPelicula peli = crearPeli();

		// Test codigo de error ID2 - pelicula no encontrada
		TPelicula error2 = new TPelicula();
		error2.SetID(-1);
		if (as.MostrarParaModificar(error2.GetID()).GetID() != -2) {
			fail("Error: ID2 - MostrarParaModificar() de pelicula con pelicula no encontrada deberia establecer su id en -2");
		}

		// Test codigo de error ID2 - pelicula no activa
		if (as.Baja(peli.GetID()) != 0)
			fail("Error: Baja() es requisito para MostrarParaModificar()");
		if (as.MostrarParaModificar(peli.GetID()).GetID() != -3) {
			fail("Error: ID2 - MostrarParaModificar() de pelicula con pelicula no activa deberia establecer su id en -3");
		}

		// Test codigo de error incorrecta - pelicula mostrada para modificar
		// correctamente
		TPelicula error0 = crearPeli();
		if (!equals(error0, as.MostrarParaModificar(error0.GetID()))) {
			fail("Error: incorrecta - MostrarParaModificar() de pelicula exitosa debería devolver pelicula correcta");
		}

	}

	@Test
	public void testModificar() {
		// Test codigo de error ID2 - pelicula no encontrada
		TPelicula error2 = crearPeli();
		error2.SetID(-1);
		if (as.Modificar(error2) != -2) {
			fail("Error: ID2 - Modificar() de pelicula con pelicula no encontrada deberia establecer su id en -2");
		}
		// Test codigo de error 3 - pelicula no activa
		TPelicula error3 = crearPeli();
		as.Baja(error3.GetID());
		if (as.Modificar(error3) != -3) {
			fail("Error: ID2 - Modificar() de pelicula con pelicula no activa deberia establecer su id en -3");
		}

	}

	@Test
	public void testListarPorProductora() {
		TPelicula peli = crearPeli();
		TProductora prod = crearProductora();
		
		// Test - productora no encontrada
		if (as.ListarPorProductora(-1).get(0).GetID() != -5) {
			fail("Error: ListarPorProductora() de pelicula con productora no encontrada deber�a devolver una pelicula con id -5");
		}
		
		// Test - listar sin errores
		// Anadimos productora para tener lista no vacia
		if(as.AnadirProductora(prod.GetID(), peli.GetID()) != 0)
			fail("Error: AnadirProductora() es requisito de este test");
		
		boolean found = false;
		ArrayList<TPelicula> listaProd = as.ListarPorProductora(prod.GetID());
		for(TPelicula p : listaProd){
			if(equals(p, peli))
				found = true;
		}
		if(!found)
			fail("Error: ListarPorProductora() deberia retornar lista con entidad creada");
		
		// Test codigo de error ID3 - productora no activa
		// Damos de baja productora
		if(asProd.Baja(prod.GetID()) != 0)
			fail("Error: Baja() productora es un requisito de este test");
		
		if (as.ListarPorProductora(prod.GetID()).get(0).GetID() != -6) {
			fail("Error: ID3 - ListarPorProductora() de pelicula con productora no activa deberia devolver una pelicula con id -6");
		}
	}

	@Test
	public void testAnadirProductora() {
		TPelicula peli = crearPeli();
		
		// Test codigo de error 2 - pelicula no existe
		TProductora prod = crearProductora();
		if (as.AnadirProductora(prod.GetID(), -1) != -2) {
			fail("Error: 2 - AnadirProductora() de pelicula con pelicula inexistente deberia devolver -2");
		}

		// Test codigo de error 3 - pelicula no activa
		TPelicula error3 = crearPeli();
		if(as.Baja(error3.GetID()) != 0)
			fail("Error: Baja() pelicula es requisito de este test");
		if (as.AnadirProductora(prod.GetID(), error3.GetID()) != -3) {
			fail("Error: 3 - AnadirProductora() de pelicula con pelicula no activa deberia devolver -3");
		}

		// Test codigo de error 5 - productora no existente
		if (as.AnadirProductora(-1, peli.GetID()) != -5) {
			fail("Error: 5 - AnadirProductora() de pelicula con productora inexistente no activa deberia devolver -5");
		}
		
		// Test codigo de error 0 - productora anadida correctamente
		if (as.AnadirProductora(prod.GetID(), peli.GetID()) != 0) {
			fail("Error: 0 - AnadirProductora() de pelicula con todo correcto deberia devolver 0");
		}
		// Test codigo de error 7 - productora ya anadida
		if (as.AnadirProductora(prod.GetID(), peli.GetID()) != -7) {
			fail("Error: 7 - AnadirProductora() de pelicula con productora ya anadida deberia devolver -7");
		}

		// Test codigo de error 6 - productora no activa
		if(asProd.Baja(prod.GetID()) != 0)
			fail("Error: Baja() productora es requisito para este test");
		
		if (as.AnadirProductora(prod.GetID(), peli.GetID()) != -6) {
			fail("Error: 6 - AnadirProductora() de pelicula con productora no activa deberia devolver -6");
		}
	}

	@Test
	public void testQuitarProductora() {

		// Test codigo de error 2 - pelicula no existe
		TProductora prod = crearProductora();
		if (as.QuitarProductora(prod.GetID(), -1) != -2) {
			fail("Error: 2 - QuitarProductora() de pelicula con pelicula inexistente deberia devolver -2");
		}

		// Test codigo de error 3 - pelicula no activa
		TPelicula peli = crearPeli();
		if(as.Baja(peli.GetID()) != 0)
			fail("Error: Baja() pelicula es un requisito para este test");
		if (as.QuitarProductora(prod.GetID(), peli.GetID()) != -3) {
			fail("Error: 3 - QuitarProductora() de pelicula con pelicula no activa deberia devolver -3");
		}

		// Test codigo de error 5 - productora no existente
		peli = crearPeli();
		if (as.QuitarProductora(-1, peli.GetID()) != -5) {
			fail("Error: 5 - QuitarProductora() de pelicula con productora inexistente no activa deberia devolver -5");
		}

		// Test codigo de error 6 - productora no activa
		if(asProd.Baja(prod.GetID()) != 0)
			fail("Error: Baja() productora es un requisito para este test");
		if (as.QuitarProductora(prod.GetID(), peli.GetID()) != -6) {
			fail("Error: 6 - QuitarProductora() de pelicula con productora no activa deberia devolver -6");
		}
		
		// Test codigo de error 8 - productora no anadida
		TProductora prod2 = crearProductora();
		if (as.QuitarProductora(prod2.GetID(), peli.GetID()) != -8) {
			fail("Error: 8 - QuitarProductora() de pelicula con productora no anadida deberia devolver -8");
		}
		
		// Test codigo de error 0 - productora quitada correctamente
		if (as.AnadirProductora(prod2.GetID(), peli.GetID()) != 0)
			fail("Error: AnadirProductora() de pelicula es requisito de este test");
		if (as.QuitarProductora(prod2.GetID(), peli.GetID()) != 0)
			fail("Error: 0 - QuitarProductora() de pelicula con todo correcto deberia devolver 0");
	}

}
