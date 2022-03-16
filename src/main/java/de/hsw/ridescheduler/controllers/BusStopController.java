package de.hsw.ridescheduler.controllers;

import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.dtos.*;
import de.hsw.ridescheduler.services.BusStopService;
import de.hsw.ridescheduler.services.ScheduleService;
import org.joda.time.LocalDateTime;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "${allowed-origin}"
, allowedHeaders = "*"
, allowCredentials = "true"
, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH, RequestMethod.TRACE, RequestMethod.PUT})
public class BusStopController {

    private final BusStopService busStopService;
    private final ScheduleService scheduleService;
    private final ModelMapper modelMapper;

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

    //Get every schedule for this busstop within the given time range
    @GetMapping("/busStops/{id}/schedules")
    public List<ScheduleResponse> getSchedulesForBusStop(@PathVariable("id") Long id, @RequestParam("startingTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startingTime, @RequestParam("duration") Integer duration) {

        return this.scheduleService.getSchedulesForBusStop(id, startingTime.toDate(), duration);
    }

    @PostMapping("/busStops/")
    public ResponseEntity<BusStopResponse> addBusStop(@RequestBody AddBusStopRequest addBusStopRequest) {
        return new ResponseEntity<>(this.modelMapper.map(
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
