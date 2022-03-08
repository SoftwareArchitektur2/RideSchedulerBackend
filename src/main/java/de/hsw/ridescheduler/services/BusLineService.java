package de.hsw.ridescheduler.services;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.beans.Schedule;
import de.hsw.ridescheduler.dtos.BusLineResponse;
import de.hsw.ridescheduler.dtos.BusStopInBusLineResponse;
import de.hsw.ridescheduler.dtos.BusStopResponse;
import de.hsw.ridescheduler.dtos.ScheduleResponse;
import de.hsw.ridescheduler.exceptions.BusLineAlreadyExistsException;
import de.hsw.ridescheduler.exceptions.BusLineHasSchedulesException;
import de.hsw.ridescheduler.exceptions.BusLineNotExistsException;
import de.hsw.ridescheduler.exceptions.BusStopNotExistsException;
import de.hsw.ridescheduler.repositorys.BusLineRepository;
import de.hsw.ridescheduler.repositorys.BusStopInBusLineRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BusLineService {

    private BusLineRepository busLineRepository;
    private BusStopInBusLineRepository busStopInBusLineRepository;
    private ModelMapper modelMapper;

    private BusStopService busStopService;

    @Autowired
    public BusLineService(BusLineRepository busLineRepository, BusStopInBusLineRepository busStopInBusLineRepository, BusStopService busStopService, ModelMapper modelMapper) {
        this.busLineRepository = busLineRepository;
        this.busStopInBusLineRepository = busStopInBusLineRepository;
        this.busStopService = busStopService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public BusLine saveBusLine(BusLine busLine) {
        if(this.busLineRepository.existsByName(busLine.getName())) {
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
    public List<ScheduleResponse> getSchedulesForBusStop(Long busLineId, Long busStopId) {
        BusLine busLine = this.getBusLineById(busLineId);
        List<Schedule> schedules = busLine.getSchedules();
        List<ScheduleResponse> response = new ArrayList<>(schedules.size());

        for(Schedule schedule : schedules) {
            Date time = schedule.getDepartureTime();

            for(BusStopInBusLine busStopInBusLine : schedule.getBusLine().getBusStops()) {
                BusStop busStop = busStopInBusLine.getBusStop();

                if(busStop.getId().equals(busStopId)) {
                    ScheduleResponse scheduleResponse = new ScheduleResponse(schedule.getId(),
                                                                             this.modelMapper.map(schedule.getBusLine(), BusLineResponse.class),
                                                                             time,
                                                                             this.modelMapper.map(busStopInBusLine, BusStopInBusLineResponse.class));
                    response.add(scheduleResponse);
                } else {
                    time = DateUtils.addMinutes(time, busStopInBusLine.getTimeToNextStop());
                }
            }
        }
        return response;
    }

    @Transactional
    public void addBusStop(Long busLineId, Long busStopId, int timeToNextStop) {
        BusLine busLine = this.getBusLineById(busLineId);
        BusStop busStop = this.busStopService.getBusStopById(busStopId);

        BusStopInBusLine busStopInBusLine = new BusStopInBusLine(busStop, busLine, timeToNextStop);
        busLine.addBusStop(busStopInBusLine);
        this.busLineRepository.save(busLine);
        //TODO change destination stop for every schedule with this busLine
    }

    @Transactional
    public void removeBusStop(Long busLineId, Long busStopId) {
        BusStopInBusLine busStopInBusLine = this.busStopInBusLineRepository.findByBusLineIdAndBusStopId(busLineId, busStopId)
                .orElseThrow(() -> new BusStopNotExistsException(busStopId));

        if(this.isBusStopLastOrFirst(busStopInBusLine)) {
            throw new IllegalArgumentException("BusStop is last or first");
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
        if(this.busLineRepository.existsByName(newName)) {
            throw new BusLineAlreadyExistsException(newName);
        }
        BusLine busLine = this.getBusLineById(busLineId);
        busLine.setName(newName);
    }

    @Transactional
    public void deleteBusLineById(Long busLineId) {
        BusLine busLine = this.getBusLineById(busLineId);
        if(busLine.getSchedules().isEmpty()) {
            this.busLineRepository.deleteById(busLineId);
        } else {
            String schedules = "";
            for(Schedule schedule : busLine.getSchedules()) {
                schedules += schedule.getId() + " ";
            }
            throw new BusLineHasSchedulesException(busLine.getName());
        }
    }
}
