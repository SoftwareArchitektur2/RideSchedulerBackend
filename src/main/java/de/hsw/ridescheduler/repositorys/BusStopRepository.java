package de.hsw.ridescheduler.repositorys;

import de.hsw.ridescheduler.beans.BusStop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusStopRepository extends CrudRepository<BusStop, Long> {
    BusStop findByName(String name);
}
