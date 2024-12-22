package org.example;

import org.apache.lucene.document.Document;

import java.util.ArrayList;
import java.util.List;

public class Paginator {
    private int currentPage = 1;
    private static final int RESULTS_PER_PAGE = 3; // Show 3 results per page
    private List<Document> results = new ArrayList<>(); // Ensure it's initialized

    public void setupPagination(List<Document> results) {
        this.results = results;
        this.currentPage = 1;
    }

    public void displayCurrentPage() {
        if (results == null || results.isEmpty()) {
            System.out.println("No results to display.");
            return;
        }

        int start = (currentPage - 1) * RESULTS_PER_PAGE;
        int end = Math.min(start + RESULTS_PER_PAGE, results.size());

        System.out.println("\nPage " + currentPage + " of " + ((results.size() + RESULTS_PER_PAGE - 1) / RESULTS_PER_PAGE));
        for (int i = start; i < end; i++) {
            Document doc = results.get(i);
            System.out.println("Title: " + doc.get("title"));
            System.out.println("Year: " + doc.get("year"));
            System.out.println("Genre: " + doc.get("genre"));
            System.out.println("Rating: " + doc.get("rating"));
            System.out.println("Overview: " + doc.get("overview"));
            System.out.println("-------------");
        }

        if (end < results.size()) {
            System.out.println("...more available on the next page.");
        }
    }

    public void nextPage() {
        if ((currentPage * RESULTS_PER_PAGE) < results.size()) {
            currentPage++;
            displayCurrentPage();
        } else {
            System.out.println("You are already on the last page.");
        }
    }

    public void previousPage() {
        if (currentPage > 1) {
            currentPage--;
            displayCurrentPage();
        } else {
            System.out.println("You are already on the first page.");
        }
    }
}
