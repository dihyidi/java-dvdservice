package org.hyidi.dvdservice.service;

import lombok.RequiredArgsConstructor;
import org.hyidi.dvdservice.model.DvdInstance;
import org.hyidi.dvdservice.model.Rental;
import org.hyidi.dvdservice.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final DvdInstanceService dvdInstanceService;

    public Rental rentDvd(Long instanceId, String renterName, int rentalDays) {
        DvdInstance instance = dvdInstanceService.getInstanceById(instanceId);

        if (instance.getStatus() != DvdInstance.Status.AVAILABLE) {
            throw new RuntimeException("Dvd Instance not available for rent");
        }

        instance.setStatus(DvdInstance.Status.RENTED);
        dvdInstanceService.saveInstance(instance);

        Rental rental = new Rental();
        rental.setDvdInstance(instance);
        rental.setRenterName(renterName);
        rental.setRentDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        rental.setReturnDate(Date.from(LocalDateTime.now().plusDays(rentalDays).atZone(ZoneId.systemDefault()).toInstant()));
        rental.setTotalPrice(instance.getDvd().getDailyPrice() * rentalDays);

        return rentalRepository.save(rental);
    }

    public Rental returnDvd(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        DvdInstance instance = rental.getDvdInstance();
        instance.setStatus(DvdInstance.Status.AVAILABLE);
        dvdInstanceService.saveInstance(instance);

        rentalRepository.delete(rental);

        return rental;
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }
}
