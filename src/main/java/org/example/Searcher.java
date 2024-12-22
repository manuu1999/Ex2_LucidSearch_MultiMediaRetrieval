package org.example;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Searcher {
    private static final String INDEX_DIR = "src/main/resources/lucene-index";
    private static final String SPELL_INDEX_DIR = "src/main/resources/spellcheck-index";

    // Store the last results to allow filtering
    private List<Document> lastResults = new ArrayList<>();

    public void searchWithSpellCheck(String queryStr) {
        try {
            // Load the spell-checker index
            FSDirectory spellDirectory = FSDirectory.open(Paths.get(SPELL_INDEX_DIR));
            SpellChecker spellChecker = new SpellChecker(spellDirectory);

            // Check if the query is misspelled
            if (!spellChecker.exist(queryStr)) {
                String[] suggestions = spellChecker.suggestSimilar(queryStr, 5); // Up to 5 suggestions
                if (suggestions.length > 0) {
                    System.out.println("Did you mean?");
                    for (String suggestion : suggestions) {
                        System.out.println(" - " + suggestion);
                    }
                    System.out.println("Please re-enter your query with one of the suggestions or try again.");
                    return; // Exit to let the user retry
                }
            }

            // Proceed with search
            FSDirectory directory = FSDirectory.open(Paths.get(INDEX_DIR));
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            StandardAnalyzer analyzer = new StandardAnalyzer();

            String[] fields = {"title", "genre", "year", "rating"};
            MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);
            parser.setAllowLeadingWildcard(true); // Enable partial match

            Query query = parser.parse(queryStr);
            TopDocs results = searcher.search(query, 100); // Fetch up to 100 results
            ScoreDoc[] hits = results.scoreDocs;

            // Clear previous results
            lastResults.clear();

            // Display results and store them for filtering
            System.out.println("Found " + hits.length + " results:");
            for (ScoreDoc hit : hits) {
                Document doc = searcher.doc(hit.doc);
                lastResults.add(doc); // Store the result for future filtering
                displayDocument(doc);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void filterResults(String filterField, String filterValue) {
        System.out.println("\nFiltered results for " + filterField + ": " + filterValue);
        List<Document> filteredResults = new ArrayList<>();

        for (Document doc : lastResults) {
            if (doc.get(filterField).toLowerCase().contains(filterValue.toLowerCase())) {
                filteredResults.add(doc);
                displayDocument(doc);
            }
        }

        // Update lastResults to the filtered list
        lastResults = filteredResults;

        if (filteredResults.isEmpty()) {
            System.out.println("No results found for the filter.");
        }
    }

    private void displayDocument(Document doc) {
        System.out.println("Title: " + doc.get("title"));
        System.out.println("Year: " + doc.get("year"));
        System.out.println("Genre: " + doc.get("genre"));
        System.out.println("Rating: " + doc.get("rating"));
        System.out.println("Overview: " + doc.get("overview"));
        System.out.println("-------------");
    }
}
