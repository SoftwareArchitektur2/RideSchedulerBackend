package de.hsw.ridescheduler.controllers;

import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.dtos.*;
import de.hsw.ridescheduler.exceptions.BusStopNotExistsException;
import de.hsw.ridescheduler.services.BusStopService;
import de.hsw.ridescheduler.services.ScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class BusStopController {

    private BusStopService busStopService;
    private ScheduleService scheduleService;
    private ModelMapper modelMapper;

    @Autowired
    public BusStopController(BusStopService busStopService, ScheduleService scheduleService, ModelMapper modelMapper) {
        this.busStopService = busStopService;
        this.scheduleService = scheduleService;
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
        return modelMapper.map(this.busStopService.getBusStopById(id), BusStopResponse.class);
    }

    @GetMapping("/busStops/{id}/busLines")
    public List<BusLineResponse> getBusLinesForBusStop(@PathVariable("id") Long id) {
        return this.busStopService.getBusLinesForBusStop(id);
    }

    @GetMapping("/busStops/{id}/schedules")
    public List<ScheduleResponse> getSchedulesForBusStop(@PathVariable("id") Long id, @RequestParam("startingTime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) Date startingTime, @RequestParam("duration") Integer duration) {
        return this.scheduleService.getSchedulesForBusStop(id, startingTime, duration);
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
