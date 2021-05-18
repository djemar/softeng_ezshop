package it.polito.ezshop.whiteBoxTests;
import org.junit.Test;

import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.ProductTypeImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;

public class TestR16_ProductTypeDb {
	
    EZShopDb ezshopDb = new EZShopDb();
    ProductTypeImpl st1;
	Integer id;

    @Before
    public void setup() {
        st1 = new ProductTypeImpl("chocolate", "341254654", 5, "white");
        ezshopDb.createConnection();
        ezshopDb.resetDB();
    	id = ezshopDb.insertProductType(st1);
        ezshopDb.closeConnection();
    }

    @After
    public void clean() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    }

	@Test
	public void testUpdateProd() {
		ezshopDb.createConnection();
		assertTrue(ezshopDb.updateProductType(id, "cake", "32563252", 56, ""));
		ezshopDb.closeConnection();
	}
	
	@Test
	public void testInvalidUpdateProd() {
		assertFalse(ezshopDb.updateProductType(id, "cake", "32563252", 56, ""));
	}
	
	@Test
	public void testUpdateQuant() {
		ezshopDb.createConnection();
		assertTrue(ezshopDb.updateQuantity(id, 1));
		assertFalse(ezshopDb.updateQuantity(id, -50));
		ezshopDb.closeConnection();
	}
	@Test
	public void testInvalidUpdateQuant() {
		assertFalse(ezshopDb.updateQuantity(id, 50));
	}
	
	@Test
	public void testUpdatePosition() {
		ezshopDb.createConnection();
		assertTrue(ezshopDb.updatePosition(id, "48-gh-324"));
		ezshopDb.closeConnection();
	}
	@Test
	public void testInvalidUpdatePosition() {
		assertFalse(ezshopDb.updatePosition(id, "48-gh-324"));
	}
	
	@Test
	public void testExistingPosition() {
		ezshopDb.createConnection();
		assertTrue(ezshopDb.checkExistingPosition("48-gh-324"));
		assertFalse(ezshopDb.checkExistingPosition("489-gh-324"));
		ezshopDb.closeConnection();
	}
	@Test
	public void testInvalidExistingPosition() {
		assertFalse(ezshopDb.checkExistingPosition("48-gh-324"));
	}
	
	@Test
	public void testDeleteProd() {
		ezshopDb.createConnection();
		assertTrue(ezshopDb.deleteProductType(id));
		ezshopDb.closeConnection();
		assertFalse(ezshopDb.deleteProductType(id));
	}
	
	@Test
	public void testInvalidDeleteProd() {
		assertFalse(ezshopDb.deleteProductType(id));
	}
	
	
	



}


