package de.hsw.ridescheduler;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.Schedule;
import de.hsw.ridescheduler.dtos.BusStopInBusLineResponse;
import de.hsw.ridescheduler.dtos.ScheduleResponse;
import de.hsw.ridescheduler.services.BusLineService;
import de.hsw.ridescheduler.services.BusStopService;
import de.hsw.ridescheduler.services.ScheduleService;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        // handle data on a test case basis to have better control and avoid tests breaking on data change
        properties = {"spring.datasource.initialization-mode=never", "spring.jpa.hibernate.ddl-auto=none"})
@Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE) // allow additional method-level @Sql statements
public class ScheduleServiceTests {

    private final ScheduleService scheduleService;
    private final BusLineService busLineService;
    private final BusStopService busStopService;

    @Autowired
    public ScheduleServiceTests(ScheduleService scheduleService, BusLineService busLineService, BusStopService busStopService) {
        this.scheduleService = scheduleService;
        this.busLineService = busLineService;
        this.busStopService = busStopService;
    }

    @Test
    @Sql("/nightBus.sql")
    public void testGetScheduleById() {
        Schedule schedule = this.scheduleService.getScheduleById(0L);
        assertEquals("line1", schedule.getBusLine().getName());
    }

    @Test
    @Sql("/data.sql")
    public void testCreateAndDeleteSchedule() {
        BusStop a = new BusStop("A", true);
        a = this.busStopService.saveBusStop(a);
        BusStop b = new BusStop("B", true);
        b = this.busStopService.saveBusStop(b);
        BusStop c = new BusStop("C", true);
        c = this.busStopService.saveBusStop(c);
        BusStop d = new BusStop("D", true);
        d = this.busStopService.saveBusStop(d);
        BusLine busLine = new BusLine("Line15");
        busLine = this.busLineService.saveBusLine(busLine);
        this.busLineService.addBusStop(2L, 11L, 1);
        this.busLineService.addBusStop(2L, 12L, 2);
        this.busLineService.addBusStop(2L, 13L, 3);
        this.busLineService.addBusStop(2L, 14L, 4);

        assertEquals(4, this.scheduleService.getAllSchedules().size());
        Schedule c1 = this.scheduleService.createSchedule(2L, new Time(26000000L), 14L);
        assertEquals(5, this.scheduleService.getAllSchedules().size());

        this.scheduleService.deleteScheduleById(c1.getId());
        this.busLineService.deleteBusLineById(busLine.getId());
        this.busStopService.deleteBusStopById(a.getId());
        this.busStopService.deleteBusStopById(b.getId());
        this.busStopService.deleteBusStopById(c.getId());
        this.busStopService.deleteBusStopById(d.getId());
        assertEquals(4, this.scheduleService.getAllSchedules().size());
    }

    @Test
    @Sql("/data.sql")
    public void testGetSchedulesForBusStop() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY,8);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        Date date = cal.getTime();

        List<ScheduleResponse> schedules = this.scheduleService.getSchedulesForBusStop(5L, date, 120);
        assertEquals(4, schedules.size());
        assertEquals("StadtBus 15", schedules.get(0).getBusLine().getName());
        assertEquals("StadtBus 16", schedules.get(2).getBusLine().getName());
    }

    @Test
    @Sql("/data.sql")
    public void testChangeDestinationStop() {
        assertEquals("Münster Dingbängerweg", this.scheduleService.getScheduleById(0L).getDestinationStop().getName());
        this.scheduleService.changeDestinationStop(0L, 0L);
        assertEquals("Münster Geiststraße", this.scheduleService.getScheduleById(0L).getDestinationStop().getName());
        this.scheduleService.changeDestinationStop(0L, 10L);
        assertEquals("Münster Dingbängerweg", this.scheduleService.getScheduleById(0L).getDestinationStop().getName());
    }

    @Test
    @Sql("/data.sql")
    public void testGetBusStops() {
        List<BusStopInBusLineResponse> busStops = this.scheduleService.getBusStops(0L, 0L);
        assertEquals(11, busStops.size());
        assertEquals("Münster Geiststraße", busStops.get(0).getName());
        assertEquals("Münster Dingbängerweg", busStops.get(10).getName());

        List<BusStopInBusLineResponse> busStops2 = this.scheduleService.getBusStops(0L, 6L);
        assertEquals(5, busStops2.size());
        assertEquals("Münster Boeselagerstr.", busStops2.get(0).getName());
        assertEquals("Münster Dingbängerweg", busStops2.get(4).getName());
    }

    @Test
    @Sql("/nightBus.sql")
    public void testGetSchedulesForNightBus() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,30);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        Date date = cal.getTime();

        List<ScheduleResponse> schedules = this.scheduleService.getSchedulesForBusStop(1L, date, 120);
        assertEquals(1, schedules.size());
        assertEquals("line1", schedules.get(0).getBusLine().getName());
        Date departureTime = DateUtils.addMinutes(date, 60);
        assertEquals(departureTime, schedules.get(0).getDepartureTime());
    }
}
