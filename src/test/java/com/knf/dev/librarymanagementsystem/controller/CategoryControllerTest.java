package com.knf.dev.librarymanagementsystem.controller;

import com.knf.dev.librarymanagementsystem.entity.Category;
import com.knf.dev.librarymanagementsystem.service.CategoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    private MockMvc mockMvc;
    private CategoryService categoryService;
    private CategoryController categoryController;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        categoryService = mock(CategoryService.class);
        categoryController = new CategoryController(categoryService);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setViewResolvers(viewResolver)
                .build();

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
    }

    @Test
    void getAllCategories_ShouldReturnCategoryListView() throws Exception {
        List<Category> categories = Arrays.asList(testCategory);
        when(categoryService.findAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-categories"))
                .andExpect(model().attribute("categories", categories));
    }

    @Test
    void getCategoryById_ShouldReturnSingleCategoryView() throws Exception {
        when(categoryService.findCategoryById(1L)).thenReturn(testCategory);

        mockMvc.perform(get("/category/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-category"))
                .andExpect(model().attribute("category", testCategory));
    }

    @Test
    void showAddCategoryForm_ShouldRenderAddFormView() throws Exception {
        mockMvc.perform(get("/addCategory"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-category"));
    }

    @Test
    void createCategory_WithValidInput_ShouldRedirectToList() throws Exception {
        mockMvc.perform(post("/add-category")
                        .param("name", "Test Category"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService).createCategory(any(Category.class));
    }

    @Test
    void createCategory_WithValidationError_ShouldReturnForm() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        Model model = mock(Model.class);
        String view = categoryController.createCategory(new Category(), result, model);

        assert(view.equals("add-category"));
    }

    @Test
    void showUpdateForm_ShouldReturnUpdateView() throws Exception {
        when(categoryService.findCategoryById(1L)).thenReturn(testCategory);

        mockMvc.perform(get("/updateCategory/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("update-category"))
                .andExpect(model().attribute("category", testCategory));
    }

    @Test
    void updateCategory_WithValidInput_ShouldRedirectToList() throws Exception {
        mockMvc.perform(post("/update-category/1")
                        .param("name", "Updated Category"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService).updateCategory(any(Category.class));
    }

    @Test
    void updateCategory_WithValidationError_ShouldReturnForm() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        Model model = mock(Model.class);
        Category category = new Category();
        category.setId(1L);
        String view = categoryController.updateCategory(1L, category, result, model);

        assert(view.equals("update-category"));
    }

    @Test
    void deleteCategory_ShouldRedirectToList() throws Exception {
        mockMvc.perform(get("/remove-category/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService).deleteCategory(1L);
    }
}
