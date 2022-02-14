package de.hsw.ridescheduler.controllers;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.dtos.*;
import de.hsw.ridescheduler.exceptions.BusLineNotExistsException;
import de.hsw.ridescheduler.exceptions.BusStopNotExistsException;
import de.hsw.ridescheduler.services.BusLineService;
import de.hsw.ridescheduler.services.BusStopService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/busLines")
    public List<BusLineResponse> getAllBuslines() {
        List<BusLine> busLines = busLineService.getAllBusLines();
        List<BusLineResponse> busLineResponses = new ArrayList<>();
        this.modelMapper.map(busLines, busLineResponses);
        return busLineResponses;
    }

    @PostMapping("/buslines")
    public void addBusline(@RequestBody AddBusLineRequest addBusLineRequest) {
        this.busLineService.saveBusLine(this.modelMapper.map(addBusLineRequest, BusLine.class));
    }

    @PatchMapping("/busLines/{id}")
    public void updateBusline(@RequestParam("id") Long id, @RequestBody UpdateBusLineRequest updateBusLineRequest) {
        BusLine busLine = this.busLineService.getBusLineById(id)
                .orElseThrow(() -> new BusLineNotExistsException(id));
        if(updateBusLineRequest.getName() != null) {
            busLine.setName(updateBusLineRequest.getName());
            this.busLineService.saveBusLine(busLine);
        }
    }

    @DeleteMapping("/busLines/{id}")
    public void removeBusLine(@RequestParam Long busLineId) {
        if(this.busLineService.getBusLineById(busLineId).isPresent()) {
            this.busLineService.deleteBusLineById(busLineId);
        } else {
            throw new BusLineNotExistsException(busLineId);
        }
    }

    @GetMapping("/busLines/{id}/busStops")
    public List<BusStopResponse> getBusStops(@RequestParam Long busLineId) {
       return this.busLineService.getAllBusStops(busLineId);
    }

    @PostMapping("/busLines/{id}/busStops")
    public void addBusStop(@RequestParam Long busLineId, @RequestBody BusStopRequest busStopRequest) {
        this.busLineService.addBusStop(busLineId, busStopRequest.getId(), busStopRequest.getTimeToNextStop());
    }

    @DeleteMapping("/busLines/{busLineId}/busStops/{busStopId}")
    public void removeBusStop(@RequestParam("busLineId") Long busLineId, @RequestParam("busStopId") Long busStopId) {
        this.busLineService.removeBusStop(busStopId, busLineId);
    }
}
