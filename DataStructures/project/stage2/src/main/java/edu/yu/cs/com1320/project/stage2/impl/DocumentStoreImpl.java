package edu.yu.cs.com1320.project.stage2.impl;
import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.impl.*;


import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage2.impl.*;
import edu.yu.cs.com1320.project.stage2.*;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class DocumentStoreImpl implements DocumentStore {
    private HashTableImpl<URI, DocumentImpl> hashTableImpl = new HashTableImpl<URI, DocumentImpl>();
    private StackImpl commandStack = new StackImpl();

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
                Function<URI, Boolean> nullDoc = URI -> saveUndoDoc(null, uri); //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA!!!!!!!
                this.commandStack.push(new Command(uri, nullDoc));
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
            Function<URI, Boolean> textFunction = URI -> saveUndoDoc(oldDocValue, uri);
            Command command = new Command(uri, textFunction);
            this.commandStack.push(command);
        }
        if(format.ordinal() == 1) {
            DocumentImpl newBytesDoc = new DocumentImpl(uri, bites);
            try{
                oldDoc = this.hashTableImpl.put(uri, newBytesDoc).hashCode();
            }catch(NullPointerException e){
                oldDoc = 0;
            }
            Function<URI, Boolean> bytesFunction = URI -> saveUndoDoc(oldDocValue, uri);
            Command command = new Command(uri, bytesFunction);
            this.commandStack.push(command);
        }

        if(oldDocValue == null){
            return 0;
        }
        return oldDoc;
    }

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
        Document delDoc = getDocument(uri);//might have to put a try block here
        this.hashTableImpl.put(uri, null);
        Function<URI, Boolean> nullDoc = URI -> saveUndoDoc((DocumentImpl) delDoc, uri);//I assume delDoc is null
        this.commandStack.push(new Command(uri, nullDoc));
        return true;
    }


    /**
     * undo the last put or delete command
     * @throws IllegalStateException if there are no actions to be undone, i.e. the command stack is empty
     */
    public void undo() throws IllegalStateException{
        try {
            ((Command) this.commandStack.pop()).undo(); //what's going on here???
        }catch(NullPointerException e){
            throw new IllegalStateException();
        }
    }

    /**
     * undo the last put or delete that was done with the given URI as its key
     * @param uri
     * @throws IllegalStateException if there are no actions on the command stack for the given URI
     */
    public void undo(URI uri) throws IllegalStateException{
        StackImpl backupStack = new StackImpl();
        boolean checkIfStuffHappened = false;
        while(this.commandStack.size() > 0){
            if(((Command)this.commandStack.peek()).getUri().equals(uri)){
                ((Command)this.commandStack.pop()).undo();
                checkIfStuffHappened = true;
                break;
            }else{
                backupStack.push(this.commandStack.pop());
            }
        }
        while(backupStack.size() > 0){
            this.commandStack.push(backupStack.pop());
        }
        if(!checkIfStuffHappened){
            throw new IllegalStateException();
        }
    }

    private boolean saveUndoDoc(DocumentImpl document, URI uri){
        if(document == null){
            hashTableImpl.put(uri, null);
        }else{
            hashTableImpl.put(uri, document);
        }
        return true;
    }
}


