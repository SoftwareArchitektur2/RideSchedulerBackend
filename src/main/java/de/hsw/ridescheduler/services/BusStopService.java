package de.hsw.ridescheduler.services;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.dtos.BusLineResponse;
import de.hsw.ridescheduler.exceptions.BusStopAlreadyExistsException;
import de.hsw.ridescheduler.exceptions.BusStopHasBusLinesException;
import de.hsw.ridescheduler.exceptions.BusStopNotExistsException;
import de.hsw.ridescheduler.repositorys.BusStopRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BusStopService {

    private final BusStopRepository busStopRepository;

    @Autowired
    public BusStopService(BusStopRepository busStopRepository) {
        this.busStopRepository = busStopRepository;
    }

    @Transactional
    public BusStop saveBusStop(BusStop busStop) {
        if (this.busStopRepository.existsByName(busStop.getName())) {
            throw new BusStopAlreadyExistsException(busStop.getName());
        }
        return this.busStopRepository.save(busStop);
    }

    @Transactional
    public List<BusStop> getAllBusStops() {
        return this.busStopRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(BusStop::getName))
                .collect(Collectors.toList());
    }

    public Optional<BusStop> getBusStopByName(String name) {
        return this.busStopRepository.findByName(name);
    }

    public BusStop getBusStopById(Long id) {
        return this.busStopRepository.findById(id).orElseThrow(() -> new BusStopNotExistsException(id));
    }

    @Transactional
    public void changeName(Long id, String name) {
        BusStop busStop = this.getBusStopById(id);
        if(busStop.getName().equals(name)) {
            return;
        }
        if(this.busStopRepository.existsByName(name)) {
            throw new BusStopAlreadyExistsException(name);
        }
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
        return this.getBusStopById(id).getBusLines()
                .stream()
                .map(busLine -> new BusLineResponse(busLine.getBusLine().getId(), busLine.getBusLine().getName()))
                .sorted(Comparator.comparing(BusLineResponse::getName))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteBusStopById(Long id) {
        BusStop busStop = this.getBusStopById(id);
        if (!busStop.getBusLines().isEmpty()) {
            StringBuilder names = new StringBuilder();
            for (BusStopInBusLine bus : busStop.getBusLines()) {
                names.append(bus.getBusLine().getName()).append(",");
            }
            throw new BusStopHasBusLinesException(names.toString());

        }
        busStopRepository.deleteById(id);
    }
}
