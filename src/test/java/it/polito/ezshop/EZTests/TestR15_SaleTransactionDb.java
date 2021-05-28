package it.polito.ezshop.EZTests;

import org.junit.Test;
import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.SaleTransactionImpl;
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

public class TestR15_SaleTransactionDb {
    EZShopDb ezshopDb = new EZShopDb();
    List<TicketEntry> list;
    SaleTransactionImpl st1;
    HashMap<String, Integer> returnedProducts = new HashMap<String, Integer>();

    @Before
    public void setup() {
        list = new ArrayList<TicketEntry>();
        TicketEntry t1 = new TicketEntryImpl("12345678912237", "Test", 1, 10, 0);
        TicketEntry t2 = new TicketEntryImpl("2905911158926", "Test", 1, 10, 0);
        list.add(t1);
        list.add(t2);
        st1 = new SaleTransactionImpl(1, 0.5, 20);
        st1.setEntries(list);
        returnedProducts.put("12345678912237", 1);
        returnedProducts.put("2905911158926", 2);

        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.insertSaleTransaction(st1);
        ezshopDb.closeConnection();
    }

    @After
    public void clean() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    }
    
    @Test
    public void testValidGetSaleTransaction() {
        SaleTransactionImpl test;
        ezshopDb.createConnection();
        test = ezshopDb.getSaleTransaction(1);
        assertNotNull(test);
        ezshopDb.closeConnection();
    }
    
    @Test
    public void testNullGetSaleTransaction() {
        SaleTransactionImpl test;
        ezshopDb.createConnection();
        test = ezshopDb.getSaleTransaction(100);
        assertNull(test);
        ezshopDb.closeConnection();
    }
    
    @Test
    public void testInvalidGetSaleTransaction() {
        SaleTransactionImpl test;
        //ezshopDb.createConnection();
        test = ezshopDb.getSaleTransaction(1);
        assertNull(test);
        //ezshopDb.closeConnection();
    }

    @Test
    public void testValidUpdateSaleTransaction() {
        boolean test;
        ezshopDb.createConnection();
        test = ezshopDb.updateSaleTransaction(1, returnedProducts, 1, true);
        assertTrue(test);
        test = ezshopDb.updateSaleTransaction(1, returnedProducts, 1, false);
        assertTrue(test);
        ezshopDb.closeConnection();
    }

    @Test
    public void testInvalidUpdateSaleTransaction() {
        boolean test;
        // ezshopDb.createConnection();
        test = ezshopDb.updateSaleTransaction(10, returnedProducts, 1, true);
        assertFalse(test);
        // ezshopDb.closeConnection();
    }

    @Test
    public void testValidDeleteSaleTransaction() {
        boolean test;
        ezshopDb.createConnection();
        test = ezshopDb.deleteSaleTransaction(1);
        assertTrue(test);
        ezshopDb.closeConnection();
    }

    @Test
    public void testInvalidDeleteSaleTransaction() {
        boolean test;
        // ezshopDb.createConnection();
        test = ezshopDb.deleteSaleTransaction(10);
        assertFalse(test);
        // ezshopDb.closeConnection();
    }

    @Test
    public void testValidPayForSaleTransaction() {
        boolean test;
        ezshopDb.createConnection();
        test = ezshopDb.payForSaleTransaction(1);
        assertTrue(test);
        ezshopDb.closeConnection();
    }

    @Test
    public void testInvalidPayForSaleTransaction() {
        boolean test;
        // ezshopDb.createConnection();
        test = ezshopDb.payForSaleTransaction(10);
        assertFalse(test);
        // ezshopDb.closeConnection();
    }

    @Test
    public void testValidSetSaleDiscount() {
        boolean test;
        ezshopDb.createConnection();
        test = ezshopDb.setSaleDiscount(1, 0.5);
        assertTrue(test);
        ezshopDb.closeConnection();
    }

    @Test
    public void testInvalidSetSaleDiscount() {
        boolean test;
        // ezshopDb.createConnection();
        test = ezshopDb.setSaleDiscount(10, 1d);
        assertFalse(test);
        // ezshopDb.closeConnection();
    }

    @Test
    public void testValidNewSaleTransactionId() {
        Integer test;
        ezshopDb.createConnection();
        test = ezshopDb.SaleTransactionNumber();
        assertNotEquals(0, test, 0);
        ezshopDb.closeConnection();
    }

    @Test
    public void testInvalidNewSaleTransactionId() {
        Integer test;
        // ezshopDb.createConnection();
        test = ezshopDb.SaleTransactionNumber();
        assertEquals(0, test, 0);
        // ezshopDb.closeConnection();
    }

}
