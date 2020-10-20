package drawmap.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Intersection {

    private Long id;
    private Double latitude;
    private Double longitude;
    private ArrayList<Pair<Segment, Intersection>> voisins = new ArrayList<Pair<Segment, Intersection>>();

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

            // 1: getting the right index to put the new intersection at, so the list is still sorted.
            if(current.getL().getLength() > s.getLength() && index == -1) {
                index = j;
                // no break because we want to check if there is any redundant neighbourhood relationship
                // (in that case it will be deleted as it will be a longer one).
            } else if(j == voisins.size() - 1 && index == -1) {
                index = voisins.size();
            }

            // 2: if there is a shorter redundancy, the neighborhood relationship won't be added.
            if(current.getL().getDestination().getId() == s.getDestination().getId()
            && current.getL().getLength() < s.getLength()) {
                index = -1;
                break;
            }

            // 3 : if there is a longer redundancy, we delete it.
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
