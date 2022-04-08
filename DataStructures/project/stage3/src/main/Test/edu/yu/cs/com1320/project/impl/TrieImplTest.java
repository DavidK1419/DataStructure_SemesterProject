package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.stage3.Document;
import edu.yu.cs.com1320.project.stage3.DocumentStore;
import edu.yu.cs.com1320.project.stage3.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage3.impl.DocumentStoreImpl;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TrieImplTest {
    @Test
    public void testTrie() throws URISyntaxException, IOException {
        DocumentStoreImpl docStore = new DocumentStoreImpl();
        String test1 = "A1 A2 A3";
        String test2 = "B1 B2 B3";
        String test3 = "C1 C2 C3";
        InputStream testStream1 = new ByteArrayInputStream(test1.getBytes(StandardCharsets.UTF_8));
        InputStream testStream2 = new ByteArrayInputStream(test2.getBytes(StandardCharsets.UTF_8));
        InputStream testStream3 = new ByteArrayInputStream(test3.getBytes(StandardCharsets.UTF_8));
        URI uri1 = new URI("1");
        URI uri2 = new URI("2");
        URI uri3 = new URI("3");
        docStore.putDocument(testStream1, uri1, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(testStream2, uri2, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(testStream3, uri3, DocumentStore.DocumentFormat.TXT);
        List<Document> listOfDocs = docStore.search("C2");
        Document d = listOfDocs.get(0);
        assertEquals("3", d.getKey().toString());
    }

    @Test
    public void testSearchByPrefix() throws IOException, URISyntaxException {
        DocumentStoreImpl docStore = new DocumentStoreImpl();
        String test1 = "tool tuesday toolbox 346d tuster454&$(*%l tu tu ";
        String test2 = "barf toy whatsapp testsers2";
        String test3 = "t@@@@@ool tuesday t:oolbox 346d tor tos tow";
        InputStream testStream1 = new ByteArrayInputStream(test1.getBytes(StandardCharsets.UTF_8));
        InputStream testStream2 = new ByteArrayInputStream(test2.getBytes(StandardCharsets.UTF_8));
        InputStream testStream3 = new ByteArrayInputStream(test3.getBytes(StandardCharsets.UTF_8));
        URI uri1 = new URI("1");
        URI uri2 = new URI("2");
        URI uri3 = new URI("3");
        docStore.putDocument(testStream1, uri1, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(testStream2, uri2, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(testStream3, uri3, DocumentStore.DocumentFormat.TXT);
        List<Document> listOfDocs = docStore.searchByPrefix("to");
        assertEquals(3,listOfDocs.size());
        assertEquals(3,Integer.parseInt(listOfDocs.get(0).getKey().toString()));
        assertEquals(1,Integer.parseInt(listOfDocs.get(1).getKey().toString()));
        assertEquals(2,Integer.parseInt(listOfDocs.get(2).getKey().toString()));
        listOfDocs = docStore.searchByPrefix("tu");
        assertEquals(2,listOfDocs.size());
        assertEquals(1,Integer.parseInt(listOfDocs.get(0).getKey().toString()));
        assertEquals(3,Integer.parseInt(listOfDocs.get(1).getKey().toString()));
    }

    @Test
    public void testDelete() throws IOException, URISyntaxException {
        DocumentStoreImpl docStore = new DocumentStoreImpl();
        String test1 = "tool tuesday toolbox 346d tuster454&$(*%l tu tu ";
        String test2 = "barf toy whatsapp testsers2";
        String test3 = "t@@@@@ool tuesday t:oolbox 346d tor tos tow";
        String test4 = "gibbly gobely gook On$e ud0n @ time todler";
        InputStream testStream1 = new ByteArrayInputStream(test1.getBytes(StandardCharsets.UTF_8));
        InputStream testStream2 = new ByteArrayInputStream(test2.getBytes(StandardCharsets.UTF_8));
        InputStream testStream3 = new ByteArrayInputStream(test3.getBytes(StandardCharsets.UTF_8));
        InputStream testStream4 = new ByteArrayInputStream(test4.getBytes(StandardCharsets.UTF_8));
        URI uri1 = new URI("1");
        URI uri2 = new URI("2");
        URI uri3 = new URI("3");
        URI uri4 = new URI("4");
        docStore.putDocument(testStream1, uri1, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(testStream2, uri2, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(testStream3, uri3, DocumentStore.DocumentFormat.TXT);
        List<Document> listOfDocs = docStore.searchByPrefix("to");
        assertEquals(3,Integer.parseInt(listOfDocs.get(0).getKey().toString()));
        listOfDocs = docStore.search("tow");
        assertEquals(3,Integer.parseInt(listOfDocs.get(0).getKey().toString()));
        docStore.deleteAll("tow");
        //System.out.println(docStore.search("tow"));
        docStore.undo(uri3);
        listOfDocs = docStore.search("tow");
        assertEquals(3,Integer.parseInt(listOfDocs.get(0).getKey().toString()));
        docStore.putDocument(testStream4, uri4, DocumentStore.DocumentFormat.TXT);
        listOfDocs = docStore.search("todler");
        Document d = listOfDocs.get(0);
        assertEquals("4", d.getKey().toString());
        docStore.undo();
        //System.out.println(docStore.search("todler"));
    }

    @Test
    public void testDeleteByPrefix() throws IOException, URISyntaxException {
        DocumentStoreImpl docStore = new DocumentStoreImpl();
        String test1 = "tool tuesday tioolbox 346d tuster454&$(*%l tu tu ";
        String test2 = "barf toy whatsapp testsers2";
        String test3 = "t@@@@l@ool tuesday t:oolbox 346d tor tos tow";
        String test4 = "gibbly gobely gook On$e ud0n @ time todler";
        InputStream testStream1 = new ByteArrayInputStream(test1.getBytes(StandardCharsets.UTF_8));
        InputStream testStream2 = new ByteArrayInputStream(test2.getBytes(StandardCharsets.UTF_8));
        InputStream testStream3 = new ByteArrayInputStream(test3.getBytes(StandardCharsets.UTF_8));
        InputStream testStream4 = new ByteArrayInputStream(test4.getBytes(StandardCharsets.UTF_8));
        URI uri1 = new URI("1");
        URI uri2 = new URI("2");
        URI uri3 = new URI("3");
        URI uri4 = new URI("4");
        docStore.putDocument(testStream1, uri1, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(testStream2, uri2, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(testStream3, uri3, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(testStream4, uri4, DocumentStore.DocumentFormat.TXT);
        Set<URI> setOfUris = docStore.deleteAllWithPrefix("tool");
        assertEquals(2, setOfUris.size());
        assertTrue(setOfUris.contains(uri1));
        assertTrue(setOfUris.contains(uri3));
        assertTrue(!setOfUris.contains(uri2));

    }

}