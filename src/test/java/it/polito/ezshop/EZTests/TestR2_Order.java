package it.polito.ezshop.EZTests;
import java.io.IOException;
import java.io.StringReader;

import it.polito.ezshop.data.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestR2_Order {
	@Test
	public void testGetSetProductCode() {
		  Order order = new OrderImpl("3546767564", 5.50,10);
		  assertEquals(order.getProductCode(),"3546767564");
		  order.setProductCode("2456574");
		  assertEquals(order.getProductCode(),"2456574");
	}
	@Test
	public void testGetSetPrice() {
		  Order order = new OrderImpl("3546767564", 5.50, 10);
		  assertEquals(order.getPricePerUnit(), 5.50, 0);
		  order.setPricePerUnit(6.00);
		  assertEquals(order.getPricePerUnit(), 6.00, 0);
	}
	@Test
	public void testGetSetQuantity() {
		  Order order = new OrderImpl("3546767564", 5.50, 10);
		  assertEquals(order.getQuantity(), 10);
		  order.setQuantity(20);
		  assertEquals(order.getQuantity(), 20);
	}
	@Test
	public void testGetSetStatus() {
		  Order order = new OrderImpl("3546767564", 5.50, 10, "PAYED", 3);
		  assertEquals(order.getStatus(), "PAYED");
		  order.setStatus("COMPLETED");
		  assertEquals(order.getStatus(), "COMPLETED");
	}
	@Test
	public void testGetSetBalanceID() {
		  Order order = new OrderImpl("3546767564", 5.50, 10, "PAYED", 3);
		  assertEquals(order.getBalanceId(), 3, 0);
		  order.setBalanceId(4);
		  assertEquals(order.getBalanceId(), 4, 0);
	}
	@Test
	public void testGetSetOrderID() {
		  Order order = new OrderImpl(4, "3546767564", 5.50, 10, "PAYED", 3);
		  assertEquals(order.getOrderId(), 4, 0);
		  order.setOrderId(5);
		  assertEquals(order.getOrderId(), 5, 0);
	}
	
}
