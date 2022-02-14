package de.hsw.ridescheduler.controllers;

import de.hsw.ridescheduler.beans.Schedule;
import de.hsw.ridescheduler.dtos.AddScheduleRequest;
import de.hsw.ridescheduler.dtos.BusStopResponse;
import de.hsw.ridescheduler.dtos.ScheduleResponse;
import de.hsw.ridescheduler.exceptions.ScheduleNotExistsException;
import de.hsw.ridescheduler.services.ScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScheduleController {

    private ScheduleService scheduleService;
    private ModelMapper modelMapper;

    @Autowired
    public ScheduleController(ScheduleService scheduleService, ModelMapper modelMapper) {
        this.scheduleService = scheduleService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/schedules")
    public void addSchedule(@RequestBody AddScheduleRequest addScheduleRequest) {
        this.scheduleService.createSchedule(addScheduleRequest.getBusLineId(), addScheduleRequest.getDepartureTime(), addScheduleRequest.getDestinationStopId());
    }

    @GetMapping("/schedules/{id}")
    public ScheduleResponse getSchedule(@PathVariable("id") Long id) {
        Schedule schedule = this.scheduleService.getScheduleById(id).orElseThrow(() -> new ScheduleNotExistsException(id));
        return this.modelMapper.map(schedule, ScheduleResponse.class);
    }

    @DeleteMapping("/schedules/{id}")
    public void deleteSchedule(@RequestParam Long id) {
        this.scheduleService.deleteScheduleById(id);
    }

    @GetMapping("schedules/{scheduleId}/busStops/{busStopId}")
    public List<BusStopResponse> getBusStops(@PathVariable("scheduleId") Long scheduleId, @PathVariable("busStopId") Long busStopId) {
        return this.scheduleService.getBusStops(scheduleId, busStopId);
    }
}
