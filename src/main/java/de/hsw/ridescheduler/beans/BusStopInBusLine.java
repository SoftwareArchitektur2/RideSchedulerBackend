package de.hsw.ridescheduler.beans;

import javax.persistence.*;

@Entity
@Table(name = "bus_stop_in_bus_line")
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
    private int timeToNextStop;

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

    public int getTimeToNextStop() {
        return timeToNextStop;
    }

    public void setTimeToNextStop(int timeToNextStop) {
        this.timeToNextStop = timeToNextStop;
    }
}
