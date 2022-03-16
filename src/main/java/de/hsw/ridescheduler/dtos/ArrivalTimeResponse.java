package de.hsw.ridescheduler.dtos;

import java.util.Date;

public class ArrivalTimeResponse {

    private Date arrivalTime;

    public ArrivalTimeResponse() {
    }

    public ArrivalTimeResponse(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
}
