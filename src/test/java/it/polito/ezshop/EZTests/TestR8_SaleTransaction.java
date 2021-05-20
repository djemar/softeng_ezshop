package it.polito.ezshop.EZTests;

import org.junit.Test;
import it.polito.ezshop.data.SaleTransactionImpl;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.data.TicketEntryImpl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;

public class TestR8_SaleTransaction {
    SaleTransactionImpl saleTransaction;

    @Before
    public void setup() {
        saleTransaction = new SaleTransactionImpl(1, 0, 20, "OPEN");
    }


    @Test
    public void testGetSetTransactionId() {
    	saleTransaction=new SaleTransactionImpl(3);
        Integer id = 3;
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
    	saleTransaction=new SaleTransactionImpl(3,0,20);
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
    @Test
    public void testSetGetEntries() {
    	TicketEntry t1= new TicketEntryImpl("1983749573", "description 1", 1, 1, 0);
    	TicketEntry t2= new TicketEntryImpl("1983749523", "description 2", 2, 1, 0);
    	TicketEntry t3= new TicketEntryImpl("1983743543", "description 3", 3, 1, 0);
    	ArrayList<TicketEntry> tlist= new ArrayList<TicketEntry>();
    	tlist.add(t1);
    	tlist.add(t2);
    	tlist.add(t3);
    	this.saleTransaction.setEntries(tlist);
    	assertEquals(tlist,this.saleTransaction.getEntries());
    }
    
    @Test
    public void testEstimatePrice() {
    	this.saleTransaction.estimatePrice();
    	assertEquals(0,this.saleTransaction.getPrice(),0);
    	TicketEntry t1= new TicketEntryImpl("1983749573", "description 1", 1, 1, 0);
    	TicketEntry t2= new TicketEntryImpl("1983749523", "description 2", 2, 1, 0);
    	TicketEntry t3= new TicketEntryImpl("1983743543", "description 3", 3, 1, 0);
    	ArrayList<TicketEntry> tlist= new ArrayList<TicketEntry>();
    	tlist.add(t1);
    	tlist.add(t2);
    	tlist.add(t3);
    	this.saleTransaction.setEntries(tlist);
    	this.saleTransaction.estimatePrice();
    	assertEquals(6.0,this.saleTransaction.getPrice(),0);
    	
    }

}
