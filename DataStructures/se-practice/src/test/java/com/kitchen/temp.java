package com.kitchen;

import com.kitchenFiller.Item;
import com.kitchenFiller.SendEmail;

public class temp {

    public void main(String args[]){
        SendEmail email = new SendEmail("traemail14@gmail.com");
        Item item = new Item("apple", 5);
        email.sendRealEmail(item);
    }
}
