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
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(new File(file), new XMLParser(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clearMap() {
        map_intersection.clear();
        list_segment.clear();
    }

    public void addIntersection(Intersection e) {
        map_intersection.put(e.getId(), e);
    }

    public void addSegment(Segment e) {
        list_segment.add(e);
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
}
