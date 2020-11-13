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
     * Returns the Request which pickup or delivery point is the closest to the Point (longitude, latitude)
     * @param longitude X coordinate of the Point
     * @param latitude Y coordinate of the Point
     * @return Request
     */
    public Request getNearestRequest(double longitude, double latitude){
        double bestEuclidianDist = Double.MAX_VALUE;
        Request result = null;
        for(int i = 0; i < requests.size(); ++i){
            double tmp = Math.sqrt(Math.pow(requests.get(i).getDelivery().getLongitude() - longitude, 2) + Math.pow(requests.get(i).getDelivery().getLatitude() - latitude, 2));
            double tmpbis = Math.sqrt(Math.pow(requests.get(i).getPickup().getLongitude() - longitude, 2) + Math.pow(requests.get(i).getPickup().getLatitude() - latitude, 2));
            if (tmp < bestEuclidianDist){
                bestEuclidianDist = tmp;
                result = requests.get(i);
            }
            if(tmpbis < bestEuclidianDist){
                bestEuclidianDist = tmpbis;
                result = requests.get(i);
            }
        }
        return result;
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
    public boolean read(String filepath, CityMap cm){
        this.requests = new ArrayList<Request>();
        XMLParser parser = new XMLParser();

        boolean status;
        status = parser.parseTour(filepath, this, cm);
        if(status) {
            setChanged();
            notifyObservers();
        }
        return status;
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
        setChanged();
        notifyObservers();
    }

    /**
     * Remove a request from the request list
     * @param r : request to be deleted
     */
    public void removeRequest(Request r) {
        requests.remove(r);
        setChanged();;
        notifyObservers();
    }

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

    public void removeAllRequests(){
        requests.removeAll(requests);
        origin = null;
    }

}