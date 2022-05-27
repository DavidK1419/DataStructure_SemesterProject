package edu.yu.cs.com1320.project.stage5.impl;

import com.google.gson.*;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        //need logic for if already exists
        FileWriter files;
        String uriString = uri.toString().replaceAll("http://", "");
        if(this.path == null){
            Files.createFile((Paths.get(String.valueOf(System.getProperty("user.dir") + File.separator + uriString /*+ ".json"*/))));
            files = new FileWriter(System.getProperty("user.dir") + File.separator + uriString + ".json");
        }else{
            Files.createFile((Paths.get(String.valueOf(this.path + File.separator + uriString + ".json"))));
            files = new FileWriter(this.path + File.separator + uriString + ".json");
        }
        Gson gson = new Gson();
        gson.toJson((val), new FileWriter(String.valueOf(files)));
        files.close();




       /* FileWriter file;
        if(this.path != null) {
            file = new FileWriter(this.path + uriString + ".json");
        }else{
            file = new FileWriter(System.getProperty("user.dir") + File.separator + uriString + ".json");
        }
        Files.createDirectories((Path) (System.getProperty("user.dir") + File.separator + uriString + ".json"));
        String fileName = getPath(uri).toString();
        File file2 = new File(fileName);
        if(file2.exists()){
            this.delete(uri);
        }
        if(this.path != null) {
            file = new FileWriter(this.path + uriString + ".json");
        }else{
            file = new FileWriter(System.getProperty("user.dir") + File.separator + uriString + ".json");
        }
        Gson gson = new Gson();
        gson.toJson((val), new FileWriter(String.valueOf(getPath(uri))));
        file.close();*/
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
            file = new FileWriter(System.getProperty("user.dir") + File.separator + uriString + ".json");
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
