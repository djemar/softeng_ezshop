package it.polito.ezshop.EZTests;

import org.junit.Test;

import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.OrderImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;

public class TestR9_OrderDb {

    EZShopDb ezshopDb = new EZShopDb();
    OrderImpl o1, o2;
    Integer id;

    // delete from sqlite_sequence where name='your_table';

    @Before
    public void setup() {
        o1 = new OrderImpl("56789342", 10.5, 50);
        o2 = new OrderImpl("46546464", 2.5, 10);
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        id = ezshopDb.insertOrder(o1);
        ezshopDb.closeConnection();
    }

    @After
    public void clean() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    }

    @Test
    public void testValidInsertOrder() {
        Integer test = -1;
        ezshopDb.createConnection();
        assertNotEquals(test, ezshopDb.insertOrder(o2));
        ezshopDb.closeConnection();
    }

    @Test
    public void testInvalidInsertOrder() {
        Integer test = -1;
        assertEquals(test, ezshopDb.insertOrder(o2));
    }

    @Test
    public void testValidGetOrder() {
        ezshopDb.createConnection();
        Integer test = ezshopDb.insertOrder(o2);
        assertNotNull(ezshopDb.getOrder(test));
        ezshopDb.closeConnection();
    }

    @Test
    public void testInvalidGetOrder() {
        Integer test = -1;
        ezshopDb.createConnection();
        assertNull(ezshopDb.getOrder(test));
        ezshopDb.closeConnection();
    }

    @Test
    public void testValidGetAllOrders() {
        // TODO an empty list is still valid if the order table is empty
        ezshopDb.createConnection();
        assertFalse(ezshopDb.getAllOrders().isEmpty());
        ezshopDb.closeConnection();
    }

    @Test
    public void testInvalidGetAllOrders() {
        assertTrue(ezshopDb.getAllOrders().isEmpty());
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

