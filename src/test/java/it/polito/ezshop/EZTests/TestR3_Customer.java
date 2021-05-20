package it.polito.ezshop.EZTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.CustomerImpl; 

public class TestR3_Customer {
	@Test
    public void testGetSetCustomerName(){
        Customer customer=new CustomerImpl("John");
        assertEquals(customer.getCustomerName(), "John");
       
        customer.setCustomerName("Jimmy");
        assertEquals(customer.getCustomerName(), "Jimmy");
    }

	@Test
    public void testSetGetCustomerCard(){
        Customer customer=new CustomerImpl("John",1,"0000000001");
        assertEquals(customer.getCustomerCard(), "0000000001");
       
        customer.setCustomerCard("0000000011");
        assertEquals(customer.getCustomerCard(), "0000000011");
    }

 
	@Test
    public void testSetGetID(){
        Customer customer=new CustomerImpl(1,"John","0000000001",0);
        Integer i=1;
        assertEquals(customer.getId(), i);
       
        customer.setId(2); 
        i=2;
        assertEquals(customer.getId(), i);
    }

    
	@Test
    public void testSetGetPoints(){
        Customer customer=new CustomerImpl(1,"John","0000000001",0);
        Integer i=0;
        assertEquals(customer.getPoints(), i);
       
        customer.setPoints(10);
        i=10;
        assertEquals(customer.getPoints(), i);
    }

}
