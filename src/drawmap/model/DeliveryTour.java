package drawmap.model;

import drawmap.util.XMLParser;

import java.util.*;

/**
 * Delivery tour model class
 */
public class DeliveryTour extends Observable{

    /**
     * Default constructor
     */
    public DeliveryTour() {
    }

    private List<Request> requests = new ArrayList<Request>();
    private Date departureTime;
    private Intersection origin;

    /**
     * Gets the tour origin
     * @return origin
     */
    public Intersection getOrigin() {
        return origin;
    }


    /**
     * Sets the tour origin
     * @param origin
     */
    public void setOrigin(Intersection origin) {
        this.origin = origin;
    }


    /**
     *
     * @param filepath : path to selected file
     * @param cm : CityMap object to populate
     */
    public void read(String filepath, CityMap cm){
        this.requests = new ArrayList<Request>();
        XMLParser parser = new XMLParser();
        parser.parseTour(filepath, this, cm);
        setChanged();
        notifyObservers();
    }

    /**
     * Gets the list of requests
     * @return requests : list of requests
     */
    public List<Request> getRequests() {
        return requests;
    }

    /**
     * Adds a request to the request list
     * @param r : request to be added
     */
    public void addRequest(Request r) {
        requests.add(r);
    }

    /**
     * Remove a request from the request list
     * @param r : request to be deleted
     */
    public void removeRequest(Request r) { requests.remove(r); }

    /**
     * Gets tour's departure time
     * @return departureTime
     */
    public Date getDepartureTime(){
        return  departureTime;
    }

    /**
     * Sets tour's departure time
     * @param d : new departure time
     */
    public void setDepartureTime(Date d){
        departureTime = d;
    }

    /**
     * Gets an iterator to iterate through the list of requests
     * @return requests.iterator()
     */
    public Iterator getRequestIterator() {
        return requests.iterator();
    }

}