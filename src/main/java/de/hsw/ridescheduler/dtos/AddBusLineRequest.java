package de.hsw.ridescheduler.dtos;

public class AddBusLineRequest {

    private String name;

    public AddBusLineRequest() {
    }

    public AddBusLineRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
