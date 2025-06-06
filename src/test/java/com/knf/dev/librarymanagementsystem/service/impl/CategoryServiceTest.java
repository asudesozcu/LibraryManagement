package com.knf.dev.librarymanagementsystem.service.impl;
import com.knf.dev.librarymanagementsystem.entity.Category;
import com.knf.dev.librarymanagementsystem.exception.NotFoundException;
import com.knf.dev.librarymanagementsystem.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category sampleCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleCategory = new Category();
        sampleCategory.setId(1L);
        sampleCategory.setName("Science");
    }

    @Test
    void findAllCategories_ShouldReturnList() {
        when(categoryRepository.findAll()).thenReturn(List.of(sampleCategory));

        List<Category> categories = categoryService.findAllCategories();

        assertEquals(1, categories.size());
        assertEquals("Science", categories.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void findCategoryById_ShouldReturnCategory_WhenExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));

        Category result = categoryService.findCategoryById(1L);

        assertNotNull(result);
        assertEquals("Science", result.getName());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void findCategoryById_ShouldThrowNotFoundException_WhenNotExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.findCategoryById(1L));
        verify(categoryRepository).findById(1L);
    }

    @Test
    void createCategory_ShouldCallSave() {
        categoryService.createCategory(sampleCategory);

        verify(categoryRepository).save(sampleCategory);
    }

    @Test
    void updateCategory_ShouldCallSave() {
        categoryService.updateCategory(sampleCategory);

        verify(categoryRepository).save(sampleCategory);
    }

    @Test
    void deleteCategory_ShouldDelete_WhenExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));

        categoryService.deleteCategory(1L);

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void deleteCategory_ShouldThrowNotFoundException_WhenNotExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.deleteCategory(1L));
        verify(categoryRepository).findById(1L);
        verify(categoryRepository, never()).deleteById(any());
    }
}