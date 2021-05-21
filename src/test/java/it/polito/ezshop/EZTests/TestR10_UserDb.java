package it.polito.ezshop.EZTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.EZShop;
import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.User;
import it.polito.ezshop.data.UserImpl;

public class TestR10_UserDb {
	
  EZShopDb ezshopDb = new EZShopDb();
  Integer id;
  UserImpl user1;
  @Before
    public void setup() {
        user1 = new UserImpl("Eli","USA","Administrator");
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        id = ezshopDb.insertUser(user1);
        user1.setId(id);
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
		user1.setRole("Administrator");
		ezshopDb.closeConnection();
	}
	
	@Test
	public void testInvalidUpdateUserRights() {
		assertFalse(ezshopDb.updateUserRights(id, "Cashier"));
	}
    @Test
    public void testDeleteUser() {
      ezshopDb.createConnection();
      assertTrue(ezshopDb.deleteUser(id));
      id=ezshopDb.insertUser(user1);
      user1.setId(id);
      ezshopDb.closeConnection();
      
    }

    @Test
    public void testInvalidDeleteUser() {
      assertFalse(ezshopDb.deleteUser(id));    
    }
    @Test 
    public void testGetAllUser(){
      ezshopDb.createConnection();
      List<User> list = ezshopDb.getAllUsers();
      assertEquals(1, list.size());
      User u = list.get(0);
      assertTrue(u.getId().equals(user1.getId())&&u.getPassword().equals(user1.getPassword()));
      ezshopDb.closeConnection();
      
    }
    @Test
    public void testGetUser(){
      ezshopDb.createConnection();
      User u=ezshopDb.getUser(id);
      assertTrue(u.getId().equals(user1.getId())&&u.getPassword().equals(user1.getPassword()));
      ezshopDb.closeConnection();
    }
    @Test
    public void testCheckCredentials(){
      ezshopDb.createConnection();
      User u=ezshopDb.checkCredentials(user1.getUsername(), user1.getPassword());
      assertTrue(u.getPassword().equals(user1.getPassword())&&u.getUsername().equals(user1.getUsername()));

      assertNull(ezshopDb.checkCredentials("mario", "cashier"));
      ezshopDb.closeConnection();
    }
    
    
}
