package it.polito.ezshop.EZTests;
import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.ProductTypeImpl;
import it.polito.ezshop.data.SaleTransactionImpl;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.data.TicketEntryImpl;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
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
	 Integer prodID;

	    @Before
	    public void setup() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException {
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
	    public void testInvalidStartSaleTransaction() throws UnauthorizedException {
	        ezshop.logout();
	        assertThrows(UnauthorizedException.class, () -> {
	            ezshop.startSaleTransaction();
	        });
	    }
	    @Test
	    public void validStartSaleTransaction() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException {
	    	ezshop.login("elisa", "elisa98");
	   	    Integer saleID = ezshop.startSaleTransaction();
	    	assertEquals(saleID, 1, 1);
	    	
	    }
	    @Test
	    public void testInvalidAddProductToSale() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
	    	ezshop.login("elisa", "elisa98");
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
	    	ezshop.login("elisa", "elisa98");
	    	ezshop.startSaleTransaction();
	    	assertTrue(ezshop.addProductToSale(1, "12345678912237", 4));        
	    	assertFalse(ezshop.activeSaleTransaction.getEntries().isEmpty());
	    	assertTrue(ezshop.deleteProductFromSale(1, "12345678912237", 1));
	    	//assertFalse(ezshop.deleteProductFromSale(1, "12345678912237", 1000));
	    }
	    @Test
	    public void testInvalidDeleteProductFromSale() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
	    	ezshop.login("elisa", "elisa98");
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
	        @Test
	        public void testInvalidApplyDiscountRateToProduct() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidDiscountRateException{
		    	ezshop.login("elisa", "elisa98");
		        assertThrows(InvalidTransactionIdException.class, () -> {
		        	ezshop.applyDiscountRateToProduct(-1, "12345678912237", 0.5);
		        });
		        assertThrows(InvalidTransactionIdException.class, () -> {
		        	ezshop.applyDiscountRateToProduct(null, "12345678912237", 0.5);
		        });
		        assertThrows(InvalidProductCodeException.class, () -> {
		        	ezshop.applyDiscountRateToProduct(1, "123456712237", 0.5);
		        });
		        assertThrows(InvalidProductCodeException.class, () -> {
		        	ezshop.applyDiscountRateToProduct(1, "", 0.5);
		        });
		        assertThrows(InvalidProductCodeException.class, () -> {
		        	ezshop.applyDiscountRateToProduct(1, null, 0.5);
		        });
		        assertThrows(InvalidDiscountRateException.class, () -> {
		        	ezshop.applyDiscountRateToProduct(1, "12345678912237", 500);
		        });

		        assertThrows(InvalidDiscountRateException.class, () -> {
		        	ezshop.applyDiscountRateToProduct(1, "12345678912237", -500);
		        });
		        //sale trans è null
		        assertFalse(ezshop.applyDiscountRateToProduct(1, "12345678912237", 0.1));
		        //prod id non c'è
		        assertFalse(ezshop.applyDiscountRateToProduct(1, "2905911158926", 0.1));
	        }
	        @Test
	        public void testValidApplyDiscountRateToProduct() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidDiscountRateException{
		    	ezshop.login("elisa", "elisa98");
		    	ezshop.startSaleTransaction();
		    	ezshop.addProductToSale(1, "12345678912237", 4);  
		        assertTrue(ezshop.applyDiscountRateToProduct(1, "12345678912237", 0.1));

	        }
	        @Test
	        public void testInvalidApplyDiscountRateToSale() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidDiscountRateException{
		    	ezshop.login("elisa", "elisa98");
		        assertThrows(InvalidTransactionIdException.class, () -> {
		        	ezshop.applyDiscountRateToSale(-1, 0.5);
		        });
		        assertThrows(InvalidTransactionIdException.class, () -> {
		        	ezshop.applyDiscountRateToSale(null, 0.5);
		        });

		        assertThrows(InvalidDiscountRateException.class, () -> {
		        	ezshop.applyDiscountRateToSale(1, 500);
		        });

		        assertThrows(InvalidDiscountRateException.class, () -> {
		        	ezshop.applyDiscountRateToSale(1, -500);
		        });
		        //sale trans è null
		        assertFalse(ezshop.applyDiscountRateToSale(1, 0.1));
		        //prod id non c'è
		        assertFalse(ezshop.applyDiscountRateToSale(1, 0.1));
	        }
	        @Test
	        public void testValidApplyDiscountRateToSale() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidDiscountRateException{
		    	ezshop.login("elisa", "elisa98");
		    	ezshop.startSaleTransaction();
		    	ezshop.addProductToSale(1, "12345678912237", 4);  
		        assertTrue(ezshop.applyDiscountRateToSale(1, 0.1));
		        ezshop.endSaleTransaction(1);
		        assertTrue(ezshop.applyDiscountRateToSale(1, 0.5));

	        }
	        
	    
	    
	    
	    
	    
	
}
