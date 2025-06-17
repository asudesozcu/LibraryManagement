package com.knf.dev.librarymanagementsystem.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.knf.dev.librarymanagementsystem.service.FileService;

@ExtendWith(MockitoExtension.class)
class FileExportControllerTest {

    @Mock
    private FileService fileService;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private FileExportController fileExportController;

    @BeforeEach
    void setUp() {
        // Any setup if needed
    }

    @Test
    void exportCSV_ShouldCallFileService() throws Exception {
        // Arrange
        String fileName = "test-file.csv";
        doNothing().when(fileService).exportCSV(fileName, response);

        // Act
        fileExportController.exportCSV(fileName, response);

        // Assert
        verify(fileService).exportCSV(fileName, response);
    }
}