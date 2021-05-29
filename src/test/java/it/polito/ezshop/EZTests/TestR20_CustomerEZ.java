package it.polito.ezshop.EZTests;

import org.junit.Test;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.exceptions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;

public class TestR20_CustomerEZ {

    EZShopDb ezshopDb = new EZShopDb();
    EZShop ezshop = new EZShop();
    Integer customerId;

    // delete from sqlite_sequence where name='your_table';

    @Before
    public void setup() throws InvalidUsernameException, InvalidPasswordException,
            InvalidRoleException, InvalidCustomerNameException, UnauthorizedException {

        ezshop.createUser("elisa", "elisa98", "Administrator");
        int cId = ezshop.createUser("diego", "psw", "Cashier");
        ezshopDb.createConnection();
        ezshopDb.updateUserRights(cId, "Count of Montecristo");
        ezshopDb.closeConnection();
        ezshop.logout();
        ezshop.login("elisa", "elisa98");
        customerId = ezshop.defineCustomer("Busoni");
    }

    @After
    public void clean() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    }

    @Test
    public void testValidDefineCustomer()
            throws InvalidCustomerNameException, UnauthorizedException {
        Integer test = -1;
        long start = System.currentTimeMillis();
        assertNotEquals(test, ezshop.defineCustomer("customerName"));
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
    }

    @Test
    public void testInvalidDefineCustomer() throws InvalidUsernameException,
            InvalidPasswordException, InvalidCustomerNameException, UnauthorizedException {
        Integer test = -1;
        ezshop.defineCustomer("Edmond");
        assertEquals(test, ezshop.defineCustomer("Edmond"));

        assertThrows(InvalidCustomerNameException.class, () -> {
            ezshop.defineCustomer(null);
        });

        assertThrows(InvalidCustomerNameException.class, () -> {
            ezshop.defineCustomer("");
        });

        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.defineCustomer("Dantes");
        });

        ezshop.login("diego", "psw");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.defineCustomer("Dantes");
        });
    }

    @Test
    public void testValidModifyCustomer() throws InvalidCustomerNameException,
            UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
        long start = System.currentTimeMillis();
        assertTrue(ezshop.modifyCustomer(customerId, "Sinbad", ""));
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
        assertTrue(ezshop.modifyCustomer(customerId, "Wilmore", ezshop.createCard()));
    }

    @Test
    public void testInvalidModifyCustomer()
            throws InvalidUsernameException, InvalidPasswordException, InvalidCustomerNameException,
            UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {

        assertThrows(InvalidCustomerCardException.class, () -> {
            ezshop.modifyCustomer(customerId, "Sinbad", "468");
        });
        assertThrows(InvalidCustomerNameException.class, () -> {
            ezshop.modifyCustomer(customerId, null, "");
        });

        assertThrows(InvalidCustomerNameException.class, () -> {
            ezshop.modifyCustomer(customerId, "", "");
        });

        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.modifyCustomer(customerId, "Dantes", "");
        });

        ezshop.login("diego", "psw");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.modifyCustomer(customerId, "Dantes", "");
        });
    }

    @Test
    public void testValidDeleteCustomer() throws UnauthorizedException, InvalidCustomerIdException {
        long start = System.currentTimeMillis();
        assertTrue(ezshop.deleteCustomer(customerId));
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
    }

    @Test
    public void testInvalidDeleteCustomer() throws InvalidUsernameException,
            InvalidPasswordException, UnauthorizedException, InvalidCustomerIdException {

        assertThrows(InvalidCustomerIdException.class, () -> {
            ezshop.deleteCustomer(-1);
        });

        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.deleteCustomer(customerId);
        });

        ezshop.login("diego", "psw");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.deleteCustomer(customerId);
        });
    }


    @Test
    public void testValidGetCustomer() throws UnauthorizedException, InvalidCustomerIdException {
        long start = System.currentTimeMillis();
        assertNotNull(ezshop.getCustomer(customerId));
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
    }

    @Test
    public void testInvalidGetCustomer() throws InvalidUsernameException, InvalidPasswordException,
            UnauthorizedException, InvalidCustomerIdException {

        assertNull(ezshop.getCustomer(9879814));

        assertThrows(InvalidCustomerIdException.class, () -> {
            ezshop.getCustomer(-1);
        });

        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.getCustomer(customerId);
        });

        ezshop.login("diego", "psw");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.getCustomer(customerId);
        });
    }

    @Test
    public void testValidGetAllCustomer() throws UnauthorizedException {
        long start = System.currentTimeMillis();
        assertFalse(ezshop.getAllCustomers().isEmpty());
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
        assertNotNull(ezshop.getAllCustomers());
    }

    @Test
    public void testInvalidGetAllCustomer()
            throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {

        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.getAllCustomers();
        });

        ezshop.login("diego", "psw");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.getAllCustomers();
        });
    }

    @Test
    public void testValidCreateCard() throws UnauthorizedException {
        long start = System.currentTimeMillis();
        assertNotNull(ezshop.createCard());
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
    }

    @Test
    public void testInvalidCreateCard()
            throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {

        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.createCard();
        });

        ezshop.login("diego", "psw");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.createCard();
        });
    }

    @Test
    public void testValidAttachCardToCustomer()
            throws UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        long start = System.currentTimeMillis();
        assertTrue(ezshop.attachCardToCustomer(ezshop.createCard(), customerId));
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
    }

    @Test
    public void testInvalidAttachCardToCustomer()
            throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException,
            InvalidCustomerIdException, InvalidCustomerCardException {

        assertFalse(ezshop.attachCardToCustomer("0123456789", customerId));

        assertThrows(InvalidCustomerCardException.class, () -> {
            ezshop.attachCardToCustomer(null, customerId);
        });

        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.attachCardToCustomer(ezshop.createCard(), customerId);
        });

        ezshop.login("diego", "psw");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.attachCardToCustomer("0123456789", customerId);
        });
    }

    @Test
    public void testValidModifyPointsOnCard()
            throws UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        String card = ezshop.createCard();
        ezshop.attachCardToCustomer(card, customerId);
        long start = System.currentTimeMillis();
        assertTrue(ezshop.modifyPointsOnCard(card, 10));
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
    }

    @Test
    public void testInvalidModifyPointsOnCard()
            throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException,
            InvalidCustomerIdException, InvalidCustomerCardException {

        assertFalse(ezshop.modifyPointsOnCard(ezshop.createCard(), 10));

        assertThrows(InvalidCustomerCardException.class, () -> {
            ezshop.modifyPointsOnCard(null, 10);
        });

        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.modifyPointsOnCard(ezshop.createCard(), 10);
        });

        ezshop.login("diego", "psw");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.modifyPointsOnCard("0123456789", 10);
        });
    }


}

