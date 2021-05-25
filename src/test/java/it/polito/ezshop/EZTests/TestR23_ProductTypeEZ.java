
package it.polito.ezshop.EZTests;

import org.junit.Test;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.OrderImpl;
import it.polito.ezshop.data.ProductType;
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

public class TestR23_ProductTypeEZ {

    EZShopDb ezshopDb = new EZShopDb();
    EZShop ezshop = new EZShop();

    Integer prodId;

    // delete from sqlite_sequence where name='your_table';

    @Before
    public void setup() throws InvalidUsernameException, InvalidPasswordException,
            InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException,
            InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException,
            InvalidLocationException, InvalidQuantityException {

        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
        ezshop.createUser("Momo", "EZ", "Administrator");
        ezshop.login("Momo", "EZ");

        List<ProductType> list = ezshop.getAllProductTypes();

        prodId = ezshop.createProductType("honey", "2905911158926", 4, "");
        ezshop.updatePosition(prodId, "347-sdfg-3673");
        ezshop.updateQuantity(prodId, 50);
        list = ezshop.getAllProductTypes();
    }

    @After
    public void clean() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    }

    @Test
    public void testValidCreateProductType() throws InvalidProductCodeException,
            InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException,
            InvalidProductDescriptionException {
        
        long start = System.currentTimeMillis();
        Integer prod_Id = ezshop.createProductType("chocolate", "12345678912237", 4, null);
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
        assertNotEquals(prod_Id, -1, 0);
        
    }

    @Test
    public void testInvalidCreateProductType() throws InvalidProductDescriptionException,
            InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException,
            InvalidUsernameException, InvalidPasswordException, InvalidRoleException {


        assertThrows(InvalidProductDescriptionException.class, () -> {
            ezshop.createProductType(null, "12345678912237", 4, null);
        });
        assertThrows(InvalidProductDescriptionException.class, () -> {
            ezshop.createProductType("", "12345678912237", 4, null);
        });
        assertThrows(InvalidPricePerUnitException.class, () -> {
            ezshop.createProductType("o", "12345678912237", -1, null);
        });
        assertThrows(InvalidPricePerUnitException.class, () -> {
            ezshop.createProductType("a", "12345678912237", 0, null);
        });

        assertThrows(InvalidProductCodeException.class, () -> {
            ezshop.createProductType("chocolate", null, 4, null);
        });
        assertThrows(InvalidProductCodeException.class, () -> {
            ezshop.createProductType("chocolate", "", 4, null);
        });

        assertEquals(-1, ezshop.createProductType("chocolate", "2905911158926", 4, null), 0);


        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.createProductType("chocolate", "12345678912237", 4, null);
        });
        ezshop.createUser("Michele", "Prova", "Cashier");
        ezshop.login("Michele", "Prova");

        assertThrows(UnauthorizedException.class, () -> {
            ezshop.createProductType("chocolate", "2905911158926", 4, null);
        });


    }

    @Test
    public void testValidUpdateProduct()
            throws InvalidProductIdException, InvalidProductDescriptionException,
            InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        long start = System.currentTimeMillis();
        assertTrue(ezshop.updateProduct(prodId, "newDescription", "12345678912237", 10, ""));
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
        assertTrue(ezshop.updateProduct(prodId, "newDescription", "442723352927", 10, ""));

        
    }

    @Test
    public void testInvalidUpdateProduct()
            throws InvalidProductIdException, InvalidProductDescriptionException,
            InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException,
            InvalidUsernameException, InvalidPasswordException, InvalidRoleException {

        assertThrows(InvalidProductIdException.class, () -> {
            ezshop.updateProduct(null, "newDescription", "2905911158926", 10, "");
        });
        assertThrows(InvalidProductIdException.class, () -> {
            ezshop.updateProduct(-1, "newDescription", "2905911158926", 10, "");
        });
        assertThrows(InvalidProductDescriptionException.class, () -> {
            ezshop.updateProduct(this.prodId, null, "2905911158926", 10, "");
        });
        assertThrows(InvalidProductDescriptionException.class, () -> {
            ezshop.updateProduct(this.prodId, "", "2905911158926", 10, "");
        });
        assertThrows(InvalidPricePerUnitException.class, () -> {
            ezshop.updateProduct(this.prodId, "honey", "2905911158926", -1, "");
        });
        assertThrows(InvalidProductCodeException.class, () -> {
            ezshop.updateProduct(this.prodId, "honey", null, 1, "");
        });
        assertThrows(InvalidProductCodeException.class, () -> {
            ezshop.updateProduct(this.prodId, "honey", "", 1, "");
        });
        assertThrows(InvalidProductCodeException.class, () -> {
            ezshop.updateProduct(this.prodId, "honey", "134fwe3", 1, "");
        });

        // assertNull(ezshop.updateProduct(this.prodId,"honey","2905911158926", 11, ""));//bar code
        // doesn't change
        ezshop.createProductType("stuff", "12345678912237", 8, null);
        assertFalse(ezshop.updateProduct(this.prodId + 1, "honey", "2905911158926", 11, ""));// no
        assertFalse(ezshop.updateProduct(this.prodId, "honey", "12345678912237", 11, ""));                                                                                     // id
        assertFalse(ezshop.updateProduct(this.prodId+8, "honey", "2727377395598", 11, ""));                                                                                     // found



        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.updateProduct(this.prodId, "honey", "2905911158926", 10, "");
        });
        ezshop.createUser("Michele", "Prova", "Cashier");
        ezshop.login("Michele", "Prova");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.updateProduct(this.prodId, "honey", "2905911158926", 10, "");
        });
    }

    @Test
    public void testValidDeleteProductType()
            throws InvalidProductIdException, UnauthorizedException {
        long start = System.currentTimeMillis();        
        assertTrue(ezshop.deleteProductType(prodId));
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
    }


    @Test
    public void testInvalidDeleteProductType()
            throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException,
            InvalidProductIdException, UnauthorizedException {
        assertThrows(InvalidProductIdException.class, () -> {
            ezshop.deleteProductType(-1);
        });
        assertThrows(InvalidProductIdException.class, () -> {
            ezshop.deleteProductType(null);
        });
        assertFalse(ezshop.deleteProductType(prodId + 1));

        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.deleteProductType(prodId);
        });
        ezshop.createUser("Michele", "Prova", "Cashier");
        ezshop.login("Michele", "Prova");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.deleteProductType(prodId);
        });

    }

    @Test
    public void testValidGetAllProductTypes()
            throws UnauthorizedException, InvalidProductDescriptionException,
            InvalidProductCodeException, InvalidPricePerUnitException {
        
        ezshop.createProductType("chocolate3", "12345678912237", 4, null);
        long start = System.currentTimeMillis();
        List<ProductType> list = ezshop.getAllProductTypes();
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
        assertEquals(list.size(), 2);
        

    }

    @Test
    public void testInvalidGetAllProductTypes() throws UnauthorizedException {


        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.getAllProductTypes();
        });

    }

    @Test
    public void testValidGetProductTypeByBarCode()
            throws InvalidProductCodeException, UnauthorizedException {
        long start = System.currentTimeMillis();
        assertNotNull(ezshop.getProductTypeByBarCode("2905911158926"));
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
    }

    @Test
    public void testInvalidGetProductTypeByBarCode()
            throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException,
            InvalidProductCodeException, UnauthorizedException {


        assertThrows(InvalidProductCodeException.class, () -> {
            ezshop.getProductTypeByBarCode(null);
        });
        assertThrows(InvalidProductCodeException.class, () -> {
            ezshop.getProductTypeByBarCode("");
        });
        assertThrows(InvalidProductCodeException.class, () -> {
            ezshop.getProductTypeByBarCode("234frg4");
        });
        assertThrows(InvalidProductCodeException.class, () -> {
            ezshop.getProductTypeByBarCode("234");
        });
        assertNull(ezshop.getProductTypeByBarCode("12345678912237"));
        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.getProductTypeByBarCode("2905911158926");
        });
        ezshop.createUser("Michele", "Prova", "Cashier");
        ezshop.login("Michele", "Prova");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.getProductTypeByBarCode("2905911158926");
        });


    }

    @Test
    public void testValidgetProductTypesByDescription() throws UnauthorizedException {
        long start = System.currentTimeMillis();
        assertEquals(ezshop.getProductTypesByDescription("honey").size(), 1);
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
        assertEquals(ezshop.getProductTypesByDescription(null).size(), 1);
        
    }

    @Test
    public void testInvalidgetProductTypesByDescription()
            throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {

        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.getProductTypesByDescription("honey");
        });
        ezshop.createUser("Michele", "Prova", "Cashier");
        ezshop.login("Michele", "Prova");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.getProductTypesByDescription("honey");
        });
    }

    @Test
    public void testValidUpdatePostion()
            throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        long start = System.currentTimeMillis();
        assertTrue(ezshop.updatePosition(prodId, "300-abc-203"));
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
        assertTrue(ezshop.updatePosition(prodId, ""));
        assertTrue(ezshop.updatePosition(prodId, null));
        
    }

    @Test
    public void testInValidUpdatePosition() throws InvalidUsernameException,
            InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException,
            InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException,
            InvalidProductIdException, InvalidLocationException {
        Integer id = ezshop.createProductType("chocolate", "12345678912237", 4, "");
        assertFalse(ezshop.updatePosition(id, "347-sdfg-3673"));

        assertThrows(InvalidLocationException.class, () -> {
            ezshop.updatePosition(prodId, "y32133rg-h8t");
        });
       assertThrows(InvalidLocationException.class, () -> {
            ezshop.updatePosition(prodId, "--");
        });
       assertThrows(InvalidLocationException.class, () -> {
           ezshop.updatePosition(prodId, "123-rom-eee");
       });
        
        assertThrows(InvalidLocationException.class, () -> {
            ezshop.updatePosition(prodId, "-3343434-");
        });

        assertThrows(InvalidProductIdException.class, () -> {
            ezshop.updatePosition(null, "347-sdfg-3673");
        });
        assertThrows(InvalidProductIdException.class, () -> {
            ezshop.updatePosition(-1, "347-sdfg-3673");
        });
        assertFalse((ezshop.updatePosition(this.prodId+20,"347-sdfg-3673")));
        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.updatePosition(prodId, "300-abc-203");
        });
        ezshop.createUser("Michele", "Prova", "Cashier");
        ezshop.login("Michele", "Prova");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.updatePosition(prodId, "300-abc-203");
        });
    }

    @Test
    public void testValidUpdateQuantity() throws InvalidProductIdException, UnauthorizedException {
        long start = System.currentTimeMillis();
        assertTrue(ezshop.updateQuantity(prodId, 10));
        long fine = System.currentTimeMillis();
        assertTrue(fine - start < 500);
    }
    @Test
    public void testInvalidUpdateQuantity()
            throws InvalidProductIdException, UnauthorizedException, InvalidUsernameException,
            InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException,
            InvalidProductCodeException, InvalidPricePerUnitException {

        assertThrows(InvalidProductIdException.class, () -> {
            ezshop.updateQuantity(-1, 10);
        });
        assertThrows(InvalidProductIdException.class, () -> {
            ezshop.updateQuantity(null, 10);
        });

        assertFalse(ezshop.updateQuantity(prodId, -100));
        Integer id = ezshop.createProductType("chocolate", "12345678912237", 4, "");

        assertFalse(ezshop.updateQuantity(id, 5));

        ezshop.logout();
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.updateQuantity(prodId, 10);
        });
        ezshop.createUser("Michele", "Prova", "Cashier");
        ezshop.login("Michele", "Prova");
        assertThrows(UnauthorizedException.class, () -> {
            ezshop.updateQuantity(prodId, 10);
        });
    }



}
