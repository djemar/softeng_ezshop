package it.polito.ezshop.EZTests;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;

import it.polito.ezshop.data.*;

import static org.junit.Assert.*;
import org.junit.Test;
public class TestR5_TicketEntry {
	@Test
	public void testGetSetBarCode() {
		  TicketEntry ticketentry = new TicketEntryImpl("3546767564", "chocolate", 20, 10.5, 0.5);
		  assertEquals(ticketentry.getBarCode(),"3546767564");
		  ticketentry.setBarCode("2456574");
		  assertEquals(ticketentry.getBarCode(),"2456574");
	}
	@Test
	public void testGetSetPrice() {
		  TicketEntry ticketentry = new TicketEntryImpl("3546767564", "chocolate", 20, 10.50, 0.5);
		  assertEquals(ticketentry.getPricePerUnit(), 10.50, 0);
		  ticketentry.setPricePerUnit(6.00);
		  assertEquals(ticketentry.getPricePerUnit(), 6.00, 0);
	}
	@Test
	public void testGetSetQuantity() {
		 TicketEntry ticketentry = new TicketEntryImpl("3546767564", "chocolate", 100, 10.50, 0.5);
		  assertEquals(ticketentry.getAmount(), 100);
		  ticketentry.setAmount(20);
		  assertEquals(ticketentry.getAmount(), 20);
	}
	@Test
	public void testGetSetProductDescription() {
		 TicketEntry ticketentry = new TicketEntryImpl("3546767564", "chocolate", 100, 10.50, 0.5);
		  assertEquals(ticketentry.getProductDescription(), "chocolate");
		  ticketentry.setProductDescription("cheese");
		  assertEquals(ticketentry.getProductDescription(), "cheese");
	}
	@Test
	public void testGetSetDiscountRate() {
		 TicketEntry ticketentry = new TicketEntryImpl("3546767564", "chocolate", 100, 10.50, 0.5);
		  assertEquals(ticketentry.getDiscountRate(), 0.5, 0);
		  ticketentry.setDiscountRate(0.4);
		  assertEquals(ticketentry.getDiscountRate(), 0.4, 0);
	}


}
