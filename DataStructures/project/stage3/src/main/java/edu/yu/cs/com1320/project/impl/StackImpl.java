package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Stack;

/**
 * @param <T>
 */
public class StackImpl<T> implements Stack<T> {
    //Subclass of a LinkedList
    class List{
        List next;
        T command;
        List(){
            next = null;
        }
    }

    private List commandStack;
    private int amount;

        public StackImpl(){
        this.amount = 0;
        this.commandStack = new List();
    }
    /**
     * @param element object to add to the Stack
     */
    public void push(T element){
        if(commandStack == null){
            commandStack.command = element;
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
    public T pop(){
        T popElement = this.commandStack.command;
        this.commandStack = this.commandStack.next;
        this.amount--;
        return (T)popElement;
    }

    /**
     *
     * @return the element at the top of the stack without removing it
     */
    public T peek(){
        return (T)this.commandStack.command; //I think
    }

    /**
     *
     * @return how many elements are currently in the stack
     */
    public int size(){
        return this.amount;
    }
}
