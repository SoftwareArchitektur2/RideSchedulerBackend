package de.hsw.ridescheduler.services;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.dtos.BusStopResponse;
import de.hsw.ridescheduler.exceptions.BusLineAlreadyExistsException;
import de.hsw.ridescheduler.exceptions.BusLineNotExistsException;
import de.hsw.ridescheduler.exceptions.BusStopNotExistsException;
import de.hsw.ridescheduler.repositorys.BusLineRepository;
import de.hsw.ridescheduler.repositorys.BusStopInBusLineRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BusLineService {

    private BusLineRepository busLineRepository;
    private BusStopInBusLineRepository busStopInBusLineRepository;

    private BusStopService busStopService;

    @Autowired
    public BusLineService(BusLineRepository busLineRepository, BusStopInBusLineRepository busStopInBusLineRepository, BusStopService busStopService) {
        this.busLineRepository = busLineRepository;
        this.busStopInBusLineRepository = busStopInBusLineRepository;
        this.busStopService = busStopService;
    }

    public void saveBusLine(BusLine busLine) {
        if(this.busLineRepository.findByName(busLine.getName()).isPresent()) {
            throw new BusLineAlreadyExistsException(busLine.getName());
        }
        this.busLineRepository.save(busLine);
    }

    public List<BusLine> getAllBusLines() {
        return this.busLineRepository.findAll();
    }

    public Optional<BusLine> getBusLineByName(String name) {
        return this.busLineRepository.findByName(name);
    }

    public Optional<BusLine> getBusLineById(Long id) {
        return this.busLineRepository.findById(id);
    }

    public List<BusStopResponse> getAllBusStops(Long busLineId) {
        BusLine busLine = this.busLineRepository
                .findById(busLineId).orElseThrow(() -> new BusLineNotExistsException(busLineId));
        List<BusStopResponse> result = new ArrayList<>(busLine.getBusStops().size());
        Date currentTime = new Date();

        for (BusStopInBusLine busStopInBusLine : busLine.getBusStops()) {
            result.add(new BusStopResponse(busStopInBusLine, currentTime));
            currentTime = DateUtils.addMinutes(currentTime, busStopInBusLine.getTimeToNextStop());
        }
        return result;
    }

    public void addBusStop(Long busStopId, Long busLineId, int timeToNextStop) {
        BusLine busLine = this.busLineRepository.findById(busLineId)
                .orElseThrow(() -> new BusLineNotExistsException(busLineId));
        BusStop busStop = this.busStopService.getBusStopById(busStopId)
                .orElseThrow(() -> new BusStopNotExistsException(busStopId));

        BusStopInBusLine busStopInBusLine = new BusStopInBusLine(busStop, busLine, timeToNextStop);
        busLine.addBusStop(busStopInBusLine);
        this.busLineRepository.save(busLine);
        this.busStopService.addBusLine(busStopId, busStopInBusLine);
    }

    public void removeBusStop(Long busStopId, Long busLineId) {
        BusLine busLine = this.busLineRepository.findById(busLineId)
                .orElseThrow(() -> new BusLineNotExistsException(busLineId));
        BusStopInBusLine busStopInBusLine = this.busStopInBusLineRepository.findByBusLineIdAndBusStopId(busLineId, busStopId)
                .orElseThrow(() -> new BusStopNotExistsException(busStopId));

        if(this.isBusStopLastOrFirst(busStopInBusLine)) {
            throw new IllegalArgumentException("BusStop is last or first");
        }
        busLine.removeBusStop(busStopInBusLine);
        this.busStopService.removeBusLine(busStopId, busStopInBusLine);
    }

    private Boolean isBusStopLastOrFirst(BusStopInBusLine busStopInBusLine) {
        return busStopInBusLine.getBusLine().getBusStops().get(0).equals(busStopInBusLine) ||
                busStopInBusLine.getBusLine().getBusStops().get(busStopInBusLine.getBusLine().getBusStops().size() - 1)
                        .equals(busStopInBusLine);
    }

    public void changeName(Long busLineId, String newName) {
        Optional<BusLine> optionalBusLine = this.busLineRepository.findById(busLineId);
        BusLine busLine = optionalBusLine.orElseThrow(() -> new BusLineNotExistsException(busLineId));
        busLine.setName(newName);
        this.busLineRepository.save(busLine);
    }

    public void deleteBusLineById(Long busLineId) {
        Optional<BusLine> optionalBusLine = this.busLineRepository.findById(busLineId);
        BusLine busLine = optionalBusLine.orElseThrow(() -> new BusLineNotExistsException(busLineId));
        if(busLine.getSchedules().isEmpty()) {
            this.busLineRepository.delete(busLine);
        } else {
            throw new IllegalArgumentException("BusLine has schedules");
        }
    }
}
