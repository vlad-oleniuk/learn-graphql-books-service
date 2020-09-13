package me.oleniuk.learn.graphql.booksservice;

import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class GraphQLDataFetchers {

    private static List<Map<String, String>> books = Arrays.asList(
            Map.of("id", "book-1",
                    "name", "Harry Potter and the Philosopher's Stone",
                    "pageCount", "223",
                    "authorId", "author-1"),
            Map.of("id", "book-2",
                    "name", "Moby Dick",
                    "pageCount", "635",
                    "authorId", "author-2"),
            Map.of("id", "book-3",
                    "name", "Interview with the vampire",
                    "pageCount", "371",
                    "authorId", "author-3")
    );

    private static List<Map<String, String>> authors = new ArrayList(Arrays.asList(
            Map.of("id", "author-1",
                    "firstName", "Joanne",
                    "lastName", "Rowling"),
            Map.of("id", "author-2",
                    "firstName", "Herman",
                    "lastName", "Melville"),
            Map.of("id", "author-3",
                    "firstName", "Anne",
                    "lastName", "Rice")
    ));

    public DataFetcher getBookByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String bookId = dataFetchingEnvironment.getArgument("id");
            return books
                    .stream()
                    .filter(book -> book.get("id").equals(bookId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getAuthorDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, String> book = dataFetchingEnvironment.getSource();
            String authorId = book.get("authorId");
            return authors
                    .stream()
                    .filter(author -> author.get("id").equals(authorId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getBooksByAuthorDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, String> parentAuthor = dataFetchingEnvironment.getSource();
            String authorId = parentAuthor.get("id");
            return books
                    .stream()
                    .filter(book -> book.get("authorId").equals(authorId))
                    .collect(Collectors.toList());
        };
    }

    public DataFetcher getBooksDataFetcher() {
        return dataFetchingEnvironment ->
                books.stream().collect(Collectors.toList());
    }


    public DataFetcher getAuthorMutator() {
        return dataFetchingEnvironment -> {
            Map<String, String> author = Map.of("id", "author-"+authors.size(),
                    "firstName", dataFetchingEnvironment.getArgument("firstName"),
                    "lastName", dataFetchingEnvironment.getArgument("lastName"));
            return author;
        };
    }


}

