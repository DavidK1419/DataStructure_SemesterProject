package edu.yu.cs.com1320.project.stage5.impl;

import com.google.gson.*;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;
import java.io.*;
import java.net.URI;

/**
 * created by the document store and given to the BTree via a call to BTree.setPersistenceManager
 */
public class DocumentPersistenceManager implements PersistenceManager<URI, Document> {
    File path;


    public DocumentPersistenceManager(File baseDir){
        if(baseDir != null){
            baseDir = new File(String.valueOf(baseDir).replaceAll("http://", "") );
        }
        this.path = baseDir;
    }

    @Override
    public void serialize(URI uri, Document val) throws IOException {
        String fileName = getPath(uri).toString();
        File file2 = new File(fileName);
        if(file2.exists()){
            this.delete(uri);
        }
        String uriString = uri.toString().replaceAll("http://", "");
        FileWriter file;
        if(this.path != null) {
            file = new FileWriter(this.path + uriString + ".json");
        }else{
            file = new FileWriter("user.dir/" + uriString + ".json");
        }
        Gson gson = new Gson();
        gson.toJson((val), new FileWriter(String.valueOf(getPath(uri))));
        file.close();
    }

    @Override
    public Document deserialize(URI uri) throws IOException {
        Gson gson = new Gson();
        Document doc = gson.fromJson(String.valueOf(getPath(uri)), Document.class);
        this.delete(uri);
        return doc;
    }

    @Override
    public boolean delete(URI uri) throws IOException {
        String fileName = getPath(uri).toString();
        File file;
        try{
            file = new File(fileName);
            file.delete();
            return true;
        }catch(Exception e){
            return false;
        }
    }

    private FileWriter getPath(URI uri) throws IOException {
        String uriString = uri.toString().replaceAll("http://", "");
        FileWriter file;
        if(this.path != null) {
            file = new FileWriter(this.path + uriString + ".json");
        }else{
            file = new FileWriter("user.dir/" + uriString + ".json");
        }
        return file;
    }
}


        /*Boolean isFileThere;
        try{
            file2 = new File(fileName);
            isFileThere = true;
        }catch(NullPointerException e){
            isFileThere = false;
        }
        if(isFileThere){
            this.delete(uri);
        }*/
