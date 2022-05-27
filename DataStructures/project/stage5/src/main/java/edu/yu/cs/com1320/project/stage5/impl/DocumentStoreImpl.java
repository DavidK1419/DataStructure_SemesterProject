package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage5.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;


public class DocumentStoreImpl implements DocumentStore {
    private BTreeImpl<URI, Document> bTreeImpl;
    private DocumentPersistenceManager manager;
    private StackImpl<Undoable> commandStack;
    private TrieImpl<URI> trie;
    private MinHeapImpl<URI> minHeap;
    private int setMaxDocumentCount;
    private int setMaxDocumentBytes;
    private boolean isThereDocsLimit;
    private boolean isThereBytesLimit;
    private boolean isThereLimit;
    private int currentAmountOfDocs;
    private int currentAmountOfBytes;

    /**
     * the two document formats supported by this document store.
     * Note that TXT means plain text, i.e. a String.
     */
    enum DocumentFormat{
        TXT,BINARY;
    };

    public DocumentStoreImpl(){
        this.bTreeImpl = new BTreeImpl<URI, Document>();
        this.commandStack = new StackImpl();
        this.trie = new TrieImpl();
        this.minHeap = new MinHeapImpl();
        this.isThereDocsLimit = false;
        this.isThereBytesLimit = false;
        this.isThereLimit = false;
    }

    public DocumentStoreImpl(File baseDir){
        this.bTreeImpl = new BTreeImpl<URI, Document>();
        this.commandStack = new StackImpl();
        this.trie = new TrieImpl();
        this.minHeap = new MinHeapImpl();
        this.isThereDocsLimit = false;
        this.isThereBytesLimit = false;
        this.isThereLimit = false;
        this.manager = new DocumentPersistenceManager(baseDir);
        this.bTreeImpl.setPersistenceManager(this.manager);
    }

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
        DocumentImpl oldDocValue = (DocumentImpl)bTreeImpl.get(uri);
        if(input == null){
            if(oldDocValue == null){
                return 0;
            }else{
                bTreeImpl.put(uri, null);
                Function<URI, Boolean> nullDoc = URI -> saveUndoDoc(null, uri);
                this.commandStack.push(new GenericCommand(uri, nullDoc));
                return oldDocValue.hashCode();
            }
        }
        int oldDoc = 0;
        byte[] bites = input.readAllBytes();
        if(format.ordinal() == 0){
            String bytesString = new String(bites, StandardCharsets.UTF_8);
            DocumentImpl newTextDoc = new DocumentImpl(uri, bytesString, new HashMap());
//            DocumentImpl newTextDoc = new DocumentImpl(uri, bytesString);
            if(isThereLimit){
                try {
                    deleteFromMinHeap(newTextDoc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //deleteFromMinHeap(newTextDoc);
            }
            newTextDoc.setLastUseTime(System.nanoTime());
            minHeap.insert(newTextDoc.getKey());
            minHeap.reHeapify(newTextDoc.getKey());
            try{
                oldDoc = this.bTreeImpl.put(uri, newTextDoc).hashCode();
            }catch(NullPointerException e){
                oldDoc = 0;
            }
            Set<String> docsWords = newTextDoc.getWords();
            for(String words : docsWords){
                this.trie.put(words, newTextDoc.getKey());
            }
            Function<URI, Boolean> textFunction;
            if(oldDocValue != null){
                textFunction = URI -> saveUndoDoc(oldDocValue, uri);
            }else{
                textFunction = URI -> savegetRidOfUndoDoc(newTextDoc, uri);
            }
            GenericCommand genericCommand = new GenericCommand(uri, textFunction);
            this.commandStack.push(genericCommand);
            currentAmountOfBytes += newTextDoc.getDocumentTxt().getBytes().length;
            currentAmountOfDocs++;
        }
        if(format.ordinal() == 1) {
            DocumentImpl newBytesDoc = new DocumentImpl(uri, bites);
            if(isThereLimit){
                try {
                    deleteFromMinHeap(newBytesDoc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //deleteFromMinHeap(newBytesDoc);
            }
            newBytesDoc.setLastUseTime(System.nanoTime());
            minHeap.insert(newBytesDoc.getKey());
            minHeap.reHeapify(newBytesDoc.getKey());
            try{
                oldDoc = this.bTreeImpl.put(uri, newBytesDoc).hashCode();
            }catch(NullPointerException e){
                oldDoc = 0;
            }
            Function<URI, Boolean> bytesFunction;
            if(oldDocValue != null){
                bytesFunction = URI -> saveUndoDoc(oldDocValue, uri);
            }else{
                bytesFunction = URI -> savegetRidOfUndoDoc(newBytesDoc, uri);
            }
            GenericCommand genericCommand  = new GenericCommand(uri, bytesFunction);
            this.commandStack.push(genericCommand);
            currentAmountOfBytes += newBytesDoc.getDocumentBinaryData().length;
            currentAmountOfDocs++;
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
        DocumentImpl doc = (DocumentImpl) this.bTreeImpl.get(uri);
        if(isThereLimit){
            try {
                deleteFromMinHeap(doc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.bTreeImpl.put(uri, doc);
        /*this.minHeap.insert(doc.getKey());
        Set<String> docsWords = doc.getWords();
        for(String words : docsWords){
            this.trie.put(words, doc.getKey());
        }*/
        doc.setLastUseTime(System.nanoTime());
        try {
            minHeap.reHeapify(doc.getKey());
        }catch(NullPointerException e){
        }
        Function<URI, Boolean> function = URI -> saveUndoDoc(doc, uri);
        GenericCommand genericCommand  = new GenericCommand(uri, function);
        this.commandStack.push(genericCommand);
        return doc;

    }

    private void deleteFromMinHeap(Document document) throws Exception { //added throws exception to method signature
        while((isThereDocsLimit && (this.currentAmountOfDocs >= this.setMaxDocumentCount)) || (isThereBytesLimit && ((this.currentAmountOfBytes + getBytes(document)) > this.setMaxDocumentBytes))){
            Document removingDoc = bTreeImpl.get((URI) this.minHeap.remove());
            bTreeImpl.moveToDisk(removingDoc.getKey());
            int amountOfBytesToDelete = 0;
            try{
                amountOfBytesToDelete = removingDoc.getDocumentTxt().getBytes().length;
            }catch(NullPointerException e){
                amountOfBytesToDelete = removingDoc.getDocumentBinaryData().length;
            }
            currentAmountOfBytes -= amountOfBytesToDelete;
            currentAmountOfDocs--;
            Function<URI, Boolean> bytesFunction = URI -> savegetRidOfUndoDoc((DocumentImpl) removingDoc, removingDoc.getKey());
            GenericCommand genericCommand  = new GenericCommand(removingDoc.getKey(), bytesFunction);
            this.commandStack.push(genericCommand);
            this.bTreeImpl.put(removingDoc.getKey(), null);
            //removeFromTrie(removingDoc);
        }
    }

    private int getBytes(Document document){
        int amountOfBytesToDelete = 0;
        try{
            amountOfBytesToDelete = document.getDocumentTxt().getBytes().length;
        }catch(NullPointerException e){
            amountOfBytesToDelete = document.getDocumentBinaryData().length;
        }
        return amountOfBytesToDelete;
    }

    private void clearBytesFromMinHeap(URI uri) throws Exception {
        bTreeImpl.get(uri).setLastUseTime(0);
        this.minHeap.reHeapify(uri);
        this.bTreeImpl.moveToDisk(((URI)this.minHeap.remove())); //this.minHeap.remove();
        int amountOfBytesToDelete = 0;
        try{
            amountOfBytesToDelete = bTreeImpl.get(uri).getDocumentTxt().getBytes().length;
        }catch(NullPointerException e){
            amountOfBytesToDelete = bTreeImpl.get(uri).getDocumentBinaryData().length;
        }
        currentAmountOfBytes -= amountOfBytesToDelete;
        currentAmountOfDocs--;
    }

    private void removeFromTrie(Document document){
        Set<String> setOfWords = document.getWords();
        for(String words : setOfWords){
            this.trie.delete(words, document.getKey());
        }
    }

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    public boolean deleteDocument(URI uri) {
        if(this.bTreeImpl.get(uri) == null){
            return false;
        }
        Document delDoc = getDocument(uri);
        this.bTreeImpl.put(uri, null);
        try {
            clearBytesFromMinHeap(delDoc.getKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //removeFromTrie(delDoc);
        Function<URI, Boolean> nullDoc = URI -> saveUndoDoc((DocumentImpl) delDoc, uri);
        this.commandStack.push(new GenericCommand(uri, nullDoc));
        return true;
    }

    /**
     * undo the last put or delete command
     * @throws IllegalStateException if there are no actions to be undone, i.e. the command stack is empty
     */
    public void undo() throws IllegalStateException{
        try {
            this.commandStack.pop().undo();
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
        StackImpl<Undoable> backupStack = new StackImpl();
        boolean checkIfStuffHappened = false;
        while(this.commandStack.size() > 0){
            if(this.commandStack.peek() instanceof GenericCommand){
                if(((GenericCommand<?>) this.commandStack.peek()).getTarget().equals(uri)){
                    this.commandStack.pop().undo();
                    checkIfStuffHappened = true;
                    break;
                }else{
                    backupStack.push(this.commandStack.pop());
                }
            }else{
                CommandSet commandSet = (CommandSet) this.commandStack.peek();
                if(commandSet.containsTarget(uri)){
                    commandSet.undo(uri);
                    if(commandSet.isEmpty()){
                        this.commandStack.pop();
                    }
                    checkIfStuffHappened = true;
                    break;
                }else{
                    backupStack.push(this.commandStack.pop());
                }
            }
        }
        while(backupStack.size() > 0){
            this.commandStack.push(backupStack.pop());
        }
        if(!checkIfStuffHappened){
            throw new IllegalStateException();
        }
    }

    /**
     * Retrieve all documents whose text contains the given keyword.
     * Documents are returned in sorted, descending order, sorted by the number of times the keyword appears in the document.
     * Search is CASE INSENSITIVE.
     * @param keyword
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    public List<Document> search(String keyword){
        List<URI> listOfDocURIs = this.trie.getAllSorted(keyword.toLowerCase(), (URI document1, URI document2) ->{
            if (this.bTreeImpl.get(document1).wordCount(keyword) > this.bTreeImpl.get(document2).wordCount(keyword)) {
                return 1;
            } else if (this.bTreeImpl.get(document1).wordCount(keyword) < this.bTreeImpl.get(document2).wordCount(keyword)) {
                return -1;
            } else {
                return 0;
            }
        });

        List<Document> listOfDocDs = new ArrayList<>();
        for(URI uri:listOfDocURIs){
            listOfDocDs.add(getDocument(uri));
            this.bTreeImpl.get(uri).setLastUseTime(System.nanoTime());
            minHeap.reHeapify(uri);
        }
        return listOfDocDs;
    }

    /**
     * Retrieve all documents whose text starts with the given prefix
     * Documents are returned in sorted, descending order, sorted by the number of times the prefix appears in the document.
     * Search is CASE INSENSITIVE.
     * @param keywordPrefix
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    public List<Document> searchByPrefix(String keywordPrefix){
        List<URI> listOfDocURIs = this.trie.getAllWithPrefixSorted(keywordPrefix.toLowerCase(), (URI document1, URI document2) ->{ //speak to Judah about this
            if (this.bTreeImpl.get(document1).wordCount(keywordPrefix) < this.bTreeImpl.get(document2).wordCount(keywordPrefix)) {
                return 1;
            } else if (this.bTreeImpl.get(document1).wordCount(keywordPrefix) > this.bTreeImpl.get(document2).wordCount(keywordPrefix)) {
                return -1;
            } else {
                return 0;
            }
        });

        List<Document> listOfDocDs = new ArrayList<>();
        for(URI uri:listOfDocURIs){
            listOfDocDs.add(getDocument(uri));
            this.getDocument(uri).setLastUseTime(System.nanoTime());
            minHeap.reHeapify(uri);
        }
        return listOfDocDs;
    }


    /**
     * Completely remove any trace of any document which contains the given keyword
     * @param keyword
     * @return a Set of URIs of the documents that were deleted.
     */
    public Set<URI> deleteAll(String keyword){
        keyword = keyword.toLowerCase();
        List<Document> search = search(keyword);
        this.trie.deleteAll(keyword);
        if(search.size() > 1){
            CommandSet commandSet = new CommandSet();
            for(Document document : search){
                Function<URI, Boolean> currentDoc = URI -> saveUndoDoc((DocumentImpl) document, document.getKey());
                GenericCommand genericCommand = new GenericCommand(document.getKey(), currentDoc);
                commandSet.addCommand((genericCommand));
            }
            this.commandStack.push(commandSet);
        }else{
            GenericCommand genericCommand;
            for(Document document : search){
                Function<URI, Boolean> currentDoc = URI -> saveUndoDoc((DocumentImpl) document, document.getKey());
                genericCommand = new GenericCommand(document.getKey(), currentDoc);
                this.commandStack.push(genericCommand);
            }
        }
        //Iterate through all the words of the document and remove the Document from the trie for each of the words
        Set<URI> uriSet = new HashSet<>();
        for(Document d : search){
            try {
                clearBytesFromMinHeap(d.getKey());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //clearBytesFromMinHeap(d);
            removeFromTrie(d);
            this.bTreeImpl.put(d.getKey(), null);
            uriSet.add((d).getKey());
        }
        return uriSet;
    }

    /**
     * Completely remove any trace of any document which contains a word that has the given prefix
     * Search is CASE INSENSITIVE.
     * @param keywordPrefix
     * @return a Set of URIs of the documents that were deleted.
     */
    public Set<URI> deleteAllWithPrefix(String keywordPrefix){
        keywordPrefix = keywordPrefix.toLowerCase();
        List<Document> listOfDelPrefix = searchByPrefix(keywordPrefix);
        if(listOfDelPrefix.size() > 1){
            CommandSet commandSet = new CommandSet();
            for(Document document : listOfDelPrefix){
                Function<URI, Boolean> currentDoc = URI -> saveUndoDoc((DocumentImpl) document, document.getKey());
                GenericCommand genericCommand = new GenericCommand(document.getKey(), currentDoc);
                commandSet.addCommand((genericCommand));
            }
            this.commandStack.push(commandSet);
        }else{
            GenericCommand genericCommand;
            for(Document document : listOfDelPrefix){
                Function<URI, Boolean> currentDoc = URI -> saveUndoDoc((DocumentImpl) document, document.getKey());
                genericCommand = new GenericCommand(document.getKey(), currentDoc);
                this.commandStack.push(genericCommand);
            }
        }

        Set<URI> uriSet = new HashSet<>();
        for(Document d : listOfDelPrefix){
            try {
                clearBytesFromMinHeap(d.getKey());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //clearBytesFromMinHeap(d);
            removeFromTrie(d);
            uriSet.add((d).getKey());
            this.bTreeImpl.put(d.getKey(), null);
        }
        return uriSet;
    }

    /**
     * set maximum number of documents that may be stored
     * @param limit
     */
    public void setMaxDocumentCount(int limit){
        this.isThereLimit = true;
        this.isThereDocsLimit = true;
        this.setMaxDocumentCount = limit;
    }

    /**
     * set maximum number of bytes of memory that may be used by all the documents in memory combined
     * @param limit
     */
    public void setMaxDocumentBytes(int limit){
        this.isThereLimit = true;
        this.isThereBytesLimit = true;
        this.setMaxDocumentBytes = limit;
    }


    private boolean saveUndoDoc(DocumentImpl document, URI uri){
        this.bTreeImpl.put(uri, document);
        getDocument(uri).setLastUseTime(System.nanoTime());
        minHeap.insert(uri);
        Set<String> docsWords = document.getWords();
        for(String words : docsWords){
            this.trie.put(words, document.getKey());
        }
        return true;
    }

    private boolean savegetRidOfUndoDoc(DocumentImpl document, URI uri){
        this.bTreeImpl.put(uri, null);
        removeFromTrie(document);
        try {
            clearBytesFromMinHeap(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //clearBytesFromMinHeap(document);
        return true;
    }
}


