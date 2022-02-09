package de.hsw.ridescheduler.dtos;

import org.springframework.lang.NonNull;

public class BusStopRequest {

    @NonNull
    private Long id;
    @NonNull
    private int timeToNextStop;

    public BusStopRequest() {
    }

    public BusStopRequest(Long id, int timeToNextStop) {
        this.id = id;
        this.timeToNextStop = timeToNextStop;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTimeToNextStop() {
        return timeToNextStop;
    }

    public void setTimeToNextStop(int timeToNextStop) {
        this.timeToNextStop = timeToNextStop;
    }
}
