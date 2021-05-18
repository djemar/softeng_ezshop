package it.polito.ezshop.whiteBoxTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestR1_User.class, TestR2_Order.class, TestR3_Customer.class, 
	TestR4_BalanceOperation.class, TestR5_TicketEntry.class, TestR6_ProductType.class,
	TestR7_ReturnTransaction.class, TestR8_SaleTransaction.class, TestR9_OrderDb.class,
	TestR10_UserDb.class, TestR11_CustomerDb.class, TestR12_BalanceOperationDb.class,
    TestR14_ReturnTransactionDb.class,TestR15_SaleTransactionDb.class,
	TestR16_ProductTypeDb.class})

public class AllTests {
}