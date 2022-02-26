package de.hsw.ridescheduler.dtos;

import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.beans.Schedule;

import java.util.Date;

public class BusStopInBusLineResponse extends BusStopResponse{

    private int timeToNextStop;
    private Date arrivalTime;

    public BusStopInBusLineResponse() {
        super();
    }

    public BusStopInBusLineResponse(Long id, String name, Boolean hasWifi, int timeToNextStop, Date arrivalTime) {
        super(id, name, hasWifi);
        this.timeToNextStop = timeToNextStop;
        this.arrivalTime = arrivalTime;
    }

    public BusStopInBusLineResponse(BusStopInBusLine busStopInBusLine, Date currentTime) {
        super(busStopInBusLine.getBusStop().getId(), busStopInBusLine.getBusStop().getName(), busStopInBusLine.getBusStop().getHasWifi());
        this.timeToNextStop = busStopInBusLine.getTimeToNextStop();
        this.arrivalTime = currentTime;
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
