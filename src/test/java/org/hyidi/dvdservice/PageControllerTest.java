package org.hyidi.dvdservice;

import org.hyidi.dvdservice.controller.PageController;
import org.hyidi.dvdservice.model.Dvd;
import org.hyidi.dvdservice.service.DvdInstanceService;
import org.hyidi.dvdservice.service.DvdService;
import org.hyidi.dvdservice.service.RentalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PageController.class)
class PageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DvdService dvdService;

    @MockitoBean
    private DvdInstanceService dvdInstanceService;

    @MockitoBean
    private RentalService rentalService;

    @Test
    @DisplayName("GET / - home page loads with model attributes")
    void testHome() throws Exception {
        when(dvdService.getAllDvds()).thenReturn(Collections.emptyList());
        when(dvdInstanceService.getAvailableInstances()).thenReturn(Collections.emptyList());
        when(rentalService.getAllRentals()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("dvds"))
                .andExpect(model().attributeExists("instances"))
                .andExpect(model().attributeExists("rentals"));
    }

    @Test
    @DisplayName("POST /dvd/add - add new DVD")
    void testAddDvd() throws Exception {
        mockMvc.perform(post("/dvd/add")
                        .param("title", "The Matrix")
                        .param("description", "Sci-fi classic")
                        .param("dailyPrice", "2.99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(dvdService, times(1)).addDvd(any(Dvd.class));
    }

    @Test
    @DisplayName("POST /instance/add - add new DVD instance")
    void testAddInstance() throws Exception {
        Dvd dvd = new Dvd();
        dvd.setId(1L);
        when(dvdService.getAllDvds()).thenReturn(Collections.singletonList(dvd));

        mockMvc.perform(post("/instance/add")
                        .param("dvdId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(dvdInstanceService, times(1)).addDvdInstance(dvd);
    }

    @Test
    @DisplayName("POST /rent - rent a DVD")
    void testRentDvd() throws Exception {
        mockMvc.perform(post("/rent")
                        .param("instanceId", "1")
                        .param("renterName", "John Doe")
                        .param("rentalDays", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(rentalService, times(1)).rentDvd(1L, "John Doe", 3);
    }

    @Test
    @DisplayName("POST /return - return a DVD")
    void testReturnDvd() throws Exception {
        mockMvc.perform(post("/return")
                        .param("rentalId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(rentalService, times(1)).returnDvd(1L);
    }
}
