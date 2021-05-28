package it.polito.ezshop.EZTests;

import org.junit.Test;

import it.polito.ezshop.data.CustomerImpl;
import it.polito.ezshop.data.EZShopDb;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;

public class TestR11_CustomerDb {

	EZShopDb ezshopDb = new EZShopDb();
	CustomerImpl c1;
	Integer id;

	@Before
	public void setup() {

		c1 = new CustomerImpl("Elisa");
		ezshopDb.createConnection();
		ezshopDb.resetDB();
		id = ezshopDb.insertCustomer(c1);
		ezshopDb.closeConnection();
	}

	@After
	public void clean() {
		ezshopDb.createConnection();
		ezshopDb.resetDB();
		ezshopDb.closeConnection();
	}

	/*
	 * @Test public void testInsertCard() { ezshopDb.createConnection();
	 * assertTrue(ezshopDb.insertCustomerCard("34563487960")); ezshopDb.closeConnection(); }
	 */

	@Test
	public void testInvalidInsertCard() {
		assertFalse(ezshopDb.insertCustomerCard("3453487960"));
	}

	@Test
	public void testUpdateCustomer() {
		ezshopDb.createConnection();
		assertTrue(ezshopDb.updateCustomer(id, "Eli", "34563487960", 50));
		ezshopDb.closeConnection();
	}

	@Test
	public void testInvalidUpdateCustomer() {
		assertFalse(ezshopDb.updateCustomer(id, "Eli", "34563487960", 50));
	}


	@Test
	public void testCustomerCard() {
		ezshopDb.createConnection();
		assertTrue(ezshopDb.insertCustomerCard("34563487960"));
		assertTrue(ezshopDb.getCustomerCard("34563487960"));
		ezshopDb.closeConnection();
	}

	@Test
	public void testInvalidGetCustomerCard() {
		assertFalse(ezshopDb.getCustomerCard("3563487960"));
	}

	@Test
	public void testValidGetCustomerCardNumber() {
		int test = 0;
		ezshopDb.createConnection();
		ezshopDb.insertCustomerCard("34563487960");
		assertNotEquals(test, ezshopDb.getCustomerCardNumber());
		ezshopDb.closeConnection();
	}

	@Test
	public void testInvalidGetCustomerCardNumber() {
		int test = 0;
		assertEquals(test, ezshopDb.getCustomerCardNumber());
	}

	@Test
	public void testValidGetCustomer() {
		int id = 0;
		ezshopDb.createConnection();
		id = ezshopDb.insertCustomer(c1);
		assertNotNull(ezshopDb.getCustomer(id));
		ezshopDb.closeConnection();
	}

	@Test
	public void testInvalidGetCustomer() {
		assertNull(ezshopDb.getCustomer(-1));
	}

	@Test
	public void testValidAttachCard() {
		ezshopDb.createConnection();
		ezshopDb.insertCustomerCard("34563487960");
		assertTrue(ezshopDb.attachCardToCustomer("34563487960", id));
		ezshopDb.closeConnection();
	}

	@Test
	public void testInvalidAttachCard() {
		ezshopDb.createConnection();
		ezshopDb.attachCardToCustomer("3453487960", id);
		assertFalse(ezshopDb.attachCardToCustomer("3453487960", id));
		ezshopDb.closeConnection();
	}

	@Test
	public void testValidGetAllCustomers() {
		ezshopDb.createConnection();
		assertFalse(ezshopDb.getAllCustomers().isEmpty());
		ezshopDb.closeConnection();
	}

	@Test
	public void testInvalidGetAllCustomers() {
		assertTrue(ezshopDb.getAllCustomers().isEmpty());
	}

	@Test
	public void testValidDeleteCustomer() {
		ezshopDb.createConnection();
		assertTrue(ezshopDb.deleteCustomer(ezshopDb.getCustomer(id)));
		ezshopDb.closeConnection();
	}

	@Test
	public void testInvalidDeleteCustomer() {
		assertFalse(ezshopDb.deleteCustomer(c1));
	}

	@Test
	public void testValidGetCustomerByCard() {
		ezshopDb.createConnection();
		ezshopDb.insertCustomerCard("3453487960");
		ezshopDb.attachCardToCustomer("3453487960", id);
		assertNotNull(ezshopDb.getCustomerByCard("3453487960"));
		ezshopDb.closeConnection();
	}

	@Test
	public void testInvalidGetCustomerByCard() {
		assertNull(ezshopDb.getCustomerByCard("null"));
		ezshopDb.createConnection();
		assertNull(ezshopDb.getCustomerByCard("126846484"));
		ezshopDb.closeConnection();
	}



}


