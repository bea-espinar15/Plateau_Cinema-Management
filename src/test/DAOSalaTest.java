package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

import Integracion.DAOFactory.DAOFactory;
import Integracion.Sala.DAOSalaImp;
import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;
import Negocio.Sala.TSala;
import Negocio.Sala.TSalaEspecial;
import Negocio.Sala.TSalaEstandar;

public class DAOSalaTest {

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
	public void test() {
		DAOSalaImp dao = (DAOSalaImp) DAOFactory.getInstance().getDAOSala();
		Random random = new Random();

		TSalaEspecial a = new TSalaEspecial();
		a.SetNombre("Sala especial " + random.nextInt());
		a.SetAforo(50);
		a.Set3D(false);
		a.SetVO(true);
		TSalaEstandar b = new TSalaEstandar();
		b.SetNombre("Sala estandar " + random.nextInt());
		b.SetAforo(30);
		b.SetAseos(true);

		Transaction t;

		t = crearTransaccion();
		t.start();

		// Test - Create()
		Integer idA = dao.Create(a);
		Integer idB = dao.Create(b);
		if (idA <= 0 || idB <= 0)
			fail("Error: Create() deberia retornar IDs positivos");

		a.SetId(idA);
		a.SetActivo(true);
		b.SetId(idB);
		b.SetActivo(true);

		t.commit();

		t = crearTransaccion();
		t.start();
		// Test - Read (ByID)
		if (!equals(a, dao.Read(idA)) || !equals(b, dao.Read(idB)))
			fail("Error: Read() por ID deberia retornar una entidad identica");

		// Test - ReadByNombre
		if (!equals(a, dao.ReadByNombre(a.GetNombre())) || !equals(b, dao.ReadByNombre(b.GetNombre())))
			fail("Error: ReadByNombre() deberia retornar una entidad identica");

		// Test - ReadAll()
		boolean aFound = false, bFound = false;
		ArrayList<TSala> allList = dao.ReadAll();
		for (TSala s : allList) {
			if (equals(s, a))
				aFound = true;
			else if (equals(s, b))
				bFound = true;
		}
		if (!aFound || !bFound)
			fail("Error: ReadAll() deberia retornar las entidades creadas");

		// Test - ReadAllEspecial()
		aFound = false;
		ArrayList<TSalaEspecial> allEspecialList = dao.ReadAllEspecial();
		for (TSala s : allEspecialList) {
			if (equals(s, a))
				aFound = true;
		}
		if (!aFound)
			fail("Error: ReadAll() deberia retornar la entidades creadas");

		// Test - ReadAllEstandar()
		bFound = false;
		ArrayList<TSalaEstandar> allEstandarList = dao.ReadAllEstandar();
		for (TSala s : allEstandarList) {
			if (equals(s, b))
				bFound = true;
		}
		if (!bFound)
			fail("Error: ReadAll() deberia retornar la entidades creadas");

		// Test - Update() especial
		TSalaEspecial aMod = new TSalaEspecial();
		aMod.SetId(idA);
		aMod.SetNombre("Sala delux " + random.nextInt());
		aMod.SetAforo(69);
		aMod.Set3D(true);
		aMod.SetVO(false);
		aMod.SetActivo(true);

		if (dao.Update(aMod) < 0)
			fail("Error: Update() deberia retornar el id de la sala");

		// Test - Update() estandar
		TSalaEstandar bMod = new TSalaEstandar();
		bMod.SetId(idB);
		bMod.SetNombre("Sala estandar " + random.nextInt());
		bMod.SetAforo(31);
		bMod.SetAseos(false);
		bMod.SetActivo(true);

		if (dao.Update(bMod) < 0)
			fail("Error: Update() deberia retornar el id de la sala");

		t.commit();

		t = crearTransaccion();
		t.start();
		// Test - Read despues de update
		if (equals(a, dao.Read(idA)) || equals(b, dao.Read(idB)))
			fail("Error: Read() despues de modificar deberia retornar entidades distintas");

		// Test - Delete()
		if (dao.Delete(idA) != 0 || dao.Delete(idB) != 0)
			fail("Error: Delete() deberia retornar 0");

		t.commit();

		t = crearTransaccion();
		t.start();
		// Test - Read despues de delete
		TSala aRead = dao.Read(idA);
		TSala bRead = dao.Read(idB);

		if (aRead.GetActivo() || bRead.GetActivo())
			fail("Error: Delet() entidades deberian estar desactivadas");

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
