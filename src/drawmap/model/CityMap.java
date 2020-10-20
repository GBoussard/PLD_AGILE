package drawmap.model;

import drawmap.util.XMLParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;

public class CityMap {

    private HashMap<Long, Intersection> map_intersection;
    private ArrayList<Segment> list_segment;

    public CityMap() {
        map_intersection = new HashMap<Long, Intersection>();
        list_segment = new ArrayList<Segment>();
    }

    public void read(String file) {
        XMLParser parser = new XMLParser(this);
        parser.parseMap(file);
    }

    public void clearMap() {
        map_intersection.clear();
        list_segment.clear();
    }

    public void addIntersection(Long id, Double latitude, Double longitude) {

        map_intersection.put(id, new Intersection(id, latitude, longitude));
    }

    public void addSegment(Double length, Long id_origin, Long id_destination, String name) {
        if(!(map_intersection.containsKey(id_origin) && map_intersection.containsKey(id_destination))) return;

        Segment s = new Segment(length, map_intersection.get(id_origin), map_intersection.get(id_destination), name);
        map_intersection.get(id_origin).addVoisin(s, map_intersection.get(id_destination));
        list_segment.add(s);
    }

    public Intersection getIntersectionById(Long id) {
        return map_intersection.get(id);
    }

    public Iterator getSegmentIterator() {
        return list_segment.iterator();
    }
    public Iterator getIntersectionIterator() {
        return map_intersection.entrySet().iterator();
    }
    public HashMap<Long, Intersection> getListeIntersections() {
        return map_intersection;
    }
    public ArrayList<Segment> getListeSegments() {
        return list_segment;
    }
}
