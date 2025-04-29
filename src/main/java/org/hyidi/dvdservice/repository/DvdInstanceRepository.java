package org.hyidi.dvdservice.repository;

import org.hyidi.dvdservice.model.DvdInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DvdInstanceRepository extends JpaRepository<DvdInstance, Long> {
    List<DvdInstance> findByStatus(DvdInstance.Status status);
}
