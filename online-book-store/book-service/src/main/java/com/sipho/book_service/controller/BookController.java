package com.sipho.book_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sipho.book_service.model.Book;
import com.sipho.book_service.service.BookService;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
public class BookController {
    @Autowired
    private BookService bookService;
    
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks(@RequestParam(required = false) String author, @RequestParam(required = false) String genre) {
        return new ResponseEntity<>(bookService.getBooks(author, genre), HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBook(@PathVariable int id) {
        Book book = bookService.getBook(id);
        if(Objects.isNull(book)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // TODO: Improved error handling
        }
        return new ResponseEntity<>(book, HttpStatus.OK);
    }
    
    @PostMapping("/books")
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.addBook(book), HttpStatus.CREATED);
    }
    
    @PutMapping("/books/{id}")
    public ResponseEntity<String> updateBook(@PathVariable int id, @RequestBody Book book) {
        if(book.getId() != id){
            return ResponseEntity.badRequest().body("ID in path and request body do not match!");
        }
        return new ResponseEntity<>(bookService.updateBook(id, book), HttpStatus.OK);
    }

    @DeleteMapping("books/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable int id){
        return new ResponseEntity<>(bookService.deleteBook(id), HttpStatus.OK);
    }


}
