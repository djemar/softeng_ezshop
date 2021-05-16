package it.polito.ezshop.whiteBoxTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestR1_User.class, TestR2_Order.class, TestR3_Customer.class})

public class AllTests {
}