package it.polito.ezshop.utilsTest;

import org.junit.Test;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.CustomerImpl;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.data.TicketEntryImpl;
import it.polito.ezshop.data.Utils;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Before;

public class UtilsTest {
    Utils u = new Utils();
    List<TicketEntry> list;
    List<Customer> listC;
    static String file;
    static {
        File outFile;

        try {
            outFile = File.createTempFile("creditcards", ".txt");
            outFile.deleteOnExit();
            System.out.println("Extracting data file: " + outFile.getAbsolutePath());
            try (InputStream in = UtilsTest.class.getResourceAsStream("creditcards.txt");
                    FileOutputStream out = new FileOutputStream(outFile)) {
                byte[] b = new byte[2048];
                int n = 0;
                while ((n = in.read(b)) != -1) {
                    out.write(b, 0, n);
                }
                file = outFile.getCanonicalPath();
            }
        } catch (IOException e) {
            file = null;
            System.err.println(e);
            outFile = null;
        }
    }

    @Before
    public void setUp() throws IOException {
        list = new ArrayList<TicketEntry>();
        listC = new ArrayList<Customer>();
        TicketEntry t1 = new TicketEntryImpl("12345678912237", "Test", 1, 1, 0);
        TicketEntry t2 = new TicketEntryImpl("2905911158926", "Test", 1, 1, 0);
        TicketEntry t3 = new TicketEntryImpl("65164684113337", "Test", 1, 1, 0);
        Customer c1 = new CustomerImpl("Elisa");
        Customer c2 = new CustomerImpl("Diego");
        list.add(t1);
        list.add(t2);
        list.add(t3);
        listC.add(c1);
        listC.add(c2);
    }

    @Test
    public void testValidBarcode() {
        assertTrue(Utils.validateBarcode("12345678912237"));
        assertTrue(Utils.validateBarcode("2905911158926"));
        assertTrue(Utils.validateBarcode("65164684113337"));
        assertTrue(Utils.validateBarcode("9780201379655"));
    }
    
    @Test
    public void testInvalidBarcode() {
        assertFalse(Utils.validateBarcode("2905911158927"));
        assertThrows(NullPointerException.class, () -> {
            Utils.validateBarcode(null);
        });
    }

    @Test
    public void WBtestInvalidLengthBarCode() {
        assertFalse(Utils.validateBarcode("9780205451379654"));
        assertFalse(Utils.validateBarcode("29059158")); 
        assertFalse(Utils.validateBarcode(""));
    }
    
    @Test
    public void WBtestInvalidDigitBarCode() {
    assertFalse(Utils.validateBarcode("978020sddd379655"));
    }

    @Test
    public void testOnlyDigit() {
        assertTrue(Utils.isOnlyDigit("9780201379654"));
        assertTrue(Utils.isOnlyDigit("6489"));
    }

    @Test
    public void testNotOnlyDigit() {
        assertFalse(Utils.isOnlyDigit("97802013796s4"));
        assertFalse(Utils.isOnlyDigit("sf"));
        assertThrows(NullPointerException.class, () -> {
            Utils.isOnlyDigit(null);
        });
    }

    @Test
    public void testValidLuhnCreditCard() {
        assertTrue(Utils.validateCreditCard("5255189604838575"));
        assertTrue(Utils.validateCreditCard("4265645498582430"));
        assertTrue(Utils.validateCreditCard("377950544155089"));
    }

    @Test
    public void WBtestInvalidLengthCreditCard() {
        assertFalse(Utils.validateCreditCard("377950544"));
        assertFalse(Utils.validateCreditCard("377950544155089195839583945938593"));
        assertFalse(Utils.validateCreditCard("52551896075"));
    }

    @Test
    public void testInvalidLuhnCreditCard() {
        assertFalse(Utils.validateCreditCard("4265645498582432"));
        assertFalse(Utils.validateCreditCard("3779505441550891"));
        assertFalse(Utils.validateCreditCard(""));
        assertThrows(NullPointerException.class, () -> {
            Utils.validateCreditCard(null);
        });
    }

    @Test
    public void testRegisteredCreditCard() {
        assertTrue(Utils.fromFile("4485370086510891", 20, file));
        assertTrue(Utils.fromFile("5100293991053009", 5, file));
    }
    @Test
    public void WBtestInvalidAmountCreditCard() {
        assertFalse(Utils.fromFile("4716258050958645", 200, file));
        assertFalse(Utils.fromFile("5100293991053009", 50, file));
    }

    @Test
    public void testUnregisteredCreditCard() {
        assertThrows(NullPointerException.class, () -> {
            Utils.fromFile("5100293991053009", 50, null);
        });
        assertFalse(Utils.fromFile("", 50, file));
        assertFalse(Utils.fromFile("5100293991053009", 50, ""));
    }

    @Test
    public void testValidUpdateFile() {
        assertTrue(Utils.updateFile(file, "4485370086510891", 20));
    }

    @Test
    public void WBtestInvalidUFile() {
        assertFalse(Utils.updateFile("creditards.txt", "5255189604838575", 20));
        assertFalse(Utils.updateFile(file, "5255189604838570", 20));
        assertFalse(Utils.updateFile(file, "5100293991053009", 42));       
    }
    @Test
    public void testInvalidUpdateFile() {
        assertFalse(Utils.updateFile("creditards.txt", "5255189604838575", 20));
        assertThrows(NullPointerException.class, () -> {
            Utils.updateFile(null, "5100293991053009", 42);
        });
        assertFalse(Utils.updateFile("", "5100293991053009", 42));
    }

    @Test
    public void testContainsProduct() {
        assertTrue(Utils.containsProduct(list, "2905911158926"));
    }

    @Test
    public void testDoesntContainProduct() {
        assertFalse(Utils.containsProduct(list, "2905911058926"));
        assertThrows(NullPointerException.class, () -> {
            Utils.containsProduct(null, "2905911058926");
        });
        assertFalse(Utils.containsProduct(list, null));
    }

    @Test
    public void testGetProductFromEntries() {
        assertNotNull(Utils.getProductFromEntries(list, "65164684113337"));
        assertThrows(NoSuchElementException.class, () -> {
            Utils.getProductFromEntries(list, "productCode");
        });
        assertThrows(NullPointerException.class, () -> {
            Utils.getProductFromEntries(null, "productCode");
        });
        assertThrows(NoSuchElementException.class, () -> {
            Utils.getProductFromEntries(list, null);
        });
    }
    @Test
    public void testValidContainsCustomer() {
    	assertTrue(Utils.containsCustomer(listC, "Elisa"));
    }
    
    @Test
    public void testInvalidContainsCustomer() {
    	assertFalse(Utils.containsCustomer(listC, "Eli"));
        assertThrows(NullPointerException.class, () -> {
            Utils.containsCustomer(null, "Elisa");
        });
        assertFalse(Utils.containsCustomer(listC, null));
    	
    }

}
