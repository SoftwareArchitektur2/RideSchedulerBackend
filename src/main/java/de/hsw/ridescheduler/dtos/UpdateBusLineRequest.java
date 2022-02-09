package de.hsw.ridescheduler.dtos;

public class UpdateBusLineRequest {

    private String name;

    public UpdateBusLineRequest() {
    }

    public UpdateBusLineRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
