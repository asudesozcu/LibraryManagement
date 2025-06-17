package com.knf.dev.librarymanagementsystem.service.impl;

import com.knf.dev.librarymanagementsystem.constant.Item;
import com.knf.dev.librarymanagementsystem.entity.Author;
import com.knf.dev.librarymanagementsystem.entity.Book;
import com.knf.dev.librarymanagementsystem.entity.Category;
import com.knf.dev.librarymanagementsystem.entity.Publisher;
import com.knf.dev.librarymanagementsystem.service.AuthorService;
import com.knf.dev.librarymanagementsystem.service.BookService;
import com.knf.dev.librarymanagementsystem.service.CategoryService;
import com.knf.dev.librarymanagementsystem.service.PublisherService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class FileServiceTest {

    @Mock
    private BookService bookService;

    @Mock
    private AuthorService authorService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private FileServiceImpl fileService;

    @Mock
    private HttpServletResponse response;

    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
    }

    @Test
    void testExportCSV_Book() throws Exception {
        Book book = new Book("123", "TestBook", "Series", "Desc");
        book.setId(1L);
        when(bookService.findAllBooks()).thenReturn(List.of(book));

        fileService.exportCSV("all-book", response);

        String output = stringWriter.toString();
        assertTrue(output.contains("123"));
        assertTrue(output.contains("TestBook"));
    }

    @Test
    void testExportCSV_Author() throws Exception {
        Author author = new Author();
        author.setId(1L);
        author.setName("Ali Veli");
        author.setDescription("Famous Turkish Author");

        when(authorService.findAllAuthors()).thenReturn(List.of(author));

        fileService.exportCSV("all-author", response);

        String output = stringWriter.toString();
        assertTrue(output.contains("Ali Veli"));
        assertTrue(output.contains("Famous Turkish Author"));
    }


    @Test
    void testExportCSV_Category() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fiction");

        when(categoryService.findAllCategories()).thenReturn(List.of(category));

        fileService.exportCSV("all-category", response);

        String output = stringWriter.toString();
        assertTrue(output.contains("Fiction"));
    }

    @Test
    void testExportCSV_Publisher() throws Exception {
        Publisher publisher = new Publisher();
        publisher.setId(1L);
        publisher.setName("Penguin");

        when(publisherService.findAllPublishers()).thenReturn(List.of(publisher));

        fileService.exportCSV("all-publisher", response);

        String output = stringWriter.toString();
        assertTrue(output.contains("Penguin"));
    }
}
