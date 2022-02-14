package de.hsw.ridescheduler.beans;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bus_line")
public class BusLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "busLine", cascade = CascadeType.MERGE)
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "busStop", cascade = CascadeType.MERGE)
    private List<BusStopInBusLine> busStops = new ArrayList<>(0);

    public BusLine() {
    }

    public BusLine(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public void addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
    }

    public void removeSchedule(Schedule schedule) {
        this.schedules.remove(schedule);
    }

    public List<BusStopInBusLine> getBusStops() {
        return busStops;
    }

    public void setBusStops(List<BusStopInBusLine> busStops) {
        this.busStops = busStops;
    }

    public void addBusStop(BusStopInBusLine busStop) {
        this.busStops.add(busStop);
    }

    public void removeBusStop(BusStopInBusLine busStop) {
        this.busStops.remove(busStop);
    }
}
