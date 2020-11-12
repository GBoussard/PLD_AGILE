package drawmap.model;

import javafx.scene.paint.Color;

import java.util.*;

/**
 * Model class for requests
 */
public class Request {


    /**
     * Constructor for Requests
     * @param pickup : pickup intersection
     * @param delivery : delivery intersection
     * @param pickupDuration : pickup duration (in seconds)
     * @param deliveryDuration : delivery duration (in seconds)
     */
    public Request(Intersection pickup, Intersection delivery, int pickupDuration, int deliveryDuration) {
        this.pickup = pickup;
        this.delivery = delivery;
        this.pickupDuration = pickupDuration;
        this.deliveryDuration = deliveryDuration;

        this.color = Color.hsb(new Random().nextInt(60)*6, 1.0,Math.random()/2.0 + 0.5);
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

    /**
     *
     */
    private Color color;

    /**
     * Gets pickup intersection
     * @return pickup
     */
    public Intersection getPickup() {
        return pickup;
    }

    /**
     * Sets pickup intersection
     * @param pickup
     */
    public void setPickup(Intersection pickup) {
        this.pickup = pickup;
    }

    /**
     * Get delivery intersection
     * @return delivery
     */
    public Intersection getDelivery() {
        return delivery;
    }

    /**
     * Sets delivery intersection
     * @param delivery
     */
    public void setDelivery(Intersection delivery) {
        this.delivery = delivery;
    }

    /**
     * Gets pickup duration (in seconds)
     * @return pickupDuration
     */
    public int getPickupDuration() {
        return pickupDuration;
    }

    /**
     * Sets pickup duration
     * @param pickupDuration
     */
    public void setPickupDuration(int pickupDuration) {
        this.pickupDuration = pickupDuration;
    }

    /**
     * Gets delivery duration
     * @return deliveryDuration
     */
    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    /**
     * Sets delivery duration
     * @param deliveryDuration
     */
    public void setDeliveryDuration(int deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }

    /**
     * Gets color
     * @return color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets color
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }
}