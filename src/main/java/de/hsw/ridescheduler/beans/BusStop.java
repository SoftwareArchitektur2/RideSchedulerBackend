package de.hsw.ridescheduler.beans;

import javax.persistence.*;
import java.util.List;

@Entity
public class BusStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "busLine", cascade = CascadeType.ALL)
    private List<BusStopInBusLine> busLines;

    public BusStop() {

    }

    public BusStop(Long id, String name, List<BusStopInBusLine> busLines) {
        this.id = id;
        this.name = name;
        this.busLines = busLines;
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

    public void addBusline(BusStopInBusLine busLine) {
        this.busLines.add(busLine);
    }

    public void removeBusline(BusStopInBusLine busLine) {
        this.busLines.remove(busLine);
    }
}
