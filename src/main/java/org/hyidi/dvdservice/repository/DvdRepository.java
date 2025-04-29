package org.hyidi.dvdservice.repository;

import org.hyidi.dvdservice.model.Dvd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DvdRepository extends JpaRepository<Dvd, Long> {
}
