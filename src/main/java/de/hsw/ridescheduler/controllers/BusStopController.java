package de.hsw.ridescheduler.controllers;

import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.dtos.*;
import de.hsw.ridescheduler.exceptions.BusStopNotExistsException;
import de.hsw.ridescheduler.services.BusStopService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class BusStopController {

    private BusStopService busStopService;
    private ModelMapper modelMapper;

    @Autowired
    public BusStopController(BusStopService busStopService, ModelMapper modelMapper) {
        this.busStopService = busStopService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/busStops/")
    public List<BusStopResponse> getAllBusStops() {
        return this.busStopService.getAllBusStops()
                .stream()
                .map(busStop -> this.modelMapper.map(busStop, BusStopResponse.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/busStops/{id}")
    public BusStopResponse getBusStopById(@PathVariable("id") Long id) {
        return modelMapper.map(this.busStopService.getBusStopById(id).orElseThrow(() -> new BusStopNotExistsException(id)), BusStopResponse.class);
    }

    @GetMapping("/busStops/{id}/busLines")
    public List<BusLineResponse> getBusLinesForBusStop(@PathVariable("id") Long id) {
        return this.busStopService.getBusLinesForBusStop(id);
    }

    @PostMapping("/busStops/")
    public ResponseEntity<BusStopResponse> addBusStop(@RequestBody AddBusStopRequest addBusStopRequest) {
        return new ResponseEntity<BusStopResponse>(this.modelMapper.map(
                this.busStopService.saveBusStop(this.modelMapper.map(addBusStopRequest, BusStop.class))
                , BusStopResponse.class), HttpStatus.CREATED);
    }

    @PatchMapping("/busStops/{id}")
    public void updateBusStop(@PathVariable("id") Long id, @RequestBody UpdateBusStopRequest updateBusStopRequest) {
        if(updateBusStopRequest.getName() != null) {
            this.busStopService.changeName(id, updateBusStopRequest.getName());
        }
        if(updateBusStopRequest.getHasWifi() != null) {
            this.busStopService.changeHasWifi(id, updateBusStopRequest.getHasWifi());
        }
    }

    @DeleteMapping("/busStops/{id}")
    public void deleteBusStop(@PathVariable("id") Long id) {
        this.busStopService.deleteBusStopById(id);
    }
}
