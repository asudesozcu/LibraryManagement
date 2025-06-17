package com.knf.dev.librarymanagementsystem.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.knf.dev.librarymanagementsystem.entity.Author;
import com.knf.dev.librarymanagementsystem.entity.Book;
import com.knf.dev.librarymanagementsystem.entity.Category;
import com.knf.dev.librarymanagementsystem.entity.Publisher;
import com.knf.dev.librarymanagementsystem.vo.AuthorRecord;
import com.knf.dev.librarymanagementsystem.vo.BookRecord;
import com.knf.dev.librarymanagementsystem.vo.CategoryRecord;
import com.knf.dev.librarymanagementsystem.vo.PublisherRecord;

class MapperTest {

    @Test
    void bookModelToVo_ShouldMapCorrectly() {
        // Arrange
        Book book1 = new Book("1234567890", "Test Book 1", "Series 1", "Description 1");
        book1.setId(1L);
        Book book2 = new Book("0987654321", "Test Book 2", "Series 2", "Description 2");
        book2.setId(2L);
        List<Book> books = Arrays.asList(book1, book2);

        // Act
        List<BookRecord> result = Mapper.bookModelToVo(books);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        
        BookRecord firstBook = result.get(0);
        assertEquals(book1.getId(), firstBook.id());
        assertEquals(book1.getIsbn(), firstBook.isbn());
        assertEquals(book1.getName(), firstBook.name());
        assertEquals(book1.getSerialName(), firstBook.serialName());
        assertEquals(book1.getDescription(), firstBook.description());

        BookRecord secondBook = result.get(1);
        assertEquals(book2.getId(), secondBook.id());
        assertEquals(book2.getIsbn(), secondBook.isbn());
        assertEquals(book2.getName(), secondBook.name());
        assertEquals(book2.getSerialName(), secondBook.serialName());
        assertEquals(book2.getDescription(), secondBook.description());
    }

    @Test
    void bookModelToVo_WithEmptyList_ShouldReturnEmptyList() {
        // Act
        List<BookRecord> result = Mapper.bookModelToVo(Collections.emptyList());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void authorModelToVo_ShouldMapCorrectly() {
        // Arrange
        Author author1 = new Author("Author 1", "Description 1");
        author1.setId(1L);
        Author author2 = new Author("Author 2", "Description 2");
        author2.setId(2L);
        List<Author> authors = Arrays.asList(author1, author2);

        // Act
        List<AuthorRecord> result = Mapper.authorModelToVo(authors);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        AuthorRecord firstAuthor = result.get(0);
        assertEquals(author1.getId(), firstAuthor.id());
        assertEquals(author1.getName(), firstAuthor.name());
        assertEquals(author1.getDescription(), firstAuthor.description());

        AuthorRecord secondAuthor = result.get(1);
        assertEquals(author2.getId(), secondAuthor.id());
        assertEquals(author2.getName(), secondAuthor.name());
        assertEquals(author2.getDescription(), secondAuthor.description());
    }

    @Test
    void authorModelToVo_WithEmptyList_ShouldReturnEmptyList() {
        // Act
        List<AuthorRecord> result = Mapper.authorModelToVo(Collections.emptyList());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void categoryModelToVo_ShouldMapCorrectly() {
        // Arrange
        Category category1 = new Category("Category 1");
        category1.setId(1L);
        Category category2 = new Category("Category 2");
        category2.setId(2L);
        List<Category> categories = Arrays.asList(category1, category2);

        // Act
        List<CategoryRecord> result = Mapper.categoryModelToVo(categories);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        CategoryRecord firstCategory = result.get(0);
        assertEquals(category1.getId(), firstCategory.id());
        assertEquals(category1.getName(), firstCategory.name());

        CategoryRecord secondCategory = result.get(1);
        assertEquals(category2.getId(), secondCategory.id());
        assertEquals(category2.getName(), secondCategory.name());
    }

    @Test
    void categoryModelToVo_WithEmptyList_ShouldReturnEmptyList() {
        // Act
        List<CategoryRecord> result = Mapper.categoryModelToVo(Collections.emptyList());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void publisherModelToVo_ShouldMapCorrectly() {
        // Arrange
        Publisher publisher1 = new Publisher("Publisher 1");
        publisher1.setId(1L);
        Publisher publisher2 = new Publisher("Publisher 2");
        publisher2.setId(2L);
        List<Publisher> publishers = Arrays.asList(publisher1, publisher2);

        // Act
        List<PublisherRecord> result = Mapper.publisherModelToVo(publishers);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        PublisherRecord firstPublisher = result.get(0);
        assertEquals(publisher1.getId(), firstPublisher.id());
        assertEquals(publisher1.getName(), firstPublisher.name());

        PublisherRecord secondPublisher = result.get(1);
        assertEquals(publisher2.getId(), secondPublisher.id());
        assertEquals(publisher2.getName(), secondPublisher.name());
    }

    @Test
    void publisherModelToVo_WithEmptyList_ShouldReturnEmptyList() {
        // Act
        List<PublisherRecord> result = Mapper.publisherModelToVo(Collections.emptyList());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
} 