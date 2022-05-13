package com.kitchenFiller;

public class KitchenManagenment {
    private SendEmail sendEmail;

    public KitchenManagenment(String email){
        this.sendEmail = new SendEmail(email);
    }

    public boolean checkStock(){
        return true;
    }
}