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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Searcher {
    private static final String INDEX_DIR = "src/main/resources/lucene-index";

    // Perform the initial search
    public List<Document> search(String queryStr) {
        List<Document> resultsList = new ArrayList<>();
        try {
            FSDirectory directory = FSDirectory.open(Paths.get(INDEX_DIR));
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            StandardAnalyzer analyzer = new StandardAnalyzer();

            // Fields to search
            String[] fields = {"title", "genre", "year", "rating"};
            MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);
            parser.setAllowLeadingWildcard(true);

            Query query = parser.parse(queryStr);
            TopDocs results = searcher.search(query, 100);

            System.out.println("Found " + results.totalHits.value + " results:");
            for (ScoreDoc hit : results.scoreDocs) {
                Document doc = searcher.doc(hit.doc);
                resultsList.add(doc);

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

        return resultsList;
    }

    // Narrow down the results based on a filter
    public List<Document> filterResults(List<Document> currentResults, String filter) {
        List<Document> filteredResults = currentResults.stream()
                .filter(doc -> doc.get("title").toLowerCase().contains(filter.toLowerCase())
                        || doc.get("genre").toLowerCase().contains(filter.toLowerCase())
                        || doc.get("year").equals(filter)
                        || doc.get("rating").equals(filter))
                .collect(Collectors.toList());

        if (filteredResults.isEmpty()) {
            System.out.println("No results found after filtering.");
        } else {
            System.out.println("Filtered results:");
            for (Document doc : filteredResults) {
                System.out.println("Title: " + doc.get("title"));
                System.out.println("Year: " + doc.get("year"));
                System.out.println("Genre: " + doc.get("genre"));
                System.out.println("Rating: " + doc.get("rating"));
                System.out.println("Overview: " + doc.get("overview"));
                System.out.println("-------------");
            }
        }

        return filteredResults;
    }
}
