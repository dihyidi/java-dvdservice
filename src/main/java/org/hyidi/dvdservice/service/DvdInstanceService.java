package org.hyidi.dvdservice.service;

import lombok.RequiredArgsConstructor;
import org.hyidi.dvdservice.model.Dvd;
import org.hyidi.dvdservice.model.DvdInstance;
import org.hyidi.dvdservice.repository.DvdInstanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DvdInstanceService {
    private final DvdInstanceRepository dvdInstanceRepository;

    public DvdInstance addDvdInstance(Dvd dvd) {
        DvdInstance instance = new DvdInstance();
        instance.setDvd(dvd);
        instance.setStatus(DvdInstance.Status.AVAILABLE);
        return dvdInstanceRepository.save(instance);
    }

    public List<DvdInstance> getAvailableInstances() {
        return dvdInstanceRepository.findByStatus(DvdInstance.Status.AVAILABLE);
    }

    public DvdInstance getInstanceById(Long id) {
        return dvdInstanceRepository.findById(id).orElseThrow(() -> new RuntimeException("Instance not found"));
    }

    public void saveInstance(DvdInstance instance) {
        dvdInstanceRepository.save(instance);
    }
}
