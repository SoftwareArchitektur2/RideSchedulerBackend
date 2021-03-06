package de.hsw.ridescheduler.repositorys;

import de.hsw.ridescheduler.beans.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusStopRepository extends JpaRepository<BusStop, Long> {
    Optional<BusStop> findByName(String name);

    boolean existsByName(String name);
}
