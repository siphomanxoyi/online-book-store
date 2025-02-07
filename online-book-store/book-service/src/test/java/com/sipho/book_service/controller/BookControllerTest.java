package com.sipho.book_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipho.book_service.model.Book;
import com.sipho.book_service.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void should_ReturnCorrectMessage_When_AddingBook() throws Exception {
        // given
        Book book = new Book();
        String expected = "Book stored successfully";
        when(bookService.addBook(book)).thenReturn(expected);

        String requestBody = objectMapper.writeValueAsString(book);
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string(expected));
        // then
        verify(bookService, times(1)).addBook(book);

    }

    @Test
    void should_GetBookById_WhenBookExists() throws Exception {
        // given
        Book book = new Book();
        book.setId(1);
        book.setTitle("Test Title");
        book.setAuthor("Test Author");

        when(bookService.getBook(1)).thenReturn(book);

        String requestBody = objectMapper.writeValueAsString(book);
        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/books/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(requestBody));

        // then
        verify(bookService, times(1)).getBook(1);
    }

    @Test
    void should_ReturnNotFound_WhenDoesNotExist() throws Exception {
        // given

        when(bookService.getBook(1)).thenReturn(null);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/books/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

        // then
        verify(bookService, times(1)).getBook(1);
    }

    @Test
    void should_ReturnBooksWhenParametersCorrect() throws Exception {
        // given
        Book firstBook = new Book(1, "firstBook", "firstAuthor", 20.0, 10L, "thriller");
        Book secondBook = new Book(1, "secondBook", "secondAuthor", 20.0, 10L, "action");
        List<Book> books = Arrays.asList(firstBook, secondBook);
        when(bookService.getBooks("firstAuthor", "secondGenre")).thenReturn(books);

        String responseBody = objectMapper.writeValueAsString(books);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/books")
                        .param("author", "firstAuthor")
                        .param("genre", "secondGenre")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));

        // then
        verify(bookService, times(1)).getBooks("firstAuthor", "secondGenre");
    }

    @Test
    void should_ReturnEmptyList_WhenNoParameters() throws Exception {
        // given
        List<Book> books = new ArrayList<>();
        when(bookService.getBooks(null, null)).thenReturn(books);

        String responseBody = objectMapper.writeValueAsString(books);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));

        // then
        verify(bookService, times(1)).getBooks(null, null);
    }

    @Test
    void should_updateBook_WhenIdMatches() throws Exception {
        // given
        String expected = "Book updated successfully";
        Book book = new Book();
        book.setId(1);
        when(bookService.updateBook(1, book)).thenReturn(expected);

        String requestBody = objectMapper.writeValueAsString(book);
        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}", 1)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(expected));

        // then

        verify(bookService, times(1)).updateBook(1, book);
    }

    @Test
    void should_throwException_WhenUpdatingBookAndIdDoesNotMatch() throws Exception {
        // given
        Book book = new Book();
        book.setId(2);
        String requestBody = objectMapper.writeValueAsString(book);
        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}", 1)
                .content(requestBody).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // then
        verify(bookService, never()).updateBook(1, book);
    }

    @Test
    void should_ReturnCorrectMessage_When_DeletingBook() throws Exception {
        // given
        String expected = "Book deleted successfully";
        when(bookService.deleteBook(1)).thenReturn(expected);


        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        verify(bookService, times(1)).deleteBook(1);
    }
}