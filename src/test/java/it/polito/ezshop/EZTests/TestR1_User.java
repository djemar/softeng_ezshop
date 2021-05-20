package it.polito.ezshop.EZTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.User;
import it.polito.ezshop.data.UserImpl;

public class TestR1_User {
	@Test
    public void testSetGetId(){
        User user= new UserImpl("Johnn", "EZLebron", "Administrator",1);
        Integer i=1;
        assertEquals(user.getId(),i);
        i=2;
        user.setId(i);
        assertEquals(user.getId(),i);


    }

	@Test
    public void testSetGetUsername(){
        User user= new UserImpl("John", "EZLebron", "Administrator");
        assertEquals(user.getUsername(), "John");
        user.setUsername("Jimmy");
        assertEquals(user.getUsername(), "Jimmy");

    }


	@Test
    public void testSetGetPassword(){
        User user= new UserImpl("John", "EZLebron", "Administrator",1);
        assertEquals(user.getPassword(), "EZLebron");
        user.setPassword("EZLakers");
        assertEquals(user.getPassword(), "EZLakers");
    }

    
	@Test
    public void testSetGetRole(){
        User user= new UserImpl("John", "EZLebron", "Administrator",1);
        assertEquals(user.getRole(), "Administrator");
        user.setRole("Cashier");
        assertEquals(user.getRole(), "Cashier");

    }


}
