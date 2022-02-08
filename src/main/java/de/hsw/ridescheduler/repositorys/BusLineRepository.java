package de.hsw.ridescheduler.repositorys;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusLineRepository extends CrudRepository<BusLine, Long> {
    BusLine findByName(String name);
}
