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
        BusLine busLine = this.busLineService.getBusLineById(id)
                .orElseThrow(() -> new BusLineNotExistsException(id));
        if(updateBusLineRequest.getName() != null) {
            busLine.setName(updateBusLineRequest.getName());
            this.busLineService.saveBusLine(busLine);
        }
    }

    @DeleteMapping("/busLines/{id}")
    public void removeBusLine(@PathVariable Long busLineId) {
        if(this.busLineService.getBusLineById(busLineId).isPresent()) {
            this.busLineService.deleteBusLineById(busLineId);
        } else {
            throw new BusLineNotExistsException(busLineId);
        }
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

    @GetMapping("/busLines/{id}/schedules/{busStopId}")
    public List<ScheduleResponse> getSchedulesForBusStop(@PathVariable Long id, @PathVariable Long busStopId) {
        return this.busLineService.getSchedulesForBusStop(id, busStopId);
    }

    @DeleteMapping("/busLines/{busLineId}/busStops/{busStopId}")
    public void removeBusStop(@PathVariable("busLineId") Long busLineId, @PathVariable("busStopId") Long busStopId) {
        this.busLineService.removeBusStop(busStopId, busLineId);
    }
}
