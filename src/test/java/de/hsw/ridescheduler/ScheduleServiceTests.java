package de.hsw.ridescheduler;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.beans.Schedule;
import de.hsw.ridescheduler.dtos.BusStopResponse;
import de.hsw.ridescheduler.services.BusLineService;
import de.hsw.ridescheduler.services.BusStopService;
import de.hsw.ridescheduler.services.ScheduleService;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ScheduleServiceTests {

    private ScheduleService scheduleService;
    private BusLineService busLineService;
    private BusStopService busStopService;

    @Autowired
    public ScheduleServiceTests(ScheduleService scheduleService, BusLineService busLineService, BusStopService busStopService) {
        this.scheduleService = scheduleService;
        this.busLineService = busLineService;
        this.busStopService = busStopService;
    }

    @Test
    public void testGetScheduleById() {
        Schedule schedule = this.scheduleService.getScheduleById(0L).get();
        assertEquals(new Time(25200000L), schedule.getDepartureTime());
    }

    @Test
    public void testCreateAndDeleteSchedule() {
        BusStop a = new BusStop("A", true);
        this.busStopService.saveBusStop(a);
        BusStop b = new BusStop("B", true);
        this.busStopService.saveBusStop(b);
        BusStop c = new BusStop("C", true);
        this.busStopService.saveBusStop(c);
        BusStop d = new BusStop("D", true);
        this.busStopService.saveBusStop(d);
        BusLine busLine = new BusLine("Line15");
        this.busLineService.saveBusLine(busLine);
        this.busLineService.addBusStop(0L, 0L, 1);
        this.busLineService.addBusStop(1L, 0L, 2);
        this.busLineService.addBusStop(2L, 0L, 3);
        this.busLineService.addBusStop(3L, 0L, 4);

        assertEquals(2, this.scheduleService.getAllSchedules().size());
        this.scheduleService.createSchedule(0L, new Time(26000000L), 3L);
        assertEquals(3, this.scheduleService.getAllSchedules().size());

        this.scheduleService.deleteScheduleById(2L);
        assertEquals(2, this.scheduleService.getAllSchedules().size());
    }

    @Test
    public void testChangeDestinationStop() {
        assertEquals("Münster Dingbängerweg", this.scheduleService.getScheduleById(0L).get().getDestinationStop().getName());
        this.scheduleService.changeDestinationStop(0L, 0L);
        assertEquals("Münster Geiststraße", this.scheduleService.getScheduleById(0L).get().getDestinationStop().getName());
        this.scheduleService.changeDestinationStop(0L, 10L);
        assertEquals("Münster Dingbängerweg", this.scheduleService.getScheduleById(0L).get().getDestinationStop().getName());
    }

    @Test
    public void testGetBusStops() {
        List<BusStopResponse> busStops = this.scheduleService.getBusStops(0L, 0L);
        assertEquals(11, busStops.size());
        assertEquals("Münster Geiststraße", busStops.get(0).getName());
        assertEquals("Münster Dingbängerweg", busStops.get(10).getName());

        List<BusStopResponse> busStops2 = this.scheduleService.getBusStops(0L, 6L);
        assertEquals(5, busStops2.size());
        assertEquals("Münster Boeselagerstr.", busStops2.get(0).getName());
        assertEquals("Münster Dingbängerweg", busStops2.get(4).getName());
    }
}
