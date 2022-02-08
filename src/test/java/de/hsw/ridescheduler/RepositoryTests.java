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
import org.springframework.test.annotation.DirtiesContext;

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
    void findScheduleByDestinationStop() {
        BusLine a = new BusLine("R25");
        this.busLineRepository.save(a);
        BusStop b = new BusStop("Kirche", Boolean.TRUE);
        this.busStopRepository.save(b);
        Schedule schedule = new Schedule(a, new Date(), b);
        this.scheduleRepository.save(schedule);
        List<Schedule> schedules = this.scheduleRepository.findByDestinationStop(b);
        assert(schedules.size() == 1);
        assert(schedules.get(0).getId() == schedule.getId());
    }
}
