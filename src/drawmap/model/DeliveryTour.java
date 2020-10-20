package drawmap.model;

import java.util.*;

/**
 * 
 */
public class DeliveryTour {

    /**
     * Default constructor
     */
    public DeliveryTour() {
    }

    /**
     * 
     */
    private List<Request> requests;
    private Date departureTime;

    public Intersection getOrigin() {
        return origin;
    }

    public void setOrigin(Intersection origin) {
        this.origin = origin;
    }

    private Intersection origin;

    public void read(String filename, CityMap cm){

    }



    public void addRequest(Request r) {
        requests.add(r);
    }

    public Date getDepartureTime(){
        return  departureTime;
    }

    public void setDepartureTime(Date d){
        departureTime = d;
    }

}