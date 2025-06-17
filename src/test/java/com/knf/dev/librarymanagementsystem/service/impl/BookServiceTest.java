package com.knf.dev.librarymanagementsystem.service.impl;

import com.knf.dev.librarymanagementsystem.entity.Book;
import com.knf.dev.librarymanagementsystem.exception.NotFoundException;
import com.knf.dev.librarymanagementsystem.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testBook = new Book("ISBN123", "Unit Test Book", "SerialX", "Book for testing");
        testBook.setId(1L);
    }

    @Test
    void findAllBooks_ShouldReturnList() {
        List<Book> mockList = List.of(testBook);
        when(bookRepository.findAll()).thenReturn(mockList);

        List<Book> result = bookService.findAllBooks();

        assertEquals(1, result.size());
        assertEquals("Unit Test Book", result.get(0).getName());
        verify(bookRepository).findAll();
    }

    @Test
    void searchBooks_WithKeyword_ShouldCallSearch() {
        when(bookRepository.search("Test")).thenReturn(List.of(testBook));

        List<Book> result = bookService.searchBooks("Test");

        assertEquals(1, result.size());
        verify(bookRepository).search("Test");
    }

    @Test
    void searchBooks_NullKeyword_ShouldCallFindAll() {
        when(bookRepository.findAll()).thenReturn(List.of(testBook));

        List<Book> result = bookService.searchBooks(null);

        assertEquals(1, result.size());
        verify(bookRepository).findAll();
    }

    @Test
    void findBookById_ShouldReturnBook_WhenExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        Book result = bookService.findBookById(1L);

        assertNotNull(result);
        assertEquals("Unit Test Book", result.getName());
        verify(bookRepository).findById(1L);
    }

    @Test
    void findBookById_ShouldThrowException_WhenNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.findBookById(1L));
        verify(bookRepository).findById(1L);
    }

    @Test
    void createBook_ShouldCallSave() {
        bookService.createBook(testBook);
        verify(bookRepository).save(testBook);
    }

    @Test
    void updateBook_ShouldCallSave() {
        bookService.updateBook(testBook);
        verify(bookRepository).save(testBook);
    }

    @Test
    void deleteBook_ShouldDelete_WhenExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        bookService.deleteBook(1L);

        verify(bookRepository).findById(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_ShouldThrowException_WhenNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.deleteBook(1L));
        verify(bookRepository).findById(1L);
        verify(bookRepository, never()).deleteById(any());
    }

    @Test
    void findPaginated_ShouldReturnCorrectPage() {
        List<Book> fullList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Book b = new Book("ISBN" + i, "Book " + i, "S" + i, "Test Book");
            b.setId((long) i);
            fullList.add(b);
        }

        when(bookRepository.findAll()).thenReturn(fullList);

        Pageable pageable = PageRequest.of(1, 3); // page 1, size 3 → 4th, 5th, 6th
        Page<Book> page = bookService.findPaginated(pageable);

        assertEquals(3, page.getContent().size());
        assertEquals("Book 4", page.getContent().get(0).getName());
        assertEquals(10, page.getTotalElements());
    }

    @Test
    void findPaginated_ShouldReturnEmpty_WhenOutOfBounds() {
        List<Book> fullList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            fullList.add(new Book("ISBN" + i, "Book " + i, "S" + i, "Test Book"));
        }

        when(bookRepository.findAll()).thenReturn(fullList);

        Pageable pageable = PageRequest.of(2, 5); // page 2, size 5 → startIndex 10 (out of bounds)
        Page<Book> page = bookService.findPaginated(pageable);

        assertTrue(page.getContent().isEmpty());
    }
}
