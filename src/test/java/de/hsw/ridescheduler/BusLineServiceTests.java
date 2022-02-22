package de.hsw.ridescheduler;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.dtos.BusStopInBusLineResponse;
import de.hsw.ridescheduler.dtos.ScheduleResponse;
import de.hsw.ridescheduler.repositorys.BusStopInBusLineRepository;
import de.hsw.ridescheduler.services.BusLineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BusLineServiceTests {

    private BusLineService busLineService;

    private BusStopInBusLineRepository repository;

    @Autowired
    public BusLineServiceTests(BusLineService busLineService, BusStopInBusLineRepository repository) {
        this.busLineService = busLineService;
        this.repository = repository;
    }

    @Test
    public void testGetAllBusLine() {
        List<BusLine> busLines = busLineService.getAllBusLines();
        assertEquals(2, busLines.size());
    }

    @Test
    public void testGetBusLineById() {
        BusLine busLine = this.busLineService.getBusLineById(0L).get();
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
    public void testChangeName() {
        assertEquals("StadtBus 15", this.busLineService.getBusLineById(0L).get().getName());
        this.busLineService.changeName(0L, "TestBusLine");
        assertEquals("TestBusLine", this.busLineService.getBusLineById(0L).get().getName());
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
