package drawmap.model;

public class Segment {

    private Double length;
    private Intersection origin;
    private Intersection destination;
    private String name;

    public Segment() {
    }

    public Segment(Double length, Intersection origin, Intersection destination, String name) {
        this.length = length;
        this.origin = origin;
        this.destination = destination;
        this.name = name;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Intersection getOrigin() {
        return origin;
    }

    public void setOrigin(Intersection origin) {
        this.origin = origin;
    }

    public Intersection getDestination() {
        return destination;
    }

    public void setDestination(Intersection destination) {
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
