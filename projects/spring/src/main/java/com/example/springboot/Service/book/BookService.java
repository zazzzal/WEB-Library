package com.example.springboot.Service.book;

import com.example.springboot.Model.Book;
import com.example.springboot.exception.CustomException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BookService {
    List<Book> findAllBooks();

    Book addBook(Book book) throws CustomException;

    Book getBookById(Long id) throws CustomException;

    Book editBook(Book book) throws CustomException;

    void deleteById(Long id) throws CustomException;

    void getWorkBook(HttpServletResponse response);

    //ToDo: FOR TESTS
    void deleteAllBooks() throws CustomException;
}
