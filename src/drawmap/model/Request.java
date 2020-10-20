package drawmap.model;

import javafx.scene.paint.Color;

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

    public Intersection getPickup() {
        return pickup;
    }

    public void setPickup(Intersection pickup) {
        this.pickup = pickup;
    }

    public Intersection getDelivery() {
        return delivery;
    }

    public void setDelivery(Intersection delivery) {
        this.delivery = delivery;
    }

    public int getPickupDuration() {
        return pickupDuration;
    }

    public void setPickupDuration(int pickupDuration) {
        this.pickupDuration = pickupDuration;
    }

    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    public void setDeliveryDuration(int deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}