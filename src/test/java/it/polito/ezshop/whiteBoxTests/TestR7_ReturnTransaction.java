package it.polito.ezshop.whiteBoxTests;

import org.junit.Test;
import it.polito.ezshop.data.ReturnTransaction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.HashMap;
import org.junit.Before;

public class TestR7_ReturnTransaction {
    ReturnTransaction returnTransaction;
    HashMap<String, Integer> returnedProductsMap = new HashMap<String, Integer>();

    @Before
    public void setup() {
        returnTransaction = new ReturnTransaction(1, 5, "OPEN", 20);
        returnedProductsMap.put("65164684113337", 2);
        returnedProductsMap.put("2905911158926", 7);
    }

    @Test
    public void testGetSetReturnId() {
        Integer id = 1;
        assertEquals(id, returnTransaction.getReturnId());
        id = 2;
        returnTransaction.setReturnId(id);
        assertEquals(id, returnTransaction.getReturnId());
    }

    @Test
    public void testGetSetTransactionId() {
        Integer id = 5;
        assertEquals(id, returnTransaction.getTransactionId());
        id = 10;
        returnTransaction.setTransactionId(id);
        assertEquals(id, returnTransaction.getTransactionId());
    }

    @Test
    public void testGetSetStatus() {
        String status = "OPEN";
        assertEquals(status, returnTransaction.getStatus());
        status = "CLOSED";
        returnTransaction.setStatus(status);
        assertEquals(status, returnTransaction.getStatus());
    }

    @Test
    public void testGetSetTotal() {
        double total = 20;
        assertEquals(total, returnTransaction.getTotal(), 0.001);
        total = 10;
        returnTransaction.setTotal(total);
        assertEquals(total, returnTransaction.getTotal(), 0.001);
    }

    @Test
    public void testSetGetReturnedMap() {
        returnTransaction.setReturnedProductsMap(returnedProductsMap);
        assertEquals(returnedProductsMap, returnTransaction.getReturnedProductsMap());
    }
}
