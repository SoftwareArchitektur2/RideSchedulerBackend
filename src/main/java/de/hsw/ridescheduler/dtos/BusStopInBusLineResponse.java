package de.hsw.ridescheduler.dtos;

import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.beans.Schedule;

import java.util.Date;

public class BusStopInBusLineResponse extends BusStopResponse{

    private Long busStopInBusLineId;
    private int timeToNextStop;
    private Date arrivalTime;

    public BusStopInBusLineResponse() {
        super();
    }

    public BusStopInBusLineResponse(Long id, String name, Boolean hasWifi, Long busStopInBusLineId, int timeToNextStop, Date arrivalTime) {
        super(id, name, hasWifi);
        this.busStopInBusLineId = busStopInBusLineId;
        this.timeToNextStop = timeToNextStop;
        this.arrivalTime = arrivalTime;
    }

    public BusStopInBusLineResponse(BusStopInBusLine busStopInBusLine, Date currentTime) {
        super(busStopInBusLine.getBusStop().getId(), busStopInBusLine.getBusStop().getName(), busStopInBusLine.getBusStop().getHasWifi());
        this.busStopInBusLineId = busStopInBusLine.getId();
        this.timeToNextStop = busStopInBusLine.getTimeToNextStop();
        this.arrivalTime = currentTime;
    }

    public Long getBusStopInBusLineId() {
        return busStopInBusLineId;
    }

    public void setBusStopInBusLineId(Long busStopInBusLineId) {
        this.busStopInBusLineId = busStopInBusLineId;
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
