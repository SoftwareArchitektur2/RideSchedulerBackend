package de.hsw.ridescheduler;

import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.dtos.BusLineResponse;
import de.hsw.ridescheduler.services.BusStopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        // handle data on a test case basis to have better control and avoid tests breaking on data change
        properties = {"spring.datasource.initialization-mode=never", "spring.jpa.hibernate.ddl-auto=none"})
@Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE) // allow additional method-level @Sql statements
public class BusStopServiceTests {

    private final BusStopService busStopService;

    @Autowired
    public BusStopServiceTests(BusStopService busStopService) {
        this.busStopService = busStopService;
    }

    @Test
    @Sql("/data.sql")
    public void testGetBusStopById() {
        assertEquals("Münster Geiststraße", busStopService.getBusStopById(0L).getName());
    }

    @Test
    @Sql("/data.sql")
    public void testGetBusStopByName() {
        assertEquals(0L, busStopService.getBusStopByName("Münster Geiststraße").get().getId());
    }

    @Test
    @Sql("/data.sql")
    public void testGetAllBusStops() {
        assertEquals(11, busStopService.getAllBusStops().size());
    }

    @Test
    @Sql("/data.sql")
    public void testChangeName() {
        assertEquals("Münster Kolde-Ring /LVM", busStopService.getBusStopById(1L).getName());
        busStopService.changeName(1L, "new Name");
        assertEquals("new Name", busStopService.getBusStopById(1L).getName());
        busStopService.changeName(1L, "Münster Kolde-Ring /LVM");
        assertEquals("Münster Kolde-Ring /LVM", busStopService.getBusStopById(1L).getName());
    }

    @Test
    @Sql("/data.sql")
    public void testChangeHasWifi() {
        assertEquals(false, busStopService.getBusStopById(5L).getHasWifi());
        busStopService.changeHasWifi(5L, true);
        assertEquals(true, busStopService.getBusStopById(5L).getHasWifi());
        busStopService.changeHasWifi(5L, false);
        assertEquals(false, busStopService.getBusStopById(5L).getHasWifi());
    }

    @Test
    @Sql("/data.sql")
    public void testAddBusStop() {
        BusStop busStop = new BusStop("new BusStop", false);
        busStopService.saveBusStop(busStop);
        assertEquals("new BusStop", busStopService.getBusStopById(busStop.getId()).getName());
        busStopService.deleteBusStopById(busStop.getId());
    }

    @Test
    @Sql("/data.sql")
    public void testGetBusLinesForBusStop() {
        List<BusLineResponse> busLines = busStopService.getBusLinesForBusStop(5L);
        assertEquals(2, busLines.size());
        assertEquals("StadtBus 15", busLines.get(0).getName());
        assertEquals("StadtBus 16", busLines.get(1).getName());
    }
}


