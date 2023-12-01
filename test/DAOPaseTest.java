package test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import Integracion.DAOFactory.DAOFactory;
import Integracion.Pase.DAOPaseImp;
import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;
import Negocio.Pase.TPase;

public class DAOPaseTest {

	private boolean equals(TPase a, TPase b) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		String aHorario = format.format(a.GetHorario()), bHorario = format.format(b.GetHorario());

		return a.GetIDPelicula().equals(b.GetIDPelicula()) && a.GetIDSala().equals(b.GetIDSala())
				&& a.GetAsientosLibres().equals(b.GetAsientosLibres())
				&& a.GetPrecioActual().equals(b.GetPrecioActual()) && aHorario.equals(bHorario);
	}

	@Test
	public void test() {
		// Se tienen que añadir asientos libres manualmente, normalmente lo
		// calcula el AS

		DAOPaseImp dao = (DAOPaseImp) DAOFactory.getInstance().getDAOPase();

		long time = System.currentTimeMillis() + 100000;

		TPase a = new TPase(new Date(time), 1, 1, 1);
		a.SetAsientosLibres(50);
		TPase b = new TPase(new Date(time + 1000000), 5, 1, 2);
		b.SetAsientosLibres(50);
		TPase c = new TPase(new Date(time + 2000000), 10, 2, 2);
		c.SetAsientosLibres(50);

		Transaction t;

		t = crearTransaccion();
		t.start();
		Integer idA = dao.Create(a);
		Integer idB = dao.Create(b);
		Integer idC = dao.Create(c);
		t.commit();
		
		// Test Create
		if(idA <= 0 || idB <= 0 || idC <= 0)
			fail("Error: Create() deberia retornar ID > 0");
		
		a.SetID(idA);
		a.SetActivo(true);
		b.SetID(idB);
		b.SetActivo(true);
		c.SetID(idC);
		c.SetActivo(true);

		t = crearTransaccion();
		t.start();

		// Test Read (ByID)
		TPase readA = dao.Read(idA);
		if (!equals(readA, a))
			fail("Error: Read() deberia retornar una entidad identica");

		TPase readB = dao.Read(idB);
		if (!equals(readB, b))
			fail("Error: Read() deberia retornar una entidad identica");

		TPase readC = dao.Read(idC);
		if (!equals(readC, c))
			fail("Error: Read() deberia retornar una entidad identica");

		// Test ReadByKey
		TPase readAByKey = dao.ReadByKey(a.GetIDSala(), a.GetHorario());
		if (!equals(readAByKey, a))
			fail("Error: ReadByKey() deberia retornar una entidad identica");

		TPase readBByKey = dao.ReadByKey(b.GetIDSala(), b.GetHorario());
		if (!equals(readBByKey, b))
			fail("Error: ReadByKey() deberia retornar una entidad identica");

		TPase readCByKey = dao.ReadByKey(c.GetIDSala(), c.GetHorario());
		if (!equals(readCByKey, c))
			fail("Error: ReadByKey() deberia retornar una entidad identica");

		// Test ReadAllByPelicula
		ArrayList<TPase> readByPeliculaList = dao.ReadAllByPelicula(a.GetIDPelicula());
		boolean aFound = false, bFound = false, cFound = false;
		for (TPase p : readByPeliculaList) {
			if (equals(p, a))
				aFound = true;
			else if (equals(p, b))
				bFound = true;
		}
		if (!aFound || !bFound)
			fail("Error: ReadAllByPelicula() deberia contener entidad A y entidad B");

		// Test ReadAllBySala
		ArrayList<TPase> readBySalaList = dao.ReadAllBySala(b.GetIDSala());
		bFound = cFound = false;
		for (TPase p : readBySalaList) {
			if (equals(p, b))
				bFound = true;
			else if (equals(p, c))
				cFound = true;
		}
		if (!bFound || !cFound)
			fail("Error: ReadAllBySala() deberia contener entidad B y entidad C");

		// Test ReadAll
		ArrayList<TPase> readAll = dao.ReadAll();
		aFound = bFound = cFound = false;
		for (TPase p : readAll) {
			if (equals(p, a))
				aFound = true;
			else if (equals(p, b))
				bFound = true;
			else if (equals(p, c))
				cFound = true;
		}
		if (!aFound || !bFound || !cFound)
			fail("Error: ReadAllBySala() deberia contener entidad B y entidad C");

		t.commit();

		t = crearTransaccion();
		t.start();

		// Test Modificar (modificar horario)
		TPase modA = new TPase(new Date(time + 100000000), 1, 1, 1);
		modA.SetID(idA);
		modA.SetAsientosLibres(50);
		dao.Update(modA);

		t.commit();

		t = crearTransaccion();
		t.start();
		readA = dao.Read(idA);
		if (equals(readA, modA))
			fail("Error: Update() deberia retornar la entidad modificada");

		// Test Delete
		dao.Delete(idA);
		dao.Delete(idB);
		dao.Delete(idC);

		t.commit();

		t = crearTransaccion();
		t.start();

		readA = dao.Read(idA);
		readB = dao.Read(idB);
		readC = dao.Read(idC);

		t.commit();

		if (readA.GetActivo())
			fail("Error: Delete() no desactiva la entidad A");
		if (readB.GetActivo())
			fail("Error: Delete() no desactiva la entidad B");
		if (readC.GetActivo())
			fail("Error: Delete() no desactiva la entidad C");
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
