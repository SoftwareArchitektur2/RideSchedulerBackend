package de.hsw.ridescheduler.controllers;

import de.hsw.ridescheduler.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class ScheduleController {

    private ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/schedules")
    public void addSchedule(@RequestBody String schedule) {
        //Erstellen eines neuen Fahrplans mit Daten aus Body
    }

    @GetMapping("/schedules/{id}")
    public void getSchedule(@PathVariable("id") Long id) {
        //Laden eines Fahrplans mit id aus Path
    }

    @DeleteMapping("/schedules/{id}")
    public void deleteSchedule(@RequestParam Long id) {
        //Löschen eines Fahrplans mit id aus Path
    }

    @GetMapping("schedules/{scheduleId}/busStops/{busStopId}")
    public void getBusStops(@PathVariable("scheduleId") Long scheduleId, @PathVariable("busStopId") Long busStopId) {
        //Laden eines aller Haltestellen eines Fahrplans ab der angegebenen Haltestelle inkl Ankunftszeiten der Haltestellen
    }
}
