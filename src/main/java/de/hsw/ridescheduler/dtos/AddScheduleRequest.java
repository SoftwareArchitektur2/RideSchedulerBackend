package de.hsw.ridescheduler.dtos;

import java.sql.Time;

public class AddScheduleRequest {

    private Long busLineId;
    private Time departureTime;
    private Long destinationStopId;

    public AddScheduleRequest() {
    }

    public AddScheduleRequest(Long busLineId, Time departureTime, Long destinationStopId) {
        this.busLineId = busLineId;
        this.departureTime = departureTime;
        this.destinationStopId = destinationStopId;
    }

    public Long getBusLineId() {
        return busLineId;
    }

    public void setBusLineId(Long busLineId) {
        this.busLineId = busLineId;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    public Long getDestinationStopId() {
        return destinationStopId;
    }

    public void setDestinationStopId(Long destinationStopId) {
        this.destinationStopId = destinationStopId;
    }
}
