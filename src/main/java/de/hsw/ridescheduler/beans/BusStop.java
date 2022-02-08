package de.hsw.ridescheduler.beans;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BusStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "hasWifi", nullable = true)
    private Boolean hasWifi;

    @OneToMany(mappedBy = "busLine", cascade = CascadeType.ALL)
    private List<BusStopInBusLine> busLines = new ArrayList<>();

    public BusStop() {

    }

    public BusStop(String name, Boolean hasWifi) {
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

    public List<BusStopInBusLine> getBusLines() {
        return busLines;
    }

    public void setBusLines(List<BusStopInBusLine> busLines) {
        this.busLines = busLines;
    }

    public void addBusLine(BusStopInBusLine busLine) {
        this.busLines.add(busLine);
    }

    public void removeBusLine(BusStopInBusLine busLine) {
        this.busLines.remove(busLine);
    }

    public Boolean getHasWifi() {
        return hasWifi;
    }

    public void setHasWifi(Boolean hasWifi) {
        this.hasWifi = hasWifi;
    }
}
