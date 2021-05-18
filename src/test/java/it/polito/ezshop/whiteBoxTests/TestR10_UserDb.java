package it.polito.ezshop.whiteBoxTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.UserImpl;

public class TestR10_UserDb {
  EZShopDb ezshopDb;
  int id;
  UserImpl user1=new UserImpl("Joe","USA","Administrator",1);
  @Before
    public void setup() {
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
    public void testDeleteUser() {
      ezshopDb.createConnection();
      assertTrue(ezshopDb.deleteUser(id));
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
		assertTrue(ezshopDb.updateUserRights(id, "Administrator"));
	}
    
}
