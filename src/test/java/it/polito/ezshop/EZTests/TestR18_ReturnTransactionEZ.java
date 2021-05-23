package it.polito.ezshop.EZTests;
import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.exceptions.InvalidCreditCardException;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
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
	    @Test
	    public void testInvalidReturnProduct() throws UnauthorizedException, InvalidTransactionIdException, InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, InvalidQuantityException, InvalidPaymentException{
	    	ezshop.login("elisa", "elisa98");
	    	assertThrows(InvalidTransactionIdException.class, () -> {
	            ezshop.returnProduct(null, "12345678912237", 1);
	        });
	        assertThrows(InvalidTransactionIdException.class, () -> {
	            ezshop.returnProduct(-500, "12345678912237", 1);
	        });
	        assertThrows(InvalidProductCodeException.class, () -> {
	            ezshop.returnProduct(1, "1238912237", 1);
	        });
	        assertThrows(InvalidProductCodeException.class, () -> {
	            ezshop.returnProduct(1, "", 1);
	        });
	        assertThrows(InvalidProductCodeException.class, () -> {
	            ezshop.returnProduct(1, null, 1);
	        });
	        assertThrows(InvalidQuantityException.class, () -> {
	            ezshop.returnProduct(1, "12345678912237", -200);
	        });
	        //return trans nulla
	        assertFalse(ezshop.returnProduct(1, "12345678912237", 2));
	        //prod non è nella lista ticketentry
	        
        	ezshop.startSaleTransaction();
        	ezshop.addProductToSale(1, "12345678912237", 4);
        	ezshop.endSaleTransaction(1);
	    	ezshop.receiveCashPayment(1, 100);
	    	ezshop.startReturnTransaction(1);
	    	//prod code non è in lista ticketentries
	        assertFalse(ezshop.returnProduct(1, "2905911158926", 2));
	        //quantità reso maggiore di quello acquistato
	    	assertFalse(ezshop.returnProduct(1, "12345678912237", 100));
	    	
	    	ezshop.logout();
	        assertThrows(UnauthorizedException.class, () -> {
	        	assertFalse(ezshop.returnProduct(1, "12345678912237", 2));
	        });
	    
	 }
	    
	    @Test
	    public void testValidReturnProduct() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidPaymentException {
        	ezshop.login("elisa", "elisa98");
        	ezshop.startSaleTransaction();
        	ezshop.addProductToSale(1, "12345678912237", 4);
        	ezshop.endSaleTransaction(1);
	    	ezshop.receiveCashPayment(1, 100);
	    	ezshop.startReturnTransaction(1);
	    	assertTrue(ezshop.returnProduct(1, "12345678912237", 2));
	    	
	    }
	    @Test
	    public void testInvalidEndReturnTransaction() throws UnauthorizedException, InvalidTransactionIdException, InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, InvalidQuantityException, InvalidPaymentException{
	    	ezshop.login("elisa", "elisa98");
	    	assertThrows(InvalidTransactionIdException.class, () -> {
	            ezshop.endReturnTransaction(null, true);
	        });
	        assertThrows(InvalidTransactionIdException.class, () -> {
	            ezshop.endReturnTransaction(-500, true);
	        });

	        //return trans nulla
	        assertFalse(ezshop.endReturnTransaction(1, true));

	    	ezshop.logout();
	        assertThrows(UnauthorizedException.class, () -> {
	            ezshop.endReturnTransaction(1, true);
	        });
	    
	 }
	    
	    @Test
	    public void testValidEndReturnTransaction() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidPaymentException {
        	ezshop.login("elisa", "elisa98");
        	ezshop.startSaleTransaction();
        	ezshop.addProductToSale(1, "12345678912237", 4);
        	ezshop.endSaleTransaction(1);
	    	ezshop.receiveCashPayment(1, 100);
	    	ezshop.startReturnTransaction(1);
	    	ezshop.returnProduct(1, "12345678912237", 2);
	    	assertTrue(ezshop.endReturnTransaction(1, true));
	    	assertTrue(ezshop.endReturnTransaction(1, false));
	    }
	    @Test
	    public void testInvalidDeleteReturnTransaction() throws UnauthorizedException, InvalidTransactionIdException, InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, InvalidQuantityException, InvalidPaymentException{
	    	ezshop.login("elisa", "elisa98");
	    	assertThrows(InvalidTransactionIdException.class, () -> {
	            ezshop.deleteReturnTransaction(null);
	        });
	        assertThrows(InvalidTransactionIdException.class, () -> {
	            ezshop.deleteReturnTransaction(-500);
	        });

	        //return trans nulla
	        assertFalse(ezshop.deleteReturnTransaction(1));
	        
	        //return già in stato pagato
	    	ezshop.startReturnTransaction(1);
	    	ezshop.endReturnTransaction(1, true);
	    	ezshop.returnCashPayment(1);
	    	assertFalse(ezshop.deleteReturnTransaction(1));
	    	
	    	ezshop.logout();
	        assertThrows(UnauthorizedException.class, () -> {
	            ezshop.deleteReturnTransaction(1);
	        });
	    
	 }
	    
	    @Test
	    public void testValidDeleteReturnTransaction() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidPaymentException {
        	ezshop.login("elisa", "elisa98");
        	ezshop.startSaleTransaction();
        	ezshop.addProductToSale(1, "12345678912237", 4);
        	ezshop.endSaleTransaction(1);
	    	ezshop.receiveCashPayment(1, 100);
	    	ezshop.startReturnTransaction(1);
	    	ezshop.returnProduct(1, "12345678912237", 2);
	    	ezshop.endReturnTransaction(1, true);
	    	assertTrue(ezshop.deleteReturnTransaction(1));
	    }
        @Test
        public void testInvalidReturnCashPayment() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidDiscountRateException, InvalidPaymentException, InvalidCreditCardException{
	    	ezshop.login("elisa", "elisa98");
	        assertThrows(InvalidTransactionIdException.class, () -> {
	        	ezshop.returnCashPayment(-500);
	        });
	        assertThrows(InvalidTransactionIdException.class, () -> {
	        	ezshop.returnCashPayment(null);
	        });

	        //return trans nulla
	     	assertEquals(ezshop.returnCashPayment(1), -1, 0);
	     	//return trans no chiusa
        	ezshop.startSaleTransaction();
        	ezshop.addProductToSale(1, "12345678912237", 4);
        	ezshop.endSaleTransaction(1);
	    	ezshop.receiveCashPayment(1, 100);
	    	ezshop.startReturnTransaction(1);
	     	assertEquals(ezshop.returnCashPayment(1), -1, 0);
	     	
	        ezshop.logout();
	        assertThrows(UnauthorizedException.class, () -> {
	        	ezshop.returnCashPayment(1);
	        });
        }
        
        @Test
        public void testValidReturnCashPayment() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidDiscountRateException, InvalidPaymentException, InvalidCreditCardException{
        	ezshop.login("elisa", "elisa98");
        	ezshop.startSaleTransaction();
        	ezshop.addProductToSale(1, "12345678912237", 4);
        	ezshop.endSaleTransaction(1);
	    	ezshop.receiveCashPayment(1, 100);
	    	ezshop.startReturnTransaction(1);
	    	ezshop.returnProduct(1, "12345678912237", 2);
	    	ezshop.endReturnTransaction(1, true);
	     	assertNotEquals(ezshop.returnCashPayment(1), -1, 0);
        }
        @Test
        public void testInvalidReturnCreditCardPayment() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidDiscountRateException, InvalidPaymentException, InvalidCreditCardException{
	    	ezshop.login("elisa", "elisa98");
	        assertThrows(InvalidTransactionIdException.class, () -> {
	        	ezshop.returnCreditCardPayment(-500, "4485370086510891");
	        });
	        assertThrows(InvalidTransactionIdException.class, () -> {
	        	ezshop.returnCreditCardPayment(null, "4485370086510891");
	        });
	        assertThrows(InvalidCreditCardException.class, () -> {
	        	ezshop.returnCreditCardPayment(1, null);
	        });
	        assertThrows(InvalidCreditCardException.class, () -> {
	        	ezshop.returnCreditCardPayment(1, "");
	        });
	        assertThrows(InvalidCreditCardException.class, () -> {
	        	ezshop.returnCreditCardPayment(1, "0891");
	        });
	        //return trans nulla
	     	assertEquals(ezshop.returnCreditCardPayment(1,"4485370086510891"), -1, 0);
	     	//return trans no chiusa
        	/*ezshop.startSaleTransaction();
        	ezshop.addProductToSale(1, "12345678912237", 4);
        	ezshop.endSaleTransaction(1);
	    	ezshop.receiveCashPayment(1, 100);
	    	ezshop.startReturnTransaction(1);
	     	assertEquals(ezshop.returnCreditCardPayment(1, "4485370086510891"), -1, 0);*/
	     	
	        ezshop.logout();
	        assertThrows(UnauthorizedException.class, () -> {
	        	ezshop.returnCreditCardPayment(1, "4485370086510891");
	        });
        }
        
        @Test
        public void testValidReturnCreditCardPayment()throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidDiscountRateException, InvalidPaymentException, InvalidCreditCardException{
        	ezshop.login("elisa", "elisa98");
        	ezshop.startSaleTransaction();
        	ezshop.addProductToSale(1, "12345678912237", 4);
        	ezshop.endSaleTransaction(1);
	    	ezshop.receiveCashPayment(1, 100);
	    	ezshop.startReturnTransaction(1);
	    	ezshop.returnProduct(1, "12345678912237", 2);
	    	ezshop.endReturnTransaction(1, true);
	     	assertNotEquals(ezshop.returnCreditCardPayment(1, "5100293991053009"), -1, 0);
        }
}