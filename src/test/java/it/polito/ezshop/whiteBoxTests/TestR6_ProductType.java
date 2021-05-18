package it.polito.ezshop.whiteBoxTests;

import static org.junit.Assert.assertEquals;

import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.ProductTypeImpl;

public class TestR6_ProductType {
    public void testSetGetQuantity(){
        
        ProductType product= new ProductTypeImpl(1,"comfortable sneakers","1789394849543",50.90,"very requested","30-90-20",10);
        Integer i=10;
        assertEquals(product.getQuantity(), i);
        i=20;
        product.setQuantity(i);
        assertEquals(product.getQuantity(), i);
    }

    public void testSetGetLocation(){
        ProductType product= new ProductTypeImpl(1,"comfortable sneakers","1789394849543",50.90,"very requested","30-90-20",10);
        assertEquals(product.getLocation(), "30-90-20");
        product.setLocation("20-30-20");
        assertEquals(product.getLocation(), "20-30-20");

    }

    public void testSetGetNote(){
        ProductType product= new ProductTypeImpl(1,"comfortable sneakers","1789394849543",50.90,"very requested","30-90-20",10);
        assertEquals(product.getNote(), "very requested");
        product.setNote("nike top shoes");
        assertEquals(product.getNote(),"nike top shoes");
    }


    public void testSetGetProductDescription(){
        ProductType product= new ProductTypeImpl(1,"comfortable sneakers","1789394849543",50.90,"very requested","30-90-20",10);
        assertEquals(product.getProductDescription(), "comfortable sneakers");
        product.setProductDescription("running sneakers");
        assertEquals(product.getProductDescription(),"running sneakers");
    }

    
    public void testSetGetBarCode(){
        ProductType product= new ProductTypeImpl(1,"comfortable sneakers","1789394849543",50.90,"very requested","30-90-20",10);
        assertEquals(product.getBarCode(), "1789394849543");
        product.setBarCode("1784677849544");
        assertEquals(product.getBarCode(),"1784677849544");

    }

    

    public void testSetGetPricePerUnit(){
        ProductType product= new ProductTypeImpl(1,"comfortable sneakers","1789394849543",50.90,"very requested","30-90-20",10);
        Double d=50.90;
        assertEquals(product.getPricePerUnit(), d);
        d=68.50;
        product.setPricePerUnit(d);
        assertEquals(product.getPricePerUnit(), d);
    }

    

    public void testSetGetId(){
        ProductType product= new ProductTypeImpl(1,"comfortable sneakers","1789394849543",50.90,"very requested","30-90-20",10);
        Integer i=1;
        assertEquals(product.getId(), i);
        i=6;
        product.setId(i);
        assertEquals(product.getId(), i);

    }

    
}
