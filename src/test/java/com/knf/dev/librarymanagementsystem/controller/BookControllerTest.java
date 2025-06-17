package com.knf.dev.librarymanagementsystem.controller;

import com.knf.dev.librarymanagementsystem.entity.Book;
import com.knf.dev.librarymanagementsystem.service.AuthorService;
import com.knf.dev.librarymanagementsystem.service.BookService;
import com.knf.dev.librarymanagementsystem.service.CategoryService;
import com.knf.dev.librarymanagementsystem.service.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest {

    private MockMvc mockMvc;
    private BookService bookService;
    private AuthorService authorService;
    private CategoryService categoryService;
    private PublisherService publisherService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        bookService = mock(BookService.class);
        authorService = mock(AuthorService.class);
        categoryService = mock(CategoryService.class);
        publisherService = mock(PublisherService.class);

        BookController bookController = new BookController(publisherService, categoryService, bookService, authorService);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setViewResolvers(viewResolver)
                .build();

        testBook = new Book("1234567890", "Test Book", "Series 1", "A book for testing");
        testBook.setId(1L);
    }

    @Test
    void findAllBooks_ShouldReturnPagedList() throws Exception {
        Page<Book> page = new PageImpl<>(Collections.singletonList(testBook));
        when(bookService.findPaginated(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/books?page=1&size=5"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-books"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attributeExists("pageNumbers"));
    }

    @Test
    void searchBook_ShouldReturnSearchResults() throws Exception {
        when(bookService.searchBooks("Test")).thenReturn(Collections.singletonList(testBook));

        mockMvc.perform(get("/searchBook").param("keyword", "Test"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-books"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("keyword", "Test"));
    }

    @Test
    void findBookById_ShouldReturnDetailView() throws Exception {
        when(bookService.findBookById(1L)).thenReturn(testBook);

        mockMvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-book"))
                .andExpect(model().attribute("book", testBook));
    }

    @Test
    void showCreateForm_ShouldReturnAddForm() throws Exception {
        when(authorService.findAllAuthors()).thenReturn(Collections.emptyList());
        when(categoryService.findAllCategories()).thenReturn(Collections.emptyList());
        when(publisherService.findAllPublishers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("publishers"));
    }

    @Test
    void createBook_Valid_ShouldRedirect() throws Exception {
        when(bookService.findAllBooks()).thenReturn(Collections.singletonList(testBook));

        mockMvc.perform(post("/add-book")
                        .flashAttr("book", testBook))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService).createBook(any(Book.class));
    }

    @Test
    void createBook_Invalid_ShouldReturnForm() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        BookController controller = new BookController(publisherService, categoryService, bookService, authorService);
        String view = controller.createBook(new Book(), result, mock(org.springframework.ui.Model.class));

        assert(view.equals("add-book"));
    }

    @Test
    void showUpdateForm_ShouldReturnUpdateView() throws Exception {
        when(bookService.findBookById(1L)).thenReturn(testBook);

        mockMvc.perform(get("/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("update-book"))
                .andExpect(model().attribute("book", testBook));
    }

    @Test
    void updateBook_Valid_ShouldRedirect() throws Exception {
        when(bookService.findAllBooks()).thenReturn(Collections.singletonList(testBook));

        mockMvc.perform(post("/update-book/1")
                        .flashAttr("book", testBook))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService).updateBook(any(Book.class));
    }

    @Test
    void updateBook_Invalid_ShouldReturnForm() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        Book book = new Book();
        book.setId(1L);
        BookController controller = new BookController(publisherService, categoryService, bookService, authorService);
        String view = controller.updateBook(1L, book, result, mock(org.springframework.ui.Model.class));

        assert(view.equals("update-book"));
    }

    @Test
    void deleteBook_ShouldRedirect() throws Exception {
        when(bookService.findAllBooks()).thenReturn(Collections.singletonList(testBook));

        mockMvc.perform(get("/remove-book/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService).deleteBook(1L);
    }
}
