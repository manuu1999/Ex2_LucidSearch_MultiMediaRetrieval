package org.example;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

public class Searcher {
    private static final String INDEX_DIR = "src/main/resources/lucene-index";

    public void search(String queryStr) {
        try {
            FSDirectory directory = FSDirectory.open(Paths.get(INDEX_DIR));
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            StandardAnalyzer analyzer = new StandardAnalyzer();

            // Boost title field matches and combine with other fields
            Query titleQuery = new QueryParser("title", analyzer).parse(queryStr);
            Query genreQuery = new QueryParser("genre", analyzer).parse(queryStr);
            Query overviewQuery = new QueryParser("overview", analyzer).parse(queryStr);

            // Combine queries with boosts
            BooleanQuery.Builder combinedQuery = new BooleanQuery.Builder();
            combinedQuery.add(new BoostQuery(titleQuery, 2.0f), BooleanClause.Occur.SHOULD);
            combinedQuery.add(new BoostQuery(genreQuery, 1.5f), BooleanClause.Occur.SHOULD);
            combinedQuery.add(overviewQuery, BooleanClause.Occur.SHOULD);

            TopDocs results = searcher.search(combinedQuery.build(), 10);
            ScoreDoc[] hits = results.scoreDocs;

            System.out.println("Found " + hits.length + " results:");
            for (ScoreDoc hit : hits) {
                Document doc = searcher.doc(hit.doc);
                System.out.println("Title: " + doc.get("title"));
                System.out.println("Year: " + doc.get("year"));
                System.out.println("Genre: " + doc.get("genre"));
                System.out.println("Rating: " + doc.get("rating_display"));
                System.out.println("Overview: " + doc.get("overview"));
                System.out.println("-------------");
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
