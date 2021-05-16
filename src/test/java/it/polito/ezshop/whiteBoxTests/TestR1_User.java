package it.polito.ezshop.whiteBoxTests;

import static org.junit.Assert.assertEquals;

import it.polito.ezshop.data.User;
import it.polito.ezshop.data.UserImpl;

public class TestR1_User {
    public void testSetGetId(){
        User user= new UserImpl("Johnn", "EZLebron", "Administrator",1);
        Integer i=1;
        assertEquals(user.getId(),i);
        i=2;
        user.setId(i);
        assertEquals(user.getId(),i);


    }


    public void testSetGetUsername(){
        User user= new UserImpl("John", "EZLebron", "Administrator",1);
        assertEquals(user.getUsername(), "John");
        user.setUsername("Jimmy");
        assertEquals(user.getUsername(), "Jimmy");

    }



    public void testSetGetPassword(){
        User user= new UserImpl("John", "EZLebron", "Administrator",1);
        assertEquals(user.getPassword(), "EZLebron");
        user.setPassword("EZLakers");
        assertEquals(user.getPassword(), "EZLakers");
    }

    

    public void testSetGetRole(){
        User user= new UserImpl("John", "EZLebron", "Administrator",1);
        assertEquals(user.getRole(), "Administrator");
        user.setRole("Cashier");
        assertEquals(user.getRole(), "Cashier");

    }


}
