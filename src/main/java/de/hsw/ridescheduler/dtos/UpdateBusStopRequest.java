package de.hsw.ridescheduler.dtos;

public class UpdateBusStopRequest {

    private String name;
    private Boolean hasWifi;

    public UpdateBusStopRequest() {
    }

    public UpdateBusStopRequest(String name, Boolean hasWifi) {
        this.name = name;
        this.hasWifi = hasWifi;
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
}
