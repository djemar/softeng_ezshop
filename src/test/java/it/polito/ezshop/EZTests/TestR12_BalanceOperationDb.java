package it.polito.ezshop.EZTests;

import org.junit.Test;
import it.polito.ezshop.data.EZShopDb;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;

public class TestR12_BalanceOperationDb {
    EZShopDb ezshopDb = new EZShopDb();

    @Before
    public void setup() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    }

    @After
    public void clean() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    }

    @Test
    public void testValidRecordBalanceUpdate() {
        boolean test;
        ezshopDb.createConnection();
        test = ezshopDb.recordBalanceUpdate(10d);
        assertTrue(test);
        ezshopDb.closeConnection();
    }

    @Test
    public void testInvalidRecordBalanceUpdate() {
        boolean test;
        // ezshopDb.createConnection();
        test = ezshopDb.recordBalanceUpdate(10d);
        assertFalse(test);
        // ezshopDb.closeConnection();
    }
}
