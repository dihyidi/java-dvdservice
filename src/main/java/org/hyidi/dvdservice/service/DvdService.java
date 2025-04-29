package org.hyidi.dvdservice.service;

import lombok.RequiredArgsConstructor;
import org.hyidi.dvdservice.model.Dvd;
import org.hyidi.dvdservice.repository.DvdRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DvdService {
    private final DvdRepository dvdRepository;

    public Dvd addDvd(Dvd dvd) {
        return dvdRepository.save(dvd);
    }

    public List<Dvd> getAllDvds() {
        return dvdRepository.findAll();
    }
}
