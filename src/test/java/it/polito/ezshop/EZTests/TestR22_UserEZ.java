
package it.polito.ezshop.EZTests;

import org.junit.Test;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;

public class TestR22_UserEZ {

    EZShopDb ezshopDb = new EZShopDb();
    EZShop ezshop = new EZShop();
    Integer userId;

    // delete from sqlite_sequence where name='your_table';

    @Before
    public void setup() throws InvalidUsernameException, InvalidPasswordException,
            InvalidRoleException, InvalidCustomerNameException, UnauthorizedException {

       
        userId =  ezshop.createUser("elisa", "elisa98", "Administrator");
        ezshop.login("elisa", "elisa98");
        
    }

    @After
    public void clean() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    }

    @Test
    public void testValidCreateUser()
            throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {

        long start = System.currentTimeMillis();
        Integer test = ezshop.createUser("Michele","Prova","Administrator");
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
        assertNotEquals(-1, test,0);
        
        
    }
    @Test 
    public void testValidReset() {
    	ezshop.reset();
    }
    @Test
    public void testInvalidCreateUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        
        
        assertThrows(InvalidUsernameException.class, () -> {
            ezshop.createUser("","Prova","Administrator");
        });
        assertThrows(InvalidUsernameException.class, () -> {
            ezshop.createUser(null,"Prova","Administrator");
        });

        assertThrows( InvalidPasswordException.class, () -> {
            ezshop.createUser("Michele","","Administrator");
        });
        assertThrows( InvalidPasswordException.class, () -> {
            ezshop.createUser("Michele",null,"Administrator");
        });

        assertThrows(InvalidRoleException.class, () -> {
           ezshop.createUser("Michele","Prova","Test");
        });
        assertThrows(InvalidRoleException.class, () -> {
            ezshop.createUser("Michele","Prova",null);
         });
        assertThrows(InvalidRoleException.class, () -> {
            ezshop.createUser("Michele","Prova","");
         });
        assertEquals(ezshop.createUser("elisa", "elisa98", "Administrator"),-1,0);
        
    }

    @Test
    public void testValidDeleteUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException{
        Integer test = -1;
        test=ezshop.createUser("Michele","Prova","Administrator");
   
        long start = System.currentTimeMillis();

        assertTrue(ezshop.deleteUser(test));      
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);   
    }

    @Test
    public void testInValidDeleteUser() throws InvalidUserIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException{
        Integer test = -1;
       
        assertThrows(InvalidUserIdException.class, () -> {
            ezshop.deleteUser(-1);
        });
        assertThrows(InvalidUserIdException.class, () -> {
            ezshop.deleteUser(null);
        });
        assertFalse(ezshop.deleteUser(this.userId+10));
        
        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.deleteUser(test);
        });

        ezshop.createUser("Michele","Prova","Cashier");
        
        ezshop.login("Michele", "Prova");
        
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.deleteUser(test);
        });
 
    }
    @Test
    public void testValidGetAllUsers() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException{
        long start = System.currentTimeMillis();

        List<User> list=ezshop.getAllUsers();
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
        assertEquals(list.size(), 1);
        
        
    }
    

    @Test
    public void testInvaldGetAllUsers() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException{
        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.getAllUsers();
        });
        ezshop.createUser("Michele","Prova","Cashier");
        
        ezshop.login("Michele", "Prova");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.getAllUsers();
        });
        
    }
    @Test
    public void testValidGetUser() throws InvalidUserIdException, UnauthorizedException{
        long start = System.currentTimeMillis();

        User u = ezshop.getUser(userId);
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
        assertNotNull(u);


}
    @Test
    public void testInvalidGetUser() throws InvalidUserIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException{
        
        assertThrows(InvalidUserIdException.class, () -> {
            ezshop.getUser(null);
        });
        assertThrows(InvalidUserIdException.class, () -> {
            ezshop.getUser(-1);
        });
        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.getUser(userId);
        });
        ezshop.createUser("Michele","Prova","Cashier");
        
        ezshop.login("Michele", "Prova");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.getUser(userId);
        });
    }
    @Test
    public void  testValidupdateUserRights() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException{
        long start = System.currentTimeMillis();

        Integer id = ezshop.createUser("Michele","Prova","Cashier");
        assertTrue(ezshop.updateUserRights(id, "ShopManager"));
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
        

    }
    @Test
    public void  testInvalidupdateUserRights() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException{
        Integer id = ezshop.createUser("Michele","Prova","Cashier");
        assertFalse(ezshop.updateUserRights(id+20, "ShopManager"));
        assertThrows(InvalidUserIdException.class, () -> {
            ezshop.updateUserRights(-1,"role" );
        });
        assertThrows(InvalidUserIdException.class, () -> {
            ezshop.updateUserRights(null,"role" );
        });
        assertThrows(InvalidRoleException.class, () -> {
            ezshop.updateUserRights(id, null);
        });
        assertThrows(InvalidRoleException.class, () -> {
            ezshop.updateUserRights(id,"" );
        });
        assertThrows(InvalidRoleException.class, () -> {
            ezshop.updateUserRights(id,"role" );
        });
        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.updateUserRights(id,"ShopManager" );
        });
        ezshop.login("Michele", "Prova");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.updateUserRights(id,"Administrator" );
        });


    }
    @Test
    public void testValidLogin() throws InvalidUsernameException, InvalidPasswordException{
        
        ezshop.logout();
        long start = System.currentTimeMillis();
        User u=ezshop.login("elisa", "elisa98");
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
        assertNotNull(u);
        
    }
    @Test
    public void testInvalidLogin() throws InvalidUsernameException, InvalidPasswordException{
        assertThrows( InvalidUsernameException.class, () -> {
            ezshop.login(null, "elisa98");
        });
        assertThrows( InvalidUsernameException.class, () -> {
            ezshop.login("", "elisa98");
        });
        
        assertThrows(InvalidPasswordException.class, () -> {
            ezshop.login("elisa", null);
        });
        assertThrows(InvalidPasswordException.class, () -> {
            ezshop.login("elisa","");
        });
        assertNull(ezshop.login("elisa","no"));
    }
    @Test
    public void testValidlogout(){
        long start = System.currentTimeMillis();

        assertTrue(ezshop.logout());
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
    }
    @Test
    public void testInValidlogout(){
        ezshop.logout();
        assertFalse(ezshop.logout());
    }


   
}