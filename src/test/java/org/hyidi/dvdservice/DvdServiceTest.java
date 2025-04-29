package org.hyidi.dvdservice;

import org.hyidi.dvdservice.model.Dvd;
import org.hyidi.dvdservice.repository.DvdRepository;
import org.hyidi.dvdservice.service.DvdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DvdServiceTest {

    private DvdRepository dvdRepository;
    private DvdService dvdService;

    @BeforeEach
    void setUp() {
        dvdRepository = mock(DvdRepository.class);
        dvdService = new DvdService(dvdRepository);
    }

    @Test
    @DisplayName("addDvd should save and return dvd")
    void testAddDvd() {
        Dvd dvd = new Dvd();
        dvd.setTitle("Test DVD");

        when(dvdRepository.save(dvd)).thenReturn(dvd);

        Dvd saved = dvdService.addDvd(dvd);

        assertThat(saved).isEqualTo(dvd);
        verify(dvdRepository, times(1)).save(dvd);
    }

    @Test
    @DisplayName("getAllDvds should return list of dvds")
    void testGetAllDvds() {
        Dvd dvd = new Dvd();
        dvd.setTitle("Another DVD");

        when(dvdRepository.findAll()).thenReturn(List.of(dvd));

        List<Dvd> dvds = dvdService.getAllDvds();

        assertThat(dvds).hasSize(1);
        assertThat(dvds.getFirst().getTitle()).isEqualTo("Another DVD");
        verify(dvdRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllDvds should return empty list when none exist")
    void testGetAllDvdsEmpty() {
        when(dvdRepository.findAll()).thenReturn(Collections.emptyList());

        List<Dvd> dvds = dvdService.getAllDvds();

        assertThat(dvds).isEmpty();
        verify(dvdRepository, times(1)).findAll();
    }
}
