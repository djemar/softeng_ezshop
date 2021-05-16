package it.polito.ezshop.whiteBoxTests;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;

import it.polito.ezshop.data.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestR4_BalanceOperation {
	
	public void testGetSetBalanceID() {
		  BalanceOperation balanceOp = new BalanceOperationImpl(1, LocalDate.now(), 10.5, "CREDIT");
		  assertEquals(balanceOp.getBalanceId(), 1);
		  balanceOp.setBalanceId(3);
		  assertEquals(balanceOp.getBalanceId(), 3);
	}
	public void testGetSetDate() {
		  BalanceOperation balanceOp = new BalanceOperationImpl(1, LocalDate.now(), 10.5, "CREDIT");
		  assertEquals(balanceOp.getDate(), LocalDate.now());
		  balanceOp.setDate(LocalDate.of(2020, 5, 1));
		  assertEquals(balanceOp.getDate(), LocalDate.of(2020, 5, 1));
	}
	public void testGetSetMoney() {
		  BalanceOperation balanceOp = new BalanceOperationImpl(1, LocalDate.now(), 10.5, "CREDIT");
		  assertEquals(balanceOp.getMoney(), 10.5, 0);
		  balanceOp.setMoney(20.5);
		  assertEquals(balanceOp.getMoney(), 20.5, 0);
	}
	public void testGetSetType() {
		  BalanceOperation balanceOp = new BalanceOperationImpl(1, LocalDate.now(), 10.5, "CREDIT");
		  assertEquals(balanceOp.getType(), "CREDIT");
		  balanceOp.setType("DEBIT");
		  assertEquals(balanceOp.getType(), "DEBIT");
	}
}
