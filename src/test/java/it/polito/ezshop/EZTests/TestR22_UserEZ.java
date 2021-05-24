
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

        ezshop.createUser("elisa", "elisa98", "Administrator");
        userId = ezshop.createUser("diego", "psw", "Cashier");
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
        Integer test = -1;
        assertNotEquals(test, ezshop.createUser("Michele","Prova","Administrator"));
    }

    @Test
    public void testInvalidCreateUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        
        
        assertThrows(InvalidUsernameException.class, () -> {
            ezshop.createUser("","Prova","Administrator");
        });

        assertThrows( InvalidPasswordException.class, () -> {
            ezshop.createUser("Michele","","Administrator");
        });

        assertThrows(InvalidRoleException.class, () -> {
           ezshop.createUser("Michele","Prova","Test");
        });

        
    }

    @Test
    public void testValidDeleteUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException{
        Integer test = -1;
        test=ezshop.createUser("Michele","Prova","Administrator");
        
        assertTrue(ezshop.deleteUser(test));         
    }

    @Test
    public void testInValidDeleteUser() throws InvalidUserIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException{
        Integer test = -1;
       
        assertThrows(InvalidUserIdException.class, () -> {
            ezshop.deleteUser(test);
        });

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
        List<User> list=ezshop.getAllUsers();
        assertEquals(list.size(), 2);
        
        
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
        User u = ezshop.getUser(userId);
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
        Integer id = ezshop.createUser("Michele","Prova","Cashier");
        assertTrue(ezshop.updateUserRights(id, "ShopManager"));

    }
    @Test
    public void  testInvalidupdateUserRights() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException{
        Integer id = ezshop.createUser("Michele","Prova","Cashier");

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
        User u=ezshop.login("elisa", "elisa98");
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
        assertTrue(ezshop.logout());
    }
    @Test
    public void testInValidlogout(){
        ezshop.logout();
        assertFalse(ezshop.logout());
    }


   
}