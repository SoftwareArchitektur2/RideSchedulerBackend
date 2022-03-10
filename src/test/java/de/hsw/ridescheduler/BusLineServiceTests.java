package de.hsw.ridescheduler;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.beans.Schedule;
import de.hsw.ridescheduler.dtos.BusStopInBusLineResponse;
import de.hsw.ridescheduler.dtos.ScheduleResponse;
import de.hsw.ridescheduler.repositorys.BusStopInBusLineRepository;
import de.hsw.ridescheduler.services.BusLineService;
import de.hsw.ridescheduler.services.BusStopService;
import de.hsw.ridescheduler.services.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(
        // handle data on a test case basis to have better control and avoid tests breaking on data change
        properties = {"spring.datasource.initialization-mode=never", "spring.jpa.hibernate.ddl-auto=none"})
@Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE) // allow additional method-level @Sql statements
public class BusLineServiceTests {

    private BusLineService busLineService;
    private BusStopService busStopService;
    private ScheduleService scheduleService;

    private BusStopInBusLineRepository repository;

    @Autowired
    public BusLineServiceTests(BusLineService busLineService, BusStopService busStopService, ScheduleService scheduleService, BusStopInBusLineRepository repository) {
        this.busLineService = busLineService;
        this.busStopService = busStopService;
        this.scheduleService = scheduleService;
        this.repository = repository;
    }

    @Test
    @Sql("/data.sql")
    public void testGetAllBusLine() {
        List<BusLine> busLines = busLineService.getAllBusLines();
        assertEquals(2, busLines.size());
    }

    @Test
    @Sql("/data.sql")
    public void testGetBusLineById() {
        BusLine busLine = this.busLineService.getBusLineById(0L);
        assertEquals("StadtBus 15", busLine.getName());
    }

    @Test
    public void testAddBusLine() {
        BusLine busLine = new BusLine("TestBusLine");
        BusLine busLine2 = this.busLineService.saveBusLine(busLine);
        assertEquals(1, this.busLineService.getAllBusLines().size());
        this.busLineService.deleteBusLineById(busLine2.getId());
        assertEquals(0, this.busLineService.getAllBusLines().size());
    }

    @Test
    @Sql("/busline-busstop.sql")
    public void testRemoveBusStopFromBusLine() {
        assertEquals(3, this.repository.findAll().size());
        this.busLineService.removeBusStop(1L);
        assertEquals(2, this.repository.findAll().size());
    }

    @Test
    @Sql("/busline-busstop.sql")
    public void testRemoveBusStopAfterBusLine() {
        this.scheduleService.deleteScheduleById(0L);
        this.scheduleService.deleteScheduleById(1L);
        this.busLineService.deleteBusLineById(0L);
        assertEquals(0, this.repository.findAll().size());
        this.busStopService.deleteBusStopById(1L);
        assertEquals(2, this.busStopService.getAllBusStops().size());
    }

    @Test
    @Transactional
    @Sql("/busline-busstop.sql")
    public void testSetNewBusStopAsDestinationStop() {
        BusLine busLine = this.busLineService.getBusLineById(0L);
        Schedule schedule1 = busLine.getSchedules().get(0);
        assertEquals(2L, schedule1.getDestinationStop().getId());
        BusStop busStop = new BusStop("TestBusStop", true);
        busStop = this.busStopService.saveBusStop(busStop);
        this.busLineService.addBusStop(busLine.getId(), busStop.getId(), 2);
        assertEquals(busStop, schedule1.getDestinationStop());
        Schedule schedule2 = busLine.getSchedules().get(1);
        assertEquals(0L, schedule2.getDestinationStop().getId());
    }

    @Test
    @Transactional
    @Sql("/data.sql")
    public void testAddBusStopsToBusLine() {
        assertEquals(2, this.busLineService.getAllBusLines().size());
        assertEquals(11, this.busStopService.getAllBusStops().size());

        BusLine newLine = new BusLine("TestBusLine");
        newLine = this.busLineService.saveBusLine(newLine);
        assertEquals(3, this.busLineService.getAllBusLines().size());
        Long newLineId = newLine.getId();
        assertEquals("TestBusLine", this.busLineService.getBusLineById(newLineId).getName());

        Long aId = this.busStopService.saveBusStop(new BusStop("a", Boolean.FALSE)).getId();
        Long bId = this.busStopService.saveBusStop(new BusStop("b", Boolean.FALSE)).getId();
        assertEquals(13, this.busStopService.getAllBusStops().size());
        assertEquals("a", this.busStopService.getBusStopById(aId).getName());
        assertEquals("b", this.busStopService.getBusStopById(bId).getName());

        this.busLineService.addBusStop(newLineId, aId, 2);
        this.busLineService.addBusStop(newLineId, bId, 1);
        assertEquals(bId, this.repository.findByBusLineIdAndBusStopId(newLineId, bId).get().getBusStop().getId());
        assertEquals(2, this.busLineService.getAllBusStops(newLineId).size());
        BusLine a = this.busLineService.getBusLineById(newLineId);
        assertEquals(2, a.getBusStops().size());
        assertEquals(2, this.busLineService.getAllBusStops(newLineId).size());
        assertEquals("a", this.busLineService.getAllBusStops(newLineId).get(0).getName());
        assertEquals("b", this.busLineService.getAllBusStops(newLineId).get(1).getName());

        this.busLineService.deleteBusLineById(newLineId);
        assertFalse(this.repository.findByBusLineIdAndBusStopId(newLineId, aId).isPresent());
        this.busStopService.deleteBusStopById(aId);
        this.busStopService.deleteBusStopById(bId);
        assertEquals(2, this.busLineService.getAllBusLines().size());
        assertEquals(11, this.busStopService.getAllBusStops().size());
    }

    @Test
    @Sql("/data.sql")
    public void testChangeName() {
        assertEquals("StadtBus 15", this.busLineService.getBusLineById(0L).getName());
        this.busLineService.changeName(0L, "TestBusLine");
        assertEquals("TestBusLine", this.busLineService.getBusLineById(0L).getName());
        this.busLineService.changeName(0L, "StadtBus 15");
    }

    @Test
    @Sql("/data.sql")
    public void testRemoveBusStop() {
        assertEquals(2, this.busLineService.getAllBusLines().size());
        assertEquals(11, this.busStopService.getAllBusStops().size());

        BusLine newLine = new BusLine("TestBusLine");
        newLine = this.busLineService.saveBusLine(newLine);
        assertEquals(3, this.busLineService.getAllBusLines().size());
        assertEquals("TestBusLine", this.busLineService.getBusLineById(newLine.getId()).getName());

        BusStop stopa = new BusStop("a", Boolean.FALSE);
        BusStop stopb = new BusStop("b", Boolean.FALSE);
        BusStop stopc = new BusStop("c", Boolean.FALSE);

        stopa = this.busStopService.saveBusStop(stopa);
        stopb = this.busStopService.saveBusStop(stopb);
        stopc = this.busStopService.saveBusStop(stopc);
        assertEquals(14, this.busStopService.getAllBusStops().size());
        assertEquals("a", this.busStopService.getBusStopById(stopa.getId()).getName());
        assertEquals("b", this.busStopService.getBusStopById(stopb.getId()).getName());
        assertEquals("c", this.busStopService.getBusStopById(stopc.getId()).getName());

        this.busLineService.addBusStop(newLine.getId(), stopa.getId(), 2);
        this.busLineService.addBusStop(newLine.getId(), stopb.getId(), 1);
        this.busLineService.addBusStop(newLine.getId(), stopc.getId(), 3);
        assertEquals(3, this.busLineService.getAllBusStops(newLine.getId()).size());
        assertEquals("a", this.busLineService.getAllBusStops(newLine.getId()).get(0).getName());
        assertEquals("b", this.busLineService.getAllBusStops(newLine.getId()).get(1).getName());
        assertEquals("c", this.busLineService.getAllBusStops(newLine.getId()).get(2).getName());

        this.busLineService.removeBusStop(this.repository.findByBusLineIdAndBusStopId(newLine.getId(), stopb.getId()).get().getId());
        assertEquals(2, this.busLineService.getAllBusStops(newLine.getId()).size());
        assertEquals("a", this.busLineService.getAllBusStops(newLine.getId()).get(0).getName());
        assertEquals("c", this.busLineService.getAllBusStops(newLine.getId()).get(1).getName());

        this.busLineService.deleteBusLineById(newLine.getId());
        this.busStopService.deleteBusStopById(stopa.getId());
        this.busStopService.deleteBusStopById(stopb.getId());
        this.busStopService.deleteBusStopById(stopc.getId());
        assertEquals(2, this.busLineService.getAllBusLines().size());
        assertEquals(11, this.busStopService.getAllBusStops().size());
    }

    @Test
    @Sql("/data.sql")
    public void testGetAllBusStopInBusLine() {
        List<BusStopInBusLine> busStopInBusLines = this.repository.findAll();
        assertEquals(17, busStopInBusLines.size());
    }

    @Test
    @Sql("/data.sql")
    public void testGetAllBusStopsForBusLine() {
        List<BusStopInBusLineResponse> busStops = this.busLineService.getAllBusStops(0L);
        assertEquals(11, busStops.size());
    }

    @Test
    @Sql("/data.sql")
    public void testGetAllSchedulesForBusLine() {
        List<ScheduleResponse> schedules = this.busLineService.getAllSchedules(0L);
        assertEquals(2, schedules.size());
    }
}
