package org.example;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Indexer {
    private static final String INDEX_DIR = "src/main/resources/lucene-index";
    private static final String SPELL_CHECK_INDEX_DIR = "src/main/resources/spellcheck-index";

    public void createIndex(String csvFilePath) {
        try {
            Path path = Paths.get(INDEX_DIR);
            if (Files.exists(path)) {
                for (var file : Objects.requireNonNull(path.toFile().listFiles())) {
                    if (file.isFile()) file.delete();
                }
            }

            FSDirectory directory = FSDirectory.open(path);
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter writer = new IndexWriter(directory, config);

            BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
            String line = br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                if (fields.length > 7) {
                    Document doc = new Document();
                    doc.add(new TextField("title", fields[1].trim(), Field.Store.YES));
                    doc.add(new TextField("year", fields[2].trim(), Field.Store.YES));
                    doc.add(new TextField("genre", fields[5].trim(), Field.Store.YES));
                    doc.add(new TextField("rating", fields[6].trim(), Field.Store.YES));
                    doc.add(new TextField("overview", fields[7].trim(), Field.Store.YES));
                    writer.addDocument(doc);
                }
            }

            writer.close();

            // Build spell-check index
            createSpellCheckIndex();
            System.out.println("Indexing completed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createSpellCheckIndex() {
        try {
            FSDirectory spellCheckDir = FSDirectory.open(Paths.get(SPELL_CHECK_INDEX_DIR));
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter writer = new IndexWriter(spellCheckDir, config);
            writer.close();
            System.out.println("Spell-check index created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
