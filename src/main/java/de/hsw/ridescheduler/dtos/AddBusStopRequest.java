package de.hsw.ridescheduler.dtos;

public class AddBusStopRequest {

    private String name;
    private Boolean hasWifi;

    public AddBusStopRequest() {
    }

    public AddBusStopRequest(String name, Boolean hasWifi) {
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
