package it.polito.ezshop.utilsTest;

import org.junit.Test;
import it.polito.ezshop.utils.Utils;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Before;

public class UtilsTest {

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
    public void setUp() throws IOException {}

    @Test
    public void testValidBarcode() {
        assertTrue(Utils.validateBarcode("12345678912237"));
        assertTrue(Utils.validateBarcode("2905911158926"));
        assertTrue(Utils.validateBarcode("65164684113337"));
        assertTrue(Utils.validateBarcode("9780201379655"));
    }

    @Test
    public void testInvalidBarcode() {
        assertFalse(Utils.validateBarcode("9780205451379654"));
        assertFalse(Utils.validateBarcode("29059158"));
        assertFalse(Utils.validateBarcode("2905911158927"));
        assertFalse(Utils.validateBarcode("978020s379655"));
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
    }

    @Test
    public void testValidLuhnCreditCard() {
        assertTrue(Utils.validateCreditCard("5255189604838575"));
        assertTrue(Utils.validateCreditCard("4265645498582430"));
        assertTrue(Utils.validateCreditCard("377950544155089"));
    }

    @Test
    public void testInvalidLuhnCreditCard() {
        assertFalse(Utils.validateCreditCard("52551896075"));
        assertFalse(Utils.validateCreditCard("4265645498582432"));
        assertFalse(Utils.validateCreditCard("3779505441550891"));
    }



    @Test
    public void testRegisteredCreditCard() {
        assertTrue(Utils.fromFile("4485370086510891", 20, file));
        assertTrue(Utils.fromFile("5100293991053009", 5, file));
    }

    @Test
    public void testUnregisteredCreditCard() {
        assertFalse(Utils.fromFile("5255189604838575", 20, file));
        assertFalse(Utils.fromFile("5100293991053009", 50, file));
    }

    @Test
    public void testValidUpdateFile() {
        assertTrue(Utils.updateFile(file, "4485370086510891", 20));
    }

    @Test
    public void testInvalidUpdateFile() {
        // assertFalse(Utils.updateFile("creditards.txt", "5255189604838575", 20));
        assertFalse(Utils.updateFile(file, "5255189604838570", 20));
        assertFalse(Utils.updateFile(file, "5100293991053009", 42));
    }

}
