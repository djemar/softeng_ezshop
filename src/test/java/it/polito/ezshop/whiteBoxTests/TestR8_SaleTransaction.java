package it.polito.ezshop.whiteBoxTests;

import org.junit.Test;
import it.polito.ezshop.data.SaleTransactionImpl;
import static org.junit.Assert.assertEquals;
import org.junit.Before;

public class TestR8_SaleTransaction {
    SaleTransactionImpl saleTransaction;

    @Before
    public void setup() {
        saleTransaction = new SaleTransactionImpl(1, 0, 20, "OPEN");
    }


    @Test
    public void testGetSetTransactionId() {
        Integer id = 1;
        assertEquals(id, saleTransaction.getTicketNumber());
        id = 10;
        saleTransaction.setTicketNumber(id);
        assertEquals(id, saleTransaction.getTicketNumber());
    }

    @Test
    public void testGetSetStatus() {
        String status = "OPEN";
        assertEquals(status, saleTransaction.getStatus());
        status = "CLOSED";
        saleTransaction.setStatus(status);
        assertEquals(status, saleTransaction.getStatus());
    }

    @Test
    public void testGetSetPrice() {
        double price = 20;
        assertEquals(price, saleTransaction.getPrice(), 0.001);
        price = 10;
        saleTransaction.setPrice(price);
        assertEquals(price, saleTransaction.getPrice(), 0.001);
    }

    @Test
    public void testGetSetDiscountRate() {
        double discountRate = 0;
        assertEquals(discountRate, saleTransaction.getDiscountRate(), 0.001);
        discountRate = 0.5;
        saleTransaction.setDiscountRate(discountRate);
        assertEquals(discountRate, saleTransaction.getDiscountRate(), 0.001);
    }
}
