package de.hsw.ridescheduler.beans;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "busline_id", nullable = false)
    private BusLine busLine;

    @Column(name = "departuretime", nullable = false)
    private Date departureTime;

    @OneToOne
    @JoinColumn(name = "busstop_id", nullable = false)
    private BusStop destinationStop;

    public Schedule() {

    }

    public Schedule(BusLine busLine, Date departureTime, BusStop destinationStop) {
        this.busLine = busLine;
        this.departureTime = departureTime;
        this.destinationStop = destinationStop;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BusLine getBusLine() {
        return busLine;
    }

    public void setBusLine(BusLine busLine) {
        this.busLine = busLine;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public BusStop getDestinationStop() {
        return destinationStop;
    }

    public void setDestinationStop(BusStop destinationStop) {
        this.destinationStop = destinationStop;
    }
}
