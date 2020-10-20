package test.model;

import drawmap.model.CityMap;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CityMapTest {

    private final CityMap map = new CityMap();

    @org.junit.jupiter.api.BeforeAll
    @org.junit.jupiter.api.Test
    void read() {
        map.read("src/test/model/ressources/mapTest.xml");

    }


    @org.junit.jupiter.api.Test
    void addIntersection() {
        map.addIntersection((long)5, 45.56, 4.021);
    }

    @org.junit.jupiter.api.Test
    void addSegment() {
        map.addSegment(203.0, (long)1, (long)5, "Rue Pierre Huchot");
    }

    @org.junit.jupiter.api.Test
    void getIntersectionById() {
        assertEquals(45.75406, map.getIntersectionById((long) 1).getLatitude());
        assertEquals(4.857418, map.getIntersectionById((long) 1).getLongitude());
    }

    @org.junit.jupiter.api.Test
    void getSegmentIterator() {
    }

    @org.junit.jupiter.api.Test
    void getIntersectionIterator() {
    }


    @org.junit.jupiter.api.Test
    void clearMap() {
        map.clearMap();
        assertEquals(false, map.getIntersectionIterator().hasNext());
        assertEquals(false, map.getSegmentIterator().hasNext());
    }
}