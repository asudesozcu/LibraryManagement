package com.knf.dev.librarymanagementsystem.service.impl;

import com.knf.dev.librarymanagementsystem.entity.Author;
import com.knf.dev.librarymanagementsystem.exception.NotFoundException;
import com.knf.dev.librarymanagementsystem.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author sampleAuthor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleAuthor = new Author("Test Author", "A famous writer");
        sampleAuthor.setId(1L);
    }

    @Test
    void findAllAuthors_ShouldReturnAuthorList() {
        List<Author> mockList = List.of(sampleAuthor);
        when(authorRepository.findAll()).thenReturn(mockList);

        List<Author> result = authorService.findAllAuthors();

        assertEquals(1, result.size());
        assertEquals("Test Author", result.get(0).getName());
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    void findAuthorById_ShouldReturnAuthor_WhenExists() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));

        Author result = authorService.findAuthorById(1L);

        assertNotNull(result);
        assertEquals("Test Author", result.getName());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void findAuthorById_ShouldThrowNotFoundException_WhenNotExists() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.findAuthorById(1L));
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void createAuthor_ShouldCallSave() {
        authorService.createAuthor(sampleAuthor);
        verify(authorRepository, times(1)).save(sampleAuthor);
    }

    @Test
    void updateAuthor_ShouldCallSave() {
        authorService.updateAuthor(sampleAuthor);
        verify(authorRepository, times(1)).save(sampleAuthor);
    }

    @Test
    void deleteAuthor_ShouldCallDelete_WhenExists() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));

        authorService.deleteAuthor(1L);

        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAuthor_ShouldThrowNotFoundException_WhenNotExists() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.deleteAuthor(1L));
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, never()).deleteById(any());
    }

    @Test
    void findPaginated_ShouldReturnCorrectPage() {
        List<Author> fullList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Author a = new Author("Author " + i, "Desc " + i);
            a.setId((long) i);
            fullList.add(a);
        }

        when(authorRepository.findAll()).thenReturn(fullList);

        Pageable pageable = PageRequest.of(1, 3); // page 1, size 3 → items 4-6
        Page<Author> page = authorService.findPaginated(pageable);

        assertEquals(3, page.getContent().size());
        assertEquals("Author 4", page.getContent().get(0).getName());
        assertEquals("Author 6", page.getContent().get(2).getName());
        assertEquals(10, page.getTotalElements());
    }

    @Test
    void findPaginated_ShouldReturnEmpty_WhenOutOfBounds() {
        List<Author> fullList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            fullList.add(new Author("Author " + i, "Desc " + i));
        }

        when(authorRepository.findAll()).thenReturn(fullList);

        Pageable pageable = PageRequest.of(3, 5); // page 3, size 5 → startIndex = 15 (out of bounds)
        Page<Author> page = authorService.findPaginated(pageable);

        assertTrue(page.getContent().isEmpty());
    }
}
