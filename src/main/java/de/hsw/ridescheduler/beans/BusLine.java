package de.hsw.ridescheduler.beans;

import javax.persistence.*;
import java.util.List;

@Entity
public class BusLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "hasWifi", nullable = true)
    private Boolean hasWifi;

    @OneToMany(mappedBy = "busLine", cascade = CascadeType.ALL)
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "busStop", cascade = CascadeType.ALL)
    private List<BusStopInBusLine> busStops;

    public BusLine() {
    }

    public BusLine(Long id, String name, Boolean hasWifi) {
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
        return this.name;
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
