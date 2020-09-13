package me.oleniuk.learn.graphql.booksservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BooksController {

    @GetMapping
    public String ping(){
        return "pong";
    }

}
