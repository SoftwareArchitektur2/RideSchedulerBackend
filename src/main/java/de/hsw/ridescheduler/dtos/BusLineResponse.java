package de.hsw.ridescheduler.dtos;

import java.util.List;

public class BusLineResponse {

    private Long id;
    private String name;
    private List<BusStopResponse> busStops;

    public BusLineResponse() {
    }

    public BusLineResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public BusLineResponse(Long id, String name, List<BusStopResponse> busStops) {
        this.id = id;
        this.name = name;
        this.busStops = busStops;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BusStopResponse> getBusStops() {
        return busStops;
    }

    public void setBusStops(List<BusStopResponse> busStops) {
        this.busStops = busStops;
    }
}
