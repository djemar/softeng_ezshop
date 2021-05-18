package it.polito.ezshop.EZTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.UserImpl;

public class TestR10_UserDb {
	
  EZShopDb ezshopDb = new EZShopDb();
  Integer id;
  UserImpl user1;
  @Before
    public void setup() {
        user1 = new UserImpl("Eli","USA","Administrator",1);
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        id = ezshopDb.insertUser(user1);
        ezshopDb.closeConnection();
    }

    @After
    public void clean() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    } 


	@Test
	public void testGetUserbyName() {
		ezshopDb.createConnection();
		assertTrue(ezshopDb.getUserbyName("Eli"));
		assertFalse(ezshopDb.getUserbyName("Mitch"));
		ezshopDb.closeConnection();
	}
	
	@Test
	public void testInvalidGetUserbyName() {
		assertFalse(ezshopDb.getUserbyName("Eli"));
	}  
	
	@Test
	public void testUpdateUserRights() {
		ezshopDb.createConnection();
		assertTrue(ezshopDb.updateUserRights(id, "Administrator"));
		ezshopDb.closeConnection();
	}
	
	@Test
	public void testInvalidUpdateUserRights() {
		assertFalse(ezshopDb.updateUserRights(id, "Administrator"));
	}
    @Test
    public void testDeleteUser() {
      ezshopDb.createConnection();
      assertTrue(ezshopDb.deleteUser(id));
      ezshopDb.closeConnection();
      
    }

    @Test
    public void testInvalidDeleteUser() {
      assertFalse(ezshopDb.deleteUser(id));    
    }
    
}
