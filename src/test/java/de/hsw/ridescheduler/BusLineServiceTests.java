package de.hsw.ridescheduler;

import de.hsw.ridescheduler.beans.BusLine;
import de.hsw.ridescheduler.services.BusLineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BusLineServiceTests {

    private BusLineService busLineService;

    @Autowired
    public BusLineServiceTests(BusLineService busLineService) {
        this.busLineService = busLineService;
    }
}
