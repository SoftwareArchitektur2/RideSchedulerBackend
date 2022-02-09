package de.hsw.ridescheduler.controllers;

import de.hsw.ridescheduler.services.BusStopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class BusStopController {

    private BusStopService busStopService;

    @Autowired
    public BusStopController(BusStopService busStopService) {
        this.busStopService = busStopService;
    }

    @GetMapping("/busStops")
    public String getBusStop() {
        return "busstop";
    }

    @PostMapping("/busStops")
    public void addBusStop(@RequestBody String busStop) {
        //Hinzufügen eines Busstops mit Namen aus dem Body
    }

    @PatchMapping("/busStops/{id}")
    public void updateBusStop(@PathVariable("id") Long id, @RequestBody String busStop) {
        //Aktualisieren eines Busstops mit id aus dem Path und neuem Namen dem Body
    }

    @DeleteMapping("/busStops/{id}")
    public void deleteBusStop(@PathVariable("id") Long id) {
        //Löschen eines Busstops mit id aus dem Path
    }
}
