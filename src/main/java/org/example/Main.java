package org.example;

import org.apache.lucene.document.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Indexer indexer = new Indexer();
        Searcher searcher = new Searcher();

        System.out.println("Welcome to IMDB Movie Search!");
        System.out.println("Building index...");
        indexer.createIndex("src/resources/imdb_top_1000.csv");
        System.out.println("Index built successfully.");

        boolean running = true;

        List<Document> currentResults = new ArrayList<>(); // To store results for narrowing down

        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. Search for movies");
            if (!currentResults.isEmpty()) {
                System.out.println("2. Narrow down the result with additional filters");
            }
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: // Search for movies
                    System.out.print("Enter search query: ");
                    String query = scanner.nextLine();
                    currentResults = searcher.search(query); // Perform initial search
                    break;

                case 2: // Narrow down results
                    if (currentResults.isEmpty()) {
                        System.out.println("No results to filter. Perform a new search first.");
                    } else {
                        System.out.print("Enter filter term to narrow down results: ");
                        String filter = scanner.nextLine();
                        currentResults = searcher.filterResults(currentResults, filter); // Narrow down results
                    }
                    break;

                case 3: // Exit
                    System.out.println("Exiting the program. Goodbye!");
                    running = false;
                    break;

                default: // Invalid input
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
