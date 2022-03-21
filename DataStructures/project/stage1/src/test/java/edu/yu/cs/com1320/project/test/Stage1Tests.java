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

    @Test
    public void multiplePutandGets2() throws URISyntaxException, IOException {
        DocumentStoreImpl docTest = new DocumentStoreImpl();
        String test1 = "A";
        String test2 = "B";
        String test3 = "C";
        String test4 = "D";
        String test5 = "E";
        String test6 = "F";
        String test7 = "G";
        InputStream testStream1 = new ByteArrayInputStream(test1.getBytes(StandardCharsets.UTF_8));
        InputStream testStream2 = new ByteArrayInputStream(test2.getBytes(StandardCharsets.UTF_8));
        InputStream testStream3 = new ByteArrayInputStream(test3.getBytes(StandardCharsets.UTF_8));
        InputStream testStream4 = new ByteArrayInputStream(test4.getBytes(StandardCharsets.UTF_8));
        InputStream testStream5 = new ByteArrayInputStream(test5.getBytes(StandardCharsets.UTF_8));
        InputStream testStream6 = new ByteArrayInputStream(test6.getBytes(StandardCharsets.UTF_8));
        InputStream testStream7 = new ByteArrayInputStream(test7.getBytes(StandardCharsets.UTF_8));
        URI uri1 = new URI("1");
        URI uri2 = new URI("2");
        URI uri3 = new URI("3");
        URI uri4 = new URI("4");
        URI uri5 = new URI("5");
        URI uri6 = new URI("6");
        URI uri7 = new URI("7");
        docTest.putDocument(testStream1, uri1, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream2, uri2, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream3, uri3, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream4, uri4, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream5, uri5, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream6, uri6, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream7, uri7, DocumentStore.DocumentFormat.TXT);
        assertEquals("A", docTest.getDocument(uri1).getDocumentTxt());
        assertEquals("B", docTest.getDocument(uri2).getDocumentTxt());
        assertEquals("C", docTest.getDocument(uri3).getDocumentTxt());
        assertEquals("D", docTest.getDocument(uri4).getDocumentTxt());
        assertEquals("E", docTest.getDocument(uri5).getDocumentTxt());
        assertEquals("F", docTest.getDocument(uri6).getDocumentTxt());
        assertEquals("G", docTest.getDocument(uri7).getDocumentTxt());
//        assertEquals("uri", docTest.getDocument(uri).getKey());

    }

    @Test
    public void multiplePutandGets3() throws URISyntaxException, IOException {
        DocumentStoreImpl docTest = new DocumentStoreImpl();
        String test1 = "A";
        String test2 = "B";
        String test3 = "C";
        String test4 = "D";
        String test5 = "E";
        String test6 = "F";
        String test7 = "G";
        String test8 = "H";
        String test9 = "I";
        String test10 = "J";
        String test11 = "K";
        String test12 = "L";
        String test13 = "M";
        String test14 = "N";
        InputStream testStream1 = new ByteArrayInputStream(test1.getBytes(StandardCharsets.UTF_8));
        InputStream testStream2 = new ByteArrayInputStream(test2.getBytes(StandardCharsets.UTF_8));
        InputStream testStream3 = new ByteArrayInputStream(test3.getBytes(StandardCharsets.UTF_8));
        InputStream testStream4 = new ByteArrayInputStream(test4.getBytes(StandardCharsets.UTF_8));
        InputStream testStream5 = new ByteArrayInputStream(test5.getBytes(StandardCharsets.UTF_8));
        InputStream testStream6 = new ByteArrayInputStream(test6.getBytes(StandardCharsets.UTF_8));
        InputStream testStream7 = new ByteArrayInputStream(test7.getBytes(StandardCharsets.UTF_8));
        InputStream testStream8 = new ByteArrayInputStream(test8.getBytes(StandardCharsets.UTF_8));
        InputStream testStream9 = new ByteArrayInputStream(test9.getBytes(StandardCharsets.UTF_8));
        InputStream testStream10 = new ByteArrayInputStream(test10.getBytes(StandardCharsets.UTF_8));
        InputStream testStream11 = new ByteArrayInputStream(test11.getBytes(StandardCharsets.UTF_8));
        InputStream testStream12 = new ByteArrayInputStream(test12.getBytes(StandardCharsets.UTF_8));
        InputStream testStream13 = new ByteArrayInputStream(test13.getBytes(StandardCharsets.UTF_8));
        InputStream testStream14 = new ByteArrayInputStream(test14.getBytes(StandardCharsets.UTF_8));
        URI uri1 = new URI("1");
        URI uri2 = new URI("2");
        URI uri3 = new URI("3");
        URI uri4 = new URI("4");
        URI uri5 = new URI("5");
        URI uri6 = new URI("6");
        URI uri7 = new URI("7");
        URI uri8 = new URI("8");
        URI uri9 = new URI("9");
        URI uri10 = new URI("10");
        URI uri11 = new URI("11");
        URI uri12 = new URI("12");
        URI uri13 = new URI("13");
        URI uri14 = new URI("14");
        docTest.putDocument(testStream1, uri1, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream2, uri2, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream3, uri3, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream4, uri4, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream5, uri5, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream6, uri6, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream7, uri7, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream8, uri8, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream9, uri9, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream10, uri10, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream11, uri11, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream12, uri12, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream13, uri13, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream14, uri14, DocumentStore.DocumentFormat.TXT);
        //System.out.println("The thing is " + docTest.getDocument(uri6).getDocumentTxt());
        //System.out.println(docTest);
        assertEquals("A", docTest.getDocument(uri1).getDocumentTxt());
        assertEquals("B", docTest.getDocument(uri2).getDocumentTxt());
        assertEquals("C", docTest.getDocument(uri3).getDocumentTxt());
        assertEquals("D", docTest.getDocument(uri4).getDocumentTxt());
        assertEquals("E", docTest.getDocument(uri5).getDocumentTxt());
        assertEquals("F", docTest.getDocument(uri6).getDocumentTxt());
        assertEquals("G", docTest.getDocument(uri7).getDocumentTxt());
        assertEquals("H", docTest.getDocument(uri8).getDocumentTxt());
        assertEquals("I", docTest.getDocument(uri9).getDocumentTxt());
        assertEquals("J", docTest.getDocument(uri10).getDocumentTxt());
        assertEquals("K", docTest.getDocument(uri11).getDocumentTxt());
        assertEquals("L", docTest.getDocument(uri12).getDocumentTxt());
        assertEquals("M", docTest.getDocument(uri13).getDocumentTxt());
        assertEquals("N", docTest.getDocument(uri14).getDocumentTxt());
//        assertEquals("uri", docTest.getDocument(uri).getKey());

    }
}
