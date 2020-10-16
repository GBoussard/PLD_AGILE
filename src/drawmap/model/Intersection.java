package drawmap.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Intersection {

    private Long id;
    private Double latitude;
    private Double longitude;
    private ArrayList<Pair<Segment, Intersection>> voisins;

    public Intersection() {
    }

    public Intersection(Long id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void addVoisin(Segment s, Intersection i) {
        if(voisins.isEmpty()) {
            voisins.add(new Pair<Segment, Intersection>(s,i));
            return;
        }
        Iterator it = voisins.iterator();

        int index = -1;
        for(int j = 0; j < voisins.size(); ++j) {
            Pair<Segment, Intersection> current = voisins.get(j);

            if(current.getL().getLength() > s.getLength() && index == -1) {
                index = j;
            }

            if(current.getL().getDestination().getId() == s.getDestination().getId()
            && current.getL().getLength() < s.getLength()) {
                index = -1;
                break;
            }
            else if(current.getL().getDestination().getId() == s.getDestination().getId()
                    && current.getL().getLength() > s.getLength()) {
                voisins.remove(i);
            }
        }

        if(index != -1) {
            voisins.add(index, new Pair<Segment, Intersection>(s,i));
        }


    }
}
