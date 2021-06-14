package it.polito.ezshop.EZTests;
import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.exceptions.InvalidCreditCardException;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPaymentException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRFIDException;
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
	    	long start = System.currentTimeMillis();
        	assertEquals(ezshop.startReturnTransaction(1), -1, 0);
	    	long fine = System.currentTimeMillis();
	    	assertTrue(fine-start< 500);
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
	    	assertFalse(ezshop.returnProduct(100, "12345678912237", 2));
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
	    	long start = System.currentTimeMillis();
	    	assertTrue(ezshop.returnProduct(1, "12345678912237", 2));
	    	assertEquals(ezshop.activeReturnTransaction.getTotal(), 4, 0);
	    	assertEquals(ezshop.activeReturnTransaction.getReturnedProductsMap().size(), 1, 0);
	    	long fine = System.currentTimeMillis();
	    	assertTrue(fine-start< 500);

	    	
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
	    	assertEquals(ezshop.getProductTypeByBarCode("12345678912237").getQuantity(), 48, 0);
	    	assertEquals(ezshop.getSaleTransaction(1).getPrice(), 4 , 0);
	    	assertEquals(ezshop.getSaleTransaction(1)
	    			.getEntries().stream().filter(x->x.getBarCode()
	    					.equals("12345678912237")).findFirst().get().getAmount(), 2 , 0);
	    	long start = System.currentTimeMillis();
	    	assertTrue(ezshop.endReturnTransaction(1, false));
	    	long fine = System.currentTimeMillis();
	    	assertTrue(fine-start< 500);

	    }
	    @Test
	    public void testInvalidDeleteReturnTransaction() throws UnauthorizedException, InvalidTransactionIdException, InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, InvalidQuantityException, InvalidPaymentException, InvalidCreditCardException{
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
        	ezshop.startSaleTransaction();
        	ezshop.addProductToSale(1, "12345678912237", 4);
        	ezshop.endSaleTransaction(1);
	    	ezshop.receiveCashPayment(1, 100);
	    	ezshop.startReturnTransaction(1);
	    	ezshop.returnProduct(1, "12345678912237", 2);
	    	ezshop.endReturnTransaction(1, true);
	    	ezshop.returnCreditCardPayment(1, "4485370086510891");
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
	    	long start = System.currentTimeMillis();
	    	assertTrue(ezshop.deleteReturnTransaction(1));
	    	assertEquals(ezshop.getProductTypeByBarCode("12345678912237").getQuantity(), 46, 0);
	    	assertEquals(ezshop.getSaleTransaction(1).getPrice(), 8 , 0);
	    	assertEquals(ezshop.getSaleTransaction(1)
	    			.getEntries().stream().filter(x->x.getBarCode()
	    					.equals("12345678912237")).findFirst().get().getAmount(), 4 , 0);
	    	long fine = System.currentTimeMillis();
	    	assertTrue(fine-start< 500);

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
	     	//return trans già pagata
        	ezshop.startSaleTransaction();
        	ezshop.startSaleTransaction();
        	ezshop.addProductToSale(1, "12345678912237", 4);
        	ezshop.endSaleTransaction(1);
	    	ezshop.receiveCashPayment(1, 100);
	    	ezshop.startReturnTransaction(1);
	    	ezshop.returnProduct(1, "12345678912237", 2);
	    	ezshop.endReturnTransaction(1, true);
	    	ezshop.returnCashPayment(1);
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
	    	long start = System.currentTimeMillis();
	     	assertEquals(ezshop.returnCashPayment(1), 4, 0);
	    	long fine = System.currentTimeMillis();
	    	assertTrue(fine-start< 500);

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
	     	

        	ezshop.startSaleTransaction();
        	ezshop.addProductToSale(1, "12345678912237", 4);
        	ezshop.endSaleTransaction(1);
	    	ezshop.receiveCashPayment(1, 100);
	    	ezshop.startReturnTransaction(1);
	    	ezshop.returnProduct(1, "12345678912237", 2);
	    	ezshop.endReturnTransaction(1, true);
	    	//credit card doesn't exist in the system
	    	assertEquals(ezshop.returnCreditCardPayment(1, "374245455400126"), -1, 0);
	    	ezshop.returnCreditCardPayment(1, "4485370086510891");
	     	// TODO return trans già pagata, ma sarà da mettere??
	     	assertEquals(ezshop.returnCreditCardPayment(1, "4485370086510891"), -1, 0);
	     	
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
	    	long start = System.currentTimeMillis();
	     	assertNotEquals(ezshop.returnCreditCardPayment(1, "5100293991053009"), -1, 0);
	    	long fine = System.currentTimeMillis();
	    	assertTrue(fine-start< 500);

        }
        @Test
    	public void testInvalidReturnProdRFID() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidQuantityException, InvalidPaymentException, InvalidOrderIdException, InvalidRFIDException {
    		ezshop.createUser("admin", "admin", "Administrator");
    		
    		assertThrows(UnauthorizedException.class,()->ezshop.returnProductRFID(10,"000000000000"));
    		ezshop.login("admin", "admin");
    		
    		assertThrows(InvalidTransactionIdException.class,()->ezshop.returnProductRFID(null,"000000000000"));
    		assertThrows(InvalidTransactionIdException.class,()->ezshop.returnProductRFID(0,"000000000000"));
    		assertThrows(InvalidTransactionIdException.class,()->ezshop.returnProductRFID(-10,"000000000000"));
    		assertThrows(InvalidRFIDException.class,()->ezshop.returnProductRFID(10,null));
    		assertThrows(InvalidRFIDException.class,()->ezshop.returnProductRFID(10,""));
    		assertThrows(InvalidRFIDException.class,()->ezshop.returnProductRFID(10,"00"));
    		assertThrows(InvalidRFIDException.class,()->ezshop.returnProductRFID(10,"0ab0"));
    		
    		assertFalse(ezshop.returnProductRFID(10,"000000000000"));
    				
    	}
    	@Test
    	public void testValidReturnProdRFID() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidQuantityException, InvalidPaymentException, InvalidRFIDException, InvalidOrderIdException {
        	ezshop.login("elisa", "elisa98");
    		int pid = ezshop.createProductType("Eggs","234829476238",2.0,"note1");
    		ezshop.updatePosition(pid, "33-H-12");
    		ezshop.recordBalanceUpdate(100.00);
    		int id=ezshop.issueOrder("234829476238",10,3.50);
    		ezshop.payOrder(id);
    		ezshop.recordOrderArrivalRFID(id,"000000000001");
    		int saleId = ezshop.startSaleTransaction();
    		ezshop.addProductToSaleRFID(saleId,"000000000001");
    		ezshop.addProductToSaleRFID(saleId,"000000000002");
    		ezshop.addProductToSaleRFID(saleId,"000000000004");
    		ezshop.endSaleTransaction(saleId);
    		ezshop.receiveCashPayment(saleId, 50);
    		Integer retId =ezshop.startReturnTransaction(saleId);
    		
    		assertFalse(ezshop.returnProductRFID(retId,"000000000000"));
    		assertTrue(ezshop.returnProductRFID(retId,"000000000004"));
    		assertTrue(ezshop.returnProductRFID(retId,"000000000002"));
    		assertFalse(ezshop.returnProductRFID(retId,"000000000003"));	
    	}
}