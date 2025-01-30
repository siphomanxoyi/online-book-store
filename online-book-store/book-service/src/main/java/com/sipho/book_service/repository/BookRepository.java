package com.sipho.book_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sipho.book_service.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{

    List<Book> findByAuthorAndGenre(String author, String genre);

    List<Book> findByAuthor(String author);

    List<Book> findByGenre(String genre);
    
}
