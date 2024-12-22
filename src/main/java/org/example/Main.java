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
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter search query: ");
                    String query = scanner.nextLine();
                    searcher.searchWithSpellCheck(query);

                    boolean filtering = true;
                    while (filtering) {
                        System.out.println("\nOptions:");
                        System.out.println("1. Narrow down the result with additional filters");
                        System.out.println("2. Search for other movies");
                        System.out.println("3. Exit");
                        System.out.print("Choose an option: ");

                        int filterChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (filterChoice) {
                            case 1:
                                System.out.print("Enter filter field (title, genre, year, rating): ");
                                String field = scanner.nextLine();
                                System.out.print("Enter filter value: ");
                                String value = scanner.nextLine();
                                searcher.filterResults(field, value);
                                break;

                            case 2:
                                filtering = false;
                                break;

                            case 3:
                                filtering = false;
                                running = false;
                                break;

                            default:
                                System.out.println("Invalid option. Please try again.");
                        }
                    }
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
