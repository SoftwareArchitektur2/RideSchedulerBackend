package de.hsw.ridescheduler.controllers;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.dtos.*;
import de.hsw.ridescheduler.exceptions.BusLineNotExistsException;
import de.hsw.ridescheduler.services.BusLineService;
import de.hsw.ridescheduler.services.BusStopService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class BusLineController {

    private BusLineService busLineService;
    private BusStopService busStopService;
    private ModelMapper modelMapper;

    @Autowired
    public BusLineController(BusLineService busLineService, BusStopService busStopService,ModelMapper modelMapper) {
        this.busLineService = busLineService;
        this.busStopService = busStopService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/busLines/")
    public List<BusLineResponse> getAllBuslines() {
        return this.busLineService.getAllBusLines()
                .stream()
                .map(busLine -> this.modelMapper.map(busLine, BusLineResponse.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/busLines/")
    public ResponseEntity<BusLineResponse> addBusline(@RequestBody AddBusLineRequest addBusLineRequest) {
        return new ResponseEntity<BusLineResponse>(this.modelMapper.map(
                this.busLineService.saveBusLine(this.modelMapper.map(addBusLineRequest, BusLine.class))
                , BusLineResponse.class), HttpStatus.CREATED);
    }

    @PatchMapping("/busLines/{id}")
    public void updateBusline(@PathVariable("id") Long id, @RequestBody UpdateBusLineRequest updateBusLineRequest) {
        this.busLineService.changeName(id, updateBusLineRequest.getName());
    }

    @DeleteMapping("/busLines/{busLineId}")
    public void removeBusLine(@PathVariable Long busLineId) {
        this.busLineService.getBusLineById(busLineId);
        this.busLineService.deleteBusLineById(busLineId);
    }

    @GetMapping("/busLines/{id}/busStops")
    public List<BusStopInBusLineResponse> getBusStops(@PathVariable Long id) {
       return this.busLineService.getAllBusStops(id);
    }

    @GetMapping("/busLines/{id}/destinationStops")
    public List<BusStopResponse> getDestinationStops(@PathVariable Long id) {
        return this.busLineService.getDestinationStops(id);
    }

    @PostMapping("/busLines/{id}/busStops")
    public void addBusStop(@PathVariable Long id, @RequestBody BusStopRequest busStopRequest) {
        this.busLineService.addBusStop(id, busStopRequest.getId(), busStopRequest.getTimeToNextStop());
    }

    @GetMapping("busLines/{id}/schedules")
    public List<ScheduleResponse> getSchedules(@PathVariable Long id) {
        return this.busLineService.getAllSchedules(id);
    }

    @GetMapping("/busLines/{busLineInBusStopId}/schedules")
    public List<ScheduleResponse> getSchedulesForBusStop(@PathVariable("busLineInBusStopId") Long busLineInBusStopId) {
        return this.busLineService.getSchedulesForBusStop(busLineInBusStopId);
    }

    @DeleteMapping("/busLines/busStops/{busLineInBusStopId}")
    public void removeBusStop(@PathVariable("busLineInBusStopId") Long busLineInBusStopId) {
        this.busLineService.removeBusStop(busLineInBusStopId);
    }
}
