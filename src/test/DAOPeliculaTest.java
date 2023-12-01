package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

import Integracion.DAOFactory.DAOFactory;
import Integracion.Pelicula.DAOPeliculaImp;
import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;
import Negocio.Pelicula.TPelicula;


public class DAOPeliculaTest {
	
	private boolean equals(TPelicula a, TPelicula b){
		if(!a.GetTitulo().equals(b.GetTitulo()) || 
				!a.GetDirector().equals(b.GetDirector()) || 
				!a.GetDuracion().equals(b.GetDuracion()) ||
				!a.GetGenero().equals(b.GetGenero()))
			return false;
		return true;
	}

	@Test
	public void test() {
		DAOPeliculaImp dao = (DAOPeliculaImp) DAOFactory.getInstance().getDAOPelicula();
		
		Transaction t;
		
		t = crearTransaccion();
		
		Random random = new Random();
		TPelicula a = new TPelicula("El perraco " + random.nextInt(), "Chucky", "accion", 98);
		TPelicula b = new TPelicula("El guapo "  + random.nextInt(), "Joaquin", "romance", 160);
		
		// Creamos 2 peliculas, a y b
		t.start();
		Integer idA = dao.Create(a);
		Integer idB = dao.Create(b);
		
		if(idA < 0 || idB < 0)
			fail("ERROR: Create() debería retornar un ID > 0");
		t.commit();
		
		t = crearTransaccion();
		t.start();
		// Test Read (ByTitulo)
		TPelicula a2 = dao.ReadByTitulo(a.GetTitulo());
		
		if (!equals(a, a2)) {
			fail("ERROR: ReadByTitulo debería retornar pelicula identica");
		}
		
		// Test Read (ByID)
		TPelicula b2 = dao.Read(idB);
		
		if (!equals(b, b2)) {
			fail("ERROR: Read (ById) debería retornar pelicula identica");
		}
		
		// Test ReadAll()
		ArrayList<TPelicula> readAllList = dao.ReadAll();
		boolean aFound = false, bFound = false;
		for(TPelicula p : readAllList){
			if(equals(p, a))
				aFound = true;
			else if(equals(p, b))
				bFound = true;
		}
		if(!aFound || !bFound)
			fail("ERROR: readAll debería contener las peliculas creadas");
		
		t.commit();
		
		t = crearTransaccion();
		t.start();
		// Test Update()
		TPelicula aUpdate = new TPelicula("El perraco " + random.nextInt(), "Chucky 2.0", "terror", 99);
		aUpdate.SetID(idA);
		if(dao.Update(aUpdate) <= 0)
			fail("ERROR: Update() debería retornar un ID > 0");
		
		t.commit();
		
		t = crearTransaccion();
		t.start();
		a2 = dao.Read(idA);
		if(equals(a2, a))
			fail("ERROR: Update() debería modificar la base de datos");
		
		// Test AddProductora()
		Integer idProductora = random.nextInt(1000);
		if(dao.AddProductora(idProductora, idA) != 0)
			fail("ERROR: AddProductora() debería retornar 0");
		t.commit();
		
		t = crearTransaccion();
		t.start();
		// Test ReadAllByProductora()
		ArrayList<TPelicula> readByProducoraList = dao.ReadAllByProductora(idProductora);
		
		aFound = false;
		for(TPelicula p : readByProducoraList){
			if(equals(p, aUpdate))
				aFound = true;
		}
		if(!aFound)
			fail("ERROR: ReadAllByProductora() debería contener la entidad agregada");
		
		// Test LinkedProductora() true
		if(dao.LinkedProductora(idProductora, idA) != 1)
			fail("ERROR: LinkedProductora() debería retornar 1");
		
		// Test RemoveProductora()
		if(dao.RemoveProductora(idProductora, idA) != 0)
			fail("ERROR: RemoveProductora() debería retornar 0");
		t.commit();
		
		t = crearTransaccion();
		t.start();
		// Test LinkedProductora() false
		if(dao.LinkedProductora(idProductora, idA) != 0)
			fail("ERROR: LinkedProductora() debería retornar 1");
		
		// Test Delete()
		dao.Delete(a2.GetID());
		dao.Delete(b2.GetID());
		t.commit();
		

		t = crearTransaccion();
		t.start();
		
		a2 = dao.Read(idA);
		b2 = dao.Read(idB);
		if(true == a2.GetActivo())
			fail("ERROR: Delete() debería retornar entidad no activa");
		if(true == b2.GetActivo())
			fail("ERROR: Delete() debería retornar entidad no activa");
		t.commit();
	}
	
	private Transaction crearTransaccion() {
		try {
			TransactionManager tm = TransactionManager.getInstance();
			tm.newTransaction();
			Transaction t = tm.getTransaction();
			return t;
		}
		catch (Exception e) {
			fail("Error transaccional");
			return null;
		}
	}
	
}

