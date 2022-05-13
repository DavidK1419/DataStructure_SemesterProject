package com.kitchen;

import com.kitchenFiller.Item;
import com.kitchenFiller.SendEmail;
import org.junit.jupiter.api.Test;

public class testKitchen {

    @Test
    public void testKitchenManagement(){
        //KitchenManagement kitchenManagement = new Ki
        SendEmail email = new SendEmail("traemail14@gmail.com");
        Item item = new Item("apple", 5);
        email.sendRealEmail(item);
    }
}
