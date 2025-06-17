package com.knf.dev.librarymanagementsystem.controller;

import com.knf.dev.librarymanagementsystem.entity.Author;
import com.knf.dev.librarymanagementsystem.service.AuthorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthorControllerTest {

    private MockMvc mockMvc;
    private AuthorService authorService;
    private AuthorController authorController;

    private Author testAuthor;

    @BeforeEach
    void setUp() {
        authorService = mock(AuthorService.class);
        authorController = new AuthorController(authorService);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(authorController)
                .setViewResolvers(viewResolver)
                .build();

        testAuthor = new Author("Test Author", "Famous fiction writer.");
        testAuthor.setId(1L);
    }

    @Test
    void findAllAuthors_ShouldReturnPagedView() throws Exception {
        Page<Author> page = new PageImpl<>(Collections.singletonList(testAuthor));
        when(authorService.findPaginated(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/authors?page=1&size=5"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-authors"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("pageNumbers"));
    }

    @Test
    void findAuthorById_ShouldReturnSingleView() throws Exception {
        when(authorService.findAuthorById(1L)).thenReturn(testAuthor);

        mockMvc.perform(get("/author/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-author"))
                .andExpect(model().attribute("author", testAuthor));
    }

    @Test
    void showCreateForm_ShouldReturnAddView() throws Exception {
        mockMvc.perform(get("/addAuthor"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-author"));
    }

    @Test
    void createAuthor_ValidInput_ShouldRedirect() throws Exception {
        when(authorService.findAllAuthors()).thenReturn(Collections.singletonList(testAuthor));

        mockMvc.perform(post("/add-author")
                        .flashAttr("author", testAuthor))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors"));

        verify(authorService).createAuthor(any(Author.class));
    }

    @Test
    void createAuthor_WithValidationError_ShouldReturnAddView() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        Author author = new Author("", ""); // Boş name ve description

        Model model = mock(Model.class);
        String view = authorController.createAuthor(author, result, model);

        assertEquals("add-author", view);
    }

    @Test
    void showUpdateForm_ShouldReturnUpdateView() throws Exception {
        when(authorService.findAuthorById(1L)).thenReturn(testAuthor);

        mockMvc.perform(get("/updateAuthor/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("update-author"))
                .andExpect(model().attribute("author", testAuthor));
    }

    @Test
    void updateAuthor_ValidInput_ShouldRedirect() throws Exception {
        when(authorService.findAllAuthors()).thenReturn(Collections.singletonList(testAuthor));

        mockMvc.perform(post("/update-author/1")
                        .flashAttr("author", testAuthor))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors"));

        verify(authorService).updateAuthor(any(Author.class));
    }

    @Test
    void updateAuthor_WithValidationError_ShouldReturnUpdateView() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        Author author = new Author("", "");
        author.setId(1L);
        Model model = mock(Model.class);
        String view = authorController.updateAuthor(1L, author, result, model);

        assertEquals("update-author", view);
    }

    @Test
    void deleteAuthor_ShouldRedirect() throws Exception {
        when(authorService.findAllAuthors()).thenReturn(Collections.singletonList(testAuthor));

        mockMvc.perform(get("/remove-author/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors"));

        verify(authorService).deleteAuthor(1L);
    }
}
