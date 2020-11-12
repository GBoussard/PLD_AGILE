package drawmap.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Model class for intersections
 */
public class Intersection {

    private Long id;
    private Double latitude;
    private Double longitude;
    private ArrayList<Pair<Segment, Intersection>> neighbours = new ArrayList<Pair<Segment, Intersection>>();

    /**
     * Default constructor
     */
    public Intersection() {
    }

    /**
     * Constructor for Intersection
     * @param id : unique identifier
     * @param latitude : intersection latitude
     * @param longitude : intersection longitude
     */
    public Intersection(Long id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets identifier
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets identifier
     * @param id : new identifier
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets latitude in decimal format
     * @return latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Sets latitude
     * @param latitude : in decimal format
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets longitude in decimal format
     * @return longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Sets longitude
     * @param longitude : in decimal format
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets neighbour list
     * @return neighbours
     */
    public ArrayList<Pair<Segment, Intersection>> getNeighbours() {
        return neighbours;
    }

    /**
     * Adds a neighbour
     * @param s : segment between this and i
     * @param i : neighbour intersection
     */
    public void addNeighbour(Segment s, Intersection i) {
        if(neighbours.isEmpty()) {
            neighbours.add(new Pair<Segment, Intersection>(s,i));
            return;
        }
        Iterator it = neighbours.iterator();

        int index = -1;
        for(int j = 0; j < neighbours.size(); ++j) {
            Pair<Segment, Intersection> current = neighbours.get(j);

            // 1: getting the right index to put the new intersection at, so the list is still sorted.
            if(current.getKey().getLength() > s.getLength() && index == -1) {
                index = j;
                // no break because we want to check if there is any redundant neighbourhood relationship
                // (in that case it will be deleted as it will be a longer one).
            } else if(j == neighbours.size() - 1 && index == -1) {
                index = neighbours.size();
            }

            // 2: if there is a shorter redundancy, the neighborhood relationship won't be added.
            if(current.getKey().getDestination().getId() == s.getDestination().getId()
            && current.getKey().getLength() < s.getLength()) {
                index = -1;
                break;
            }

            // 3 : if there is a longer redundancy, we delete it.
            else if(current.getKey().getDestination().getId() == s.getDestination().getId()
                    && current.getKey().getLength() > s.getLength()) {
                neighbours.remove(current);
            }
        }

        if(index != -1) {
            neighbours.add(index, new Pair<Segment, Intersection>(s,i));
        }


    }
}
