package it.polito.ezshop.acceptanceTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import it.polito.ezshop.utilsTest.*;
import it.polito.ezshop.whiteBoxTests.*;
@RunWith(Suite.class)
@SuiteClasses({AllTests.class, UtilsTest.class})
public class TestEZShop {
    
    
}
