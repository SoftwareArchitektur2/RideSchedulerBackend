package de.hsw.ridescheduler.services;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.repositorys.BusLineRepository;
import de.hsw.ridescheduler.repositorys.BusStopInBusLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void saveBusLine(String busLine) {
        this.busLineRepository.save(new BusLine(busLine));
    }

    public Optional<BusLine> getBusLineByName(String name) {
        return this.busLineRepository.findByName(name);
    }

    public Optional<BusLine> getBusLineById(Long id) {
        return this.busLineRepository.findById(id);
    }

    public void addBusStop(Long busStopId, Long busLineId, int timeToNextStop) {
        Optional<BusLine> optionalBusLine = this.busLineRepository.findById(busLineId);
        BusLine busLine = optionalBusLine.orElseThrow(() -> new IllegalArgumentException("BusLine not found"));
        BusStop busStop = this.busStopService.getBusStopById(busStopId).orElseThrow(() -> new IllegalArgumentException("BusStop not found"));;
        BusStopInBusLine busStopInBusLine = new BusStopInBusLine(busStop, busLine, timeToNextStop);
        busLine.addBusStop(busStopInBusLine);
        this.busLineRepository.save(busLine);
        this.busStopService.addBusLine(busStopId, busStopInBusLine);
    }

    public void removeBusStop(Long busStopId, Long busLineId) {
        Optional<BusLine> optionalBusLine = this.busLineRepository.findById(busLineId);
        BusLine busLine = optionalBusLine.orElseThrow(() -> new IllegalArgumentException("BusLine not found"));
        Optional<BusStopInBusLine> optionalBusStopInBusLine = this.busStopInBusLineRepository.findByBusLineIdAndBusStopId(busLineId, busStopId);
        BusStopInBusLine busStopInBusLine = optionalBusStopInBusLine.orElseThrow(() -> new IllegalArgumentException("BusLine not found"));
        if(this.isBusStopLastOrFirst(busStopInBusLine)) {
            throw new IllegalArgumentException("BusStop is last or first");
        }
        busLine.removeBusStop(busStopInBusLine);
        this.busStopService.removeBusLine(busStopId, busStopInBusLine);
    }

    private Boolean isBusStopLastOrFirst(BusStopInBusLine busStopInBusLine) {
        return busStopInBusLine.getBusLine().getBusStops().get(0).equals(busStopInBusLine) ||
                busStopInBusLine.getBusLine().getBusStops().get(busStopInBusLine.getBusLine().getBusStops().size() - 1).equals(busStopInBusLine);
    }

    public void changeName(Long busLineId, String newName) {
        Optional<BusLine> optionalBusLine = this.busLineRepository.findById(busLineId);
        BusLine busLine = optionalBusLine.orElseThrow(() -> new IllegalArgumentException("BusLine not found"));
        busLine.setName(newName);
        this.busLineRepository.save(busLine);
    }

    public void deleteBusLineById(Long busLineId) {
        Optional<BusLine> optionalBusLine = this.busLineRepository.findById(busLineId);
        BusLine busLine = optionalBusLine.orElseThrow(() -> new IllegalArgumentException("BusLine not found"));
        if(busLine.getSchedules().isEmpty()) {
            this.busLineRepository.delete(busLine);
        } else {
            throw new IllegalArgumentException("BusLine has schedules");
        }
    }
}
