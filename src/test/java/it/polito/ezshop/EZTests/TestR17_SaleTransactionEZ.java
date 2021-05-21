package it.polito.ezshop.EZTests;
import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.ProductTypeImpl;
import it.polito.ezshop.data.SaleTransactionImpl;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.data.TicketEntryImpl;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.After;
import org.junit.Before;

public class TestR17_SaleTransactionEZ {	
	
	 EZShop ezshop = new EZShop();
	 EZShopDb ezshopdb = new EZShopDb();
	 SaleTransactionImpl activeSaleTransaction = null;
	 User u = null;
	 Integer id;
	 Integer saleID;
	    @Before
	    public void setup() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException {
	    	ezshopdb.createConnection();
	    	ezshopdb.resetDB();
	    	ezshopdb.closeConnection();
	    	ezshop.logout();
	    	ezshop.createUser("elisa", "elisa98", "Administrator");
	    	u = ezshop.login("elisa", "elisa98");
	    	id = ezshop.createProductType("chocolate", "12345678912237", 2, "");
	    	ezshop.updatePosition(id, "347-sdfg-3673");
	    	ezshop.updateQuantity(id, 50);
	    	ezshop.logout();
	    }
	    @After
	    public void clean() {
	    	ezshopdb.createConnection();
	    	ezshopdb.resetDB();
	    	ezshopdb.closeConnection();
	    }
	    @Test
	    public void testInvalidStartSaleTransaction() throws UnauthorizedException {
	        ezshop.logout();
	        assertThrows(UnauthorizedException.class, () -> {
	            ezshop.startSaleTransaction();
	        });
	    }
	    @Test
	    public void validStartSaleTransaction() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException {
	    	u = ezshop.login("elisa", "elisa98");
	    	saleID = ezshop.startSaleTransaction();
	    	assertEquals(saleID, 1, 1);
	    	
	    }
	    @Test
	    public void testInvalidAddProductToSale() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
	    	u = ezshop.login("elisa", "elisa98");
	        assertThrows(InvalidTransactionIdException.class, () -> {
	        	ezshop.addProductToSale(-1, "12345678912237", 4);
	        });
	        assertThrows(InvalidTransactionIdException.class, () -> {
	        	ezshop.addProductToSale(null, "12345678912237", 4);
	        });
	        assertThrows(InvalidProductCodeException.class, () -> {
	        	ezshop.addProductToSale(1, "123456712237", 4);
	        });
	        assertThrows(InvalidProductCodeException.class, () -> {
	        	ezshop.addProductToSale(1, "", 4);
	        });
	        assertThrows(InvalidProductCodeException.class, () -> {
	        	ezshop.addProductToSale(1, null, 4);
	        });
	        assertThrows(InvalidQuantityException.class, () -> {
	        	ezshop.addProductToSale(1, "12345678912237", -4);
	        });
	        assertThrows(InvalidProductCodeException.class, () -> {
	        	ezshop.addProductToSale(1, "123458912237", 4);
	        });

	        assertFalse(ezshop.addProductToSale(1, "2905911158926", 4));
	        assertFalse(ezshop.addProductToSale(1, "12345678912237", 4));
	        assertFalse(ezshop.addProductToSale(1, "12345678912237", 3));
	        ezshop.logout();
	        assertThrows(UnauthorizedException.class, () -> {
	        	ezshop.addProductToSale(1, "12345678912237", 4);
	        });
	        
	    }
	    @Test
	    public void testValidAddProductToSale() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidProductIdException {
	    	u = ezshop.login("elisa", "elisa98");
	    	ezshop.updateQuantity(id, 10);
	    	saleID = ezshop.startSaleTransaction();
	    	assertTrue(ezshop.addProductToSale(1, "12345678912237", 4));
	    	assertTrue(ezshop.deleteProductFromSale(1, "12345678912237", 1));
	    	assertFalse(ezshop.deleteProductFromSale(1, "12345678912237", 15));
	    }
	    @Test
	    public void testInvalidDeleteProductFromSale() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
	    	u = ezshop.login("elisa", "elisa98");
	        assertThrows(InvalidTransactionIdException.class, () -> {
	        	ezshop.deleteProductFromSale(-1, "12345678912237", 4);
	        });
	        assertThrows(InvalidTransactionIdException.class, () -> {
	        	ezshop.deleteProductFromSale(null, "12345678912237", 4);
	        });
	        assertThrows(InvalidProductCodeException.class, () -> {
	        	ezshop.deleteProductFromSale(1, "123456712237", 4);
	        });
	        assertThrows(InvalidProductCodeException.class, () -> {
	        	ezshop.deleteProductFromSale(1, "", 4);
	        });
	        assertThrows(InvalidProductCodeException.class, () -> {
	        	ezshop.deleteProductFromSale(1, null, 4);
	        });
	        assertThrows(InvalidQuantityException.class, () -> {
	        	ezshop.deleteProductFromSale(1, "12345678912237", -4);
	        });
	        assertThrows(InvalidProductCodeException.class, () -> {
	        	ezshop.deleteProductFromSale(1, "123458912237", 4);
	        });

	        assertFalse(ezshop.deleteProductFromSale(1, "2905911158926", 4));
	        assertFalse(ezshop.deleteProductFromSale(1, "12345678912237", 4));
	        assertFalse(ezshop.deleteProductFromSale(1, "12345678912237", 3));
	        ezshop.logout();
	        assertThrows(UnauthorizedException.class, () -> {
	        	ezshop.deleteProductFromSale(1, "12345678912237", 4);
	        });

	        
	    }
	    
	    
	    
	
}
