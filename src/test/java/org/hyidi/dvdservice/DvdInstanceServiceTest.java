package org.hyidi.dvdservice;

import org.hyidi.dvdservice.model.Dvd;
import org.hyidi.dvdservice.model.DvdInstance;
import org.hyidi.dvdservice.repository.DvdInstanceRepository;
import org.hyidi.dvdservice.service.DvdInstanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DvdInstanceServiceTest {

    private DvdInstanceRepository dvdInstanceRepository;
    private DvdInstanceService dvdInstanceService;

    @BeforeEach
    void setUp() {
        dvdInstanceRepository = mock(DvdInstanceRepository.class);
        dvdInstanceService = new DvdInstanceService(dvdInstanceRepository);
    }

    @Test
    @DisplayName("addDvdInstance should save and return new instance")
    void testAddDvdInstance() {
        Dvd dvd = new Dvd();
        DvdInstance instance = new DvdInstance();
        instance.setDvd(dvd);
        instance.setStatus(DvdInstance.Status.AVAILABLE);

        when(dvdInstanceRepository.save(any(DvdInstance.class))).thenReturn(instance);

        DvdInstance saved = dvdInstanceService.addDvdInstance(dvd);

        assertThat(saved).isNotNull();
        assertThat(saved.getDvd()).isEqualTo(dvd);
        assertThat(saved.getStatus()).isEqualTo(DvdInstance.Status.AVAILABLE);

        verify(dvdInstanceRepository, times(1)).save(any(DvdInstance.class));
    }

    @Test
    @DisplayName("getAvailableInstances should return list")
    void testGetAvailableInstances() {
        when(dvdInstanceRepository.findByStatus(DvdInstance.Status.AVAILABLE))
                .thenReturn(Collections.emptyList());

        var result = dvdInstanceService.getAvailableInstances();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(dvdInstanceRepository, times(1)).findByStatus(DvdInstance.Status.AVAILABLE);
    }

    @Test
    @DisplayName("getInstanceById should return instance if found")
    void testGetInstanceByIdFound() {
        DvdInstance instance = new DvdInstance();
        instance.setId(1L);

        when(dvdInstanceRepository.findById(1L)).thenReturn(Optional.of(instance));

        var result = dvdInstanceService.getInstanceById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(dvdInstanceRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getInstanceById should throw if not found")
    void testGetInstanceByIdNotFound() {
        when(dvdInstanceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> dvdInstanceService.getInstanceById(1L));

        verify(dvdInstanceRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("saveInstance should call repository save")
    void testSaveInstance() {
        DvdInstance instance = new DvdInstance();

        dvdInstanceService.saveInstance(instance);

        verify(dvdInstanceRepository, times(1)).save(instance);
    }
}
