package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

import Integracion.DAOFactory.DAOFactory;
import Integracion.Pelicula.DAOPeliculaImp;
import Integracion.Productora.DAOProductoraImp;
import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;
import Negocio.Pelicula.TPelicula;
import Negocio.Productora.TProductora;

public class DAOProductoraTest {
	
	private boolean equals(TProductora a, TProductora b) {
		if (!a.GetCIF().equals(b.GetCIF()) || 
				!a.GetNombre().equals(b.GetNombre())  ||
				!a.GetTelefono().equals(b.GetTelefono()) ||
				!a.GetDireccion().equals(b.GetDireccion()))
			return false;
		return true;
	}

	@Test
	public void test() {
		DAOProductoraImp dao = (DAOProductoraImp) DAOFactory.getInstance().getDAOProductora();
		Random random = new Random();
		
		TProductora a = new TProductora("Productora " + random.nextInt(), "cif " + random.nextInt(), random.nextInt(), "C/ Olmo 17");
		
		String nombreB = "Productora " + random.nextInt();
		TProductora b = new TProductora(nombreB, "cif " + random.nextInt(), random.nextInt(), "C/ Olmo 178");
		
		String cifC = "cif " + random.nextInt();
		TProductora c = new TProductora("Productora " + random.nextInt(), cifC, random.nextInt(), "direccion");

		Transaction t;
		
		t = crearTransaccion();
		t.start();
		// Test - Create()
		Integer idA = dao.Create(a);
		Integer idB = dao.Create(b);
		Integer idC = dao.Create(c);
		if(idA <= 0 || idB <= 0 || idC <= 0)
			fail("Error: Create() deberia retornar IDs positivos");
		t.commit();
		
		t = crearTransaccion();
		t.start();
		// Test - Read (ByID)
		if (!equals(a, dao.Read(idA))) {
			fail("Error: Read() by ID deberia retornar entidad identica");
		}
		
		// Test - ReadByNombre
		if(!equals(b, dao.ReadByNombre(nombreB)))
			fail("Error: ReadByNombre() deberia retornar entidad identica");
		
		// Test - ReadByCIF
		if(!equals(c, dao.ReadByCIF(cifC)))
			fail("Error: ReadByCif() deberia retornar entidad identica");
		
		
		// Test - ReadAll
		ArrayList<TProductora> allList = dao.ReadAll();
		boolean aFound = false, bFound = false;
		for(TProductora p : allList){
			if(equals(p, a))
				aFound = true;
			else if(equals(p, b))
				bFound = true;
		}
		if(!aFound || !bFound)
			fail("Error: ReadAll() deberia retornar todas las entidades creadas");
		
		// Test - ReadAllByPelicula
		// Primero creamos una pelicula
		DAOPeliculaImp daoPeli = (DAOPeliculaImp) DAOFactory.getInstance().getDAOPelicula();
		TPelicula peli = new TPelicula("El perraco " + random.nextInt(), "Chucky", "accion", 98);
		Integer idPeli = daoPeli.Create(peli);
		if(idPeli <= 0 || daoPeli.AddProductora(idA, idPeli) != 0)
			fail("Error: ReadAllByPelicula() falla dependencia de DAO pelicula");
		t.commit();
		
		t = crearTransaccion();
		t.start();
		ArrayList<TProductora> porPeliList = dao.ReadAllByPelicula(idPeli);
		aFound = false;
		for(TProductora p : porPeliList){
			if(equals(p, a))
				aFound = true;
		}
		if(!aFound)
			fail("Error: ReadAllByPelicula deberira retornar productora creada");
		
		// Test - Update()
		TProductora modA = new TProductora("Productora " + random.nextInt(), "cif " + random.nextInt(), random.nextInt(), "Calle modificada");
		modA.SetID(idA);
		if(dao.Update(modA) < 0)
			fail("Error: Update() deberia retornar id productora");
		t.commit();
		
		t = crearTransaccion();
		t.start();
		// Test - Read (ByID) despues de modificar
		if(equals(a, dao.Read(idA)))
			fail("Error: Read() despues de modificar deberia retornar una entidad diferente");
		
		// Test - Delete()
		if(dao.Delete(idA) != 0 || dao.Delete(idB) != 0 || dao.Delete(idC) != 0)
			fail("Error: Delete() deberia retornar 0");
		t.commit();
		
		t = crearTransaccion();
		t.start();
		// Test - Read despues de eliminar
		if(dao.Read(idA).GetActivo() || dao.Read(idB).GetActivo() || dao.Read(idC).GetActivo())
			fail("Error: Read() despues de eliminar deberia retornar entidades desactivadas");
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
