package de.hsw.ridescheduler.services;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.exceptions.BusStopAlreadyExistsException;
import de.hsw.ridescheduler.repositorys.BusStopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusStopService {

    private BusStopRepository busStopRepository;

    @Autowired
    public BusStopService(BusStopRepository busStopRepository) {
        this.busStopRepository = busStopRepository;
    }

    public void saveBusStop(BusStop busStop) {
        if(this.busStopRepository.existsByName(busStop.getName())) {
            throw new BusStopAlreadyExistsException(busStop.getName());
        }
        this.busStopRepository.save(busStop);
    }

    public List<BusStop> getAllBusStops() {
            return this.busStopRepository.findAll();
    }

    public Optional<BusStop> getBusStopByName(String name) {
        return this.busStopRepository.findByName(name);
    }

    public Optional<BusStop> getBusStopById(Long id) {
        return this.busStopRepository.findById(id);
    }

    public void changeName(Long id, String name) {
        BusStop busStop = this.busStopRepository.findById(id).get();
        busStop.setName(name);
        busStopRepository.save(busStop);
    }

    public void changeHasWifi(Long id, Boolean hasWifi) {
        BusStop busStop = this.busStopRepository.findById(id).get();
        busStop.setHasWifi(hasWifi);
        busStopRepository.save(busStop);
    }

    public void addBusLine(Long id, BusStopInBusLine busLine) {
        Optional<BusStop> busStopOptional = busStopRepository.findById(id);
        BusStop busStop = busStopOptional.orElseThrow(() -> new IllegalArgumentException("BusStop not found"));
        busStop.addBusLine(busLine);
    }

    public void removeBusLine(Long id, BusStopInBusLine busLine) {
        Optional<BusStop> busStopOptional = busStopRepository.findById(id);
        BusStop busStop = busStopOptional.orElseThrow(() -> new IllegalArgumentException("BusStop not found"));
        busStop.removeBusLine(busLine);
    }

    public void deleteBusStopById(Long id) {
        Optional<BusStop> busStopOptional = busStopRepository.findById(id);
        if(busStopOptional.isPresent() && busStopOptional.get().getBusLines().isEmpty()) {
            busStopRepository.deleteById(id);
        }
    }
}
