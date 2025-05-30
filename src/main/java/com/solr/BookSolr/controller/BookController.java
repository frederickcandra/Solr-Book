package com.solr.BookSolr.controller;

import com.solr.BookSolr.model.Book;
import com.solr.BookSolr.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping
    public String addBook(@RequestBody Book book) throws Exception {
        service.save(book);
        return "Book saved successfully!";
    }

    @GetMapping
    public List<Book> getAllBooks() throws Exception {
        return service.findAll();
    }


    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam("q") String keyword) {
        return service.searchByKeyword(keyword);
    }

}
