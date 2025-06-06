package com.knf.dev.librarymanagementsystem.controller;

import com.knf.dev.librarymanagementsystem.entity.Publisher;
import com.knf.dev.librarymanagementsystem.service.PublisherService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PublisherControllerTest {

    private MockMvc mockMvc;
    private PublisherService publisherService;
    private PublisherController publisherController;

    private Publisher testPublisher;

    @BeforeEach
    void setUp() {
        publisherService = mock(PublisherService.class);
        publisherController = new PublisherController(publisherService);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(publisherController)
                .setViewResolvers(viewResolver)
                .build();

        testPublisher = new Publisher();
        testPublisher.setId(1L);
        testPublisher.setName("Test Publisher");
    }

    @Test
    void findAllPublishers_ShouldReturnListView() throws Exception {
        List<Publisher> list = Arrays.asList(testPublisher);
        when(publisherService.findAllPublishers()).thenReturn(list);

        mockMvc.perform(get("/publishers"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-publishers"))
                .andExpect(model().attribute("publishers", list));

        verify(publisherService).findAllPublishers();
    }

    @Test
    void findPublisherById_ShouldReturnSingleView() throws Exception {
        when(publisherService.findPublisherById(1L)).thenReturn(testPublisher);

        mockMvc.perform(get("/publisher/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-publisher"))
                .andExpect(model().attribute("publisher", testPublisher));
    }

    @Test
    void showCreateForm_ShouldReturnAddView() throws Exception {
        mockMvc.perform(get("/addPublisher"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-publisher"));
    }

    @Test
    void createPublisher_ValidInput_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/add-publisher")
                        .param("name", "Test Publisher"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/publishers"));

        verify(publisherService).createPublisher(any(Publisher.class));
    }

    @Test
    void createPublisher_WithValidationError_ShouldReturnAddView() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        Model model = mock(Model.class);
        String view = publisherController.createPublisher(new Publisher(), result, model);

        assert(view.equals("add-publisher"));
    }

    @Test
    void showUpdateForm_ShouldReturnUpdateView() throws Exception {
        when(publisherService.findPublisherById(1L)).thenReturn(testPublisher);

        mockMvc.perform(get("/updatePublisher/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("update-publisher"))
                .andExpect(model().attribute("publisher", testPublisher));
    }

    @Test
    void updatePublisher_ValidInput_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/update-publisher/1")
                        .param("name", "Updated Publisher"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/publishers"));

        verify(publisherService).updatePublisher(any(Publisher.class));
    }

    @Test
    void updatePublisher_WithValidationError_ShouldReturnUpdateView() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        Publisher publisher = new Publisher();
        publisher.setId(1L);
        Model model = mock(Model.class);
        String view = publisherController.updatePublisher(1L, publisher, result, model);

        assert(view.equals("update-publishers"));
    }

    @Test
    void deletePublisher_ShouldRedirect() throws Exception {
        mockMvc.perform(get("/remove-publisher/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/publishers"));

        verify(publisherService).deletePublisher(1L);
    }
}
