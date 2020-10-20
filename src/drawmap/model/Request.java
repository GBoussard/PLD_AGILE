package drawmap.model;

import java.util.*;

/**
 * 
 */
public class Request {

    public Request(Intersection pickup, Intersection delivery, int pickupDuration, int deliveryDuration) {
        this.pickup = pickup;
        this.delivery = delivery;
        this.pickupDuration = pickupDuration;
        this.deliveryDuration = deliveryDuration;
    }

    /**
     * Default constructor
     */
    public Request() {
    }

    /**
     * 
     */
    private Intersection pickup;

    /**
     * 
     */
    private Intersection delivery;

    /**
     * 
     */
    private int pickupDuration;

    /**
     * 
     */
    private int deliveryDuration;


}