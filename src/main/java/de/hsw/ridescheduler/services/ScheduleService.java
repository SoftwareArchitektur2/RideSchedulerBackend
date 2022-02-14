package de.hsw.ridescheduler.services;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.beans.Schedule;
import de.hsw.ridescheduler.dtos.BusStopResponse;
import de.hsw.ridescheduler.exceptions.BusStopNotExistsException;
import de.hsw.ridescheduler.exceptions.ScheduleNotExistsException;
import de.hsw.ridescheduler.repositorys.ScheduleRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    public List<BusStopResponse> getBusStops(Long scheduleId, Long busStopId) {
        List<BusStopResponse> result = new ArrayList<>();
        BusStop currentBusStop = this.busStopService.getBusStopById(busStopId).orElseThrow(() -> new BusStopNotExistsException(busStopId));
        Schedule schedule = this.scheduleRepository.findById(scheduleId).orElseThrow(() -> new ScheduleNotExistsException(scheduleId));
        List<BusStopInBusLine> busStops = new ArrayList<>(schedule.getBusLine().getBusStops());


        //check if the busLine is scheduled backwards
        if(!busStops.get(busStops.size()-1).getBusStop().equals(schedule.getDestinationStop())) {
            Collections.reverse(busStops);
        }


        //find starting point of currentBusStop and count already driven time
        int minutesDriven = 0;
        int indexOfCurrentBusStop = 0;
        for(int i = 0; i < busStops.size() && indexOfCurrentBusStop == 0; i++) {
            if(busStops.get(i).getBusStop().equals(currentBusStop)) {
                    indexOfCurrentBusStop = i;
            }
            minutesDriven = minutesDriven + busStops.get(i).getTimeToNextStop();
        }


        //copy busStops from currentBusStop to result and calculate arrival times
        Date currentTime = DateUtils.addMinutes(schedule.getDepartureTime(), minutesDriven);
        for(int i = indexOfCurrentBusStop; i < busStops.size(); i++) {
            result.add(new BusStopResponse(busStops.get(i), currentTime));
            currentTime = DateUtils.addMinutes(currentTime, busStops.get(i).getTimeToNextStop());
        }
        return result;
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
