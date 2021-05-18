package it.polito.ezshop.whiteBoxTests;
import org.junit.Test;

import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.OrderImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import it.polito.ezshop.data.EZShopDb;
import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;

public class TestR9_OrderDb {
	
    EZShopDb ezshopDb = new EZShopDb();
    OrderImpl st1;
	Integer id;

    @Before
    public void setup() {
        st1 = new OrderImpl("56789342",10.5, 50);
        ezshopDb.createConnection();
        ezshopDb.resetDB();
    	id = ezshopDb.insertOrder(st1);
        ezshopDb.closeConnection();
    }

    @After
    public void clean() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    }

    @Test
    public void testUpdateOrder() {
        ezshopDb.createConnection();
    	assertTrue(ezshopDb.updateOrder(id, "PAYED", 5));
        ezshopDb.closeConnection();
    	assertFalse(ezshopDb.updateOrder(id, "PAYED", 5));
    }
    
   @Test
    public void testInvalidUpdateOrder() {
    	assertFalse(ezshopDb.updateOrder(id, "PAYED", 5));
    }

}
 
