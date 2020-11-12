package drawmap.model;

/**
 * Model class for segments
 */
public class Segment {

    private Double length;
    private Intersection origin;
    private Intersection destination;
    private String name;

    /**
     * Default constructor
     */
    public Segment() {
    }

    /**
     * Constructor for Segment
     * @param length : segment length
     * @param origin : origin intersection
     * @param destination : destination intersection
     * @param name : segment name
     */
    public Segment(Double length, Intersection origin, Intersection destination, String name) {
        this.length = length;
        this.origin = origin;
        this.destination = destination;
        this.name = name;
    }

    /**
     * Gets length
     * @return length
     */
    public Double getLength() {
        return length;
    }

    /**
     * Sets length
     * @param length
     */
    public void setLength(Double length) {
        this.length = length;
    }

    /**
     * Gets origin intersection
     * @return origin
     */
    public Intersection getOrigin() {
        return origin;
    }

    /**
     * Sets origin intersection
     * @param origin
     */
    public void setOrigin(Intersection origin) {
        this.origin = origin;
    }

    /**
     * Gets destination intersection
     * @return destination
     */
    public Intersection getDestination() {
        return destination;
    }

    /**
     * Sets destination intersection
     * @param destination
     */
    public void setDestination(Intersection destination) {
        this.destination = destination;
    }

    /**
     * Gets name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}
