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
        UserImpl User0 = new UserImpl("john", "pippo", "tester", 0);
        UserImpl User1 = new UserImpl("tom", "pippo", "tester", 1);
        UserImpl User2 = new UserImpl("tommy", "pippo", "cashier", 2);
        ezshopDb.insertUser(User0);
        ezshopDb.insertUser(User1);
        ezshopDb.insertUser(User2);
        ezshopDb.deleteUser(0);
        ezshopDb.getUser(1);
        ezshopDb.updateUserRights(1,"engineer") ;
        ezshopDb.closeConnection();

    }

}
