package com.sipho.book_service.service;

import com.sipho.book_service.model.Book;
import com.sipho.book_service.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @InjectMocks
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookService = new BookService(bookRepository);
    }

    @Test
    void should_saveBookToDatabase() {
        //given
        Book book = new Book();
        //when
        bookService.addBook(book);

        //then

        verify(bookRepository).save(book);
    }

    @Test
    void should_deleteBookFromDatabase() {
        int bookId = 1;

        bookService.deleteBook(bookId);

        verify(bookRepository).deleteById(bookId);
    }

    @Test
    void should_GetBookByIdFromDatabase_When_GetBookCalled() {
        int bookId = 1;

        bookService.getBook(bookId);

        verify(bookRepository).findById(bookId);
    }

    @Test
    void should_ReturnAllBooks_When_AuthorAndGenreEmpty() {
        String author = "";
        String genre = "";

        bookService.getBooks(author, genre);

        verify(bookRepository).findAll();
    }

    @Test
    void should_ReturnAllBooks_When_AuthorAndGenreNull() {
        String author = null;
        String genre = null;

        bookService.getBooks(author, genre);

        verify(bookRepository).findAll();
    }

    @Test
    void should_ReturnBooksOfGenreOnly_When_AuthorEmpty() {
        String author = "";
        String genre = "Thriller";

        bookService.getBooks(author, genre);

        verify(bookRepository).findByGenre(genre);
    }

    @Test
    void should_ReturnBooksByAuthorOnly_When_GenreEmpty() {
        String author = "Paul Coelho";
        String genre = "";

        bookService.getBooks(author, genre);

        verify(bookRepository).findByAuthor(author);
    }

    @Test
    void should_saveBookToDatabase_When_UpdateBookCalled() {
        //given
        int bookId = 1;
        Book book = new Book();
        book.setId(bookId);

        //when
        bookService.updateBook(bookId,book);

        //then

        verify(bookRepository).save(book);
    }

    @Test
    void should_ReturnCorrectMessage_When_SaveBookCalled() {
        // given
        String expected = "Book stored successfully";
        Book book = new Book();
        // when

        String actual = bookService.addBook(book);

        // then
        verify(bookRepository, times(1)).save(book);
        assertEquals(expected, actual);
    }
}
