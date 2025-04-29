package org.hyidi.dvdservice;

import org.hyidi.dvdservice.model.Dvd;
import org.hyidi.dvdservice.model.DvdInstance;
import org.hyidi.dvdservice.model.Rental;
import org.hyidi.dvdservice.repository.RentalRepository;
import org.hyidi.dvdservice.service.DvdInstanceService;
import org.hyidi.dvdservice.service.RentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RentalServiceTest {

    private RentalRepository rentalRepository;
    private DvdInstanceService dvdInstanceService;
    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        rentalRepository = mock(RentalRepository.class);
        dvdInstanceService = mock(DvdInstanceService.class);
        rentalService = new RentalService(rentalRepository, dvdInstanceService);
    }

    @Test
    @DisplayName("rentDvd should rent available instance")
    void testRentDvdSuccess() {
        Long instanceId = 1L;
        String renterName = "John Doe";
        int rentalDays = 5;

        Dvd dvd = new Dvd();
        dvd.setId(instanceId);
        dvd.setDailyPrice(2.0);

        DvdInstance instance = new DvdInstance();
        instance.setId(instanceId);
        instance.setDvd(dvd);
        instance.setStatus(DvdInstance.Status.AVAILABLE);

        Rental savedRental = new Rental();
        savedRental.setDvdInstance(instance);

        when(dvdInstanceService.getInstanceById(instanceId)).thenReturn(instance);
        when(rentalRepository.save(any(Rental.class))).thenReturn(savedRental);

        Rental rental = rentalService.rentDvd(instanceId, renterName, rentalDays);

        assertThat(rental).isNotNull();
        assertThat(rental.getDvdInstance()).isEqualTo(instance);

        verify(dvdInstanceService, times(1)).saveInstance(instance);
        verify(rentalRepository, times(1)).save(any(Rental.class));
    }

    @Test
    @DisplayName("rentDvd should throw if instance is not available")
    void testRentDvdInstanceNotAvailable() {
        Long instanceId = 1L;

        DvdInstance instance = new DvdInstance();
        instance.setId(instanceId);
        instance.setStatus(DvdInstance.Status.RENTED);

        when(dvdInstanceService.getInstanceById(instanceId)).thenReturn(instance);

        assertThrows(RuntimeException.class, () -> rentalService.rentDvd(instanceId, "Someone", 3));

        verify(dvdInstanceService, never()).saveInstance(any());
        verify(rentalRepository, never()).save(any());
    }

    @Test
    @DisplayName("returnDvd should return and delete rental")
    void testReturnDvdSuccess() {
        Long rentalId = 1L;

        DvdInstance instance = new DvdInstance();
        instance.setStatus(DvdInstance.Status.RENTED);

        Rental rental = new Rental();
        rental.setId(rentalId);
        rental.setDvdInstance(instance);

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        Rental returned = rentalService.returnDvd(rentalId);

        assertThat(returned).isEqualTo(rental);
        assertThat(instance.getStatus()).isEqualTo(DvdInstance.Status.AVAILABLE);

        verify(dvdInstanceService, times(1)).saveInstance(instance);
        verify(rentalRepository, times(1)).delete(rental);
    }

    @Test
    @DisplayName("returnDvd should throw if rental not found")
    void testReturnDvdRentalNotFound() {
        Long rentalId = 1L;

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rentalService.returnDvd(rentalId));

        verify(rentalRepository, never()).delete(any());
        verify(dvdInstanceService, never()).saveInstance(any());
    }

    @Test
    @DisplayName("getAllRentals should return list")
    void testGetAllRentals() {
        when(rentalRepository.findAll()).thenReturn(Collections.emptyList());

        var result = rentalService.getAllRentals();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(rentalRepository, times(1)).findAll();
    }
}
