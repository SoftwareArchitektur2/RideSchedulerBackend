package de.hsw.ridescheduler.dtos;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;

import java.util.Date;

public class ScheduleResponse {

    private BusLineResponse busLine;
    private Date departureTime;
    private BusStopInBusLineResponse destinationStop;

    public ScheduleResponse() {
    }

    public ScheduleResponse(BusLineResponse busLine, Date departureTime, BusStopInBusLineResponse destinationStop) {
        this.busLine = busLine;
        this.departureTime = departureTime;
        this.destinationStop = destinationStop;
    }

    public BusLineResponse getBusLine() {
        return busLine;
    }

    public void setBusLine(BusLineResponse busLine) {
        this.busLine = busLine;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public BusStopInBusLineResponse getDestinationStop() {
        return destinationStop;
    }

    public void setDestinationStop(BusStopInBusLineResponse destinationStop) {
        this.destinationStop = destinationStop;
    }
}
