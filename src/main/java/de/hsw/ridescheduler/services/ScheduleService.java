package de.hsw.ridescheduler.services;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.beans.Schedule;
import de.hsw.ridescheduler.dtos.BusLineResponse;
import de.hsw.ridescheduler.dtos.BusStopInBusLineResponse;
import de.hsw.ridescheduler.dtos.ScheduleResponse;
import de.hsw.ridescheduler.exceptions.ScheduleNotExistsException;
import de.hsw.ridescheduler.repositorys.ScheduleRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.*;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final BusLineService busLineService;
    private final BusStopService busStopService;
    private final ModelMapper modelMapper;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, BusLineService busLineService, BusStopService busStopService, ModelMapper modelMapper) {
        this.scheduleRepository = scheduleRepository;
        this.busLineService = busLineService;
        this.busStopService = busStopService;
        this.modelMapper = modelMapper;
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ScheduleNotExistsException(id));
    }

   @Transactional
    public List<BusStopInBusLineResponse> getBusStops(Long scheduleId, Long busStopId) {
        List<BusStopInBusLineResponse> result = new ArrayList<>();
        BusStop currentBusStop = this.busStopService.getBusStopById(busStopId);
        Schedule schedule = this.scheduleRepository.findById(scheduleId).orElseThrow(() -> new ScheduleNotExistsException(scheduleId));

       //check if the busLine is scheduled backwards
       List<BusStopInBusLine> busStops = new ArrayList<>(schedule.getBusLine().getBusStops());

       //check if the busLine is scheduled backwards
       if(!busStops.get(busStops.size()-1).getBusStop().equals(schedule.getDestinationStop())) {
           Collections.reverse(busStops);
       }
       //calculate drivingtime from starting point to busStop
       int minutesDriven = 0;
       boolean found = false;
       int indexOfCurrentBusStop = 0;
       for(int i = 0; i < busStops.size() && !found; i++) {
           if(busStops.get(i).getBusStop().equals(currentBusStop)) {
               found = true;
               indexOfCurrentBusStop = i;
           } else {
               minutesDriven = minutesDriven + busStops.get(i).getTimeToNextStop();
           }
       }
       //add drivingtime to departure time
       Date currentTime = this.calculateTodaysDepartureTime(schedule.getDepartureTime());
       currentTime = DateUtils.addMinutes(currentTime, minutesDriven);

        for(int i = indexOfCurrentBusStop; i < busStops.size(); i++) {
            result.add(new BusStopInBusLineResponse(busStops.get(i), currentTime));
            currentTime = DateUtils.addMinutes(currentTime, busStops.get(i).getTimeToNextStop());
        }
        return result;
    }

    private Date calculateTodaysDepartureTime(Date departureTime) {
        Date currentTime = new Date();
        Calendar currentCalender = Calendar.getInstance();
        currentCalender.setTime(currentTime);

        Calendar departureCalender = GregorianCalendar.getInstance();
        departureCalender.setTime(departureTime);
        currentCalender.set(Calendar.HOUR_OF_DAY, departureCalender.get(Calendar.HOUR_OF_DAY));
        currentCalender.set(Calendar.MINUTE, departureCalender.get(Calendar.MINUTE));
        currentCalender.set(Calendar.SECOND, departureCalender.get(Calendar.SECOND));
        currentCalender.set(Calendar.MILLISECOND, departureCalender.get(Calendar.MILLISECOND));
        return currentCalender.getTime();
    }

    public Date getArrivalTimeForBusStop(Schedule schedule, BusStop busStop) {
        //check if the busLine is scheduled backwards
        List<BusStopInBusLine> busStops = new ArrayList<>(schedule.getBusLine().getBusStops());

        //check if the busLine is scheduled backwards
        if(!busStops.get(busStops.size()-1).getBusStop().equals(schedule.getDestinationStop())) {
            Collections.reverse(busStops);
        }
        //calculate drivingtime from starting point to busStop
        int minutesDriven = 0;
        boolean found = false;
        for(int i = 0; i < busStops.size() && !found; i++) {
            if(busStops.get(i).getBusStop().equals(busStop)) {
                found = true;
            } else {
                minutesDriven = minutesDriven + busStops.get(i).getTimeToNextStop();
            }
        }
        //add drivingtime to departure time
        Date currentTime = this.calculateTodaysDepartureTime(schedule.getDepartureTime());
        currentTime = DateUtils.addMinutes(currentTime, minutesDriven);
        return currentTime;
    }

    //TODO refactor this with good tests
    @Transactional
    public List<ScheduleResponse> getSchedulesForBusStop(Long buStopId, Date startingTime, Integer duration) {
        Date endingTime = DateUtils.addMinutes(startingTime, duration);
        List<ScheduleResponse> result = new ArrayList<>();
        BusStop busStop = this.busStopService.getBusStopById(buStopId);

        for(BusStopInBusLine busLine : busStop.getBusLines()) {

            for(Schedule schedule : busLine.getBusLine().getSchedules()) {
                Date arrivalTime = this.getArrivalTimeForBusStop(schedule, busStop);
                if(arrivalTime.after(startingTime) && arrivalTime.before(endingTime)) {
                    result.add(new ScheduleResponse(schedule.getId(), this.modelMapper.map(busLine.getBusLine(), BusLineResponse.class)
                            , arrivalTime
                            , new BusStopInBusLineResponse(schedule.getDestinationStop(), arrivalTime)));
                }
            }
        }
        result.sort(Comparator.comparing(ScheduleResponse::getDepartureTime));
        return result;
    }

    public Schedule createSchedule(Long BusLineId, Time departureTime, Long DestinationStopId) {
        BusLine busline = this.busLineService.getBusLineById(BusLineId);
        BusStop destinationStop = this.busStopService.getBusStopById(DestinationStopId);
        Schedule schedule = new Schedule(busline, departureTime, destinationStop);
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public void changeDestinationStop(Long scheduleId, Long destinationStopId) {
        Schedule schedule = this.getScheduleById(scheduleId);
        schedule.setDestinationStop(this.busStopService.getBusStopById(destinationStopId));
    }

    public void deleteScheduleById(Long id) {
        Schedule schedule = this.getScheduleById(id);
        scheduleRepository.delete(schedule);
    }
}
