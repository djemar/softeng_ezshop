package it.polito.ezshop.EZTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.ProductTypeImpl;
import it.polito.ezshop.data.UserImpl;

public class TestWBDB {
    EZShopDb ezshopDb = new EZShopDb();
  Integer id_u;
  UserImpl user1;
  ProductTypeImpl st1;
  Integer id_p;
  @Before
    public void setup() {
        user1 = new UserImpl("Eli","USA","Administrator",1);
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        id_u = ezshopDb.insertUser(user1);
        ezshopDb.closeConnection();
        
        st1 = new ProductTypeImpl("chocolate", "341254654", 5, "white");
        ezshopDb.createConnection();
        ezshopDb.resetDB();
    	id_p = ezshopDb.insertProductType(st1);
		st1.setId(id_p);
        ezshopDb.closeConnection();
    }

    @After
    public void clean() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    } 
    @Test
    public void testInvalidDeleteUser() {
      assertFalse(ezshopDb.deleteUser(id_u));    
    }
    @Test
    public void testInvalidInsertProductType() {
    	ProductTypeImpl st2 = new ProductTypeImpl("milk", "341254645", 5, "white");
    	assertEquals(ezshopDb.insertProductType(st2), -1, 0);    
    }
    @Test
    public void testInvalidGetProductTypesByDescription() {
    	
    	assertFalse(ezshopDb.getProductTypesByDescription("").size()==1);    
    }
    @Test
    public void testInvalidGetAllProductTypes() {
    	assertFalse(ezshopDb.getAllProductTypes().size()==1);
    }
    @Test
    public void testInvalidCheckCredentials() {
    	assertNull(ezshopDb.checkCredentials("mario", "Cashier"));
    }
    
    
}
