package de.hsw.ridescheduler.beans;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "bus_stop_in_bus_line", schema = "ridescheduler")
public class BusStopInBusLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bus_stop_id", nullable = false)
    private BusStop busStop;

    @ManyToOne
    @JoinColumn(name = "bus_line_id", nullable = false)
    private BusLine busLine;

    @Column(name = "timeToNextStop", nullable = false)
    private Integer timeToNextStop;

    public BusStopInBusLine() {
    }

    public BusStopInBusLine(BusStop busStop, BusLine busLine, int timeToNextStop) {
        this.busStop = busStop;
        this.busLine = busLine;
        this.timeToNextStop = timeToNextStop;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BusStop getBusStop() {
        return busStop;
    }

    public void setBusStop(BusStop busStop) {
        this.busStop = busStop;
    }

    public BusLine getBusLine() {
        return busLine;
    }

    public void setBusLine(BusLine busLine) {
        this.busLine = busLine;
    }

    public Integer getTimeToNextStop() {
        return timeToNextStop;
    }

    public void setTimeToNextStop(Integer timeToNextStop) {
        this.timeToNextStop = timeToNextStop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusStopInBusLine that = (BusStopInBusLine) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, busStop, busLine, timeToNextStop);
    }
}
