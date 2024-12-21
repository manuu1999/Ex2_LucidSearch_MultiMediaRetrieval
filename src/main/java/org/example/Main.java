package org.example;

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

        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. Search for movies");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    System.out.print("Enter search query (keywords, title, genre, etc.): ");
                    String query = scanner.nextLine();
                    searcher.search(query); // Directly pass the query to the searcher
                    break;

                case 2:
                    System.out.println("Exiting the program. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
