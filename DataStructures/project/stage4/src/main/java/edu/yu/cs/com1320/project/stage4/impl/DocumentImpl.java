package edu.yu.cs.com1320.project.stage4.impl;

import edu.yu.cs.com1320.project.stage4.Document;

import java.net.URI;
import java.util.*;

public class DocumentImpl implements Document {
    private final URI uri;
    private String txt;
    private byte[] binaryData;
    private String[] spaceTxt;
    private Map<String, Integer> wordCounter;
    private boolean checkWhatHeckJudahWantsFromMyLife = false;
    private Set<String> setOfStrings = new HashSet<>();
    private long lastTimeUsed;


    public DocumentImpl(URI uri, String txt){
        if((uri == null) || (txt == null)){
            throw new IllegalArgumentException();
        }
        this.uri = uri;
        this.txt = txt;
        this.wordCounter = new HashMap<>();
        this.spaceTxt = this.txt.split(" ");
        for(int counter = 0; counter < this.spaceTxt.length; counter++){
            this.setOfStrings.add((this.spaceTxt[counter]).replaceAll("[^A-Za-z0-9]", ""));
            this.wordCounter.put((((this.spaceTxt[counter]).replaceAll("[^A-Za-z0-9]", "")).toLowerCase()), (wordCounter.getOrDefault(this.spaceTxt[counter], 0) + 1));
        }
    }

    public DocumentImpl(URI uri, byte[] binaryData){
        if((uri == null) || (binaryData == null)){
            throw new IllegalArgumentException();
        }
        this.uri = uri;
        this.binaryData = binaryData;
        this.checkWhatHeckJudahWantsFromMyLife = true;
    }


    /**
     * @return content of text document
     */
    public String getDocumentTxt(){
        return this.txt;
    }


    /**
     * @return content of binary data document
     */
    public byte[] getDocumentBinaryData(){
        return this.binaryData;
    }

    /**
     * @return URI which uniquely identifies this document
     */
    public URI getKey(){
        return this.uri;
    }

    /**
     * how many times does the given word appear in the document?
     * @param word
     * @return the number of times the given words appears in the document. If it's a binary document, return 0.
     */
    public int wordCount(String word){
        word = word.toLowerCase(); //fixed 2 tests
        if(checkWhatHeckJudahWantsFromMyLife){
            return 0;
        }
        int wordCounter = 0;
        for (int i = 0; i < this.spaceTxt.length; i++){
            String tester = this.spaceTxt[i].replaceAll("[^A-Za-z0-9]", "").toLowerCase();
            if(tester.startsWith(word)){
                wordCounter++;
            }
        }
        return wordCounter;
    }

    /**
     * @return all the words that appear in the document
     */
    public Set<String> getWords(){
        return this.setOfStrings;
    }

    /**
     * return the last time this document was used, via put/get or via a search result
     * (for stage 4 of project)
     */
    public long getLastUseTime(){
        return this.lastTimeUsed;
    }

    public void setLastUseTime(long timeInNanoseconds){
        this.lastTimeUsed = timeInNanoseconds;
    }

    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + (txt != null ? txt.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(binaryData);
        return result;
    }

    @Override
    public boolean equals(Object document){
        return this.hashCode() == document.hashCode();
    }

    @Override
    public int compareTo(Document o) {
        if(this.lastTimeUsed > o.getLastUseTime()){
            return 1;
        }else if(this.lastTimeUsed < o.getLastUseTime()){
            return -1;
        }else {
            return 0;
        }
    }
}