package de.hsw.ridescheduler.repositorys;

import de.hsw.ridescheduler.beans.BusStopInBusLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusStopInBusLineRepository extends JpaRepository<BusStopInBusLine, Long> {
    List<BusStopInBusLine> findByBusLineIdAndBusStopId(Long busLineId, Long busStopId);
}
