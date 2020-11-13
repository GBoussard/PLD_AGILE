package drawmap.model;

import drawmap.util.XMLParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;

/**
 * City map model class
 */
public class CityMap extends Observable {

    private HashMap<Long, Intersection> map_intersection;
    private ArrayList<Segment> list_segment;

    /**
     * Default constructor
     */
    public CityMap() {
        super();
        map_intersection = new HashMap<Long, Intersection>();
        list_segment = new ArrayList<Segment>();
    }

    /**
     * Reads an XML file and stores it
     * @param file : path to the map file
     */
    public boolean read(String file) {
        this.clearMap();
        XMLParser parser = new XMLParser();

        boolean status;
        status = parser.parseMap(file, this);
        if(status) {
            setChanged();
            notifyObservers();
        }
        return status;
    }

    /**
     * Clears the current data structure
     */
    public void clearMap() {
        map_intersection.clear();
        list_segment.clear();
    }

    /**
     * Adds an intersection to the map
     * @param id : unique identifier
     * @param latitude : intersection's latitude
     * @param longitude : intersection's longitude
     */
    public void addIntersection(Long id, Double latitude, Double longitude) {

        map_intersection.put(id, new Intersection(id, latitude, longitude));
    }

    /**
     * Returns the intersection closest to the Point (longitude, latitude)
     * @param longitude X coordinate of the Point
     * @param latitude Y coordinate of the Point
     * @return Intersection
     */
    public Intersection findIntersection(Double longitude, Double latitude){
        Double bestEuclidianDist = Double.MAX_VALUE;
        Intersection result = null;
        for(Intersection i : map_intersection.values()){
            //System.out.println(i.getLatitude()+" : "+i.getLongitude());
            double tmp = Math.sqrt(Math.pow(longitude - i.getLongitude(), 2) + (Math.pow(latitude - i.getLatitude(), 2)));
            if(tmp < bestEuclidianDist){
                bestEuclidianDist = tmp;
                result = i;
            }
        }
        return result;
    }

    /**
     * Adds a segment to the map
     * @param length : segment length
     * @param id_origin : origin intersection identifier
     * @param id_destination : destionation intersection identifier
     * @param name : segment name
     */
    public void addSegment(Double length, Long id_origin, Long id_destination, String name) {
        if(!(map_intersection.containsKey(id_origin) && map_intersection.containsKey(id_destination))) return;

        Segment s = new Segment(length, map_intersection.get(id_origin), map_intersection.get(id_destination), name);
        map_intersection.get(id_origin).addNeighbour(s, map_intersection.get(id_destination));
        list_segment.add(s);
    }

    /**
     * Gets an intersection by its identifier
     * @param id : identifier of the requested intersection
     * @return Intersection
     */
    public Intersection getIntersectionById(Long id) {
        return map_intersection.get(id);
    }

    /**
     * Gets an iterator to iterate through the list of segments
     * @return Iterator
     */
    public Iterator getSegmentIterator() {
        return list_segment.iterator();
    }

    /**
     * Gets an iterator to iterate through the list of intersections
     * @return Iterator
     */
    public Iterator getIntersectionIterator() {
        return map_intersection.entrySet().iterator();
    }

    /**
     * Gets the intersection map
     * @return map_intersection
     */
    public HashMap<Long, Intersection> getListeIntersections() {
        return map_intersection;
    }

    /**
     * Gets the segment list
     * @return list_segment
     */
    public ArrayList<Segment> getListeSegments() {
        return list_segment;
    }
}
