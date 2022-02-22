package de.hsw.ridescheduler.dtos;

public class BusStopResponse {

    private Long id;
    private String name;
    private Boolean hasWifi;

    public BusStopResponse() {
    }

    public BusStopResponse(Long id, String name, Boolean hasWifi) {
        this.id = id;
        this.name = name;
        this.hasWifi = hasWifi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
