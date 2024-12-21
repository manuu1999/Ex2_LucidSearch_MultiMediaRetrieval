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
                    System.out.println("\nSearch by:");
                    System.out.println("1. Title");
                    System.out.println("2. Year");
                    System.out.println("3. Genre");
                    System.out.print("Choose a search field: ");
                    int fieldChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline

                    String field;
                    switch (fieldChoice) {
                        case 1:
                            field = "title";
                            break;
                        case 2:
                            field = "year";
                            break;
                        case 3:
                            field = "genre";
                            break;
                        default:
                            System.out.println("Invalid field choice. Returning to menu.");
                            continue;
                    }

                    System.out.print("Enter search query: ");
                    String query = scanner.nextLine();
                    searcher.search(field, query);
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
