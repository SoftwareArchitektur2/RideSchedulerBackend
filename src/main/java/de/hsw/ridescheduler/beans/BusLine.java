package de.hsw.ridescheduler.beans;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "bus_line", schema = "ridescheduler")
public class BusLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "busLine", cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "busLine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BusStopInBusLine> busStops = new ArrayList<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusLine busLine = (BusLine) o;
        return Objects.equals(id, busLine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, schedules, busStops);
    }
}
