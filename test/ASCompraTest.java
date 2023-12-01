package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import Negocio.ASFactory.ASFactory;
import Negocio.Cliente.ASCliente;
import Negocio.Cliente.TClienteNormal;
import Negocio.Compra.ASCompra;
import Negocio.Compra.TCompra;
import Negocio.Compra.TLineaCompra;
import Negocio.Pase.ASPase;
import Negocio.Pase.TPase;
import Negocio.Pelicula.ASPelicula;
import Negocio.Pelicula.TPelicula;
import Negocio.Sala.ASSala;
import Negocio.Sala.TSalaEstandar;
import utilities.Pair;

public class ASCompraTest {

	private static Random random;
	private static long sysTime, time;
	private static ASCompra asCompra;
	private static ASPelicula asPelicula;
	private static ASPase asPase;
	private static ASCliente asCliente;

	@BeforeClass
	public static void beforeClass() {
		// Tiempo random para que se puede ejecutar varias veces seguidas
		random = new Random();
		sysTime = System.currentTimeMillis();
		time = sysTime + (long) ((random.nextDouble() + 1) * sysTime);
		asCompra = ASFactory.getInstance().GetASCompra();
		asPelicula = ASFactory.getInstance().GetASPelicula();
		asPase = ASFactory.getInstance().GetASPase();
		asCliente = ASFactory.getInstance().GetASCliente();
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
	
	private Integer crearSala() {
		ASSala asSala = ASFactory.getInstance().GetASSala();
		TSalaEstandar sala = new TSalaEstandar();
		sala.SetNombre(getAlphaNumericString(4));
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
		peli.SetTitulo(getAlphaNumericString(4));
		peli.SetDirector(getAlphaNumericString(4));
		peli.SetDuracion(1000);
		peli.SetGenero(getAlphaNumericString(4));
		Integer idPeli = asPeli.Alta(peli);
		if (idPeli < 0)
			fail("Error: Alta() pelicula es requisito para poder testear");
		return idPeli;
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
		ASPase asPase = ASFactory.getInstance().GetASPase();
		TPase pase = new TPase(new Date(time), 1, idPeli, idSala);
		Integer idPase = asPase.Alta(pase);
		if (idPase < 0)
			fail("Error: Alta() pase es requisito para poder testear");
		return idPase;
	}

	private Pair<Integer, Integer> crearCompra() {
		Integer idCliente = crearCliente();
		TCompra compra = new TCompra(null, null, null, idCliente);
		Integer idPase = crearPase();
		TLineaCompra lc = new TLineaCompra(null, idPase, 3, null);
		HashMap<Integer, TLineaCompra> compraPases = new HashMap<>();
		compraPases.put(0, lc);
		Integer idCompra = asCompra.Cerrar(compra, compraPases);
		if(idCompra < 0)
			fail("Error: Cerrar() compra es requisito para poder testear");
		Pair<Integer, Integer> CyP = new Pair<>();
		CyP.setObj1(idCompra);
		CyP.setObj2(idPase);
		return CyP;
	}
	
	private TCompra crearCompraYDevolverTransfer() {
		Integer idCliente = crearCliente();
		TCompra compra = new TCompra(null, null, null, idCliente);
		Integer idPase = crearPase();
		TLineaCompra lc = new TLineaCompra(null, idPase, 3, null);
		HashMap<Integer, TLineaCompra> compraPases = new HashMap<>();
		compraPases.put(0, lc);
		Integer idCompra = asCompra.Cerrar(compra, compraPases);
		if(idCompra < 0)
			fail("Error: Cerrar() compra es requisito para poder testear");
		compra.SetID(idCompra);
		return compra;
	}

    private boolean equals(TCompra a, TCompra b) {
        return a.GetID().equals(b.GetID()) && a.GetIDCliente().equals(b.GetIDCliente()) && a.GetPrecioTotal().equals(b.GetPrecioTotal());
    }
	
	@Test
	public void testCerrar() {
		TCompra compra = new TCompra(null, null, null, -1);
		Integer idPase = crearPase();
		TLineaCompra lc = new TLineaCompra(null, idPase, 3, null);
		HashMap<Integer, TLineaCompra> compraPases = new HashMap<>();
		compraPases.put(0, lc);

		// Test codigo de error 6 - compra con cliente no existente
		if (asCompra.Cerrar(compra, compraPases) != -6) {
			fail("Error: 6 - Cerrar() de compra con cliente inexistente deberia devolver -6");
		}
		
		Integer idCliente1 = crearCliente();
		TCompra compra1 = new TCompra(null, null, null, idCliente1);
		Integer idPase1 = crearPase();
		TLineaCompra lc1 = new TLineaCompra(null, idPase1, 3, null);
		HashMap<Integer, TLineaCompra> compraPases1 = new HashMap<>();
		compraPases1.put(0, lc1);
		asCliente.Baja(idCliente1);
		
		// Test codigo de error 7 - compra con cliente no activo
		if (asCompra.Cerrar(compra1, compraPases1) != -7) {
			fail("Error: 7 - Cerrar() de compra con cliente no activo deberia devolver -7");
		}

		Integer idCliente3 = crearCliente();
		TCompra compra3 = new TCompra(null, null, null, idCliente3);
		HashMap<Integer, TLineaCompra> compraPases3 = new HashMap<>();

		// Test codigo de error 8 - compra vacia
		if (asCompra.Cerrar(compra3, compraPases3) != -8) {
			fail("Error: 8 - Cerrar() de comprar con compra vacia deberia devolver -8");
		}
		
		Integer idCliente4 = crearCliente();
		TCompra compra4 = new TCompra(null, null, null, idCliente4);
		TLineaCompra lc4 = new TLineaCompra(null, -1, 3, null);
		HashMap<Integer, TLineaCompra> compraPases4 = new HashMap<>();
		compraPases4.put(0, lc4);

		// Test codigo de error 2 - compra con pase inexistente
		if (asCompra.Cerrar(compra4, compraPases4) != -2) {
			fail("Error: 2 - Cerrar() de comprar con pase no existente deberia devolver -2");
		}

		Integer idCliente5 = crearCliente();
		TCompra compra5 = new TCompra(null, null, null, idCliente5);
		Integer idPase5 = crearPase();
		TLineaCompra lc5 = new TLineaCompra(null, idPase5, 3, null);
		HashMap<Integer, TLineaCompra> compraPases5 = new HashMap<>();
		compraPases5.put(0, lc5);
		asPase.Baja(idPase5);

		// Test codigo de error 3 - compra con pase no activo
		if (asCompra.Cerrar(compra5, compraPases5) != -3) {
			fail("Error: 3 - Cerrar() de comprar con pase no activo deberia devolver -3");
		}

//		Integer idCliente6 = crearCliente();
//		TCompra compra6 = new TCompra(null, null, null, idCliente6);
//		Integer idPase6 = crearPase();
//		TPase pase = asPase.Mostrar(idPase6);
//		pase.SetHorario(new Date(sysTime + (long) ((random.nextDouble() + 1) * sysTime)));
//		asPase.Modificar(pase);
//		TLineaCompra lc6 = new TLineaCompra(null, idPase6, 3, null);
//		HashMap<Integer, TLineaCompra> compraPases6 = new HashMap<>();
//		compraPases6.put(0, lc6);
//		
//		// Test codigo de error 9 - compra con pase caducado
//		if (asCompra.Cerrar(compra6, compraPases6) != -9) {
//			fail("Error: 9 - Cerrar() de comprar con pase caducado deberia devolver -9");
//		}

		Integer idCliente7 = crearCliente();
		TCompra compra7 = new TCompra(null, null, null, idCliente7);
		Integer idPase7 = crearPase();
		TLineaCompra lc7 = new TLineaCompra(null, idPase7, 10000, null);
		HashMap<Integer, TLineaCompra> compraPases7 = new HashMap<>();
		compraPases7.put(0, lc7);

		// Test codigo de error 10 - compra con pase con asientos insuficientes
		if (asCompra.Cerrar(compra7, compraPases7) != -10) {
			fail("Error: 10 - Cerrar() de comprar con pase con asientos insuficientes deberia devolver -10");
		}
	}

	@Test
	public void testMostrar() {
		Pair<Integer, Integer> CyP = crearCompra();
		// Test codigo de error 4 - compra inexistente
		if (asCompra.Mostrar(-1).GetID() != -4) {
			fail("Error: 4 - Mostrar() con compra inexistente deberia establecer su ID en -4");
		}
		// Test mostrar correcto
		if(asCompra.Mostrar(CyP.getFirst()).GetID() < 0){
			fail("Error: Mostrar() correcto debería devolver compra con id positivo");
		}
	}

	@Test
	public void testListar() {
		Pair<Integer, Integer> CyP = crearCompra();
		TCompra compra = asCompra.Mostrar(CyP.getFirst());
		// Listar - normal
		// Buscar en lista
		boolean found = false;
		ArrayList<TCompra> listaAll = asCompra.Listar();
		for (TCompra c  : listaAll) {
			if (equals(c, compra))
				found = true;
		}

		if (!found)
			fail("Error: Listar() sin error debería contener la compra creada");
	}

	@Test
	public void testMostrarEnDetalle() {
		Pair<Integer, Integer> CyP = crearCompra();
		TCompra compra = asCompra.Mostrar(CyP.getFirst());

		// Test codigo de error 4 - compra inexistente
		if (asCompra.MostrarEnDetalle(-1).GetCompra().GetID() != -4) {
			fail("Error: 4 - MostrarEnDetalle() con compra inexistente deberia establecer su ID en -4");
		}

		// Mostrarendetalle correcto
		if (asCompra.MostrarEnDetalle(compra.GetID()).GetCompra().GetID() < 0) {
			fail("Error: MostrarEnDetalle() correcto debería devolver una compra con id positivo");
		}

	}

	@Test
	public void testListarPorCliente() {
		
		TCompra compra = crearCompraYDevolverTransfer();

		// Test codigo de error 6 - cliente inexistente
		if (asCompra.ListarPorCliente(-1).get(0).GetID() != -6) {
			fail("Error: 6 - ListarPorCliente() con cliente inexistente deberia devolver una compra con ID -6");
		}
		
		//Test listarPorCliente correcto
		if(asCompra.ListarPorCliente(compra.GetIDCliente()).get(0).GetID() < 0){
			fail("Error: ListarPorCliente() correcto debería devolver una compra con ID positivo");
		}
		
		asCliente.Baja(compra.GetIDCliente());

		// Test codigo de error 7 - cliente no activo
		if (asCompra.ListarPorCliente(compra.GetIDCliente()).get(0).GetID() != -7) {
			fail("Error: 7 - ListarPorCliente() con cliente no activo deberia devolver una compra con ID -7");
		}

	}

	@Test
	public void testListarPorPase() {
		
		// Test codigo de error 2 - pase inexistente
		if (asCompra.ListarPorPase(-1).get(0).GetID() != -2) {
			fail("Error: 2 - ListarPorPase() con pase inexistente deberia devolver una compra con ID -2");
		}
		
		Pair<Integer, Integer> CyP = crearCompra();
		
		//Test listarPorPase correcto
		if(asCompra.ListarPorPase(CyP.getSecond()).get(0).GetID() < 0){
			fail("Error: ListarPorPase() correcto debería devolver una compra con ID positivo");
		}
		
		asPase.Baja(CyP.getSecond());

		// Test codigo de error 3 - pase no activo
		if (asCompra.ListarPorPase(CyP.getSecond()).get(0).GetID() != -3) {
			fail("Error: 3 - ListarPorPase() con pase no activo deberia devolver una compra con ID -3");
		}
	}

	@Test
	public void testCalcularTotalComprasPorPelicula() {
		Pair<Integer, Integer> CyP = crearCompra();
		TPase pase = asPase.Mostrar(CyP.getFirst());
		Integer idPeli = pase.GetIDPelicula();
		
		// Test codigo de error 11 - pelicula inexistente
		if (asCompra.CalcularTotalComprasPorPelicula(-1) != -11) {
			fail("Error: 11 - CalcularTotalComprasPorPelicula() con pelicula inexistente deberia devolver -11");
		}
		
		if(asPelicula.Baja(idPeli) != 0)
			fail("error");

		// Test codigo de error 12 - pelicula no activa
		if (asCompra.CalcularTotalComprasPorPelicula(idPeli) != -12) {
			fail("Error: 12 - CalcularTotalComprasPorPelicula() con pelicula no activa deberia devolver -12");
		}
	}

	@Test
	public void DevolverCompra() {
		Pair<Integer, Integer> CyP = crearCompra();
		
		// Test codigo de error 4 - compra inexistente
		if (asCompra.DevolverCompra(-1, CyP.getSecond(), 1) != -4) {
			fail("Error: 4 - DevolverCompra() con compra inexistente deberia devolver -4");
		}

		// Test codigo de error 2 - pase inexistente
		if (asCompra.DevolverCompra(CyP.getFirst(), -1, 1) != -2) {
			fail("Error: 2 - DevolverCompra() con pase inexistente deberia devolver -2");
		}

		Integer idPase = crearPase();
		// Test codigo de error 5 - pase no incluido en compra
		if (asCompra.DevolverCompra(CyP.getFirst(), idPase, 1) != -5) {
			fail("Error: 5 - DevolverCompra() con pase no incluido en compra deberia devolver -5");
		}
		
		// Test devolver compra correcto
		if(asCompra.DevolverCompra(CyP.getFirst(), CyP.getSecond(), 1) != 0){
			fail("Error: DevolverCompra() correcto debería devolver 0");
		}

	}
	
	@Test
	public void testVerResumenDeCompra() {
		Pair<Integer, Integer> CyP = crearCompra();
		if(asCompra.VerResumenDeCompra(-1).get(0).GetIDCompra() != -4){
			fail("Error: VerResumenDeCompra() con compra no existente debería devolver -4");
		}
		if(asCompra.VerResumenDeCompra(CyP.getFirst()).get(0).GetIDCompra() < 0){
			fail("Error: VerResumenDeCompra() correcto debería devolver una compra con id positivo en la primera posición");
		}
	}

}
