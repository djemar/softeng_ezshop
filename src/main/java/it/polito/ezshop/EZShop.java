package it.polito.ezshop;

import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.Utils;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.view.EZShopGUI;

public class EZShop {

        public static void main(String[] args) throws UnauthorizedException {
                EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
                EZShopGUI gui = new EZShopGUI(ezShop);


        }

        public static void testDb() { // test function for db implementation
                EZShopDb ezshopDb = new EZShopDb();
                ezshopDb.createConnection();
                // ezshopDb.resetTables();

                // ProductTypeImpl product0 = new ProductTypeImpl(4, "panino alla nutella",
                // "6291041500213", 5, "nutella");
                // ezshopDb.insertProductType(product0);
                // ezshopDb.getUser(1);
                // ezshopDb.updateUserRights(1,"engineer");
                // ezshopDb.checkCredentials("toms", "pippo");
                // ezshopDb.getUserId();
                // ezshopDb.getUserbyName("tommy");
                // ezshopDb.getUserbyName("ms");
                // ProductTypeImpl product0 = new ProductTypeImpl(4, "panino alla nutella",
                // "4562738492", 5, "nutella");

                // UserImpl User0 = new UserImpl("john", "pippo", "tester", 0);
                // UserImpl User1 = new UserImpl("tom", "pippo", "tester", 1);
                // UserImpl User2 = new UserImpl("tommy", "pippo", "cashier", 2);
                // ezshopDb.insertUser(User0);
                // ezshopDb.insertUser(User1);
                // ezshopDb.insertUser(User2);
                // ezshopDb.deleteUser(0);
                // ezshopDb.getUser(1);
                // ezshopDb.updateUserRights(1,"engineer");

                // ProductTypeImpl product0 = new ProductTypeImpl(0, "panino alla nutella",
                // "4562738492", 5, "nutella");
                // ProductTypeImpl product1 = new ProductTypeImpl(1, "formaggio", "45662738492", 6,
                // "fontina");
                // ProductTypeImpl product2 = new ProductTypeImpl(2, "cioccolato", "45668492", 3,
                // "fondente");
                // ProductTypeImpl product3 = new ProductTypeImpl(3, "cioccolato", "45668yg92", 3,
                // "bianco");
                // ezshopDb.insertProductType(product0);
                // ezshopDb.insertProductType(product1);
                // ezshopDb.insertProductType(product2);
                // ezshopDb.insertProductType(product3);

                // ezshopDb.updateProductType(0, "pasta", "2546785", 6 , "carbonara");
                // ezshopDb.deleteProductType(1);
                // ezshopDb.getAllProductTypes();
                // ezshopDb.getProductTypeByBarCode("45668492"); //description=cioccolato
                // ezshopDb.getProductTypesByDescription("cioccolato"); //id=2 id=3
                // ezshopDb.updateQuantity(2, 10);
                // ezshopDb.updatePosition(2, "magazzino n3");

                // ezshopDb.getProductTypeById(4); //panino
                // ezshopDb.getProductTypeById(8); //nostampa

                // ezshopDb.updatePosition(2, "magazzino n3");
                ezshopDb.closeConnection();
        }

}
