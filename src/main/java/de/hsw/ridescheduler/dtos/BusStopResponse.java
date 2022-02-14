package de.hsw.ridescheduler.dtos;

import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;

import java.util.Date;

public class BusStopResponse {

    private String name;
    private Boolean hasWifi;
    private int timeToNextStop;
    private Date arrivalTime;

    public BusStopResponse() {
    }

    public BusStopResponse(String name, Boolean hasWifi, int timeToNextStop) {
        this.name = name;
        this.hasWifi = hasWifi;
        this.timeToNextStop = timeToNextStop;
    }

    public BusStopResponse(String name, Boolean hasWifi, int timeToNextStop, Date arrivalTime) {
        this.name = name;
        this.hasWifi = hasWifi;
        this.timeToNextStop = timeToNextStop;
        this.arrivalTime = arrivalTime;
    }

    public BusStopResponse(BusStopInBusLine busStop, Date arrivalTime) {
        BusStop bs = busStop.getBusStop();
        this.name = bs.getName();
        this.hasWifi = bs.getHasWifi();
        this.timeToNextStop = busStop.getTimeToNextStop();
        this.arrivalTime = new Date(arrivalTime.getTime());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getHasWifi() {
        return hasWifi;
    }

    public void setHasWifi(Boolean hasWifi) {
        this.hasWifi = hasWifi;
    }

    public int getTimeToNextStop() {
        return timeToNextStop;
    }

    public void setTimeToNextStop(int timeToNextStop) {
        this.timeToNextStop = timeToNextStop;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
