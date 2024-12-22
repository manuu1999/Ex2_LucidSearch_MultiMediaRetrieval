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

    private List<Document> lastResults = new ArrayList<>();
    private Paginator paginator = new Paginator();

    public void searchWithSpellCheck(String queryStr) {
        try {
            if (isNumeric(queryStr)) {
                // Skip spell checking for numeric inputs
                performSearch(queryStr);
                return;
            }

            FSDirectory spellDirectory = FSDirectory.open(Paths.get(SPELL_INDEX_DIR));
            SpellChecker spellChecker = new SpellChecker(spellDirectory);

            if (!spellChecker.exist(queryStr)) {
                String[] suggestions = spellChecker.suggestSimilar(queryStr, 5);
                if (suggestions.length > 0) {
                    System.out.println("Did you mean?");
                    for (String suggestion : suggestions) {
                        System.out.println(" - " + suggestion);
                    }
                    return;
                }
            }

            performSearch(queryStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performSearch(String queryStr) {
        try {
            FSDirectory directory = FSDirectory.open(Paths.get(INDEX_DIR));
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            StandardAnalyzer analyzer = new StandardAnalyzer();

            String[] fields = {"title", "genre", "year", "rating"};
            MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);
            parser.setAllowLeadingWildcard(true);

            Query query = parser.parse(queryStr);
            TopDocs results = searcher.search(query, 100);

            lastResults.clear();
            for (ScoreDoc hit : results.scoreDocs) {
                lastResults.add(searcher.doc(hit.doc));
            }

            paginator.setupPagination(lastResults);
            paginator.displayCurrentPage();

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void filterResults(String filterField, String filterValue) {
        List<Document> filteredResults = new ArrayList<>();
        for (Document doc : lastResults) {
            if (doc.get(filterField).toLowerCase().contains(filterValue.toLowerCase())) {
                filteredResults.add(doc);
            }
        }
        lastResults = filteredResults;
        paginator.setupPagination(lastResults);
        paginator.displayCurrentPage();
    }

    public void paginate(String direction) {
        if ("next".equalsIgnoreCase(direction)) {
            paginator.nextPage();
        } else if ("prev".equalsIgnoreCase(direction)) {
            paginator.previousPage();
        }
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
