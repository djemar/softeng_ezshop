package it.polito.ezshop.EZTests;

import org.junit.Test;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.OrderImpl;
import it.polito.ezshop.data.Order;
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

public class TestR19_OrderEZ {

    EZShopDb ezshopDb = new EZShopDb();
    EZShop ezshop = new EZShop();
    OrderImpl o1, o2;
    Integer orderId, prodId;

    // delete from sqlite_sequence where name='your_table';

    @Before
    public void setup() throws InvalidUsernameException, InvalidPasswordException,
            InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException,
            InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException,
            InvalidLocationException, InvalidQuantityException {

        ezshop.createUser("elisa", "elisa98", "Administrator");
        ezshop.createUser("diego", "d", "Cashier");
        ezshop.logout();
        ezshop.login("elisa", "elisa98");
        prodId = ezshop.createProductType("chocolate", "12345678912237", 4, "");
        ezshop.recordBalanceUpdate(100.0);
        ezshop.updatePosition(prodId, "347-sdfg-3673");
        ezshop.updateQuantity(prodId, 50);
        orderId = ezshop.issueOrder("12345678912237", 10, 1.0);
    }

    @After
    public void clean() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    }

    @Test
    public void testValidIssueOrder() throws InvalidProductCodeException, InvalidQuantityException,
            InvalidPricePerUnitException, UnauthorizedException {
        Integer test = -1, result;
        long start = System.currentTimeMillis();
        result = ezshop.issueOrder("12345678912237", 1, 1.0);
        long fine = System.currentTimeMillis();
        assertNotEquals(test, result);
        assertTrue(fine - start < 500);
    }

    @Test
    public void testInvalidIssueOrder() throws InvalidProductCodeException,
            InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException,
            InvalidUsernameException, InvalidPasswordException {
        Integer test = -1;

        assertEquals(test, ezshop.issueOrder("2905911158926", 1, 1.0));

        assertThrows(InvalidProductCodeException.class, () -> {
            ezshop.issueOrder("123456782237", 1, 1.0);
        });
        assertThrows(InvalidProductCodeException.class, () -> {
            ezshop.issueOrder(null, 1, 1.0);
        });
        assertThrows(InvalidQuantityException.class, () -> {
            ezshop.issueOrder("12345678912237", 0, 1.0);
        });
        assertThrows(InvalidPricePerUnitException.class, () -> {
            ezshop.issueOrder("12345678912237", 1, 0);
        });

        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.issueOrder("12345678912237", 1, 1.0);
        });
        ezshop.login("diego", "d");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.issueOrder("12345678912237", 1, 1.0);
        });
    }

    @Test
    public void testValidPayOrderFor() throws InvalidProductCodeException, InvalidQuantityException,
            InvalidPricePerUnitException, UnauthorizedException {
        Integer test = -1, result;
        long start = System.currentTimeMillis();
        result = ezshop.payOrderFor("12345678912237", 1, 1.0);
        long fine = System.currentTimeMillis();
        assertNotEquals(test, result);
        assertTrue(fine - start < 500);
    }

    @Test
    public void testInvalidPayOrderFor() throws InvalidProductCodeException,
            InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException,
            InvalidUsernameException, InvalidPasswordException {
        Integer test = -1;

        assertEquals(test, ezshop.payOrderFor("2905911158926", 1, 1.0));
        assertEquals(test, ezshop.payOrderFor("12345678912237", 100, 100.0));

        assertThrows(InvalidProductCodeException.class, () -> {
            ezshop.payOrderFor("123456782237", 1, 1.0);
        });
        assertThrows(InvalidProductCodeException.class, () -> {
            ezshop.issueOrder(null, 1, 1.0);
        });
        assertThrows(InvalidQuantityException.class, () -> {
            ezshop.payOrderFor("12345678912237", 0, 1.0);
        });
        assertThrows(InvalidPricePerUnitException.class, () -> {
            ezshop.payOrderFor("12345678912237", 1, 0);
        });

        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.payOrderFor("12345678912237", 1, 1.0);
        });
        ezshop.login("diego", "d");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.payOrderFor("12345678912237", 1, 1.0);
        });
    }

    @Test
    public void testValidPayOrder() throws InvalidOrderIdException, UnauthorizedException {
        boolean result;
        long start = System.currentTimeMillis();
        result = ezshop.payOrder(orderId);
        long fine = System.currentTimeMillis();
        assertTrue(result);
        assertTrue(fine - start < 500);
    }

    @Test
    public void testInvalidPayOrder() throws UnauthorizedException, InvalidUsernameException,
            InvalidPasswordException, InvalidOrderIdException {

        assertFalse(ezshop.payOrder(64846));
        ezshop.payOrder(orderId);

        assertFalse(ezshop.payOrder(orderId));

        assertThrows(InvalidOrderIdException.class, () -> {
            ezshop.payOrder(-1);
        });
        assertThrows(InvalidOrderIdException.class, () -> {
            ezshop.payOrder(null);
        });

        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.payOrder(orderId);
        });
        ezshop.login("diego", "d");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.payOrder(orderId);

        });
    }

    @Test
    public void testValidRecordOrderArrival()
            throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        ezshop.payOrder(orderId);
        boolean result;
        long start = System.currentTimeMillis();
        result = ezshop.recordOrderArrival(orderId);
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
        assertTrue(result);
    }

    @Test
    public void testInvalidRecordOrderArrival() throws UnauthorizedException,
            InvalidUsernameException, InvalidPasswordException, InvalidOrderIdException,
            InvalidLocationException, InvalidProductIdException, InvalidProductDescriptionException,
            InvalidProductCodeException, InvalidPricePerUnitException {

        assertFalse(ezshop.recordOrderArrival(64846));

        ezshop.payOrder(orderId);

        ezshop.deleteProductType(prodId);
        assertThrows(InvalidLocationException.class, () -> {
            ezshop.recordOrderArrival(orderId);
        });

        prodId = ezshop.createProductType("chocolate", "12345678912237", 4, "");
        ezshop.updatePosition(prodId, "347-sdfg-3673");

        ezshop.recordOrderArrival(orderId);
        assertTrue(ezshop.recordOrderArrival(orderId));



        assertThrows(InvalidOrderIdException.class, () -> {
            ezshop.recordOrderArrival(-1);
        });

        assertThrows(InvalidOrderIdException.class, () -> {
            ezshop.recordOrderArrival(null);
        });

        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.recordOrderArrival(orderId);
        });
        ezshop.login("diego", "d");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.recordOrderArrival(orderId);
        });
    }

    @Test
    public void testValidGetAllOrders() throws UnauthorizedException {
        List<Order> result;
        long start = System.currentTimeMillis();
        result = ezshop.getAllOrders();
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
        assertNotNull(result);
    }

    @Test
    public void testInvalidGetAllOrders()
            throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException {

        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.getAllOrders();
        });
        ezshop.login("diego", "d");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.getAllOrders();

        });


    }
    @Test
    public void testValidRecordOrderArrivalRFID() throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException, InvalidRFIDException{
        ezshop.payOrder(orderId);
        boolean result;
        long start = System.currentTimeMillis();
        result = ezshop.recordOrderArrivalRFID(orderId,"000000000010");
        result = ezshop.recordOrderArrivalRFID(orderId,"000000000010");

        long fine = System.currentTimeMillis();
        //assertTrue(fine - start < 500);
        assertTrue(result);
    }
    @Test
    public void testInvalidRecordOrderArrivalRFID() throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException, InvalidRFIDException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidUsernameException, InvalidPasswordException, InvalidQuantityException{
        assertFalse(ezshop.recordOrderArrivalRFID(64846,"000000000090"));

        ezshop.payOrder(orderId);

        ezshop.deleteProductType(prodId);
        assertThrows(InvalidLocationException.class, () -> {
            ezshop.recordOrderArrivalRFID(orderId,"000000000100");
        });

        prodId = ezshop.createProductType("honey", "2905911158926", 10, "");
        ezshop.updatePosition(prodId, "347-sdfg-3673");
        orderId=ezshop.issueOrder( "2905911158926", 2, 2);
        ezshop.payOrder(orderId);
        assertTrue(ezshop.recordOrderArrivalRFID(orderId,"000000000100"));

        int orderId2=ezshop.issueOrder( "2905911158926", 2, 2);
        ezshop.payOrder(orderId2);
        assertThrows(InvalidRFIDException.class, () -> {
            ezshop.recordOrderArrivalRFID(orderId2,"000000000100");
        });


        assertThrows(InvalidOrderIdException.class, () -> {
            ezshop.recordOrderArrivalRFID(-1,"000000000100");
        });

        assertThrows(InvalidOrderIdException.class, () -> {
            ezshop.recordOrderArrivalRFID(null,"000000000100");
        });
        assertThrows(InvalidRFIDException.class, () -> {
            ezshop.recordOrderArrivalRFID(orderId,"");
        });

        assertThrows(InvalidRFIDException.class, () -> {
            ezshop.recordOrderArrivalRFID(orderId,null);
        });
        ezshop.recordBalanceUpdate(100000);

        assertThrows(InvalidRFIDException.class, () -> {
            ezshop.recordOrderArrivalRFID(1,"test");
        });
        assertThrows(InvalidRFIDException.class, () -> {
            ezshop.recordOrderArrivalRFID(1,"00000test0");
        });
        
        assertThrows(InvalidRFIDException.class, () -> {
            ezshop.recordOrderArrivalRFID(1,"0000100");
        });
        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.recordOrderArrivalRFID(orderId,"000000000100");
        });
        ezshop.login("diego", "d");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.recordOrderArrivalRFID(orderId,"000000000100");
        });
    }
    

}

