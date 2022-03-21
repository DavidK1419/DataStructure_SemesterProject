package edu.yu.cs.com1320.project.impl;

/**
 * @param <T>
 */
public class StackImpl implements Stack<T>{
    //Subclass of a LinkedList
    class List{
        List next;
        Command command;
        List(){
            next = null;
        }
    }

    private List commandStack;
    private int amount;

    StackImpl(){
        this.amount = 0;
        this.commandStack = new List();
    }
    /**
     * @param element object to add to the Stack
     */
    void push(T element){
        if(commandStack == null){
            commandStack = element;
        }else{
            List addElement = new List();
            addElement.command = element;
            addElement.next = this.commandStack;
            this.commandStack = addElement;
        }
        this.amount++;
    }

    /**
     * removes and returns element at the top of the stack
     * @return element at the top of the stack, null if the stack is empty
     */
    T pop(){
        Command popElement = this.commandStack.command;
        this.commandStack = this.commandStack.next;
        this.amount--;
        return popElement;
    }

    /**
     *
     * @return the element at the top of the stack without removing it
     */
    T peek(){
        return this.commandStack.command; //I think
    }

    /**
     *
     * @return how many elements are currently in the stack
     */
    int size(){
        return this.amount;
    }
}
