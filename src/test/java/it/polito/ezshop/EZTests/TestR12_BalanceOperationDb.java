package it.polito.ezshop.EZTests;

import org.junit.Test;

import it.polito.ezshop.data.BalanceOperationImpl;
import it.polito.ezshop.data.EZShopDb;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.Before;

public class TestR12_BalanceOperationDb {
    EZShopDb ezshopDb = new EZShopDb();
    BalanceOperationImpl balanceOp;

    @Before
    public void setup() {
        balanceOp = new BalanceOperationImpl(1, LocalDate.now(), 10.5, "CREDIT");
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.insertBalanceOperation(balanceOp);
        ezshopDb.closeConnection();
    }

    @After
    public void clean() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    }

    @Test
    public void testValidInsertBalanceOperation() {
        int test = -1;
        ezshopDb.createConnection();
        assertNotEquals(test, ezshopDb.insertBalanceOperation(balanceOp));
        ezshopDb.closeConnection();
    }

    @Test
    public void testInvalidInsertBalanceOperation() {
        int test = -1;
        assertEquals(test, ezshopDb.insertBalanceOperation(balanceOp));
    }

    @Test
    public void testValidGetBalance() {
        ezshopDb.createConnection();
        assertNotNull(ezshopDb.getBalance());
        ezshopDb.closeConnection();
    }

    @Test
    public void testInvalidGetBalance() {
        assertNotNull(ezshopDb.getBalance());
    }

    @Test
    public void testValidRecordBalanceUpdate() {
        boolean test;
        ezshopDb.createConnection();
        test = ezshopDb.recordBalanceUpdate(10);
        assertTrue(test);
        test = ezshopDb.recordBalanceUpdate(-100);
        assertTrue(test);
        ezshopDb.closeConnection();
    }

    @Test
    public void testInvalidRecordBalanceUpdate() {
        boolean test;
        // ezshopDb.createConnection();
        test = ezshopDb.recordBalanceUpdate(10);
        assertFalse(test);
        // ezshopDb.closeConnection();
    }

    @Test
    public void testgetFromAllBalanceOperations() {
        ezshopDb.createConnection();
        assertNotNull(ezshopDb.getAllBalanceOperations(LocalDate.now(), null));
        assertTrue(ezshopDb.getAllBalanceOperations(LocalDate.of(2022, 10, 15), null).isEmpty());
        ezshopDb.closeConnection();
    }

    @Test
    public void testgetToAllBalanceOperations() {
        ezshopDb.createConnection();
        assertNotNull(ezshopDb.getAllBalanceOperations(null, LocalDate.now()));
        ezshopDb.closeConnection();
    }

    @Test
    public void testgetFromToAllBalanceOperations() {
        ezshopDb.createConnection();
        assertNotNull(
                ezshopDb.getAllBalanceOperations(LocalDate.now(), LocalDate.of(2022, 10, 15)));
        ezshopDb.closeConnection();
    }

    @Test
    public void testgetAllBalanceOperations() {
        ezshopDb.createConnection();
        assertNotNull(ezshopDb.getAllBalanceOperations(null, null));
        ezshopDb.closeConnection();
    }

    @Test
    public void testInvalidgetAllBalanceOperations() {
        boolean test;
        test = ezshopDb.getAllBalanceOperations(LocalDate.of(2022, 10, 15), null).isEmpty();
        assertTrue(test);
    }
}
