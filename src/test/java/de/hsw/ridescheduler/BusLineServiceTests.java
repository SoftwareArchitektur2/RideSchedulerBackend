package de.hsw.ridescheduler;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.dtos.BusStopInBusLineResponse;
import de.hsw.ridescheduler.dtos.ScheduleResponse;
import de.hsw.ridescheduler.repositorys.BusStopInBusLineRepository;
import de.hsw.ridescheduler.services.BusLineService;
import de.hsw.ridescheduler.services.BusStopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BusLineServiceTests {

    private BusLineService busLineService;
    private BusStopService busStopService;

    private BusStopInBusLineRepository repository;

    @Autowired
    public BusLineServiceTests(BusLineService busLineService, BusStopService busStopService, BusStopInBusLineRepository repository) {
        this.busLineService = busLineService;
        this.busStopService = busStopService;
        this.repository = repository;
    }

    @Test
    public void testGetAllBusLine() {
        List<BusLine> busLines = busLineService.getAllBusLines();
        assertEquals(2, busLines.size());
    }

    @Test
    public void testGetBusLineById() {
        BusLine busLine = this.busLineService.getBusLineById(0L);
        assertEquals("StadtBus 15", busLine.getName());
    }

    @Test
    public void testAddBusLine() {
        BusLine busLine = new BusLine("TestBusLine");
        BusLine busLine2 = this.busLineService.saveBusLine(busLine);
        assertEquals(3, this.busLineService.getAllBusLines().size());
        this.busLineService.deleteBusLineById(busLine2.getId());
    }

    @Test
    @Transactional
    public void testAddBusStopsToBusLine() {
        assertEquals(2, this.busLineService.getAllBusLines().size());
        assertEquals(11, this.busStopService.getAllBusStops().size());

        BusLine newLine = new BusLine("TestBusLine");
        newLine = this.busLineService.saveBusLine(newLine);
        assertEquals(3, this.busLineService.getAllBusLines().size());
        assertEquals("TestBusLine", this.busLineService.getBusLineById(newLine.getId()).getName());

        BusStop stopa = new BusStop("a", Boolean.FALSE);
        BusStop stopb = new BusStop("b", Boolean.FALSE);
        stopa = this.busStopService.saveBusStop(stopa);
        stopb = this.busStopService.saveBusStop(stopb);
        assertEquals(13, this.busStopService.getAllBusStops().size());
        assertEquals("a", this.busStopService.getBusStopById(stopa.getId()).getName());
        assertEquals("b", this.busStopService.getBusStopById(stopb.getId()).getName());

        this.busLineService.addBusStop(newLine.getId(), stopa.getId(), 2);
        this.busLineService.addBusStop(newLine.getId(), stopb.getId(), 1);
        assertEquals(2, this.busLineService.getBusLineById(newLine.getId()).getBusStops().size());
        assertEquals(2, this.busLineService.getAllBusStops(newLine.getId()).size());
        assertEquals("a", this.busLineService.getAllBusStops(newLine.getId()).get(0).getName());
        assertEquals("b", this.busLineService.getAllBusStops(newLine.getId()).get(1).getName());

        this.busLineService.deleteBusLineById(newLine.getId());
        this.busStopService.deleteBusStopById(stopa.getId());
        this.busStopService.deleteBusStopById(stopb.getId());
        assertEquals(2, this.busLineService.getAllBusLines().size());
        assertEquals(11, this.busStopService.getAllBusStops().size());
    }

    @Test
    public void testChangeName() {
        assertEquals("StadtBus 15", this.busLineService.getBusLineById(0L).getName());
        this.busLineService.changeName(0L, "TestBusLine");
        assertEquals("TestBusLine", this.busLineService.getBusLineById(0L).getName());
        this.busLineService.changeName(0L, "StadtBus 15");
    }

    @Test
    public void testGetAllBusStopInBusLine() {
        List<BusStopInBusLine> busStopInBusLines = this.repository.findAll();
        assertEquals(17, busStopInBusLines.size());
    }

    @Test
    public void testGetAllBusStopsForBusLine() {
        List<BusStopInBusLineResponse> busStops = this.busLineService.getAllBusStops(0L);
        assertEquals(11, busStops.size());
    }

    @Test
    public void testGetAllSchedulesForBusLine() {
        List<ScheduleResponse> schedules = this.busLineService.getAllSchedules(0L);
        assertEquals(2, schedules.size());
    }
}
