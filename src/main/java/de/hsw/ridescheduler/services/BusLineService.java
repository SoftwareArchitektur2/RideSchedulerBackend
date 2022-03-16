package de.hsw.ridescheduler.services;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.beans.Schedule;
import de.hsw.ridescheduler.dtos.BusStopInBusLineResponse;
import de.hsw.ridescheduler.dtos.BusStopResponse;
import de.hsw.ridescheduler.dtos.ScheduleResponse;
import de.hsw.ridescheduler.exceptions.*;
import de.hsw.ridescheduler.repositorys.BusLineRepository;
import de.hsw.ridescheduler.repositorys.BusStopInBusLineRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusLineService {

    private final BusLineRepository busLineRepository;
    private final BusStopInBusLineRepository busStopInBusLineRepository;
    private final ModelMapper modelMapper;

    private final BusStopService busStopService;

    @Autowired
    public BusLineService(BusLineRepository busLineRepository, BusStopInBusLineRepository busStopInBusLineRepository, BusStopService busStopService, ModelMapper modelMapper) {
        this.busLineRepository = busLineRepository;
        this.busStopInBusLineRepository = busStopInBusLineRepository;
        this.busStopService = busStopService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public BusLine saveBusLine(BusLine busLine) {
        if (this.busLineRepository.existsByName(busLine.getName())) {
            throw new BusLineAlreadyExistsException(busLine.getName());
        }
        return this.busLineRepository.save(busLine);
    }

    public List<BusLine> getAllBusLines() {
        return this.busLineRepository.findAll();
    }

    public BusLine getBusLineByName(String name) {
        return this.busLineRepository.findByName(name).orElseThrow(() -> new BusLineNotExistsException(name));
    }

    public BusLine getBusLineById(Long id) {
        return this.busLineRepository.findById(id).orElseThrow(() -> new BusLineNotExistsException(id));
    }

    @Transactional
    public List<BusStopInBusLineResponse> getAllBusStops(Long busLineId) {
        BusLine busLine = this.getBusLineById(busLineId);
        List<BusStopInBusLineResponse> result = new ArrayList<>(busLine.getBusStops().size());
        Date currentTime = new Date();

        for (BusStopInBusLine busStopInBusLine : busLine.getBusStops()) {
            result.add(new BusStopInBusLineResponse(busStopInBusLine, currentTime));
            currentTime = DateUtils.addMinutes(currentTime, busStopInBusLine.getTimeToNextStop());
        }
        return result;
    }

    @Transactional
    public List<BusStopResponse> getDestinationStops(Long busLineId) {
        BusLine busLine = this.getBusLineById(busLineId);
        List<BusStopResponse> response = new ArrayList<>(2);
        response.add(this.modelMapper.map(busLine.getBusStops().get(0).getBusStop(), BusStopResponse.class));
        response.add(this.modelMapper.map(busLine.getBusStops().get(busLine.getBusStops().size() - 1).getBusStop(), BusStopResponse.class));
        return response;
    }

    @Transactional
    public List<ScheduleResponse> getAllSchedules(Long busLineId) {
        BusLine busLine = this.getBusLineById(busLineId);
        return busLine.getSchedules()
                .stream()
                .map(schedule -> this.modelMapper.map(schedule, ScheduleResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Date> getSchedulesForBusStop(Long busLineId, Long busStopId) {
        List<Schedule> schedules = this.getBusLineById(busLineId).getSchedules();
        List<Date> response = new ArrayList<>(schedules.size());

        for (Schedule schedule : schedules) {
            List<BusStopInBusLine> busStops = new ArrayList<>(schedule.getBusLine().getBusStops());
            if (schedule.getDestinationStop().equals(busStops.get(busStops.size() - 1).getBusStop())) {
                response.add(iterateOverBusStopsForSchedule(schedule, busStops, busStopId));
            } else {
                response.add(iterateBackwardsOverBusStopsForSchedule(schedule, busStops, busStopId));
            }
        }
        return response;
    }

    private Date iterateOverBusStopsForSchedule(Schedule schedule, List<BusStopInBusLine> busStops, Long busStopId) {
        Date arrivalTime = schedule.getDepartureTime();
        for (BusStopInBusLine busStopInBusLine : busStops) {
            if (busStopId.equals(busStopInBusLine.getBusStop().getId())) {
                return arrivalTime;
            } else {
                arrivalTime = DateUtils.addMinutes(arrivalTime, busStopInBusLine.getTimeToNextStop());
            }
        }
        throw new BusStopNotExistsException(busStopId);
    }

    private Date iterateBackwardsOverBusStopsForSchedule(Schedule schedule, List<BusStopInBusLine> busStops, Long busStopId) {
        Date arrivalTime = schedule.getDepartureTime();
        for (int i = busStops.size() - 1; i > 0; i--) {
            if (busStopId.equals(busStops.get(i).getBusStop().getId())) {
                return arrivalTime;
            } else {
                arrivalTime = DateUtils.addMinutes(arrivalTime, busStops.get(i).getTimeToNextStop());
            }
        }
        throw new BusStopNotExistsException(busStopId);
    }

    @Transactional
    public void addBusStop(Long busLineId, Long busStopId, int timeToNextStop) {
        BusLine busLine = this.getBusLineById(busLineId);
        BusStop busStop = this.busStopService.getBusStopById(busStopId);

        if (busLine.getBusStops().size() > 0 && busLine.getSchedules().size() > 0) {
            BusStop destinationStop = busLine.getBusStops().get(busLine.getBusStops().size() - 1).getBusStop();

            BusStopInBusLine busStopInBusLine = new BusStopInBusLine(busStop, busLine, timeToNextStop);
            busLine.addBusStop(busStopInBusLine);
            this.busLineRepository.save(busLine);

            busLine.getSchedules().stream()
                    .filter(schedule -> schedule.getDestinationStop().equals(destinationStop))
                    .forEach(schedule -> schedule.setDestinationStop(busStop));
        } else {
            BusStopInBusLine busStopInBusLine = new BusStopInBusLine(busStop, busLine, timeToNextStop);
            busLine.addBusStop(busStopInBusLine);
            this.busLineRepository.save(busLine);
        }
    }

    @Transactional
    public void removeBusStop(Long busLineInBusStopId) {
        BusStopInBusLine busStopInBusLine = this.busStopInBusLineRepository.findById(busLineInBusStopId)
                .orElseThrow(() -> new BusStopNotExistsException(busLineInBusStopId));
        if (this.isBusStopLastOrFirst(busStopInBusLine)) {
            throw new BusStopIsLastOrFirstException(busStopInBusLine.getBusStop().getName(), busStopInBusLine.getBusLine().getName());
        }
        BusLine busLine = busStopInBusLine.getBusLine();
        busLine.getBusStops().remove(busStopInBusLine);
        this.busLineRepository.save(busLine);
    }

    private Boolean isBusStopLastOrFirst(BusStopInBusLine busStopInBusLine) {
        return busStopInBusLine.getBusLine().getBusStops().get(0).equals(busStopInBusLine) ||
                busStopInBusLine.getBusLine().getBusStops().get(busStopInBusLine.getBusLine().getBusStops().size() - 1)
                        .equals(busStopInBusLine);
    }

    @Transactional
    public void changeName(Long busLineId, String newName) {
        if (this.busLineRepository.existsByName(newName)) {
            throw new BusLineAlreadyExistsException(newName);
        }
        BusLine busLine = this.getBusLineById(busLineId);
        busLine.setName(newName);
        this.busLineRepository.save(busLine);
    }

    @Transactional
    public void deleteBusLineById(Long busLineId) {
        BusLine busLine = this.getBusLineById(busLineId);
        if (!busLine.getSchedules().isEmpty()) {
            StringBuilder schedules = new StringBuilder();
            for (Schedule schedule : busLine.getSchedules()) {
                schedules.append(schedule.getId()).append(" ");
            }
            throw new BusLineHasSchedulesException(busLine.getName());
        }
        this.busLineRepository.deleteById(busLineId);
    }
}
