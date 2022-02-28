package edu.yu.cs.com1320.project.test;

import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage1.DocumentStore;
import edu.yu.cs.com1320.project.stage1.impl.DocumentStoreImpl;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Stage1Tests {

    @Test
    public void testPutAndGet(){
        HashTableImpl<Integer, String> testMapThing = new HashTableImpl<>();
        int key = 1234;
        testMapThing.put(key, "YAY");
        assertEquals("YAY", testMapThing.get(key));
        testMapThing.put(key, null);
        assertEquals(null, testMapThing.get(key));
    }


    @Test
    public void multiplePutsAndGets(){
        HashTableImpl<Integer, String> testMapThing = new HashTableImpl<>();
        int key1 = 1;
        int key2 = 2;
        int key3 = 3;
        testMapThing.put(key1, "A");
        testMapThing.put(key2, "B");
        testMapThing.put(key3, "C");
        assertEquals("A", testMapThing.get(key1));
        assertEquals("C", testMapThing.get(key3));
        assertEquals("B", testMapThing.get(key2));
        testMapThing.put(key1, null);
        testMapThing.put(key3, "HI");
        assertEquals(null, testMapThing.get(key1));
        assertEquals("HI", testMapThing.get(key3));
    }

    @Test
    public void generalTest() throws IOException, URISyntaxException {
        DocumentStoreImpl generalTest = new DocumentStoreImpl();
        String test = "YAY";
        InputStream testStream = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
        URI uri = new URI("HI");
        generalTest.putDocument(testStream, uri, DocumentStore.DocumentFormat.TXT);
        assertEquals("YAY", generalTest.getDocument(uri).getDocumentTxt());
        assertEquals(uri, generalTest.getDocument(uri).getKey());
    }

    @Test
    public void moreTests() throws URISyntaxException, IOException {
        DocumentStoreImpl docTest = new DocumentStoreImpl();
        String test = "A";
        InputStream testStream = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
        URI uri = new URI("1");
        String test2 = "B";
        InputStream testStream2 = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
        URI uri2 = new URI("2");
        docTest.putDocument(testStream, uri, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream2, uri2, DocumentStore.DocumentFormat.BINARY);
        assertEquals("A", docTest.getDocument(uri).getDocumentTxt());
        assertEquals(uri, docTest.getDocument(uri).getKey());
        /*String test3 = "BLAH";
        InputStream testStream3 = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
        docTest.putDocument(testStream3, uri, DocumentStore.DocumentFormat.TXT);
        assertEquals("BLAH", docTest.getDocument(uri).getDocumentTxt());*/

    }
}
