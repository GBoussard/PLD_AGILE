package test.model;

import drawmap.model.*;
import org.junit.jupiter.api.TestInstance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ComputeTourTest {

    private final CityMap map = new CityMap();
    private final DeliveryTour dt = new DeliveryTour();
    private final ComputeTour ct = new ComputeTour(map, dt);

    @org.junit.jupiter.api.BeforeAll
    void read() {
        map.read("src/test/model/ressources/mapTest.xml");
        dt.read("src/test/model/ressources/deliveryTest.xml", map);

    }


    @org.junit.jupiter.api.Test
    void computeTour() {
        ct.computeTour();
        List<Segment> path = ct.getPath();

        assertEquals(1, path.get(0).getOrigin().getId());
        assertEquals(2, path.get(0).getDestination().getId());
        assertEquals(1, path.get(1).getDestination().getId());
        assertEquals(3, path.get(2).getDestination().getId());
        assertEquals(2, path.get(3).getDestination().getId());
        assertEquals(1, path.get(4).getDestination().getId());
    }


}