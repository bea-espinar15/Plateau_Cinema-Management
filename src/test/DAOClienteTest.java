package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import Integracion.Cliente.DAOCliente;
import Integracion.DAOFactory.DAOFactory;
import Integracion.Transactions.Transaction;
import Integracion.Transactions.TransactionManager;
import Negocio.Cliente.TCliente;
import Negocio.Cliente.TClienteNormal;
import Negocio.Cliente.TClienteVip;

public class DAOClienteTest {

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
	
	private boolean equals(TCliente a, TCliente b) {
		return a.GetID().equals(b.GetID()) && a.GetDNI().equals(b.GetDNI()) && a.GetNombre().equals(b.GetNombre())
				&& a.GetCorreo().equals(b.GetCorreo()) && a.GetActivo().equals(b.GetActivo());
	}

	private boolean equals(TClienteNormal a, TClienteNormal b) {
		return equals((TCliente) a, (TCliente) b) && a.GetFacturacion().equals(b.GetFacturacion());
	}
	
	private boolean equals(TClienteVip a, TClienteVip b) {
		return equals((TCliente) a, (TCliente) b) && a.GetDescuento() == b.GetDescuento() && a.GetAntiguedad() == b.GetAntiguedad();
	}

	@Test
	public void test() {
		DAOCliente dao = DAOFactory.getInstance().getDAOCliente();
		Transaction t;

		t = crearTransaccion();
		t.start();
		
		// Test - Create()
		// TClienteNormal(id, dni, nombre, correo, facturacion, activo)
		TClienteNormal cn = new TClienteNormal(null, getAlphaNumericString(8), "Ruben", "ruben.ruben@hotmail.com", 123, true);
		// TClienteVip(id, dni, nombre, correo, descuento, antiguedad, activo)
		TClienteVip cv = new TClienteVip(null, getAlphaNumericString(8), "Sofia", "sofia.sofia@gmail.com", 20, 1, true);
		
		Integer idN = dao.Create(cn);
		if (idN < 0)
			fail("No se ha podido crear el cliente normal");
		Integer idV = dao.Create(cv);
		if (idV < 0)
			fail("No se ha podido crear el cliente vip");
		cn.SetID(idN);
		cv.SetID(idV);
		t.commit();
		
		cn.SetID(idN);
		cn.SetActivo(true);
		
		cv.SetID(idV);
		cv.SetActivo(true);

		// Test Read
		t = crearTransaccion();
		t.start();
		TClienteNormal cnTest1 = (TClienteNormal) dao.Read(cn.GetID());
		TClienteVip cvTest1 = (TClienteVip) dao.Read(cv.GetID());
		if (!equals(cn, cnTest1))
			fail("Error: Read() deberia retornar una entidad identica");
		if (!equals(cv, cvTest1))
			fail("Error: Read() deberia retornar una entidad identica");
		t.commit();

		// Test ReadByDni
		t = crearTransaccion();
		t.start();
		TClienteNormal cnTest3 = (TClienteNormal) dao.ReadByDNI(cn.GetDNI());
		TClienteVip cvTest3 = (TClienteVip) dao.ReadByDNI(cv.GetDNI());
		if (!equals(cn, cnTest3))
			fail("Error: ReadByDNI() deberia retornar una entidad identica");
		if (!equals(cv, cvTest3))
			fail("Error: ReadByDNI() deberia retornar una entidad identica");
		t.commit();

		// Tests ReadAll...
		boolean cnFound, cvFound;
		// Test ReadAll
		t = crearTransaccion();
		t.start();
		ArrayList<TCliente> readAll = dao.ReadAll();
		cnFound = cvFound = false;
		for (TCliente c : readAll) {
			if (c instanceof TClienteNormal) {
				if (equals(c, cn))
					cnFound = true;
			} else {
				if (equals(c, cv))
					cvFound = true;
			}
		}
		if (!cnFound || !cvFound)
			fail("Error: ReadAll() deberia contener entidad CN y entidad CV");
		t.commit();

		// Test ReadAllNormal
		t = crearTransaccion();
		t.start();
		ArrayList<TClienteNormal> readAllNormal = dao.readAllNormal();
		cnFound = false;
		for (TClienteNormal c : readAllNormal) {
			if (equals(c, cn))
				cnFound = true;
		}
		if (!cnFound)
			fail("Error: ReadAllNormal() deberia contener entidad CN");
		t.commit();

		// Test ReadAllVip
		t = crearTransaccion();
		t.start();
		ArrayList<TClienteVip> readAllVip = dao.ReadAllVIP();
		cvFound = false;
		for (TClienteVip c : readAllVip) {
			if (equals(c, cv))
				cvFound = true;
		}
		if (!cvFound)
			fail("Error: ReadAllVip() deberia contener entidad CV");
		t.commit();

		// Test Update
		// TClienteNormal(id, dni, nombre, correo, facturacion, activo)
		TClienteNormal cnMod = new TClienteNormal(cn.GetID(), getAlphaNumericString(8), "Ruben",
				"ruben.ruben@hotmail.com", 123, true);
		// TClienteVip(id, dni, nombre, correo, descuento, antiguedad, activo)
		TClienteVip cvMod = new TClienteVip(cv.GetID(), getAlphaNumericString(8), "Sofia", "sofia.sofia@gmail.com", 20,
				1, true);
		dao.Update(cnMod);
		dao.Update(cvMod);
		if (equals(cnMod, cn))
			fail("Error: Update() deberia modificar el DNI");
		if (equals(cvMod, cv))
			fail("Error: Update() deberia modificar el DNI");

		// Test Delete
		t = crearTransaccion();
		t.start();
		dao.Delete(cn.GetID());
		dao.Delete(cv.GetID());

		TClienteNormal cnTest2 = (TClienteNormal) dao.Read(cn.GetID());
		TClienteVip cvTest2 = (TClienteVip) dao.Read(cv.GetID());

		if (cnTest2.GetActivo())
			fail("Error: Delete() no desactiva el cliente normal");
		if (cvTest2.GetActivo())
			fail("Error: Delete() no desactiva el cliente vip");
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
