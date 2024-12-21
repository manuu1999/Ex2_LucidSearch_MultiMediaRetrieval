package org.example;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Searcher {
    private static final String INDEX_DIR = "src/main/resources/lucene-index";

    public void search(String queryStr) {
        try {
            FSDirectory directory = FSDirectory.open(Paths.get(INDEX_DIR));
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            StandardAnalyzer analyzer = new StandardAnalyzer();

            // Define field boosts
            Map<String, Float> boosts = new HashMap<>();
            boosts.put("title", 2.0f);  // Higher relevance for title
            boosts.put("genre", 1.0f);
            boosts.put("year", 1.0f);
            boosts.put("rating", 1.0f);

            // Create MultiFieldQueryParser with field boosts
            MultiFieldQueryParser parser = new MultiFieldQueryParser(
                    boosts.keySet().toArray(new String[0]), analyzer
            );
            parser.setAllowLeadingWildcard(true); // Enable partial match

            Query query = parser.parse(queryStr);

            TopDocs results = searcher.search(query, 10);
            ScoreDoc[] hits = results.scoreDocs;

            System.out.println("Found " + hits.length + " results:");
            for (ScoreDoc hit : hits) {
                Document doc = searcher.doc(hit.doc);
                System.out.println("Title: " + doc.get("title"));
                System.out.println("Year: " + doc.get("year"));
                System.out.println("Genre: " + doc.get("genre"));
                System.out.println("Rating: " + doc.get("rating"));
                System.out.println("Overview: " + doc.get("overview"));
                System.out.println("-------------");
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
