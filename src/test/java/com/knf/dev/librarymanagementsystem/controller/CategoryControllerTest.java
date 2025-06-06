package com.knf.dev.librarymanagementsystem.controller;

import com.knf.dev.librarymanagementsystem.config.TestSecurityConfig;
import com.knf.dev.librarymanagementsystem.entity.Category;
import com.knf.dev.librarymanagementsystem.service.CategoryService;
import com.knf.dev.librarymanagementsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

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
//Projede eğer var olan kitaba bağlı bir publisher vs silersen weeoe atıyor. daha düzgün yonetilmiyor. ve sanırım testlerde bu kontrol edılmıyor
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

    @Test
    void findAllCategories_ShouldReturnCategoriesList() throws Exception {
        when(categoryService.findAllCategories()).thenReturn(categoryList);

        mockMvc.perform(get("/categories")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("list-categories"))
                .andExpect(model().attribute("categories", categoryList));

        verify(categoryService, times(1)).findAllCategories();
    }

    @Test
    void findCategoryById_ShouldReturnCategory() throws Exception {
        when(categoryService.findCategoryById(1L)).thenReturn(testCategory);

        mockMvc.perform(get("/category/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("list-category"))
                .andExpect(model().attribute("category", testCategory));

        verify(categoryService, times(1)).findCategoryById(1L);
    }

    @Test
    void createCategory_ShouldRedirectToCategories() throws Exception {
        doNothing().when(categoryService).createCategory(any(Category.class));

        mockMvc.perform(post("/add-category")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Test Category"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService, times(1)).createCategory(any(Category.class));
    }

    @Test
    void updateCategory_ShouldRedirectToCategories() throws Exception {
        doNothing().when(categoryService).updateCategory(any(Category.class));

        mockMvc.perform(post("/update-category/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Updated Category"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService, times(1)).updateCategory(any(Category.class));
    }

    @Test
    void deleteCategory_ShouldRedirectToCategories() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(get("/remove-category/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService, times(1)).deleteCategory(1L);
    }

    @Test
    void showCreateForm_ShouldReturnAddCategoryPage() throws Exception {
        mockMvc.perform(get("/addCategory")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("add-category"));
    }

    @Test
    void showUpdateForm_ShouldReturnUpdateCategoryPage() throws Exception {
        when(categoryService.findCategoryById(1L)).thenReturn(testCategory);

        mockMvc.perform(get("/updateCategory/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("update-category"))
                .andExpect(model().attribute("category", testCategory));

        verify(categoryService, times(1)).findCategoryById(1L);
    }
} 