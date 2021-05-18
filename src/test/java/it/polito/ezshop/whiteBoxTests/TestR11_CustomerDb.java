package it.polito.ezshop.whiteBoxTests;
import org.junit.Test;

import it.polito.ezshop.data.CustomerImpl;
import it.polito.ezshop.data.EZShopDb;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;

public class TestR11_CustomerDb {
	
    EZShopDb ezshopDb = new EZShopDb();
    CustomerImpl st1;
	Integer id;

    @Before
    public void setup() { 
    	
        st1 = new CustomerImpl("Elisa");
        ezshopDb.createConnection();
        ezshopDb.resetDB();
    	id = ezshopDb.insertCustomer(st1);
        ezshopDb.closeConnection();
    }

    @After
    public void clean() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    }
	
	/*@Test
	public void testInsertCard() {
		ezshopDb.createConnection();
		assertTrue(ezshopDb.insertCustomerCard("34563487960"));
		ezshopDb.closeConnection();
	}*/

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
	

	
	
	
	
	
	
}	
	
	
	