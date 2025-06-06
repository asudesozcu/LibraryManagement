package com.knf.dev.librarymanagementsystem.service.impl;

import com.knf.dev.librarymanagementsystem.entity.Publisher;
import com.knf.dev.librarymanagementsystem.exception.NotFoundException;
import com.knf.dev.librarymanagementsystem.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



import com.knf.dev.librarymanagementsystem.entity.Publisher;
import com.knf.dev.librarymanagementsystem.exception.NotFoundException;
import com.knf.dev.librarymanagementsystem.repository.PublisherRepository;

import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherServiceImpl publisherService;

    private Publisher samplePublisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        samplePublisher = new Publisher();
        samplePublisher.setId(1L);
        samplePublisher.setName("Sample Publisher");
    }

    @Test
    void findAllPublishers_ShouldReturnPublisherList() {
        List<Publisher> mockList = List.of(samplePublisher);
        when(publisherRepository.findAll()).thenReturn(mockList);

        List<Publisher> result = publisherService.findAllPublishers();

        assertEquals(1, result.size());
        assertEquals("Sample Publisher", result.get(0).getName());
        verify(publisherRepository, times(1)).findAll();
    }

    @Test
    void findPublisherById_ShouldReturnPublisher_WhenExists() {
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(samplePublisher));

        Publisher result = publisherService.findPublisherById(1L);

        assertNotNull(result);
        assertEquals("Sample Publisher", result.getName());
        verify(publisherRepository, times(1)).findById(1L);
    }

    @Test
    void findPublisherById_ShouldThrowNotFoundException_WhenNotExists() {
        when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> publisherService.findPublisherById(1L));
        verify(publisherRepository, times(1)).findById(1L);
    }

    @Test
    void createPublisher_ShouldCallSave() {
        publisherService.createPublisher(samplePublisher);
        verify(publisherRepository, times(1)).save(samplePublisher);
    }

    @Test
    void updatePublisher_ShouldCallSave() {
        publisherService.updatePublisher(samplePublisher);
        verify(publisherRepository, times(1)).save(samplePublisher);
    }

    @Test
    void deletePublisher_ShouldCallDelete_WhenExists() {
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(samplePublisher));

        publisherService.deletePublisher(1L);

        verify(publisherRepository, times(1)).findById(1L);
        verify(publisherRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePublisher_ShouldThrowNotFoundException_WhenNotExists() {
        when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> publisherService.deletePublisher(1L));
        verify(publisherRepository, times(1)).findById(1L);
        verify(publisherRepository, never()).deleteById(any());
    }
}

