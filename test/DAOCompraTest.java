package test;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import Integracion.Compra.DAOCompra;
import Integracion.Compra.DAOLineaCompra;
import Integracion.DAOFactory.DAOFactory;
import Integracion.Pase.DAOPaseImp;
import Integracion.Pelicula.DAOPeliculaImp;
import Integracion.Sala.DAOSalaImp;
import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;
import Negocio.Compra.TCompra;
import Negocio.Compra.TLineaCompra;
import Negocio.Pase.TPase;
import Negocio.Pelicula.TPelicula;
import Negocio.Sala.TSalaEstandar;

public class DAOCompraTest {
	private static Random random;
	private static DAOCompra daoC;
	private static DAOLineaCompra daoL;

	private boolean equals(TCompra a, TCompra b) {
		return a.GetID().equals(b.GetID()) && a.GetIDCliente().equals(b.GetIDCliente())
				&& a.GetPrecioTotal().equals(b.GetPrecioTotal());
	}

	private Integer crearSala() {
		DAOSalaImp dao = (DAOSalaImp) DAOFactory.getInstance().getDAOSala();
		TSalaEstandar sala = new TSalaEstandar();
		sala.SetNombre("nombre " + random.nextInt());
		sala.SetAforo(50);
		sala.SetAseos(false);
		Integer idSala = dao.Create(sala);
		if (idSala < 0)
			fail("Error: Alta() sala es requisito para poder testear");
		return idSala;
	}

	private Integer crearPeli() {
		DAOPeliculaImp dao = (DAOPeliculaImp) DAOFactory.getInstance().getDAOPelicula();

		TPelicula peli = new TPelicula();
		peli.SetTitulo("titulo " + random.nextInt());
		peli.SetDirector("dir " + random.nextInt());
		peli.SetDuracion(1000);
		peli.SetGenero("genero " + random.nextInt());
		Integer idPeli = dao.Create(peli);
		if (idPeli < 0)
			fail("Error: Alta() pelicula es requisito para poder testear");
		return idPeli;
	}

	private Integer crearPase() {
		Integer idPeli = crearPeli();
		Integer idSala = crearSala();
		DAOPaseImp dao = (DAOPaseImp) DAOFactory.getInstance().getDAOPase();
		TPase pase = new TPase(
				new Date(System.currentTimeMillis() + (long) ((random.nextDouble() + 1) * System.currentTimeMillis())),
				1, idPeli, idSala);
		pase.SetAsientosLibres(10);
		Integer idPase = dao.Create(pase);
		if (idPase < 0)
			fail("Error: Alta() pase es requisito para poder testear");
		return idPase;
	}

	private TCompra crearCompra() {
		long time = System.currentTimeMillis() + (long) (random.nextDouble() + 1) * System.currentTimeMillis();

		// Test - crear sin errores
		TCompra c = new TCompra(null, 100, new Date(time), 1);

		Integer idC = daoC.Create(c);
		if (idC < 0) {
			fail("Error: Create() deberia retornar ID > 0");
		}
		c.SetID(idC);

		return c;
	}

	private TLineaCompra crearLineaCompra(){
		TCompra c = crearCompra();
		
		TLineaCompra l = new TLineaCompra(c.GetID(), crearPase(), 2, 50);
		Integer result = daoL.Create(l);
		if (result < 0)
			fail("No se ha podido crear la lineacompra");
		
		return l;
	}
	
	@BeforeClass
	public static void beforeClass() {
		random = new Random();
		daoC = DAOFactory.getInstance().getDAOCompra();
		daoL = DAOFactory.getInstance().getDAOLineaCompra();
	}

	@Test
	public void testCreate() {
		Transaction t = crearTransaccion();
		t.start();
		// Test - crear sin errores
		crearCompra();

		// Test - error fecha null
		TCompra c1 = new TCompra(null, 100, null, 1);
		Integer idC1 = daoC.Create(c1);
		if (idC1 != -4) {
			t.rollback();
			fail("Error: Create() deberia retornar -4");
		}
		t.commit();
	}

	@Test
	public void testRead() {
		Transaction t = crearTransaccion();
		t.start();
		TCompra c = crearCompra();

		// Read - sin errores
		if (!equals(c, daoC.Read(c.GetID()))) {
			t.rollback();
			fail("Error: Read (ByID) deberia retornar una entidad identica");
		}

		// Read - compra no existente
		if (daoC.Read(-1) != null) {
			t.rollback();
			fail("Error: Read (ByID) deberia retornar null si no existe");
		}

		// Read con excepcion
		if (daoC.Read(null).GetID() != -4) {
			t.rollback();
			fail("Error: Read (ByID) deberia retornar -4 con excepcion");
		}
		t.commit();
	}

	@Test
	public void Update() {
		Transaction t = crearTransaccion();
		t.start();
		TCompra c = crearCompra();

		// Update - sin errores
		c.SetPrecioTotal(random.nextInt());
		c.SetIDCliente(99);
		c.SetFecha(new Date(System.currentTimeMillis()));
		if (daoC.Update(c) != 0) {
			t.rollback();
			fail("Error: Update() sin errores deberia retornar 0");
		}

		// Update - con exception
		c.SetPrecioTotal(null);
		if (daoC.Update(c) != -4) {
			t.rollback();
			fail("Error: Update() con exception deberia retornar -4");
		}
		t.commit();
	}

	@Test
	public void testDelete() {
		Transaction t = crearTransaccion();
		t.start();
		
		TCompra c = crearCompra();

		// Delete - sin errores
		if (daoC.Delete(c.GetID()) != 0) {
			t.rollback();
			fail("Error: Delete() sin errores deberia retornar 0");
		}

		// Delete - con exception
		if (daoC.Delete(null) != -4) {
			t.rollback();
			fail("Error: Delete() con exception deberia retornar -4");
		}
		t.commit();
	}

	@Test
	public void testReadAll() {
		Transaction t = crearTransaccion();
		t.start();
		
		TCompra c1 = crearCompra(), c2 = crearCompra();

		boolean found1 = false, found2 = false;
		ArrayList<TCompra> all = daoC.ReadAll();
		for (TCompra c : all) {
			if (equals(c, c1))
				found1 = true;
			else if (equals(c, c2))
				found2 = true;
		}

		if (!found1 || !found2) {
			t.rollback();
			fail("Error: ReadAll() deberia retornar lista que contiene entidades creadas");
		}

		t.commit();
	}

	@Test
	public void testReadAllByCliente() {
		Transaction t = crearTransaccion();
		t.start();
		TCompra c1 = crearCompra();

		// ReadAllByCliente - sin errores
		boolean found = false;
		ArrayList<TCompra> lista = daoC.ReadAllByCliente(c1.GetIDCliente());
		for (TCompra c : lista) {
			if (equals(c, c1))
				found = true;
		}

		if (!found) {
			t.rollback();
			fail("Error: ReadAllByCliente() deberia retornar lista que contiene entidad creada");
		}
		
		// ReadAllByCliente - cliente no existe
		lista = daoC.ReadAllByCliente(-1);
		if(!lista.isEmpty()){
			t.rollback();
			fail("Error: ReadAllByCliente() deberia retornar lista vacia si cliente no existe");
		}
		
		// ReadAllByCliente - exception
		lista = daoC.ReadAllByCliente(null);
		if(lista.get(0).GetID() != -4){
			t.rollback();
			fail("Error: ReadAllByCliente() deberia retornar lista con cliente unico con ID -4");
		}
		
		t.commit();
	}
	
	@Test
	public void testReadAllByPase(){
		Transaction t = crearTransaccion();
		t.start();
		
		// ReadAllByPase - sin error
		TLineaCompra l = crearLineaCompra();
		TCompra c1 = daoC.Read(l.GetIDCompra());
		ArrayList<TCompra> all = daoC.ReadAllByPase(l.GetIDPase());
		boolean found = false;
		for(TCompra c : all){
			if(equals(c, c1))
				found = true;
		}
		if(!found){
			t.rollback();
			fail("Error:");
		}
		
		t.commit();
	}
	
	private Transaction crearTransaccion() {
		try {
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			Transaction t = tm.getTransaction();
			return t;
		} catch (Exception e) {
			fail("Error transaccional");
			return null;
		}
	}
}
