package de.hsw.ridescheduler;

import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.beans.BusStopInBusLine;
import de.hsw.ridescheduler.dtos.BusLineResponse;
import de.hsw.ridescheduler.dtos.ScheduleResponse;
import de.hsw.ridescheduler.services.BusStopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BusStopServiceTests {

    private BusStopService busStopService;

    @Autowired
    public BusStopServiceTests(BusStopService busStopService) {
        this.busStopService = busStopService;
    }

    @Test
    public void testGetBusStopById() {
        assertEquals("Münster Geiststraße", busStopService.getBusStopById(0L).getName());
    }

    @Test
    public void testGetBusStopByName() {
        assertEquals(0L, busStopService.getBusStopByName("Münster Geiststraße").get().getId());
    }

    @Test
    public void testGetAllBusStops() {
        assertEquals(11, busStopService.getAllBusStops().size());
    }

    @Test
    public void testChangeName() {
        assertEquals("Münster Kolde-Ring /LVM", busStopService.getBusStopById(1L).getName());
        busStopService.changeName(1L, "new Name");
        assertEquals("new Name", busStopService.getBusStopById(1L).getName());
        busStopService.changeName(1L, "Münster Kolde-Ring /LVM");
        assertEquals("Münster Kolde-Ring /LVM", busStopService.getBusStopById(1L).getName());
    }

    @Test
    public void testChangeHasWifi() {
        assertEquals(false, busStopService.getBusStopById(5L).getHasWifi());
        busStopService.changeHasWifi(5L, true);
        assertEquals(true, busStopService.getBusStopById(5L).getHasWifi());
        busStopService.changeHasWifi(5L, false);
        assertEquals(false, busStopService.getBusStopById(5L).getHasWifi());
    }

    @Test
    public void testAddBusStop() {
        BusStop busStop = new BusStop("new BusStop", false);
        busStopService.saveBusStop(busStop);
        assertEquals("new BusStop", busStopService.getBusStopById(busStop.getId()).getName());
        busStopService.deleteBusStopById(busStop.getId());
    }

    @Test
    public void testGetBusLinesForBusStop() {
        List<BusLineResponse> busLines = busStopService.getBusLinesForBusStop(5L);
        assertEquals(2, busLines.size());
        assertEquals("StadtBus 15", busLines.get(0).getName());
        assertEquals("StadtBus 16", busLines.get(1).getName());
    }
}


