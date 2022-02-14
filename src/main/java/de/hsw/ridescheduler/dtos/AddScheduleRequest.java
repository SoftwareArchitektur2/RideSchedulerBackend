package de.hsw.ridescheduler.dtos;

import java.util.Date;

public class AddScheduleRequest {

    private Long busLineId;
    private Date departureTime;
    private Long destinationStopId;

    public AddScheduleRequest() {
    }

    public AddScheduleRequest(Long busLineId, Date departureTime, Long destinationStopId) {
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

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Long getDestinationStopId() {
        return destinationStopId;
    }

    public void setDestinationStopId(Long destinationStopId) {
        this.destinationStopId = destinationStopId;
    }
}
