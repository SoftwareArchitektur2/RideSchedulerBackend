package de.hsw.ridescheduler.dtos;

public class BusStopResponse {

    private String name;
    private Boolean hasWifi;
    private int timeToNextStop;

    public BusStopResponse() {
    }

    public BusStopResponse(String name, Boolean hasWifi, int timeToNextStop) {
        this.name = name;
        this.hasWifi = hasWifi;
        this.timeToNextStop = timeToNextStop;
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
}
