package de.hsw.ridescheduler.controllers;

import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.dtos.AddBusStopRequest;
import de.hsw.ridescheduler.dtos.UpdateBusStopRequest;
import de.hsw.ridescheduler.services.BusStopService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BusStopController {

    private BusStopService busStopService;
    private ModelMapper modelMapper;

    @Autowired
    public BusStopController(BusStopService busStopService, ModelMapper modelMapper) {
        this.busStopService = busStopService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/busStops")
    public void addBusStop(@RequestBody AddBusStopRequest addBusStopRequest) {
        this.busStopService.saveBusStop(modelMapper.map(addBusStopRequest, BusStop.class));
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
