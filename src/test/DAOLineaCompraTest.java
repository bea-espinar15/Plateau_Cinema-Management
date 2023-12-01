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

public class DAOLineaCompraTest {
	private static Random random;
	private static DAOCompra daoC;
	private static DAOLineaCompra daoL;
	
	@BeforeClass
	public static void beforeClass() {
		random = new Random();
		daoC = DAOFactory.getInstance().getDAOCompra();
		daoL = DAOFactory.getInstance().getDAOLineaCompra();
	}

	private boolean equals(TLineaCompra a, TLineaCompra b) {
		return a.GetIDCompra().equals(b.GetIDCompra()) && a.GetIDPase().equals(b.GetIDPase())
				&& a.GetNEntradas().equals(b.GetNEntradas()) && a.GetPrecio().equals(b.GetPrecio());
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
	
	private TPase crearPase() {
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
		pase.SetActivo(true);
		pase.SetID(idPase);
		return pase;
	}
	
	private TCompra crearCompra() {
		long time = System.currentTimeMillis() + (long) (random.nextDouble() + 1) * System.currentTimeMillis();
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
		
		TLineaCompra l = new TLineaCompra(c.GetID(), crearPase().GetID(), 2, 50);
		Integer result = daoL.Create(l);
		if (result < 0)
			fail("No se ha podido crear la lineacompra");
		
		return l;
	}
	
	@Test
	public void testCreate() {
		Transaction t = crearTransaccion();
		t.start();
		
		// Create - sin errores
		crearLineaCompra();
		
		// Create - error al insertar (no existe foreign key)

		
		TLineaCompra l = new TLineaCompra(-1, -1, 2, 50);
		if (daoL.Create(l) != -1){
			t.rollback();
			fail("No se ha podido crear la lineacompra");
		}
		
		t.commit();
	}
	
	@Test
	public void testRead(){
		Transaction t = crearTransaccion();
		t.start();
		
		// Read - sin errores
		TLineaCompra l = crearLineaCompra();
		if(!equals(l, daoL.Read(l.GetIDPase(), l.GetIDCompra()))){
			t.rollback();
			fail("Error:");
		}
		
		// Read - no existe
		if(daoL.Read(-1, -1) != null){
			t.rollback();
			fail("Error:");
		}
		
		// Read - exception
		if(daoL.Read(-1, null).GetIDCompra() != -1){
			t.rollback();
			fail("Error:");
		}
		
		t.commit();
	}
	
	@Test
	public void testUpdate(){
		Transaction t = crearTransaccion();
		t.start();
		
		// Update - sin errores
		TLineaCompra l = crearLineaCompra();
		
		l.SetNEntradas(99);
		if(daoL.Update(l) != 0){
			t.rollback();
			fail("Error:");
		}
		
		// Update - error (no existe idcompra)
		l.SetIDCompra(-1);
		if(daoL.Update(l) != -1){
			t.rollback();
			fail("Error:");
		}
		
		// Update - error exception
		l.SetIDCompra(null);
		if(daoL.Update(l) != -1){
			t.rollback();
			fail("Error:");
		}
		
		t.commit();
	}
	
	@Test
	public void testReadAllByCompra(){
		Transaction t = crearTransaccion();
		t.start();
		
		// ReadAllByCompra - sin error
		TLineaCompra l = crearLineaCompra();
		boolean found = false;
		ArrayList<TLineaCompra> all = daoL.ReadAllByCompra(l.GetIDCompra());
		for(TLineaCompra lc : all){
			if(equals(lc, l))
				found = true;
		}
		if(!found){
			t.rollback();
			fail("Error:");
		}
		
		// sin error - ninguna linea compra existe
		TCompra c = crearCompra();
		all = daoL.ReadAllByCompra(c.GetID());
		if(!all.isEmpty()){
			t.rollback();
			fail("Error:");
		}
		
		// exception
		all = daoL.ReadAllByCompra(null);
		if(all.get(0).GetIDCompra() != -4) {
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
