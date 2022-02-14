package de.hsw.ridescheduler.dtos;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;

import java.util.Date;

public class ScheduleResponse {

    private BusLine busLine;
    private Date departureTime;
    private BusStop destinationStop;

    public ScheduleResponse() {
    }

    public ScheduleResponse(BusLine busLine, Date departureTime, BusStop destinationStop) {
        this.busLine = busLine;
        this.departureTime = departureTime;
        this.destinationStop = destinationStop;
    }

    public BusLine getBusLine() {
        return busLine;
    }

    public void setBusLine(BusLine busLine) {
        this.busLine = busLine;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public BusStop getDestinationStop() {
        return destinationStop;
    }

    public void setDestinationStop(BusStop destinationStop) {
        this.destinationStop = destinationStop;
    }
}
