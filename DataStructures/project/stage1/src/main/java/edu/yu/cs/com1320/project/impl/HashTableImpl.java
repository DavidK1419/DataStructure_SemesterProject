package edu.yu.cs.com1320.project.impl;


import edu.yu.cs.com1320.project.HashTable;

/**
 * 
 * Instances of HashTable should be constructed with two type parameters, one for the type of the keys in the table and one for the type of the values
 * @param <Key>
 * @param <Value>
 */
public class HashTableImpl<Key, Value> implements HashTable<Key, Value> {
    class Entry<Key, Value>{
        Key key;
        Value value;
        Entry<Key, Value> next;
        Entry(Key k, Value v){
            if(k == null){
                throw new IllegalArgumentException();
            }
            key = k;
            value = v;
            next = null;
        }
        boolean checkNullStat(){
            return next == null;
        }

    }

    private Entry<Key, Value>[] table;
    public HashTableImpl(){
        this.table = new Entry[5];
    }
    private int hashFunction(Key key){
        return (key.hashCode() & 0x7fffffff) % this.table.length;
    }


    /**
     * @param k the key whose value should be returned
     * @return the value that is stored in the HashTable for k, or null if there is no such key in the table
     */
    public Value get(Key k){
        int hashIndex = this.hashFunction(k);
        int index = getIndex(hashIndex);
        Entry currentIndex = this.table[index];
        while (currentIndex != null) {
            if (currentIndex.key.equals(k)) {
                return (Value)currentIndex.value;
            }
            currentIndex = currentIndex.next;
        }
        return null;
    }


    /**
     * @param k the key at which to store the value
     * @param v the value to store.
     * To delete an entry, put a null value.
     * @return if the key was already present in the HashTable, return the previous value stored for the key. If the key was not already present, return null.
     */
    public Value put(Key k, Value v){
        int hashIndex = this.hashFunction(k);
        int index = getIndex(hashIndex);
        Entry currentIndex = this.table[index];
        Value oldValue = get(k);
        Entry backUpCounter = currentIndex;
        if(this.table[index] == null){
            this.table[index] = new Entry(k, v);
            return oldValue;
        }else {
            while (currentIndex != null) {
                if (currentIndex.key.equals(k)) {
                    currentIndex.value = v;
                    return oldValue;
                }
                if(!currentIndex.checkNullStat()){
                    backUpCounter = currentIndex;
                }
                currentIndex = currentIndex.next;
            }
            backUpCounter.next = new Entry(k, v);
        }
        return oldValue;
    }


    private int getIndex(int number){
        return number % 5;
    }

}
