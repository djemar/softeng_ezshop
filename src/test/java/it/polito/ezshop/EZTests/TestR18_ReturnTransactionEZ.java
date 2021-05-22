package it.polito.ezshop.EZTests;
import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPaymentException;
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

public class TestR18_ReturnTransactionEZ {
	
	 EZShop ezshop = new EZShop();
	 EZShopDb ezshopdb = new EZShopDb();
	 Integer prodID;

	    @Before
	    public void setup() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException  {
	    	ezshopdb.createConnection();
	    	ezshopdb.resetDB();
	    	ezshopdb.closeConnection();
	    	ezshop.logout();
	    	ezshop.createUser("elisa", "elisa98", "Administrator");
	    	ezshop.login("elisa", "elisa98");
	    	prodID = ezshop.createProductType("chocolate", "12345678912237", 2, "");
	    	ezshop.updatePosition(prodID, "347-sdfg-3673");
	    	ezshop.updateQuantity(prodID, 50);
	    	ezshop.logout();
	    }
	    @After
	    public void clean() {
	    	ezshopdb.createConnection();
	    	ezshopdb.resetDB();
	    	ezshopdb.closeConnection();
	    }
	    @Test
	    public void testInvalidStartReturnTransaction() throws UnauthorizedException, InvalidTransactionIdException, InvalidUsernameException, InvalidPasswordException{
	    	ezshop.login("elisa", "elisa98");
	    	assertThrows(InvalidTransactionIdException.class, () -> {
	            ezshop.startReturnTransaction(null);
	        });
	        assertThrows(InvalidTransactionIdException.class, () -> {
	            ezshop.startReturnTransaction(-500);
	        });
	        //sale transaction inesistente
	        assertEquals(ezshop.startReturnTransaction(1), -1, 0);
	    	ezshop.logout();
	        assertThrows(UnauthorizedException.class, () -> {
	            ezshop.startReturnTransaction(1);
	        });
	    
	 }
	    
	    @Test
	    public void testValidStartReturnTransaction() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidPaymentException {
        	ezshop.login("elisa", "elisa98");
        	ezshop.startSaleTransaction();
        	ezshop.addProductToSale(1, "12345678912237", 4);
        	ezshop.endSaleTransaction(1);
        	assertEquals(ezshop.startReturnTransaction(1), -1, 0);
	    	ezshop.receiveCashPayment(1, 100);
	    	assertNotEquals(ezshop.startReturnTransaction(1), -1, 0);
	    	
	    }
}