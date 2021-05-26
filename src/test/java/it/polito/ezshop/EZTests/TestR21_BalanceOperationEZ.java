package it.polito.ezshop.EZTests;

import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.exceptions.InvalidCreditCardException;
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

public class TestR21_BalanceOperationEZ { 
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
		ezshop.createUser("diego", "diego96", "Cashier");
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
	public void testInvalidRecordBalanceUpdate()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
		ezshop.login("elisa", "elisa98");

		ezshop.login("elisa", "elisa98");
		ezshop.startSaleTransaction();
		ezshop.addProductToSale(1, "12345678912237", 4);
		ezshop.endSaleTransaction(1);
		assertFalse(ezshop.recordBalanceUpdate(-1000));
		ezshop.logout();
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.recordBalanceUpdate(10);
		});

		ezshop.logout();
		ezshop.login("diego", "diego96");
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.recordBalanceUpdate(10);
		});
	}

	@Test
	public void testValidRecordBalanceUpdate() throws UnauthorizedException,
			InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException,
			InvalidProductCodeException, InvalidQuantityException, InvalidCreditCardException {
		ezshop.login("elisa", "elisa98");
		ezshop.startSaleTransaction();
		ezshop.addProductToSale(1, "12345678912237", 4);
		ezshop.endSaleTransaction(1);
		ezshop.receiveCreditCardPayment(1, "4485370086510891");
		long start = System.currentTimeMillis();
		assertTrue(ezshop.recordBalanceUpdate(-8));
		assertEquals(ezshop.computeBalance(), 0, 0);
		assertTrue(ezshop.recordBalanceUpdate(8));
		assertEquals(ezshop.computeBalance(), 8, 0);
		long fine = System.currentTimeMillis();
		assertTrue(fine - start < 500);

	}

	@Test
	public void testGetCreditsAndDebits()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
		ezshop.login("elisa", "elisa98");
		long start = System.currentTimeMillis();
		assertTrue(ezshop.getCreditsAndDebits(null, null).isEmpty());
		long fine = System.currentTimeMillis();
		assertTrue(fine - start < 500);
		ezshop.recordBalanceUpdate(8);
		assertEquals(ezshop.getCreditsAndDebits(null, null).size(), 1, 0);
		ezshop.recordBalanceUpdate(-80);
		assertEquals(ezshop.getCreditsAndDebits(null, null).size(), 1, 0);
		ezshop.logout();
		ezshop.login("diego", "diego96");
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.getCreditsAndDebits(null, null);
		});
		ezshop.logout();
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.getCreditsAndDebits(null, null);
		});
	}

	@Test
	public void testComputeBalance()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
		ezshop.login("elisa", "elisa98");
		long start = System.currentTimeMillis();
		assertEquals(ezshop.computeBalance(), 0.0, 0);
		long fine = System.currentTimeMillis();
		assertTrue(fine - start < 500);
		ezshop.recordBalanceUpdate(100);
		assertEquals(ezshop.computeBalance(), 100, 0);

		ezshop.logout();
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.computeBalance();
		});

		ezshop.logout();
		ezshop.login("diego", "diego96");
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.computeBalance();
		});
	}



}
