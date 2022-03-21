package edu.yu.cs.com1320.project.stage2.impl;

import edu.yu.cs.com1320.project.stage2.Document;

import java.net.URI;
import java.util.Arrays;

public class DocumentImpl implements Document {
    private final URI uri;
    private String txt;
    private byte[] binaryData;


    public DocumentImpl(URI uri, String txt){
        if((uri == null) || (txt == null)){
            throw new IllegalArgumentException();
        }
        this.uri = uri;
        this.txt = txt;
    }

    public DocumentImpl(URI uri, byte[] binaryData){
        if((uri == null) || (binaryData == null)){
            throw new IllegalArgumentException();
        }
        this.uri = uri;
        this.binaryData = binaryData;
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
}