package drawmap.controller;

import drawmap.model.CityMap;
import drawmap.model.DeliveryTour;
import drawmap.model.ComputeTour;
import drawmap.model.Request;

public interface State {
    public default void loadMap(Controller c){}; // initialState
    public default void loadRequests(Controller c){}; // MapLoadedState
    public default void computeTour(Controller c){}; // RequestsLoadedState
    public default void addRequest(Controller c, Request request, ListOfCommands l){}; // ComputeTourState
    public default void removeRequest(Controller c, Request r, ListOfCommands l){}; // ComputeTourState
    public default void undo(ListOfCommands l){}; // ComputeTourState & RequestsLoadedState
    public default void redo(ListOfCommands l){}; // // ComputeTourState & RequestsLoadedState
    public default void highlightRequestPointInMap(Controller c, String intersectionId){};
    public default void highlightRequestPointInRequestView(Controller c, String intersectionId){};
    public default void computeRoadMap(ComputeTour ct){}; // ComputeTourState




}
