package de.hsw.ridescheduler.controllers;

import de.hsw.ridescheduler.beans.Schedule;
import de.hsw.ridescheduler.dtos.AddScheduleRequest;
import de.hsw.ridescheduler.dtos.BusStopInBusLineResponse;
import de.hsw.ridescheduler.dtos.ScheduleResponse;
import de.hsw.ridescheduler.exceptions.ScheduleNotExistsException;
import de.hsw.ridescheduler.services.ScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ScheduleController {

    private ScheduleService scheduleService;
    private ModelMapper modelMapper;

    @Autowired
    public ScheduleController(ScheduleService scheduleService, ModelMapper modelMapper) {
        this.scheduleService = scheduleService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/schedules/")
    public ResponseEntity<ScheduleResponse> addSchedule(@RequestBody AddScheduleRequest addScheduleRequest) {
        return new ResponseEntity<ScheduleResponse>(this.modelMapper.map(
                this.scheduleService.createSchedule(addScheduleRequest.getBusLineId(), addScheduleRequest.getDepartureTime(), addScheduleRequest.getDestinationStopId())
                , ScheduleResponse.class), HttpStatus.CREATED);
    }

    @GetMapping("/schedules/{id}")
    public ScheduleResponse getSchedule(@PathVariable("id") Long id) {
        Schedule schedule = this.scheduleService.getScheduleById(id).orElseThrow(() -> new ScheduleNotExistsException(id));
        return this.modelMapper.map(schedule, ScheduleResponse.class);
    }

    @DeleteMapping("/schedules/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        this.scheduleService.deleteScheduleById(id);
    }

    @GetMapping("/schedules/{scheduleId}/busStops/{busStopId}")
    public List<BusStopInBusLineResponse> getBusStops(@PathVariable("scheduleId") Long scheduleId, @PathVariable("busStopId") Long busStopId) {
        return this.scheduleService.getBusStops(scheduleId, busStopId);
    }
}
