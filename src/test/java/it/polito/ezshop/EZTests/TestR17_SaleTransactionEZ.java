package it.polito.ezshop.EZTests;

import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.ProductTypeImpl;
import it.polito.ezshop.data.SaleTransactionImpl;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.data.TicketEntryImpl;
import it.polito.ezshop.data.User;
import it.polito.ezshop.data.Utils;
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

public class TestR17_SaleTransactionEZ {

	EZShop ezshop = new EZShop();
	EZShopDb ezshopdb = new EZShopDb();
	Integer prodID;

	@Before
	public void setup() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException,
			InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException,
			InvalidLocationException {
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
	public void testvalidStartSaleTransaction()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException {
		ezshop.login("elisa", "elisa98");
		long start = System.currentTimeMillis();
		Integer saleID = ezshop.startSaleTransaction();
		long fine = System.currentTimeMillis();
		assertEquals(saleID, 1, 1);
		assertTrue(fine - start < 500);

	}

	@Test
	public void testInvalidAddProductToSale()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
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
		assertFalse(ezshop.addProductToSale(1, "12345678912237", 4000));
		ezshop.logout();
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.addProductToSale(1, "12345678912237", 4);
		});

	}

	@Test

	public void testInvalidAddProductToSalebyRFID()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
		ezshop.login("elisa", "elisa98");
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.addProductToSaleRFID(-1, "1234567891");
		});
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.addProductToSaleRFID(null, "1234567891");
		});
		assertThrows(InvalidRFIDException.class, () -> {
			ezshop.addProductToSaleRFID(1, "");
		});
		assertThrows(InvalidRFIDException.class, () -> {
			ezshop.addProductToSaleRFID(1, null);
		});
		assertThrows(InvalidRFIDException.class, () -> {
			ezshop.addProductToSaleRFID(1, "123456gtf7891");
		});
		assertThrows(InvalidRFIDException.class, () -> {
			ezshop.addProductToSaleRFID(1, "12345678915453");
		});

		ezshop.logout();
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.addProductToSale(1, "1234567891", 4);
		});

	}

	@Test
	public void testValidAddProductToSale() throws UnauthorizedException, InvalidUsernameException,
			InvalidPasswordException, InvalidTransactionIdException, InvalidProductCodeException,
			InvalidQuantityException, InvalidProductIdException {
		ezshop.login("elisa", "elisa98");
		ezshop.startSaleTransaction();
		assertTrue(ezshop.addProductToSale(1, "12345678912237", 4));
		assertEquals(ezshop.activeSaleTransaction.getPrice(), 8, 0);
		assertEquals(ezshop.getProductTypeByBarCode("12345678912237").getQuantity(), 46, 0);
		assertFalse(ezshop.activeSaleTransaction.getEntries().isEmpty());
		assertTrue(ezshop.addProductToSale(1, "12345678912237", 4));
		long start = System.currentTimeMillis();
		assertTrue(ezshop.deleteProductFromSale(1, "12345678912237", 1));
		assertEquals(ezshop.activeSaleTransaction.getEntries().stream()
				.filter(x -> x.getBarCode().equals("12345678912237")).findFirst().get().getAmount(),
				7, 0);
		assertEquals(ezshop.getProductTypeByBarCode("12345678912237").getQuantity(), 43, 0);
		long fine = System.currentTimeMillis();
		assertFalse(ezshop.deleteProductFromSale(1, "12345678912237", 1000));
		assertTrue(ezshop.deleteProductFromSale(1, "12345678912237", 7));
		assertEquals(ezshop.getProductTypeByBarCode("12345678912237").getQuantity(), 50, 0);
		assertEquals(ezshop.activeSaleTransaction.getPrice(), 0, 0);
		assertEquals(ezshop.activeSaleTransaction.getEntries().size(), 0, 0);
		assertTrue(fine - start < 500);
		assertFalse(ezshop.deleteProductFromSale(1, "12345678912237", 1000));
	}

	@Test
	/* TODO */
	public void testValidAddProductToSaleRFID()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			InvalidProductIdException, InvalidRFIDException, InvalidOrderIdException,
			InvalidLocationException, InvalidPricePerUnitException {
		ezshop.login("elisa", "elisa98");
		ezshop.recordBalanceUpdate(100);
		int orderId = ezshop.payOrderFor("12345678912237", 1, 4);
		ezshop.recordOrderArrivalRFID(orderId, "123456789111");
		ezshop.startSaleTransaction();
		long start = System.currentTimeMillis();
		assertTrue(ezshop.addProductToSaleRFID(1, "123456789111"));
		long fine = System.currentTimeMillis();
		assertTrue(fine - start < 500);
		assertEquals(ezshop.activeSaleTransaction.getPrice(), 2, 0);
		assertEquals(ezshop.getProductTypeByBarCode("12345678912237").getQuantity(), 50, 0);

	}

	@Test
	public void testInvalidDeleteProductFromSale()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
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
	public void testValidDeleteProductFromSaleRFID() throws UnauthorizedException,
			InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException,
			InvalidRFIDException, InvalidQuantityException, InvalidProductCodeException,
			InvalidPricePerUnitException, InvalidOrderIdException, InvalidLocationException {
		ezshop.login("elisa", "elisa98"); // TODO
		ezshop.recordBalanceUpdate(100);

		int orderId = ezshop.payOrderFor("12345678912237", 2, 4);
		ezshop.recordOrderArrivalRFID(orderId, "123456789111");

		int id = ezshop.startSaleTransaction();
		assertTrue(ezshop.addProductToSaleRFID(id, "123456789111"));
		assertTrue(ezshop.addProductToSaleRFID(id, "123456789112"));

		assertTrue(ezshop.deleteProductFromSaleRFID(id, "123456789111"));

	}

	@Test
	public void testInvalidDeleteProductFromSaleRFID() throws UnauthorizedException,
			InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException,
			InvalidRFIDException, InvalidQuantityException, InvalidProductCodeException,
			InvalidPricePerUnitException, InvalidOrderIdException, InvalidLocationException {
		ezshop.login("elisa", "elisa98"); // TODO
		ezshop.recordBalanceUpdate(100);

		int orderId = ezshop.payOrderFor("12345678912237", 2, 4);
		ezshop.recordOrderArrivalRFID(orderId, "123456789111");

		int id = ezshop.startSaleTransaction();
		assertTrue(ezshop.addProductToSaleRFID(id, "123456789111"));

		assertFalse(ezshop.deleteProductFromSaleRFID(id, "123456789112"));
		assertTrue(ezshop.deleteProductFromSaleRFID(id, "123456789111"));
		assertFalse(ezshop.deleteProductFromSaleRFID(id, "123456789111"));


		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.deleteProductFromSaleRFID(-1, "123456789111");
		});
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.deleteProductFromSaleRFID(null, "123456789111");
		});
		assertThrows(InvalidRFIDException.class, () -> {
			ezshop.deleteProductFromSaleRFID(1, null);
		});
		assertThrows(InvalidRFIDException.class, () -> {
			ezshop.deleteProductFromSaleRFID(1, "");
		});
		assertThrows(InvalidRFIDException.class, () -> {
			ezshop.deleteProductFromSaleRFID(1, "error00");
		});
		assertThrows(InvalidRFIDException.class, () -> {
			ezshop.deleteProductFromSaleRFID(1, "12345891223165167");
		});


		assertFalse(ezshop.deleteProductFromSaleRFID(1, "290591115822"));
		ezshop.logout();
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.deleteProductFromSaleRFID(1, "123456789111");
		});
	}

	@Test
	public void testInvalidApplyDiscountRateToProduct() throws UnauthorizedException,
			InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException,
			InvalidProductCodeException, InvalidQuantityException, InvalidDiscountRateException {
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
		// sale trans è null
		assertFalse(ezshop.applyDiscountRateToProduct(1, "12345678912237", 0.1));
		// prod id non c'è
		assertFalse(ezshop.applyDiscountRateToProduct(1, "2905911158926", 0.1));
		ezshop.logout();
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.applyDiscountRateToProduct(1, "12345678912237", 0.1);
		});
	}

	@Test
	public void testValidApplyDiscountRateToProduct() throws UnauthorizedException,
			InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException,
			InvalidProductCodeException, InvalidQuantityException, InvalidDiscountRateException {
		ezshop.login("elisa", "elisa98");
		ezshop.startSaleTransaction();
		ezshop.addProductToSale(1, "12345678912237", 4);
		long start = System.currentTimeMillis();
		assertTrue(ezshop.applyDiscountRateToProduct(1, "12345678912237", 0.1));
		long fine = System.currentTimeMillis();
		assertTrue(fine - start < 500);
		assertEquals(ezshop.activeSaleTransaction.getEntries().stream()
				.filter(x -> x.getBarCode().equals("12345678912237")).findFirst().get()
				.getDiscountRate(), 0.1, 0);

	}

	@Test
	public void testInvalidApplyDiscountRateToSale()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			InvalidDiscountRateException, InvalidPaymentException {
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
		// sale trans è null
		assertFalse(ezshop.applyDiscountRateToSale(1, 0.1));
		// sale id non c'è
		ezshop.startSaleTransaction();
		assertFalse(ezshop.applyDiscountRateToSale(100, 0.1));
		// sale trans già pagata
		ezshop.endSaleTransaction(1);
		ezshop.receiveCashPayment(1, 500);
		assertFalse(ezshop.applyDiscountRateToSale(1, 0.1));
		ezshop.logout();
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.applyDiscountRateToSale(1, 0.1);
		});
	}

	@Test
	public void testValidApplyDiscountRateToSale() throws UnauthorizedException,
			InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException,
			InvalidProductCodeException, InvalidQuantityException, InvalidDiscountRateException {
		ezshop.login("elisa", "elisa98");
		ezshop.startSaleTransaction();
		ezshop.addProductToSale(1, "12345678912237", 4);
		assertTrue(ezshop.applyDiscountRateToSale(1, 0.1));
		ezshop.endSaleTransaction(1);
		long start = System.currentTimeMillis();
		assertTrue(ezshop.applyDiscountRateToSale(1, 0.5));
		long fine = System.currentTimeMillis();
		assertTrue(fine - start < 500);
		assertEquals(ezshop.getSaleTransaction(1).getDiscountRate(), 0.5, 0);


	}

	@Test
	public void testInvalidComputePointsForSale() throws UnauthorizedException,
			InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException,
			InvalidProductCodeException, InvalidQuantityException, InvalidDiscountRateException {
		ezshop.login("elisa", "elisa98");
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.computePointsForSale(-1);
		});
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.computePointsForSale(null);
		});
		ezshop.logout();
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.computePointsForSale(1);
		});
	}

	@Test
	public void testValidComputePointsForSale()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			InvalidDiscountRateException, InvalidPaymentException {
		ezshop.login("elisa", "elisa98");
		ezshop.startSaleTransaction();
		ezshop.addProductToSale(1, "12345678912237", 5);
		// stato open
		assertEquals(ezshop.computePointsForSale(1), 0, 1);
		ezshop.addProductToSale(1, "12345678912237", 5);
		ezshop.endSaleTransaction(1);
		long start = System.currentTimeMillis();
		// stato closed
		assertEquals(ezshop.computePointsForSale(1), 0, 2);
		long fine = System.currentTimeMillis();
		assertTrue(fine - start < 500);
		// stato payed
		ezshop.receiveCashPayment(1, 500);
		assertEquals(ezshop.computePointsForSale(1), 0, 2);

	}

	@Test
	public void testInvalidEndSaleTransaction() throws UnauthorizedException,
			InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException,
			InvalidProductCodeException, InvalidQuantityException, InvalidDiscountRateException {
		ezshop.login("elisa", "elisa98");
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.endSaleTransaction(-500);
		});
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.endSaleTransaction(null);
		});
		assertFalse(ezshop.endSaleTransaction(1));
		// testo sale trans già chiusa
		ezshop.startSaleTransaction();
		ezshop.endSaleTransaction(1);
		assertFalse(ezshop.endSaleTransaction(1));
		ezshop.logout();
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.endSaleTransaction(1);
		});
	}

	@Test
	public void testValidEndSaleTransaction() throws UnauthorizedException,
			InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException,
			InvalidProductCodeException, InvalidQuantityException, InvalidDiscountRateException {
		ezshop.login("elisa", "elisa98");
		ezshop.startSaleTransaction();
		ezshop.addProductToSale(1, "12345678912237", 4);
		long start = System.currentTimeMillis();
		assertTrue(ezshop.endSaleTransaction(1));
		long fine = System.currentTimeMillis();
		assertTrue(fine - start < 500);
		assertEquals(ezshop.getSaleTransaction(1).getPrice(), 8, 0);

	}

	@Test
	public void testInvalidDeleteSaleTransaction()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			InvalidDiscountRateException, InvalidPaymentException {
		ezshop.login("elisa", "elisa98");
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.deleteSaleTransaction(-500);
		});
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.deleteSaleTransaction(null);
		});
		// sale trans nulla
		assertFalse(ezshop.deleteSaleTransaction(1));
		// testo sale trans già pagata
		ezshop.startSaleTransaction();
		ezshop.endSaleTransaction(1);
		ezshop.receiveCashPayment(1, 100);
		assertFalse(ezshop.deleteSaleTransaction(1));
		ezshop.logout();
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.deleteSaleTransaction(1);
		});
	}

	@Test
	public void testValidDeleteSaleTransaction() throws UnauthorizedException,
			InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException,
			InvalidProductCodeException, InvalidQuantityException, InvalidDiscountRateException {
		ezshop.login("elisa", "elisa98");
		ezshop.startSaleTransaction();
		ezshop.addProductToSale(1, "12345678912237", 4);
		ezshop.endSaleTransaction(1);
		long start = System.currentTimeMillis();
		assertTrue(ezshop.deleteSaleTransaction(1));
		long fine = System.currentTimeMillis();
		assertEquals(ezshop.getProductTypeByBarCode("12345678912237").getQuantity(), 50, 0);
		assertTrue(fine - start < 500);
	}

	@Test
	public void testInvalidGetSaleTransaction()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			InvalidDiscountRateException, InvalidPaymentException {
		ezshop.login("elisa", "elisa98");
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.getSaleTransaction(-500);
		});
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.getSaleTransaction(null);
		});
		// sale trans nulla
		assertNull(ezshop.getSaleTransaction(1));
		ezshop.logout();
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.deleteSaleTransaction(1);
		});
	}

	@Test
	public void testValidGetSaleTransaction() throws UnauthorizedException,
			InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException,
			InvalidProductCodeException, InvalidQuantityException, InvalidDiscountRateException {
		ezshop.login("elisa", "elisa98");
		ezshop.startSaleTransaction();
		ezshop.endSaleTransaction(1);
		long start = System.currentTimeMillis();
		assertNotNull(ezshop.getSaleTransaction(1));
		long fine = System.currentTimeMillis();
		assertTrue(fine - start < 500);

	}

	@Test
	public void testInvalidReceiveCashPayment()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			InvalidDiscountRateException, InvalidPaymentException {
		ezshop.login("elisa", "elisa98");
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.receiveCashPayment(-500, 500);
		});
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.receiveCashPayment(null, 500);
		});
		assertThrows(InvalidPaymentException.class, () -> {
			ezshop.receiveCashPayment(1, -50);
		});
		assertThrows(InvalidPaymentException.class, () -> {
			ezshop.receiveCashPayment(1, 0);
		});
		// sale trans nulla
		assertEquals(ezshop.receiveCashPayment(1, 500), -1, 0);
		// sale trans già pagata
		ezshop.startSaleTransaction();
		ezshop.addProductToSale(1, "12345678912237", 4);
		ezshop.endSaleTransaction(1);
		ezshop.receiveCashPayment(1, 500);
		assertEquals(ezshop.receiveCashPayment(1, 500), -1, 0);

		ezshop.logout();
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.receiveCashPayment(1, 500);
		});
	}

	@Test
	public void testValidReceiveCashPayment()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			InvalidDiscountRateException, InvalidPaymentException {
		ezshop.login("elisa", "elisa98");
		ezshop.startSaleTransaction();
		ezshop.addProductToSale(1, "12345678912237", 4);
		ezshop.endSaleTransaction(1);
		long start = System.currentTimeMillis();
		assertEquals(ezshop.receiveCashPayment(1, 10), 2, 0);
		long fine = System.currentTimeMillis();
		assertEquals(ezshop.computeBalance(), 8, 0);
		assertTrue(fine - start < 500);

	}

	@Test
	public void testInvalidReceiveCreditCardPayment()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			InvalidDiscountRateException, InvalidPaymentException, InvalidCreditCardException {
		ezshop.login("elisa", "elisa98");
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.receiveCreditCardPayment(-500, "4485370086510891");
		});
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.receiveCreditCardPayment(null, "4485370086510891");
		});

		assertThrows(InvalidCreditCardException.class, () -> {
			ezshop.receiveCreditCardPayment(1, null);
		});
		assertThrows(InvalidCreditCardException.class, () -> {
			ezshop.receiveCreditCardPayment(1, "");
		});
		assertThrows(InvalidCreditCardException.class, () -> {
			ezshop.receiveCreditCardPayment(1, "0891");
		});
		// sale trans nulla
		assertFalse(ezshop.receiveCreditCardPayment(1, "4485370086510891"));
		// not enough money
		ezshop.startSaleTransaction();
		ezshop.addProductToSale(1, "12345678912237", 4);
		ezshop.endSaleTransaction(1);
		assertFalse(ezshop.receiveCreditCardPayment(1, "4716258050958645"));
		// sale trans già pagata
		ezshop.receiveCreditCardPayment(1, "4485370086510891");
		assertFalse(ezshop.receiveCreditCardPayment(1, "4485370086510891"));

		ezshop.logout();
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.receiveCreditCardPayment(1, "4485370086510891");
		});
	}

	@Test
	public void testValidReceiveCreditCardPayment()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			InvalidDiscountRateException, InvalidPaymentException, InvalidCreditCardException {
		ezshop.login("elisa", "elisa98");
		ezshop.startSaleTransaction();
		ezshop.addProductToSale(1, "12345678912237", 4);
		ezshop.endSaleTransaction(1);
		long start = System.currentTimeMillis();
		assertTrue(ezshop.receiveCreditCardPayment(1, "4485370086510891"));
		long fine = System.currentTimeMillis();
		assertTrue(fine - start < 500);
		assertEquals(ezshop.computeBalance(), 8, 0);

	}



}
