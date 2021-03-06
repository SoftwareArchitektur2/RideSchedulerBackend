package de.hsw.ridescheduler.beans;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "bus_stop", schema = "ridescheduler")
public class BusStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "hasWifi")
    private Boolean hasWifi;

    @OneToMany(mappedBy = "busStop", cascade = CascadeType.ALL, orphanRemoval = true)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusStop busStop = (BusStop) o;
        return id.equals(busStop.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, hasWifi, busLines);
    }
}
