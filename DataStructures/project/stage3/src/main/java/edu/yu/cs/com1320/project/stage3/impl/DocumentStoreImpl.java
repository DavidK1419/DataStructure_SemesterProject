package edu.yu.cs.com1320.project.stage3.impl;

import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage3.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;


public class DocumentStoreImpl implements DocumentStore {
    private HashTableImpl<URI, DocumentImpl> hashTableImpl = new HashTableImpl<URI, DocumentImpl>();
    private StackImpl<Undoable> commandStack = new StackImpl();
    private TrieImpl<Document> trie = new TrieImpl();

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
                Function<URI, Boolean> nullDoc = URI -> saveUndoDoc(null, uri);
                this.commandStack.push(new GenericCommand(uri, nullDoc));
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
            Set<String> docsWords = newTextDoc.getWords();
            for(String words : docsWords){
                this.trie.put(words, newTextDoc);
            }
            Function<URI, Boolean> textFunction;
            if(oldDocValue != null){
                textFunction = URI -> saveUndoDoc(oldDocValue, uri);
            }else{
                textFunction = URI -> savegetRidOfUndoDoc(newTextDoc, uri);
            }
            GenericCommand genericCommand = new GenericCommand(uri, textFunction);
            this.commandStack.push(genericCommand);
        }
        if(format.ordinal() == 1) {
            DocumentImpl newBytesDoc = new DocumentImpl(uri, bites);
            try{
                oldDoc = this.hashTableImpl.put(uri, newBytesDoc).hashCode();
            }catch(NullPointerException e){
                oldDoc = 0;
            }
            Function<URI, Boolean> bytesFunction = URI -> saveUndoDoc(oldDocValue, uri);
            GenericCommand genericCommand  = new GenericCommand(uri, bytesFunction);
            this.commandStack.push(genericCommand);
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
        Document delDoc = getDocument(uri);
        this.hashTableImpl.put(uri, null);
        //should prob add null code
        Set<String> setOfWords = delDoc.getWords();
        for(String words : setOfWords){
            this.trie.delete(words, delDoc);
        }
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
            this.commandStack.pop().undo(); //removed cast to GenericCommand
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
        return this.trie.getAllSorted(keyword.toLowerCase(), (Document document1, Document document2) ->{
            if (document1.wordCount(keyword) > document2.wordCount(keyword)) {
                return 1;
            } else if (document1.wordCount(keyword) < document2.wordCount(keyword)) {
                return -1;
            } else {
                return 0;
            }
        });
    }

    /**
     * Retrieve all documents whose text starts with the given prefix
     * Documents are returned in sorted, descending order, sorted by the number of times the prefix appears in the document.
     * Search is CASE INSENSITIVE.
     * @param keywordPrefix
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    public List<Document> searchByPrefix(String keywordPrefix){
        return this.trie.getAllWithPrefixSorted(keywordPrefix.toLowerCase(), (Document document1, Document document2) ->{ //speak to Judah about this
            if (document1.wordCount(keywordPrefix) < document2.wordCount(keywordPrefix)) {
                return 1;
            } else if (document1.wordCount(keywordPrefix) > document2.wordCount(keywordPrefix)) {
                return -1;
            } else {
                return 0;
            }
        });
    }


    /**
     * Completely remove any trace of any document which contains the given keyword
     * @param keyword
     * @return a Set of URIs of the documents that were deleted.
     */
    public Set<URI> deleteAll(String keyword){
        keyword = keyword.toLowerCase(); //new
        List<Document> search = search(keyword);
        this.trie.deleteAll(keyword); //new
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
            Set<String> innerWordsToDelete = d.getWords();
            for(String singleInnerWordsToDelete : innerWordsToDelete){
                this.trie.delete(singleInnerWordsToDelete, d);
                uriSet.add((d).getKey());
            }
            //new stuff added to remove docs from hashtable
            this.hashTableImpl.put(d.getKey(), null);//new line only passed one
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
        keywordPrefix = keywordPrefix.toLowerCase();//new
        List<Document> checkIfWillWork = searchByPrefix(keywordPrefix);//this one
        if(checkIfWillWork.size() > 1){
            CommandSet commandSet = new CommandSet();
            for(Document document : checkIfWillWork){
                Function<URI, Boolean> currentDoc = URI -> saveUndoDoc((DocumentImpl) document, document.getKey());
                GenericCommand genericCommand = new GenericCommand(document.getKey(), currentDoc);
                commandSet.addCommand((genericCommand));
            }
            this.commandStack.push(commandSet);
        }else{
            GenericCommand genericCommand;
            for(Document document : checkIfWillWork){
                Function<URI, Boolean> currentDoc = URI -> saveUndoDoc((DocumentImpl) document, document.getKey());
                genericCommand = new GenericCommand(document.getKey(), currentDoc);
                this.commandStack.push(genericCommand);
            }
        }
        Set<URI> uriSet = new HashSet<>();
        for(Document d : checkIfWillWork){
            Set<String> innerWordsToDelete = d.getWords(); //this inner part of the for-loop is new
            for(String singleInnerWordsToDelete : innerWordsToDelete){
                this.trie.delete(singleInnerWordsToDelete, d);
                uriSet.add((d).getKey());
            }
            this.hashTableImpl.put(d.getKey(), null); //and this
        }
        return uriSet;
    }


    private boolean saveUndoDoc(DocumentImpl document, URI uri){
        this.hashTableImpl.put(uri, document);
        Set<String> docsWords = document.getWords();
        for(String words : docsWords){
            this.trie.put(words, document);
        }
        return true;
    }

    private boolean savegetRidOfUndoDoc(DocumentImpl document, URI uri){
        this.hashTableImpl.put(uri, null);
        Set<String> docsWords = document.getWords();
        for(String words : docsWords){
            this.trie.delete(words, document);
        }
        return true;
    }
}


