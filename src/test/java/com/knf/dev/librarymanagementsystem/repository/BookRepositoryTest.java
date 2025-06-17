package com.knf.dev.librarymanagementsystem.repository;

import com.knf.dev.librarymanagementsystem.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testSaveAndFindById_ShouldReturnBook() {
        // GIVEN
        Book book = new Book("111-222", "Test Book", "S1", "A book for unit testing.");
        bookRepository.save(book);

        // WHEN
        Book found = bookRepository.findById(book.getId()).orElse(null);

        // THEN
        assertNotNull(found);
        assertEquals("Test Book", found.getName());
        assertEquals("111-222", found.getIsbn());
    }

    @Test
    void testFindAll_ShouldReturnList() {
        // GIVEN
        Book book1 = new Book("abc-123", "Alpha Book", "A1", "First book.");
        Book book2 = new Book("def-456", "Beta Book", "B1", "Second book.");
        bookRepository.save(book1);
        bookRepository.save(book2);

        // WHEN
        List<Book> books = bookRepository.findAll();

        // THEN
        assertTrue(books.size() >= 2); // varsa diğer kitaplarla birlikte >= 2 olabilir
    }

    @Test
    void testSearch_ShouldReturnMatchingBooks() {
        // GIVEN
        Book book = new Book("xyz-999", "Searchable Book", "SER-009", "For search test.");
        bookRepository.save(book);

        // WHEN
        List<Book> results = bookRepository.search("Searchable");

        // THEN
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(b -> b.getName().contains("Searchable")));
    }

    @Test
    void testSearch_ShouldReturnEmptyList_WhenNoMatch() {
        // WHEN
        List<Book> results = bookRepository.search("nonexistent-keyword-xyz");

        // THEN
        assertTrue(results.isEmpty());
    }

    @Test
    void testDeleteById_ShouldRemoveBook() {
        // GIVEN
        Book book = new Book("del-101", "Delete Me", "DEL1", "To be deleted.");
        bookRepository.save(book);
        Long id = book.getId();

        // WHEN
        bookRepository.deleteById(id);

        // THEN
        assertFalse(bookRepository.findById(id).isPresent());
    }
}
