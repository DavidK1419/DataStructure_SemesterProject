package edu.yu.cs.com1320.project.stage1.impl;

import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage1.Document;
import edu.yu.cs.com1320.project.stage1.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage1.DocumentStore;
import edu.yu.cs.com1320.project.stage1.impl.DocumentStoreImpl;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class DocumentStoreImpl implements DocumentStore {
    private HashTableImpl<URI, DocumentImpl> hashTableImpl = new HashTableImpl<URI, DocumentImpl>();

    /**
     * the two document formats supported by this document store.
     * Note that TXT means plain text, i.e. a String.
     */
    enum DocumentFormat{
        TXT,BINARY;
    };

    /**
     * @param input the document being put
     * @param uri unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return if there is no previous doc at the given URI, return 0. If there is a previous doc, return the hashCode of the previous doc.
     * If InputStream is null, this is a delete, and thus return either the hashCode of the deleted doc or 0 if there is no doc to delete.
     * @throws IOException if there is an issue reading input
     * @throws IllegalArgumentException if uri or format are null
     */

    public int putDocument(InputStream input, URI uri, DocumentStore.DocumentFormat format) throws IOException{
        if((uri == null) || (format == null)){
            throw new IllegalArgumentException();
        }
        DocumentImpl oldDocValue = hashTableImpl.get(uri);
        if(input == null){
            if(oldDocValue == null){
                return 0;
            }else{
                hashTableImpl.put(uri, null);
                return oldDocValue.hashCode();
            }
        }
        int oldDoc = 0;
        byte[] bites = input.readAllBytes();
        if(format.ordinal() == 0){
            String bytesString = new String(bites, StandardCharsets.UTF_8);
            DocumentImpl newTextDoc = new DocumentImpl(uri, bytesString);
            try{
                oldDoc = this.hashTableImpl.put(uri, newTextDoc).hashCode();
            }catch(NullPointerException e){
                oldDoc = 0;
            }
        }
        if(format.ordinal() == 1) {
            DocumentImpl newBytesDoc = new DocumentImpl(uri, bites);
            //oldDoc = this.hashTableImpl.put(uri, newBytesDoc).hashCode();
            try{
                oldDoc = this.hashTableImpl.put(uri, newBytesDoc).hashCode();
            }catch(NullPointerException e){
                oldDoc = 0;
            }
        }
        if(oldDocValue == null){
            return 0;
        }
        return oldDoc;
    }
    /*System.out.println(oldDoc);
            System.out.println(uri);
            System.out.println(newTextDoc);
            System.out.println(this.hashTableImpl.put(uri, newTextDoc).hashCode());*/

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document
     */
    public Document getDocument(URI uri){
    	return this.hashTableImpl.get(uri);
    }

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    public boolean deleteDocument(URI uri){
        if(this.hashTableImpl.get(uri) == null){
            return false;
        }
        this.hashTableImpl.put(uri, null);
        return true;
    }
}


