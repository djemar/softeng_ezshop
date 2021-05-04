package it.polito.ezshop;

import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.UserImpl;
import it.polito.ezshop.view.EZShopGUI;

public class EZShop {

    public static void main(String[] args) {
        EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
        EZShopGUI gui = new EZShopGUI(ezShop);

        testDb();

    }

    public static void testDb() { // test function for db implementation
        EZShopDb ezshopDb = new EZShopDb();
        ezshopDb.createConnection();
        ezshopDb.insertUser(new UserImpl("john", "pippo", "tester", 0));
        ezshopDb.closeConnection();
    }

}
