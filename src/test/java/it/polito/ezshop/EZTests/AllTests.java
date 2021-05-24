package it.polito.ezshop.EZTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestR1_User.class, TestR2_Order.class, TestR3_Customer.class,
                TestR4_BalanceOperation.class, TestR5_TicketEntry.class, TestR6_ProductType.class,
                TestR7_ReturnTransaction.class, TestR8_SaleTransaction.class, TestR9_OrderDb.class,
                TestR10_UserDb.class, TestR11_CustomerDb.class, TestR12_BalanceOperationDb.class,
                TestR14_ReturnTransactionDb.class, TestR15_SaleTransactionDb.class,
                TestR17_SaleTransactionEZ.class, TestR16_ProductTypeDb.class, TestWBDB.class,
                TestR18_ReturnTransactionEZ.class, TestR19_OrderEZ.class, TestR20_CustomerEZ.class,
                TestR21_BalanceOperationEZ.class, TestR23_ProductTypeEZ.class,
                TestR22_UserEZ.class})

public class AllTests {
}
