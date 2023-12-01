package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import Negocio.ASFactory.ASFactory;
import Negocio.Sala.ASSala;
import Negocio.Sala.TSala;
import Negocio.Sala.TSalaEspecial;
import Negocio.Sala.TSalaEstandar;
 
public class ASSalaTest {
	private static ASSala as;
	private static Random random;

	@BeforeClass
	public static void beforeClass() {
		as = ASFactory.getInstance().GetASSala();
		random = new Random();
	}
	
	private boolean equals(TSala a, TSala b) {
		boolean subclassEquals = false;
		if (a instanceof TSalaEspecial && b instanceof TSalaEspecial)
			subclassEquals = equals((TSalaEspecial) a, (TSalaEspecial) b);
		else if (a instanceof TSalaEstandar && b instanceof TSalaEstandar)
			subclassEquals = equals((TSalaEstandar) a, (TSalaEstandar) b);

		return subclassEquals && a.GetNombre().equals(b.GetNombre()) && a.GetAforo().equals(b.GetAforo())
				&& a.GetActivo().equals(b.GetActivo()) && a.GetId().equals(b.GetId());
	}

	private boolean equals(TSalaEspecial a, TSalaEspecial b) {
		return a.Get3D() == b.Get3D() && a.GetVO() == b.GetVO();
	}

	private boolean equals(TSalaEstandar a, TSalaEstandar b) {
		return a.GetAseos() == b.GetAseos();
	}

	@Test
	public void altaTest() {
		// ID y activo no deberian de ser usados al crear
		TSalaEstandar sEstandar = new TSalaEstandar();
		sEstandar.SetNombre("Estandar " + random.nextInt());
		sEstandar.SetAforo(69);
		sEstandar.SetAseos(true);
		
		TSalaEspecial sEspecial = new TSalaEspecial();
		sEspecial.SetNombre("Especial " + random.nextInt());
		sEspecial.SetAforo(420);
		sEspecial.Set3D(false);
		sEspecial.SetVO(true);
		
		// Test - alta sala normal sin errores
		Integer idEstandar = as.Alta(sEstandar);
		if(idEstandar <= 0)
			fail("Error: Alta() deberia retornar ID > 0");
		
		// Test - alta sala especial sin errores
		Integer idEspecial = as.Alta(sEspecial);
		if(idEspecial <= 0)
			fail("Error: Alta() deberia retornar ID > 0");
		
		// Test - alta sala ya existente activa
		if(as.Alta(sEstandar) != -2)
			fail("Error: Alta() sala ya existente deberia retornar -2");
		
		// Test - alta sala ya existente inactiva
		// Primero damos de baja la sala
		if(as.Baja(idEstandar) != 0)
			fail("Error: Baja() es requisito de alta sala ya existente inactiva");
		if(as.Alta(sEstandar) < 0)
			fail("Error: Alta() sala ya existente inactiva se deberia retornar el id de la sala");
	}

	@Test
	public void bajaTest() {
		// ID y activo no deberian de ser usados al crear
		TSalaEstandar sEstandar = new TSalaEstandar();
		sEstandar.SetNombre("Estandar " + random.nextInt());
		sEstandar.SetAforo(69);
		sEstandar.SetAseos(true);
		
		TSalaEspecial sEspecial = new TSalaEspecial();
		sEspecial.SetNombre("Especial " + random.nextInt());
		sEspecial.SetAforo(420);
		sEspecial.Set3D(false);
		sEspecial.SetVO(true);
		
		Integer idEstandar = as.Alta(sEstandar);
		Integer idEspecial = as.Alta(sEspecial);
		if(idEstandar <= 0 || idEspecial <= 0)
			fail("Error: Alta() es requisito de Baja()");
		
		// Test - baja sala inexistente
		if(as.Baja(-1) != -3)
			fail("Error: Baja() sala inexistente deberia retornar -3");
		
		// Test - baja estandar sin errores
		if(as.Baja(idEstandar) != 0)
			fail("Error: Baja() sala estandar sin errores deberia retornar 0");
		
		// Test - baja especial sin errores
		if(as.Baja(idEspecial) != 0)
			fail("Error: Baja() sala especial sin errores deberia retornar 0");
		
		// Test - baja sala inactiva
		if(as.Baja(idEstandar) != -4)
			fail("Error: Baja() sala inactiva deberia retornar -4");
	}

	@Test
	public void modificarTest() {
		// ID y activo no deberian de ser usados al crear
		TSalaEstandar sEstandar = new TSalaEstandar();
		String estandarNombre = "Estandar " + random.nextInt();
		sEstandar.SetNombre(estandarNombre);
		sEstandar.SetAforo(69);
		sEstandar.SetAseos(true);
		
		TSalaEspecial sEspecial = new TSalaEspecial();
		sEspecial.SetNombre("Especial " + random.nextInt());
		sEspecial.SetAforo(420);
		sEspecial.Set3D(false);
		sEspecial.SetVO(true);
		
		Integer idEstandar = as.Alta(sEstandar);
		Integer idEspecial = as.Alta(sEspecial);
		if(idEstandar <= 0 || idEspecial <= 0)
			fail("Error: Alta() es requisito de Modificar()");
		sEstandar.SetActivo(true);
		sEstandar.SetId(idEstandar);
		sEspecial.SetActivo(true);
		sEspecial.SetId(idEspecial);
		
		// Test - modificar sala inexistente
		TSala inexistente = new TSala();
		inexistente.SetId(-1);
		if(as.Modificar(inexistente) != -3)
			fail("Error: Modificar() sala inexistente deberia retornar -3");
		
		// Test - modificar sala inactiva
		// Desactivamos sala
		if(as.Baja(idEstandar) != 0)
			fail("Error: Baja() es requisito de Modificar()");
		if(as.Modificar(sEstandar) != -4)
			fail("Error: Modificar() sala inactiva deberia retornar -4");
		
		// Test - modificar sala a nombre ya existente
		TSalaEspecial modificada = new TSalaEspecial();
		modificada.SetNombre(estandarNombre);
		modificada.SetAforo(420);
		modificada.Set3D(false);
		modificada.SetVO(true);
		modificada.SetId(idEspecial);
		modificada.SetActivo(true);

		if(as.Modificar(modificada) != -5)
			fail("Error: Modificar() sala nombre duplicado deberia retornar -5");
		
		// Test - modificar sin errores
		modificada.SetNombre("Modificada " + random.nextInt());
		if(as.Modificar(modificada) < 0)
			fail("Error: Modificar() sin errores deberia retornar el id de la sala modificada");
	}

	@Test
	public void mostrarParaModificar() {
		// Solo tenemos que testear el error -4 (sala no activa)
		// Otros codigos de error se testean en testMostrar()
		TSalaEstandar sEstandar = new TSalaEstandar();
		sEstandar.SetNombre("Estandar " + random.nextInt());
		sEstandar.SetAforo(69);
		sEstandar.SetAseos(true);
		
		Integer idEstandar = as.Alta(sEstandar);
		if(idEstandar <= 0)
			fail("Error: Alta() es requisito de mostrarParaModificar()");
		sEstandar.SetActivo(true);
		sEstandar.SetId(idEstandar);
		
		if(as.Baja(idEstandar) != 0)
			fail("Error: Baja() es requisito de mostrarParaModificar()");
		
		// Test - mostrarParaModificar sala inactiva
		if(as.MostrarParaModificar(idEstandar).GetId() != -4)
			fail("Error: MostrarParaModificar() sala no activa deberia retornar -4");
	}

	@Test
	public void mostrarTest() {
		// Creamos sala para tener sala para mostrar
		// ID y activo no deberian de ser usados al crear
		TSalaEstandar sEstandar = new TSalaEstandar();
		sEstandar.SetNombre("Estandar " + random.nextInt());
		sEstandar.SetAforo(69);
		sEstandar.SetAseos(true);
		
		Integer idEstandar = as.Alta(sEstandar);
		if(idEstandar <= 0)
			fail("Error: Alta() es requisito de Mostrar()");
		sEstandar.SetActivo(true);
		sEstandar.SetId(idEstandar);
		
		// Test - mostrar sala inexistente
		if(as.Mostrar(-1).GetId() != -3)
			fail("Error: Mostrar() sala inexistente deberia retornar entidad con ID -3");
		
		// Test - mostrar sala sin errores
		if(!equals(sEstandar, as.Mostrar(idEstandar)))
			fail("Error: Mostrar() sala sin errores deberia retornar entidad identica");
	}

	@Test
	public void listarTest() {
		// Creamos salas para tener lista no vacia
		// ID y activo no deberian de ser usados al crear
		TSalaEstandar sEstandar = new TSalaEstandar();
		sEstandar.SetNombre("Estandar " + random.nextInt());
		sEstandar.SetAforo(69);
		sEstandar.SetAseos(true);
		
		TSalaEspecial sEspecial = new TSalaEspecial();
		sEspecial.SetNombre("Especial " + random.nextInt());
		sEspecial.SetAforo(420);
		sEspecial.Set3D(false);
		sEspecial.SetVO(true);
		
		Integer idEstandar = as.Alta(sEstandar);
		Integer idEspecial = as.Alta(sEspecial);
		if(idEstandar <= 0 || idEspecial <= 0)
			fail("Error: Alta() es requisito de Listar()");
		sEstandar.SetActivo(true);
		sEstandar.SetId(idEstandar);
		sEspecial.SetActivo(true);
		sEspecial.SetId(idEspecial);
		
		ArrayList<TSala> listaAll = as.Listar();
		
		// Test - retorna lista no vacia
		if(listaAll.get(0).GetId() == -1)
			fail("Error: listar() deberia retornar lista no vacia");
		
		// Test - lista contiene entidades creadas
		boolean estandarFound = false, especialFound = false;
		for(TSala s : listaAll){
			if(equals(s, sEstandar))
				estandarFound = true;
			else if(equals(s, sEspecial))
				especialFound = true;
		}
		if(!estandarFound || !especialFound)
			fail("Error: Listar() deberia contener todas las entidades creadas");
	}

	@Test
	public void listarEstandarTest() {
		// Creamos salas para tener lista no vacia
		// ID y activo no deberian de ser usados al crear
		TSalaEstandar sEstandar = new TSalaEstandar();
		sEstandar.SetNombre("Estandar " + random.nextInt());
		sEstandar.SetAforo(69);
		sEstandar.SetAseos(true);
		
		Integer idEstandar = as.Alta(sEstandar);
		if(idEstandar <= 0)
			fail("Error: Alta() es requisito de Listar()");
		sEstandar.SetActivo(true);
		sEstandar.SetId(idEstandar);
		
		ArrayList<TSalaEstandar> listaEstandar = as.ListarEstandar();
		
		// Test - retorna lista no vacia
		if(listaEstandar.get(0).GetId() == -1)
			fail("Error: listar() deberia retornar lista no vacia");
		
		// Test - lista contiene entidades creadas
		boolean estandarFound = false;
		for(TSala s : listaEstandar){
			if(equals(s, sEstandar))
				estandarFound = true;
		}
		if(!estandarFound)
			fail("Error: Listar() deberia contener todas las entidades creadas");
	}
	
	@Test
	public void listarEspecialTest() {
		// Creamos salas para tener lista no vacia
		// ID y activo no deberian de ser usados al crear
		TSalaEspecial sEspecial = new TSalaEspecial();
		sEspecial.SetNombre("Especial " + random.nextInt());
		sEspecial.SetAforo(420);
		sEspecial.Set3D(false);
		sEspecial.SetVO(true);
		
		Integer idEspecial = as.Alta(sEspecial);
		if(idEspecial <= 0)
			fail("Error: Alta() es requisito de Listar()");
		sEspecial.SetActivo(true);
		sEspecial.SetId(idEspecial);
		
		ArrayList<TSalaEspecial> listaEspecial = as.ListarEspecial();
		
		// Test - retorna lista no vacia
		if(listaEspecial.get(0).GetId() == -1)
			fail("Error: listar() deberia retornar lista no vacia");
		
		// Test - lista contiene entidades creadas
		boolean especialFound = false;
		for(TSala s : listaEspecial){
			if(equals(s, sEspecial))
				especialFound = true;
		}
		if(!especialFound)
			fail("Error: Listar() deberia contener todas las entidades creadas");
	}
}
