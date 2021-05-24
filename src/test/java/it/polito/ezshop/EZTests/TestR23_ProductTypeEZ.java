
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
        Integer prod_Id = ezshop.createProductType("chocolate", "12345678912237", 4, null);
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
        List<ProductType> list = ezshop.getAllProductTypes();
        assertTrue(ezshop.updateProduct(prodId, "newDescription", "12345678912237", 10, ""));

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

        assertFalse(ezshop.updateProduct(this.prodId + 1, "honey", "2905911158926", 11, ""));// no
                                                                                             // id
                                                                                             // found



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
        assertTrue(ezshop.deleteProductType(prodId));
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
        List<ProductType> list = ezshop.getAllProductTypes();
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

        assertNotNull(ezshop.getProductTypeByBarCode("2905911158926"));

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
        assertEquals(ezshop.getProductTypesByDescription("honey").size(), 1);
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
        assertTrue(ezshop.updatePosition(prodId, "300-abc-203"));
        assertTrue(ezshop.updatePosition(prodId, ""));
    }

    @Test
    public void testInValidUpdatePosition() throws InvalidUsernameException,
            InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException,
            InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException,
            InvalidProductIdException, InvalidLocationException {
        Integer id = ezshop.createProductType("chocolate", "12345678912237", 4, "");
        assertFalse(ezshop.updatePosition(id, "347-sdfg-3673"));

        assertThrows(InvalidLocationException.class, () -> {
            ezshop.updatePosition(prodId, "y4g-345-h8t");
        });

        assertThrows(InvalidProductIdException.class, () -> {
            ezshop.updatePosition(null, "347-sdfg-3673");
        });
        assertThrows(InvalidProductIdException.class, () -> {
            ezshop.updatePosition(-1, "347-sdfg-3673");
        });

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
        assertTrue(ezshop.updateQuantity(prodId, 10));
    }

    public void testInalidUpdateQuantity()
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
