package com.solr.BookSolr.controller;

import com.solr.BookSolr.model.Book;
import com.solr.BookSolr.response.ApiResponse;
import com.solr.BookSolr.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    private Map<String, Object> createHeader(String message, int status, int count) {
        Map<String, Object> header = new HashMap<>();
        header.put("status", status);
        header.put("message", message);
        header.put("count", count);
        header.put("timestamp", System.currentTimeMillis());
        return header;
    }

    @PostMapping
    public ApiResponse<String> addBook(@RequestBody Book book) throws Exception {
        service.save(book);
        return new ApiResponse<>(
                createHeader("Book saved successfully", 200, 1),
                "Book saved successfully!"
        );
    }

    @PostMapping("/bulk")
    public ApiResponse<String> addBooks(@RequestBody List<Book> books) throws Exception {
        service.saveAll(books);
        return new ApiResponse<>(
                createHeader("Bulk insert successful", 200, books.size()),
                books.size() + " books saved successfully!"
        );
    }

    @GetMapping
    public ApiResponse<List<Book>> getAllBooks() throws Exception {
        List<Book> result = service.findAll();
        return new ApiResponse<>(
                createHeader("Books fetched successfully", 200, result.size()),
                result
        );
    }

    @GetMapping("/search")
    public ApiResponse<List<Book>> searchBooks(@RequestParam("q") String keyword) {
        List<Book> result = service.searchByKeyword(keyword);
        return new ApiResponse<>(
                createHeader("Search completed", 200, result.size()),
                result
        );
    }
}