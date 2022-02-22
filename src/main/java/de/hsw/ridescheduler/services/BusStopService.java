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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        this.busStopRepository.save(busStop);
        return busStop;
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
        if(this.busStopRepository.findByName(name).isPresent()) {
            throw new BusStopAlreadyExistsException(name);
        }
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
        BusStop busStop = busStopOptional.orElseThrow(() -> new BusStopNotExistsException(id));
        busStop.addBusLine(busLine);
    }

    public void removeBusLine(Long id, BusStopInBusLine busLine) {
        Optional<BusStop> busStopOptional = busStopRepository.findById(id);
        BusStop busStop = busStopOptional.orElseThrow(() -> new BusStopNotExistsException(id));
        busStop.removeBusLine(busLine);
    }

    @Transactional
    public List<BusLineResponse> getBusLinesForBusStop(Long id) {
        BusStop busStop = busStopRepository.findById(id).orElseThrow(() -> new BusStopNotExistsException(id));
        List<BusStopInBusLine> busLines = busStop.getBusLines();
        List<BusLineResponse> responses = new ArrayList<>(busLines.size());
        busLines.forEach(busLine -> responses.add(new BusLineResponse(busLine.getBusLine().getId(), busLine.getBusLine().getName())));
        return responses;
    }

    @Transactional
    public void deleteBusStopById(Long id) {
        Optional<BusStop> busStopOptional = busStopRepository.findById(id);
        BusStop busStop = busStopOptional.orElseThrow(() -> new BusStopNotExistsException(id));
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
