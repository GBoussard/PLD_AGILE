package test.model;

import drawmap.model.CityMap;
import drawmap.model.DeliveryTour;
import drawmap.model.Intersection;
import drawmap.model.Request;
import org.junit.jupiter.api.TestInstance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeliveryTourTest {

    private final CityMap map = new CityMap();
    private final DeliveryTour dt = new DeliveryTour();

    @org.junit.jupiter.api.BeforeAll
    @org.junit.jupiter.api.Test
    void read() {
        map.read("src/test/model/ressources/mapTest.xml");
        dt.read("src/test/model/ressources/deliveryTest.xml", map);
        try{
            Date departureTime = new SimpleDateFormat("H:m:s").parse("8:0:0");
            assertEquals(departureTime, dt.getDepartureTime());
            assertEquals(map.getIntersectionById((long) 1), dt.getOrigin());

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @org.junit.jupiter.api.Test
    void addRequest() {
        Request r = new Request(map.getIntersectionById((long)2), map.getIntersectionById((long)4), 200, 200);
        dt.addRequest(r);
        List<Request> lr = dt.getRequests();
        assertEquals(lr.get(lr.size() - 1), r);
    }


    @org.junit.jupiter.api.Test
    void removeRequest() {
        List<Request> lr = dt.getRequests();
        assertEquals(lr.get(lr.size() - 1), lr.get(0));
    }


    @org.junit.jupiter.api.Test
    void getIntersectionById() {
        assertEquals(45.75406, map.getIntersectionById((long) 1).getLatitude());
        assertEquals(4.857418, map.getIntersectionById((long) 1).getLongitude());
    }

}