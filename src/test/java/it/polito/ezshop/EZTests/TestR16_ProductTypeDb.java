package it.polito.ezshop.EZTests;
import org.junit.Test;

import it.polito.ezshop.data.EZShopDb;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.ProductTypeImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;

public class TestR16_ProductTypeDb {
	
    EZShopDb ezshopDb = new EZShopDb();
    ProductTypeImpl st1;
	Integer id;
 
    @Before
    public void setup() {
        st1 = new ProductTypeImpl("chocolate", "341254654", 5, "white");
        ezshopDb.createConnection();
        ezshopDb.resetDB();
    	id = ezshopDb.insertProductType(st1);
		st1.setId(id);
        ezshopDb.closeConnection();
    }

    @After
    public void clean() {
        ezshopDb.createConnection();
        ezshopDb.resetDB();
        ezshopDb.closeConnection();
    }

	@Test
	public void testUpdateProd() {
		ezshopDb.createConnection();
		assertTrue(ezshopDb.updateProductType(id, "cake", "32563252", 56, "m"));
		
		ezshopDb.closeConnection();
	}
	
	@Test
	public void testInvalidUpdateProd() {
		assertFalse(ezshopDb.updateProductType(id, "cake", "32563252", 56, ""));
	}
	
	@Test
	public void testUpdateQuant() {
		ezshopDb.createConnection();
		assertFalse(ezshopDb.updateQuantity(id, 1));
		assertTrue(ezshopDb.updateQuantity(id, -50));
		ezshopDb.closeConnection();
	}
	@Test
	public void testInvalidUpdateQuant() {
		assertTrue(ezshopDb.updateQuantity(id, 50));
	}
	
	@Test
	public void testInvalidUpdatePosition() {
		assertFalse(ezshopDb.updatePosition(id, "48-gh-324"));
	}
	
	@Test
	public void testPosition() {
		ezshopDb.createConnection();
		assertTrue(ezshopDb.updatePosition(id, "48-gh-324"));
		assertTrue(ezshopDb.checkExistingPosition("48-gh-324"));
		assertFalse(ezshopDb.checkExistingPosition("489-gh-324"));
		ezshopDb.closeConnection();
	}
	@Test
	public void testInvalidExistingPosition() {
		assertTrue(ezshopDb.checkExistingPosition("48-gh-324"));
	}
	
	@Test
	public void testDeleteProd() {
		ezshopDb.createConnection();
		assertTrue(ezshopDb.deleteProductType(id));
		ezshopDb.closeConnection();
		assertFalse(ezshopDb.deleteProductType(id));
	}
	
	@Test
	public void testInvalidDeleteProd() {
		assertFalse(ezshopDb.deleteProductType(id));
	}

	@Test
	public void testGetAllProductypes(){
		ezshopDb.createConnection();
		List<ProductType> list = ezshopDb.getAllProductTypes();
		assertEquals( 1,list.size());
		ProductType p = list.get(0);
		assertTrue(p.getId().equals(st1.getId())&&p.getBarCode().equals(st1.getBarCode()));
		ezshopDb.closeConnection();
	}
	
	@Test
	public void testGetgetProductTypesByDescription(){
		ezshopDb.createConnection();
		List<ProductType> list = ezshopDb.getProductTypesByDescription(st1.getProductDescription());
		assertEquals( 1,list.size());
		ProductType p = list.get(0);
		assertTrue(p.getId().equals(st1.getId())&&p.getBarCode().equals(st1.getBarCode()));
		ezshopDb.closeConnection();
	}
	@Test
	public void testInvalidGetgetProductTypesByDescription(){
		ezshopDb.createConnection();
		List<ProductType> list = ezshopDb.getProductTypesByDescription("  ");
		assertEquals(0 ,list.size());
		ezshopDb.closeConnection();
	}
	@Test 
	public void testGetProductById() {
		ezshopDb.createConnection();
		ProductTypeImpl p = ezshopDb.getProductTypeById(id);
		assertTrue(p.getId().equals(st1.getId()));
		ezshopDb.closeConnection();
	}
	@Test
	public void getProductTypeByBarCode() {
		ezshopDb.createConnection();
		ProductTypeImpl p = ezshopDb.getProductTypeByBarCode(st1.getBarCode());
		assertTrue(p.getId().equals(st1.getId()));
		ProductTypeImpl p2 = ezshopDb.getProductTypeByBarCode("no");
		assertNull(p2);
		ezshopDb.closeConnection();
	}
	


}


