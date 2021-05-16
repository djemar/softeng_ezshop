package it.polito.ezshop.whiteBoxTests;
import java.io.IOException;
import java.io.StringReader;

import it.polito.ezshop.data.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestR2_Order {
	
	public void testGetSet() {
		  Order order = new OrderImpl("3546767564", 5.50 ,10);
		  assertEquals(order.getPricePerUnit(), 5.50, 0);
		  order.setPricePerUnit(6.00);
		  assertEquals(order.getPricePerUnit(), 6.00, 0);
		 }
}
