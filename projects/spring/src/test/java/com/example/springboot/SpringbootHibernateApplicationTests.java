package com.example.springboot;


import com.example.springboot.Model.Author;
import com.example.springboot.Model.Book;
import com.example.springboot.Model.Genre;
import com.example.springboot.Service.book.BookService;
import com.example.springboot.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.AfterTestExecution;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class SpringbootHibernateApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Test
    void getAllBooksTest() throws Exception {
        this.mockMvc.perform(get("/books/all"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllAuthorsTest() throws Exception {
        this.mockMvc.perform(get("/authors/all"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllGenresTest() throws Exception {
        this.mockMvc.perform(get("/genres/all"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void addBookTest() throws Exception {
        Book book = new Book();
        book.setName("Adventures");
        Genre genre = new Genre();
        genre.setName("Fantasy");
        Author author1 = new Author();
        author1.setName("Tolstoy");
        Author author2 = new Author();
        author2.setName("Esenin");
        Author author3 = new Author();
        author3.setName("Dostoevskiy");
        book.setGenre(genre);
        book.getAuthors().add(author1);
        book.getAuthors().add(author2);
        book.getAuthors().add(author3);
        mockMvc.perform(post("/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(book)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void addInvalidBookWithoutNameTest() throws Exception {
        Book book = new Book();
        book.setName("Adventures");
        Author author1 = new Author();
        author1.setName("Tolstoy");
        Author author2 = new Author();
        author2.setName("Esenin");
        Author author3 = new Author();
        author3.setName("Dostoevskiy");
        book.getAuthors().add(author1);
        book.getAuthors().add(author2);
        book.getAuthors().add(author3);
        mockMvc.perform(post("/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(book)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addInvalidBookWithoutGenreTest() throws Exception {
        Book book = new Book();
        book.setName("Adventures");
        Author author1 = new Author();
        author1.setName("Tolstoy");
        Author author2 = new Author();
        author2.setName("Esenin");
        Author author3 = new Author();
        author3.setName("Dostoevskiy");
        book.getAuthors().add(author1);
        book.getAuthors().add(author2);
        book.getAuthors().add(author3);
        mockMvc.perform(post("/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(book)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addInvalidBookWithoutAuthorsTest() throws Exception {
        Book book = new Book();
        Genre genre = new Genre();
        genre.setName("Fantasy");
        book.setGenre(genre);
        mockMvc.perform(post("/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(book)))
                .andExpect(status().is4xxClientError());
    }


    @Test
    void deleteBookTest() throws Exception {
        Book book = new Book();
        book.setName("Adventures");
        Genre genre = new Genre();
        genre.setName("Fantasy");
        Author author1 = new Author();
        author1.setName("Tolstoy");
        Author author2 = new Author();
        author2.setName("Esenin");
        Author author3 = new Author();
        author3.setName("Dostoevskiy");
        book.setGenre(genre);
        book.getAuthors().add(author1);
        book.getAuthors().add(author2);
        book.getAuthors().add(author3);
        book = bookService.addBook(book);
        mockMvc.perform(delete("/delete/{id}", book.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(book)));
    }

    @Test
    void getBook() throws Exception {
        Book book = new Book();
        book.setName("Adventures");
        Genre genre = new Genre();
        genre.setName("Fantasy");
        Author author1 = new Author();
        author1.setName("Tolstoy");
        Author author2 = new Author();
        author2.setName("Esenin");
        Author author3 = new Author();
        author3.setName("Dostoevskiy");
        book.setGenre(genre);
        book.getAuthors().add(author1);
        book.getAuthors().add(author2);
        book.getAuthors().add(author3);
        bookService.addBook(book);
        this.mockMvc.perform(get("/books/edit/{id}", book.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(book.getName()))
                .andExpect(jsonPath("$.genre.name").value(book.getGenre().getName()))
                .andExpect(jsonPath("$.authors.size()").value(book.getAuthors().size()));
    }

    @After
    public void deleteDb() throws CustomException, InterruptedException {
        bookService.deleteAllBooks();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
