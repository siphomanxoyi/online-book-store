package com.sipho.book_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sipho.book_service.model.Book;
import com.sipho.book_service.repository.BookRepository;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    private static final String BOOK_SAVED_SUCCESS_MSG = "Book stored successfully";
    private static final String BOOK_UPDATED_SUCCESS_MSG = "Book updated successfully";
    private static final String BOOK_DELETED_SUCCESS_MSG = "Book deleted succesfully";
    
    @Autowired
    private final BookRepository bookRepository;


    public List<Book> getBooks(String author, String genre) {
        if(StringUtils.isEmpty(author) && StringUtils.isEmpty(genre)){
            return bookRepository.findAll();
        } else if(!StringUtils.isEmpty(author) && !StringUtils.isEmpty(genre)){
            return bookRepository.findByAuthorAndGenre(author, genre);
        } else if (!StringUtils.isEmpty(author)) {
            return bookRepository.findByAuthor(author);
        } else {
            return bookRepository.findByGenre(genre);
        }
    }

    public Book getBook(int id) {
        return bookRepository.findById(id).orElse(null); // TODO: Do better error handling for case where no matches
    }

    public String addBook(Book book) {
        log.info("Book: {}", book);
        bookRepository.save(book);
        return BOOK_SAVED_SUCCESS_MSG;
    }

    public String updateBook(int id, Book book) {
        bookRepository.save(book);
        return BOOK_UPDATED_SUCCESS_MSG;
    }

    public String deleteBook(int id) {
        bookRepository.deleteById(id);
        return BOOK_DELETED_SUCCESS_MSG;
    }
    
}
