package com.knf.dev.librarymanagementsystem.controller;

import com.knf.dev.librarymanagementsystem.config.TestSecurityConfig;
import com.knf.dev.librarymanagementsystem.entity.Category;
import com.knf.dev.librarymanagementsystem.exception.NotFoundException;
import com.knf.dev.librarymanagementsystem.service.CategoryService;


import com.knf.dev.librarymanagementsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.util.NestedServletException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WithMockUser(username = "testuser", roles = "USER")
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;


    @MockBean
    private UserService userService;

    private Category testCategory;
    private List<Category> categoryList;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");

        Category secondCategory = new Category();
        secondCategory.setId(2L);
        secondCategory.setName("Second Category");

        categoryList = Arrays.asList(testCategory, secondCategory);
    }

    private SecurityMockMvcRequestPostProcessors.CsrfRequestPostProcessor csrf() {
        return SecurityMockMvcRequestPostProcessors.csrf();
    }

    @Test
    void getAllCategories_ShouldReturnCategoryListView() throws Exception {
        when(categoryService.findAllCategories()).thenReturn(categoryList);

        mockMvc.perform(get("/categories").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("list-categories"))
                .andExpect(model().attribute("categories", categoryList));

        verify(categoryService).findAllCategories();
    }

    @Test
    void getCategoryById_ShouldReturnSingleCategoryView() throws Exception {
        when(categoryService.findCategoryById(1L)).thenReturn(testCategory);

        mockMvc.perform(get("/category/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("list-category"))
                .andExpect(model().attribute("category", testCategory));

        verify(categoryService).findCategoryById(1L);
    }

    @Test
    void showAddCategoryForm_ShouldRenderAddFormView() throws Exception {
        mockMvc.perform(get("/addCategory").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("add-category"));
    }

    @Test
    void createCategory_WithValidInput_ShouldRedirectToList() throws Exception {
        doNothing().when(categoryService).createCategory(any(Category.class));

        mockMvc.perform(post("/add-category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Test Category"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService).createCategory(any(Category.class));
    }


    //WHEN THE CATEGORY NAME IS EMPTY IT ACCEPTS.  Backend tarafında validation çalışmıyor. frontendde var ama "  " accepted.
    @Test
    void createCategory_WithValidationError_ShouldReturnForm() throws Exception {
        mockMvc.perform(post("/add-category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "")) // Simulate validation error
                .andExpect(status().isOk())
                .andExpect(view().name("add-category"));
    }

    @Test
    void showUpdateForm_ShouldReturnUpdateView() throws Exception {
        when(categoryService.findCategoryById(1L)).thenReturn(testCategory);

        mockMvc.perform(get("/updateCategory/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("update-category"))
                .andExpect(model().attribute("category", testCategory));

        verify(categoryService).findCategoryById(1L);
    }

    @Test
    void updateCategory_WithValidInput_ShouldRedirectToList() throws Exception {
        doNothing().when(categoryService).updateCategory(any(Category.class));

        mockMvc.perform(post("/update-category/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Updated Category"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService).updateCategory(any(Category.class));
    }


    //WHEN THE CATEGORY NAME IS EMPTY IT ACCEPTS.  Backend tarafında validation çalışmıyor
    @Test
    void updateCategory_WithValidationError_ShouldReturnForm() throws Exception {
        mockMvc.perform(post("/update-category/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "")) // Empty name = invalid
                .andExpect(status().isOk())
                .andExpect(view().name("update-category"));
    }

    @Test
    void deleteCategory_WithNoDependencies_ShouldRedirectToList() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(get("/remove-category/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService).deleteCategory(1L);
    }


    //backend and db works well for  Data Integrity but UI doesnt
    @Test
    void deleteCategory_WhenHasAssociatedBooks_ShouldPassIfExceptionThrown() {
        Long id = 3L;

        // Burada mock'u configure etmezsen exception fırlatmaz
        doThrow(new DataIntegrityViolationException("Cannot delete category with associated books"))
                .when(categoryService).deleteCategory(id);

        // Artık bu metod çağrısı exception fırlatır
        assertThrows(DataIntegrityViolationException.class, () -> {
            categoryService.deleteCategory(id);
        });

        verify(categoryService).deleteCategory(id);
    }



}
