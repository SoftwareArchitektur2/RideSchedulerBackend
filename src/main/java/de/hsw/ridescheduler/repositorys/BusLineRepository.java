package de.hsw.ridescheduler.repositorys;

import de.hsw.ridescheduler.beans.BusLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusLineRepository extends JpaRepository<BusLine, Long> {
    Optional<BusLine> findByName(String name);
}
