package it.polito.ezshop.EZTests;

import org.junit.Test;
import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.ReturnTransaction;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.data.TicketEntryImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.After;
import org.junit.Before;

public class TestR14_ReturnTransactionDb {
    EZShopDb ezshopDb = new EZShopDb();
    List<TicketEntry> list;
    ReturnTransaction rt1;
    HashMap<String, Integer> returnedProducts = new HashMap<String, Integer>();

    @Before
    public void setup() {
        list = new ArrayList<TicketEntry>();
        TicketEntry t1 = new TicketEntryImpl("12345678912237", "Test", 1, 10, 0);
        TicketEntry t2 = new TicketEntryImpl("2905911158926", "Test", 1, 10, 0);
        TicketEntry t3 = new TicketEntryImpl("65164684113337", "Test", 1, 10, 0);
        list.add(t1);
        list.add(t2);
        list.add(t3);
        rt1 = new ReturnTransaction(1, 2, "CLOSED", 20);

        returnedProducts.put("12345678912237", 1);
        returnedProducts.put("2905911158926", 2);
        rt1.setReturnedProductsMap(returnedProducts);
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.insertReturnTransaction(rt1);

        ezshopDb.closeConnection();
    }

    @After
    public void clean() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    }

    @Test
    public void testValidGetReturnTransaction() {
    	ReturnTransaction test;
        ezshopDb.createConnection();
        test = ezshopDb.getReturnTransaction(1);
        assertNotNull(test);
        assertFalse(test.getReturnedProductsMap().isEmpty());
        ezshopDb.closeConnection();
    }
    
    @Test
    public void testNullGetReturnTransaction() {
    	ReturnTransaction test;
        ezshopDb.createConnection();
        test = ezshopDb.getReturnTransaction(100);
        assertNull(test);
        ezshopDb.closeConnection();
    }
    
    @Test
    public void testInvalidGetReturnTransaction() {
    	ReturnTransaction test;
        test = ezshopDb.getReturnTransaction(100);
        assertNull(test);
    }

    @Test
    public void testValidDeleteReturnTransaction() {
        boolean test;
        ezshopDb.createConnection();
        test = ezshopDb.deleteReturnTransaction(1);
        assertTrue(test);
        ezshopDb.closeConnection();
    }

    @Test
    public void testInvalidDeleteReturnTransaction() {
        boolean test;
        // ezshopDb.createConnection();
        test = ezshopDb.deleteReturnTransaction(10);
        assertFalse(test);
        // ezshopDb.closeConnection();
    }

    @Test
    public void testValidNewReturnTransactionId() {
        Integer test;
        ezshopDb.createConnection();
        test = ezshopDb.newReturnTransactionId();
        assertNotEquals(-1, test, 0);
        ezshopDb.closeConnection();
    }

    @Test
    public void testInvalidNewReturnTransactionId() {
        Integer test;
        // ezshopDb.createConnection();
        test = ezshopDb.newReturnTransactionId();
        assertEquals(-1, test, 0);
        // ezshopDb.closeConnection();
    } 
    @Test
    public void testValidPayForReturnTransaction() {
        boolean test;
        ezshopDb.createConnection();
        test = ezshopDb.payForSaleTransaction(1);
        assertTrue(test);
        ezshopDb.closeConnection();
    }

    @Test
    public void testInvalidPayForReturnTransaction() {
        boolean test;
        // ezshopDb.createConnection();
        test = ezshopDb.payForSaleTransaction(10);
        assertFalse(test);
        // ezshopDb.closeConnection();
    }
}
