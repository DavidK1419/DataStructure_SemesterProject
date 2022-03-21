package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.stage2.DocumentStore;
import edu.yu.cs.com1320.project.stage2.impl.DocumentStoreImpl;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class StackImplTest {
    @Test
    public void testStackPeek() throws URISyntaxException, IOException {
        StackImpl stack = new StackImpl();
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
        docTest.deleteDocument(uri1);
        boolean check = true;
        try{
            docTest.getDocument(uri1).getDocumentTxt();
        }catch(NullPointerException e){
            check = false;
        }
        if(check){
            throw new IllegalArgumentException();
        }
        docTest.undo();
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
        docTest.putDocument(testStream14, uri13, DocumentStore.DocumentFormat.TXT);
        docTest.undo();
        docTest.undo(uri6);
        boolean check2 = true;
        try{
            docTest.getDocument(uri6).getDocumentTxt();
        }catch(NullPointerException e){
            check2 = false;
        }
        if(check2){
            throw new IllegalArgumentException();
        }
       // assertEquals(null, docTest.getDocument(uri6).getDocumentTxt());
    }

    @Test
    public void testUndo() throws URISyntaxException, IOException {
        StackImpl stack = new StackImpl();
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
        assertEquals("A", docTest.getDocument(uri1).getDocumentTxt());
        assertEquals("B", docTest.getDocument(uri2).getDocumentTxt());
        assertEquals("C", docTest.getDocument(uri3).getDocumentTxt());

        docTest.putDocument(testStream4, uri2, DocumentStore.DocumentFormat.TXT);
        assertEquals("D", docTest.getDocument(uri2).getDocumentTxt());
        docTest.putDocument(testStream5, uri3, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream6, uri2, DocumentStore.DocumentFormat.TXT);
        docTest.putDocument(testStream7, uri2, DocumentStore.DocumentFormat.TXT);
        assertEquals("G", docTest.getDocument(uri2).getDocumentTxt());
        //System.out.println(docTest.getDocument(uri2).getDocumentTxt());
        docTest.undo();
        assertEquals("F", docTest.getDocument(uri2).getDocumentTxt());

    }

    }
