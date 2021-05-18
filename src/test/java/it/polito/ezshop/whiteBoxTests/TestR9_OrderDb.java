package it.polito.ezshop.whiteBoxTests;
import org.junit.Test;

import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.OrderImpl;
import it.polito.ezshop.data.SaleTransactionImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Before;

public class TestR9_OrderDb {
	EZShopDb ezshopDb = new EZShopDb();
		  
		  @Test
		  public void testUpdateOrd() {
		    ezshopDb.createConnection();	    
			Integer id = ezshopDb.insertOrder(new OrderImpl("56789342",10.5, 50));
			assertTrue(ezshopDb.updateOrder(id, "PAYED", 5));
			ezshopDb.closeConnection();
		  }
		  
}
 
