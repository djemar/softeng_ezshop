package it.polito.ezshop.whiteBoxTests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.UserImpl;

public class TestR10_UserDb {
	EZShopDb ezshopDb;
	UserImpl user1=new UserImpl("Joe","USA","Administrator",1);
	@Before
    public void setup() {
       
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.insertUser(user1);
        ezshopDb.closeConnection();
    }

    @After
    public void clean() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    }
    /*getUserbyname
    updateuserrights
    */
    @Test
    public void testDeleteUser() {
    	ezshopDb.createConnection();
    	assertTrue(ezshopDb.deleteUser(user1.getId()));
    	ezshopDb.closeConnection();
    	
    }
    @Test
    public void testGetUserbyname() {
    	ezshopDb.createConnection();
    	assertTrue(ezshopDb.getUserbyName(user1.getUsername()));
    	ezshopDb.closeConnection();
    }
    @Test 
    public void testUpdateuserrights() {
    	ezshopDb.createConnection();
    	assertTrue(ezshopDb.updateUserRights(user1.getId(),"Cashier"));
    	ezshopDb.closeConnection();
    	
    }
}
