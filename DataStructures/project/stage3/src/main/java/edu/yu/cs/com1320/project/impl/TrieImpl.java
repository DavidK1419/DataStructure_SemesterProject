package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Trie;

import java.util.*;

public class TrieImpl<Value> implements Trie<Value>{
    private static final int alphabetSize = 256; // extended ASCII
    private Node root; // root of trie
    private Set<Value> tempSetOfValues = new HashSet();
    private boolean tempValue = false;
    private Set<Value> deleteAllWithPrefixSortedSet = new HashSet<>();
    private List<Value> getAll = new ArrayList<>();
    private Set<Value> getAllWithPrefixSorteSet = new HashSet<>();

    //Subclass of Nodes
    public static class Node<Value> {
        protected Collection collectionOfVals = new HashSet();
        protected Node[] links = new Node[TrieImpl.alphabetSize];
    }

    public TrieImpl(){
        this.root = new Node();
    }

    /**
     * add the given value at the given key
     * @param key
     * @param val
     */
    public void put(String key, Value val){
        //deleteAll the value from this key
        if (val == null) {
            throw new IllegalArgumentException();
            //this.deleteAll(key);
        }else{
            this.root = put(this.root, key, val, 0);
        }
    }

    private Node put(Node node, String key, Value val, int d) {
        key = key.toLowerCase();
        //create a new node
        if (node == null) {
            node = new Node();
        }
        //we've reached the last node in the key,
        //set the value for the key and return the node
        if (d == key.length()){
            try {
                node.collectionOfVals.add(val);
            }catch(NullPointerException e){
            }
            return node;
        }
        //proceed to the next node in the chain of nodes that forms the desired key
        char c = key.charAt(d);
        node.links[c] = this.put(node.links[c], key, val, d + 1);
        return node;
    }

    /**
     * get all exact matches for the given key, sorted in descending order.
     * Search is CASE INSENSITIVE.
     * @param key
     * @param comparator used to sort values
     * @return a List of matching Values, in descending order
     */
    public List<Value> getAllSorted(String key, Comparator<Value> comparator){
        this.getAll = new ArrayList<>();
        this.get(this.root, key, 0);
        if (this.getAll == null) {
            return null;
        }
        this.getAll.sort(comparator);
        return this.getAll;
    }


    private List<Value> get(Node node, String key, int d){
        if (node == null) {
            return null;
        }
        //we've reached the last node in the key,
        //return the node
        if (d == key.length()) {
            this.getAll.addAll(node.collectionOfVals);
            return this.getAll;
            //return listOfValues;
        }
        //proceed to the next node in the chain of nodes that
        //forms the desired key
        char c = key.charAt(d);
        return this.get(node.links[c], key, d + 1); ///*, listOfValues*/
    }

    /**
     * get all matches which contain a String with the given prefix, sorted in descending order.
     * For example, if the key is "Too", you would return any value that contains "Tool", "Too", "Tooth", "Toodle", etc.
     * Search is CASE INSENSITIVE.
     * @param prefix
     * @param comparator used to sort values
     * @return a List of all matching Values containing the given prefix, in descending order
     */
    public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator){
        this.getAllWithPrefixSorteSet = new HashSet<>();
        getAllWithPrefixSorted(this.root, prefix, 0);
        List<Value> listOfValues = new ArrayList<>();
        for(Value documents : this.getAllWithPrefixSorteSet){
            listOfValues.add(documents);
        }
        if(listOfValues == null){
            listOfValues = new ArrayList<>();
        }else{
            listOfValues.sort(comparator);
        }
        return listOfValues;
    }


    private Node getAllWithPrefixSorted(Node node, String prefix, int d) {
        //return null;*/
        //link was null - return null, indicating a miss
        if (node == null) {
            return null;
        }
        //we've reached the last node in the key,
        //return the node
        if (d == prefix.length()) {
            iterateOverEverything(node);
            return node;
        }
        //proceed to the next node in the chain of nodes that
        //forms the desired key
        char c = prefix.charAt(d);
        return this.getAllWithPrefixSorted(node.links[c], prefix, d + 1);
    }

    private void iterateOverEverything(Node node){
        if (node == null) {
            return;
        }
        this.getAllWithPrefixSorteSet.addAll(node.collectionOfVals);
        for (int c = 0; c < this.alphabetSize; c++) {
            if (node.links[c] != null) {
                iterateOverEverything(node.links[c]);
            }
        }
    }


    /**
     * Delete the subtree rooted at the last character of the prefix.
     * Search is CASE INSENSITIVE.
     * @param prefix
     * @return a Set of all Values that were deleted.
     */
    public Set<Value> deleteAllWithPrefix(String prefix){
        this.deleteAllWithPrefixSortedSet = new HashSet<>();
        this.getAllWithPrefixSorteSet = new HashSet<>();
        getAllWithPrefixSorted(this.root, prefix, 0);
        deleteAllWithPrefixSorted(this.root, prefix, 0);
        deleteAll(prefix);
        return this.getAllWithPrefixSorteSet;
    }

    private Set<Value> deleteAllWithPrefixSorted(Node node, String prefix, int d) {
        //link was null - return null, indicating a miss
        if (node == null) {
            return null;
        }
        //we've reached the last node in the key,
        //return the node
        if (d == prefix.length()) {
            node = null;
            return this.deleteAllWithPrefixSortedSet;
        }
        //proceed to the next node in the chain of nodes that
        //forms the desired key
        char c = prefix.charAt(d);
        return this.deleteAllWithPrefixSorted(node.links[c], prefix, d + 1/*, fillingValues*/);
    }

    /*private void iterateOverEverythingAndDelete(Node node){
        if (node == null) {
            return;
        }
        this.deleteAllWithPrefixSortedSet.addAll(node.collectionOfVals);
        for (int c = 0; c < this.alphabetSize; c++) {
            if (node.links[c] != null) {
                iterateOverEverything(node.links[c]);
                this.deleteAllWithPrefixSortedSet.addAll(node.collectionOfVals);

            }
        }
    }*/


    /**
     * Delete all values from the node of the given key (do not remove the values from other nodes in the Trie)
     * @param key
     * @return a Set of all Values that were deleted.
     */
    public Set<Value> deleteAll(String key){
        this.root = deleteAll(this.root, key, 0);
        Set<Value> setOfValues = new HashSet<>();
        for(Object values : this.tempSetOfValues){
            setOfValues.add((Value)values);
        }
        return setOfValues;
    }


    private Node deleteAll(Node node, String key, int d) {
        if (node == null) {
            return null;
        }
        //we're at the node to del - set the val to null
        if (d == key.length()) {
            this.tempSetOfValues.clear();
            this.tempSetOfValues.addAll(node.collectionOfVals);
            node.collectionOfVals.clear();
        } else {
            //continue down the trie to the target node
            char c = key.charAt(d);
            node.links[c] = this.deleteAll(node.links[c], key, d + 1);
        }
        //this node has a val – do nothing, return the node
        if (!node.collectionOfVals.isEmpty()) {
            return node;
        }
        //remove subtrie rooted at x if it is completely empty
        for (int c = 0; c <this.alphabetSize; c++) {
            if (node.links[c] != null) {
                return node; //not empty
            }
        }
        //empty - set this link to null in the parent
        return null;
    }

    /**
     * Remove the given value from the node of the given key (do not remove the value from other nodes in the Trie)
     * @param key
     * @param val
     * @return the value which was deleted. If the key did not contain the given value, return null.
     */
    public Value delete(String key, Value val){
        this.tempValue = false;
        this.root = delete(this.root, key, 0, val);
        if(this.tempValue == true){
            return val;
        }else{
            return null;
        }

    }

    private Node delete(Node node, String key, int d, Value val) {
        if (node == null) {
            return null;
        }
        //we're at the node to del - set the val to null
        if ((d == key.length()) && (node.collectionOfVals.contains(val))) { //prbably have to account for that fact that there are multiple vals there
            this.tempValue = true;
            node.collectionOfVals.remove(val);
            return node;
        }
        //continue down the trie to the target node
        else {
            char c = key.charAt(d);
            node.links[c] = this.delete(node.links[c], key, d + 1, val);
        }
        //this node has a val – do nothing, return the node
        if (!node.collectionOfVals.isEmpty()) {
            return node;
        }
        //remove subtrie rooted at x if it is completely empty
        for (int c = 0; c <this.alphabetSize; c++) {
            if (node.links[c] != null) {
                return node; //not empty
            }
        }
        //empty - set this link to null in the parent
        return null;
    }
}


