# Practical Exercise 2 Solutions
These are my Solutions (Manuel Buser) for the Practical Exercise 2 in the module Multimedia Retrieval. In the different commit versions you can see the steps I took to achieve the solutions for each sub task. 

## Open Project Documentation in the Resource Folder
Please open the Project Documentation first, which will be a guide through the whole development process of this practical project. The document is named: "Documentation Practical Exercise 2.pdf" and can be found under: "src/resources".

## Introduction
This project implements a robust movie search system using Lucene, focusing on indexing, searching, and enhancing query results. It explores advanced text retrieval techniques, including fuzzy matching, spell-checking, faceted search, and pagination. The system works with a movie dataset from Kaggle.com and enables users to search and filter movie information interactively.

## Features
Indexing: Movies are indexed with fields like title, genre, year, and rating.
Basic Search: Retrieve movies based on keywords.
Fuzzy Matching: Handle misspelled queries with partial matches.
Spell-Checking: Suggest corrections for misspelled inputs.
Faceted Search: Filter results by genre, year, or rating.
Pagination: Display results in pages for better navigation.

## How to Run
-Clone the repository to your local machine
-Open the project in IntelliJ IDEA or another IDE of your choice.
-Install required Lucene dependencies via Maven.
-Place the movie dataset (imdb_top_1000.csv) in the src/resources directory.
-Run the Main.java file to start the interactive search system.

## Requirements
-Java 19
-Maven for dependency management
-IntelliJ IDEA or any Java IDE
