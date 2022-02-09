package de.hsw.ridescheduler.services;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.Schedule;
import de.hsw.ridescheduler.repositorys.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final BusLineService busLineService;

    private final BusStopService busStopService;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, BusLineService busLineService, BusStopService busStopService) {
        this.scheduleRepository = scheduleRepository;
        this.busLineService = busLineService;
        this.busStopService = busStopService;
    }

    public void createSchedule(Long BusLineId, Date departureTime, Long DestinationStopId) {
        BusLine busline = this.busLineService.getBusLineById(BusLineId).orElseThrow(() -> new IllegalArgumentException("BusLine not found"));
        BusStop destinationStop = this.busStopService.getBusStopById(DestinationStopId).orElseThrow(() -> new IllegalArgumentException("BusLine not found"));
        Schedule schedule = new Schedule(busline, departureTime, destinationStop);
        scheduleRepository.save(schedule);
    }

    public void changeDestinationStop(Long id, Long destinationStopId) {
        Optional<Schedule> optionalSchedule = this.scheduleRepository.findById(id);
        Schedule schedule = optionalSchedule.orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        schedule.setDestinationStop(this.busStopService.getBusStopById(destinationStopId).orElseThrow(() -> new IllegalArgumentException("BusLine not found")));
        scheduleRepository.save(schedule);
    }

    public void deleteScheduleById(Long id) {
        scheduleRepository.deleteById(id);
    }
}
