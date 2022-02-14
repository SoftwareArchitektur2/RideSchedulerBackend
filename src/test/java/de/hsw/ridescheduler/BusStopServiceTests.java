package de.hsw.ridescheduler;

import de.hsw.ridescheduler.beans.BusStop;
import de.hsw.ridescheduler.services.BusStopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BusStopServiceTests {

    private BusStopService busStopService;

    @Autowired
    public BusStopServiceTests(BusStopService busStopService) {
        this.busStopService = busStopService;
    }

    @Test
    public void testGetBusStopById() {
        assert (busStopService.getBusStopById(Long.valueOf(0)).get().getName().equals("Münster Geiststraße"));
    }

    @Test
    public void testGetBusStopByName() {
        assert (busStopService.getBusStopByName("Münster Geiststraße").get().getId().equals(Long.valueOf(0)));
    }

    @Test
    public void testGetAllBusStops() {
        assert (busStopService.getAllBusStops().size() == 11);
    }

    @Test
    public void testChangeName() {
        busStopService.changeName(Long.valueOf(1), "new Name");
        assert (busStopService.getBusStopById(Long.valueOf(1)).get().getName().equals("new Name"));
        busStopService.changeName(Long.valueOf(1), "Münster Kolde-Ring /LVM");
    }
    
}
