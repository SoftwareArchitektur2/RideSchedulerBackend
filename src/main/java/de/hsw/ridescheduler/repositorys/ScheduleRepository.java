package de.hsw.ridescheduler.repositorys;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.Schedule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
    Iterable<Schedule> findByDepartureTime(Date departureTime);
    Iterable<Schedule> findByDestinationStop(BusStop destinationStop);
}
