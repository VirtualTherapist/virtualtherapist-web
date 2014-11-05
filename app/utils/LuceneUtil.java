package utils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Weight;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

public class LuceneUtil {

    private static LuceneUtil instance;
    private final String FIELD_NAME = "question";
    private Directory directory;

    private LuceneUtil() {
        directory = new RAMDirectory();
    }

    public static LuceneUtil getInstance() {
        if(instance == null) {
            instance = new LuceneUtil();
        }
        return instance;
    }

    public void test() {
        System.out.println("Running test");
        addToDocument("Dit is een voorbeeldvraag over mijn telefoon");
        addToDocument("Is mijn telefoon waterdicht?");
        addToDocument("Is mijn telefoon tot 500 meter waterdicht?");
        addToDocument("Is mijn telefoon niet waterdicht?");
        addToDocument("Is mijn vriendin haar telefoon waterdicht?");
        addToDocument("Is haar mobiel waterdicht?");

        String[] array = {"telefoon", "waterdicht"};

        searchInDocument(array);
    }

    public void searchInDocument(String[] searchTerms) {
        try{
            System.out.println("Searching in doc");
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);

            PhraseQuery query = new PhraseQuery();
            for(String termString : searchTerms) {
                Term term = new Term(FIELD_NAME, termString);
                query.add(term);
            }
            System.out.println(query.toString());

            ScoreDoc[] scores = searcher.search(query, null, 1000).scoreDocs;
            System.out.println("Number of hits: " + scores.length);
            for(ScoreDoc scoreDoc : scores) {
                System.out.println(scoreDoc.toString());
            }

        }catch(IOException e) {
            e.printStackTrace();
        }

    }

    public void addToDocument(String text) {
        try{
            System.out.println("adding text to doc");
            Analyzer analyzer = new StandardAnalyzer();


            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
            IndexWriter indexWriter = new IndexWriter(directory, config);
            Document doc = new Document();
            doc.add(new Field(FIELD_NAME, text, TextField.TYPE_STORED));
            indexWriter.addDocument(doc);
            indexWriter.close();
        }catch(IOException e) {
            e.printStackTrace();
        }

    }

}
