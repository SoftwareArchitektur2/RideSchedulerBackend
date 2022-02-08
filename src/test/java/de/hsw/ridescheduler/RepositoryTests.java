package de.hsw.ridescheduler;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.Schedule;
import de.hsw.ridescheduler.repositorys.BusLineRepository;
import de.hsw.ridescheduler.repositorys.BusStopRepository;
import de.hsw.ridescheduler.repositorys.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class RepositoryTests {

    private BusLineRepository busLineRepository;

    private BusStopRepository busStopRepository;

    private ScheduleRepository scheduleRepository;

    @Autowired
    public RepositoryTests(BusLineRepository busLineRepository, BusStopRepository busStopRepository, ScheduleRepository scheduleRepository) {
        this.busLineRepository = busLineRepository;
        this.busStopRepository = busStopRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Test
    void addBusStops() {
        BusStop a = new BusStop("Kirche", Boolean.TRUE);
        BusStop b = new BusStop("Krankenhaus", Boolean.FALSE);

        this.busStopRepository.save(a);
        this.busStopRepository.save(b);

        List<BusStop> busStops = new ArrayList<>();
        this.busStopRepository.findAll().forEach(busStops::add);
        assert(busStops.size() == 2);
        assert(busStops.get(0).getName().equals("Kirche"));
        assert(busStops.get(1).getName().equals("Krankenhaus"));
    }

    @Test
    void addBusLines() {
        BusLine a = new BusLine("R25");
        BusLine b = new BusLine("R26");

        this.busLineRepository.save(a);
        this.busLineRepository.save(b);

        List<BusLine> busLines = new ArrayList<>();
        this.busLineRepository.findAll().forEach(busLines::add);
        assert(busLines.size() == 2);
        assert(busLines.get(0).getName().equals("R25"));
        assert(busLines.get(1).getName().equals("R26"));
    }

    @Test
    void addSchedule() {
        BusLine a = new BusLine("R25");
        this.busLineRepository.save(a);
        BusStop b = new BusStop("Kirche", Boolean.TRUE);
        this.busStopRepository.save(b);
        Schedule schedule = new Schedule(a, new Date(), b);
        this.scheduleRepository.save(schedule);

        assert (this.scheduleRepository.findById(schedule.getId()).isPresent());
    }

    @Test
    void findBusStopById() {
        BusStop a = new BusStop("Kirche", Boolean.TRUE);
        this.busStopRepository.save(a);
        Optional<BusStop> b = this.busStopRepository.findById(a.getId());
        assert(b.isPresent());
        assert (b.get().getName().equals("Kirche"));
    }

    @Test
    void findBusLineById() {
        BusLine a = new BusLine("R25");
        this.busLineRepository.save(a);
        Optional<BusLine> b = this.busLineRepository.findById(a.getId());
        assert(b.isPresent());
        assert (b.get().getName().equals("R25"));
    }

    @Test
    void findScheduleById() {
        BusLine a = new BusLine("R25");
        this.busLineRepository.save(a);
        BusStop b = new BusStop("Kirche", Boolean.TRUE);
        this.busStopRepository.save(b);
        Schedule schedule = new Schedule(a, new Date(), b);
        this.scheduleRepository.save(schedule);
        Optional<Schedule> c = this.scheduleRepository.findById(schedule.getId());
        assert(c.isPresent());
        assert (c.get().getBusLine().getName().equals("R25"));
    }

    @Test
    void findBusStopByName() {
        BusStop a = new BusStop("Kirche", Boolean.TRUE);
        this.busStopRepository.save(a);
        BusStop b = this.busStopRepository.findByName("Kirche");
        assert(b.getId() == a.getId());
    }

    @Test
    void findBusLineByName() {
        BusLine a = new BusLine("R25");
        this.busLineRepository.save(a);
        BusLine b = this.busLineRepository.findByName("R25");
        assert(b.getId() == a.getId());
    }

    @Test
    void findScheduleByDestinationStop() {
        BusLine a = new BusLine("R25");
        this.busLineRepository.save(a);
        BusStop b = new BusStop("Kirche", Boolean.TRUE);
        this.busStopRepository.save(b);
        Schedule schedule = new Schedule(a, new Date(), b);
        this.scheduleRepository.save(schedule);
        List<Schedule> schedules = new ArrayList<>();
        this.scheduleRepository.findByDestinationStop(b).forEach(schedules::add);
        assert(schedules.size() == 1);
        assert(schedules.get(0).getId() == schedule.getId());
    }
}
