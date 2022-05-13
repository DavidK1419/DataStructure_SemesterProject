package com.kitchenFiller;

public class Item {

    private String name;
    private int weight;
    
    public Item (String name,int weight){
        this.name = name;
        this.weight = weight;
    }

    /*
    *Return the name of the item
    */
    public String getName (){
        return this.name;
    }
}

