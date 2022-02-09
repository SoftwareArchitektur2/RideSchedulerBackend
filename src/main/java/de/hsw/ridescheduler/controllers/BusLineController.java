package de.hsw.ridescheduler.controllers;

import de.hsw.ridescheduler.services.BusLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class BusLineController {

    private BusLineService busLineService;

    @Autowired
    public BusLineController(BusLineService busLineService) {
        this.busLineService = busLineService;
    }

    @GetMapping("/busLines")
    public void getAllBuslines() {
        //Return alle Buslinien
        //Requestbody mit optionaler Haltestelle -> Dann nur die Buslinien, die daran angebunden sind
    }

    @PostMapping("/buslines")
    public void addBusline(@RequestBody String busline) {

    }

    @PatchMapping("/busLines/{id}")
    public void updateBusline(@RequestBody String busline) {
        //Update einer Buslinie
        //Body neuen namen der Buslinie
    }

    @DeleteMapping("/busLines/{id}")
    public void removeBusLine(@RequestParam Long busLineId) {
        //Entfernen einer Buslinie
    }

    @GetMapping("/busLines/{id}/busStops")
    public void getBusStops(@RequestParam Long busLineId) {
        //Liefert alle Haltestellen einer Buslinie
    }

    @PostMapping("/busLines/{id}/busStops")
    public void addBusStop(@RequestParam Long busLineId, @RequestBody String busStop) {
        //Hinzufügen einer Haltestelle zu einer Buslinie -> Body muss Haltestelle und Fahrzeit enthalten
    }

    @DeleteMapping("/busLines/{busLineId}/busStops/{busStopId}")
    public void removeBusStop(@RequestParam("busLineId") Long busLineId, @RequestParam("busStopId") Long busStopId) {
        //Entfernen einer Haltestelle von einer Buslinie
    }
}
