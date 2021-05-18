package it.polito.ezshop.EZTests;

import org.junit.Test;
import it.polito.ezshop.data.ReturnTransaction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
        returnTransaction = new ReturnTransaction(1, 5);
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
    public void testUpdateTotal() {
        double total = 20;
        returnTransaction.setTotal(total);
        returnTransaction.updateTotal(30);
        total = 50;
        assertEquals(total, returnTransaction.getTotal(), 0.001);
    }

    @Test
    public void testGetSetReturnedProductMap() {
        returnTransaction.setReturnedProductsMap(returnedProductsMap);
        assertEquals(returnedProductsMap, returnTransaction.getReturnedProductsMap());
    }

    @Test
    public void testAddProductToReturn() {
        returnTransaction.addProductToReturn("65164684113337", 1);
        assertTrue(returnTransaction.getReturnedProductsMap().containsKey("65164684113337"));
        assertEquals(1, returnTransaction.getReturnedProductsMap().get("65164684113337"), 0);
    }
}
