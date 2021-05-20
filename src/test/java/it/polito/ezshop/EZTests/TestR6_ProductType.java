package it.polito.ezshop.EZTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.ProductTypeImpl;

public class TestR6_ProductType {
	@Test
    public void testSetGetQuantity(){
        
        ProductType product= new ProductTypeImpl(1,"comfortable sneakers","1789394849543",50.90,"very requested","30-90-20",10);
        Integer i=10;
        assertEquals(product.getQuantity(), i);
        i=20;
        product.setQuantity(i);
        assertEquals(product.getQuantity(), i); 
    }
	@Test
    public void testSetGetLocation(){
        ProductType product= new ProductTypeImpl(1,"comfortable sneakers","1789394849543",50.90,"very requested","30-90-20",10);
        assertEquals(product.getLocation(), "30-90-20");
        product.setLocation("20-30-20");
        assertEquals(product.getLocation(), "20-30-20");

    }
	@Test
    public void testSetGetNote(){
        ProductType product= new ProductTypeImpl("comfortable sneakers","1789394849543",50.90,"very requested");
        assertEquals(product.getNote(), "very requested");
        product.setNote("nike top shoes");
        assertEquals(product.getNote(),"nike top shoes");
    }

	@Test
    public void testSetGetProductDescription(){
        ProductType product= new ProductTypeImpl(1,"comfortable sneakers","1789394849543",50.90,"very requested","30-90-20",10);
        assertEquals(product.getProductDescription(), "comfortable sneakers");
        product.setProductDescription("running sneakers");
        assertEquals(product.getProductDescription(),"running sneakers");
    }

	@Test
    public void testSetGetBarCode(){
        ProductType product= new ProductTypeImpl(1,"comfortable sneakers","1789394849543",50.90,"very requested","30-90-20",10);
        assertEquals(product.getBarCode(), "1789394849543");
        product.setBarCode("1784677849544");
        assertEquals(product.getBarCode(),"1784677849544");

    }

    
	@Test
    public void testSetGetPricePerUnit(){
        ProductType product= new ProductTypeImpl(1,"comfortable sneakers","1789394849543",50.90,"very requested","30-90-20",10);
        Double d=50.90;
        assertEquals(product.getPricePerUnit(), d);
        d=68.50;
        product.setPricePerUnit(d);
        assertEquals(product.getPricePerUnit(), d);
    }

    
	@Test
    public void testSetGetId(){
        ProductType product= new ProductTypeImpl(1,"comfortable sneakers","1789394849543",50.90,"very requested","30-90-20",10);
        Integer i=1;
        assertEquals(product.getId(), i);
        i=6;
        product.setId(i);
        assertEquals(product.getId(), i);

    }

    
}
