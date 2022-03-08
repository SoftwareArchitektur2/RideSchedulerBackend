package de.hsw.ridescheduler.services;
import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.beans.Schedule;
import de.hsw.ridescheduler.dtos.BusLineResponse;
import de.hsw.ridescheduler.dtos.BusStopInBusLineResponse;
import de.hsw.ridescheduler.dtos.ScheduleResponse;
import de.hsw.ridescheduler.exceptions.BusStopAlreadyExistsException;
import de.hsw.ridescheduler.exceptions.BusStopHasBusLinesException;
import de.hsw.ridescheduler.exceptions.BusStopNotExistsException;
import de.hsw.ridescheduler.repositorys.BusStopRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BusStopService {

    private BusStopRepository busStopRepository;
    private ModelMapper modelMapper;

    @Autowired
    public BusStopService(BusStopRepository busStopRepository, ModelMapper modelMapper) {
        this.busStopRepository = busStopRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public BusStop saveBusStop(BusStop busStop) {
        if (this.busStopRepository.existsByName(busStop.getName())) {
            throw new BusStopAlreadyExistsException(busStop.getName());
        }
        return this.busStopRepository.save(busStop);
    }

    public List<BusStop> getAllBusStops() {
        return this.busStopRepository.findAll();
    }

    public Optional<BusStop> getBusStopByName(String name) {
        return this.busStopRepository.findByName(name);
    }

    public BusStop getBusStopById(Long id) {
        return this.busStopRepository.findById(id).orElseThrow(() -> new BusStopNotExistsException(id));
    }

    @Transactional
    public void changeName(Long id, String name) {
        if(this.busStopRepository.existsByName(name)) {
            throw new BusStopAlreadyExistsException(name);
        }
        BusStop busStop = this.getBusStopById(id);
        busStop.setName(name);
        this.busStopRepository.save(busStop);
    }

    @Transactional
    public void changeHasWifi(Long id, Boolean hasWifi) {
        BusStop busStop = this.getBusStopById(id);
        busStop.setHasWifi(hasWifi);
        this.busStopRepository.save(busStop);
    }


    @Transactional
    public List<BusLineResponse> getBusLinesForBusStop(Long id) {
        BusStop busStop = this.getBusStopById(id);
        return busStop.getBusLines()
                .stream()
                .map(busLine -> new BusLineResponse(busLine.getBusLine().getId(), busLine.getBusLine().getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteBusStopById(Long id) {
        BusStop busStop = this.getBusStopById(id);
        if (!busStop.getBusLines().isEmpty()) {
            String names = "";
            for (BusStopInBusLine bus : busStop.getBusLines()) {
                names += bus.getBusLine().getName() + ",";
            }
            throw new BusStopHasBusLinesException(names);

        }
        busStopRepository.deleteById(id);
    }
}
