package it.polito.ezshop.acceptanceTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import it.polito.ezshop.EZTests.*;
import it.polito.ezshop.utilsTest.*;
@RunWith(Suite.class)
@SuiteClasses({AllTests.class, UtilsTest.class})
public class TestEZShop {
    
    
}
