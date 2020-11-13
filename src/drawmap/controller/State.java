package drawmap.controller;

import drawmap.model.CityMap;
import drawmap.model.DeliveryTour;
import drawmap.model.ComputeTour;
import drawmap.model.Request;
import javafx.util.Pair;

public interface State {
    public default boolean loadMap(Controller c){return false;}; // initialState
    public default boolean loadRequests(Controller c){return false;}; // MapLoadedState
    public default void computeTour(Controller c){}; // RequestsLoadedState
    public default void addRequest(Controller c, Pair<Double, Double> coordPickup, Pair<Double, Double> coordDelivery, Pair<Double, Double> previous, Pair<Double, Double> next, int pickupDuration, int deliveryDuration, ListOfCommands l){}; // ComputeTourState
    public default void removeRequest(Controller c, Pair<Double, Double> coord, ListOfCommands l){}; // ComputeTourState
    public default void undo(ListOfCommands l, Controller c){}; // ComputeTourState & RequestsLoadedState
    public default void redo(ListOfCommands l, Controller c){}; // // ComputeTourState & RequestsLoadedState
    public default void highlightRequestPointInMap(Controller c, String intersectionId){};
    public default void highlightRequestPointInRequestView(Controller c, String intersectionId){};
    public default void computeRoadMap(ComputeTour ct){}; // ComputeTourState




}
