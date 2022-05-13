package com.kitchenFiller;
import java.util.HashMap;
import java.util.Map;
public class ShoppingList {
        private Map <Item, Integer> currentList;
        public ShoppingList(){
            this.currentList = new HashMap<Item, Integer>();
        }
        /*
        * add a new instance of Item to the list with the given number of the product
        * if Item already exists in the list call updateList
         */
        public void addToList(Item item, int number){
            if(this.currentList.containsKey(item)){
                this.updateList(item,number);
            }
            this.currentList.put(item, number);
        }
        /*
        * Update map with every usage/order
        * envoke the shopping class, go online and order the product at the cheapest price
        * if Item doesn't exist in the list, calla addToList
         */
        public void updateList(Item item, int number){
            if(!this.currentList.containsKey(item)){
                this.addToList(item,number);
            }
            this.currentList.replace(item,number);
        }
        /*
        * return a COPY of the current stock Map
         */
        public Map<Item, Integer> getMapOfItems(){
            return this.currentList;
        }
    }
