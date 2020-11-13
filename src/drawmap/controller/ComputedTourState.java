package drawmap.controller;

import drawmap.model.ComputeTour;
import drawmap.model.Intersection;
import drawmap.model.Request;
import javafx.util.Pair;

import java.io.File;

public class ComputedTourState implements State{

    @Override
    public void computeRoadMap(ComputeTour ct){

    }

    @Override
    public void computeTour(Controller c){
        c.getComputeTour().computeTour();
    }

    @Override
    public void undo(ListOfCommands l, Controller c){
        l.undo();
        computeTour(c);
    }

    @Override
    public void redo(ListOfCommands l, Controller c){
        l.redo();
        computeTour(c);
    }

    @Override
    public void addRequest(Controller c, Pair<Double, Double> coordPickup, Pair<Double, Double> coordDelivery, Pair<Double, Double> previous, Pair<Double, Double> next, int pickupDuration, int DeliveryDuration, ListOfCommands l){
        Intersection pickup = c.getComputeTour().getCityMap().findIntersection(coordPickup.getKey(), coordPickup.getValue());
        Intersection delivery = c.getComputeTour().getCityMap().findIntersection(coordDelivery.getKey(), coordDelivery.getValue());
        /*Intersection previousInter = c.getComputeTour().getNearestIntersection(previous);
        Intersection nextInter = c.getComputeTour().getNearestIntersection(next);*/
        Request r = new Request(pickup, delivery, pickupDuration*60, DeliveryDuration*60);
        if(r != null){
            l.addCommands(new AddCommand(c.getComputeTour().getDeliveryTour(), r));
            computeTour(c);
        }
    }

    @Override
    public void removeRequest(Controller c, Pair<Double, Double> coord, ListOfCommands l){
        Request r = c.getComputeTour().getDeliveryTour().getNearestRequest(coord.getKey(), coord.getValue());
        if(r != null){
            l.addCommands(new ReverseCommand(new AddCommand(c.getComputeTour().getDeliveryTour(), r)));
            computeTour(c);
        }
    }

    @Override
    public boolean loadMap(ListOfCommands l, Controller c){
        c.getDeliveryTour().removeAllRequests();
        c.getComputeTour().setComputed(false);
        File map = c.getMainView().chooseFile("Choose a map file");

        boolean status = false;
        if(map != null) {
            status = c.getCityMap().read(map.getAbsolutePath());
            if(status)  {
                l.getListCommands().clear();
                l.setI(-1);
            }
        }
        return status;
    }



    @Override
    public void highlightRequestPointInRequestView(Controller c, String intersectionId){
        c.getMainView().focusClickedRequestInRequestView(intersectionId);
    }

    @Override
    public void highlightRequestPointInMap(Controller c, String intersectionId){
        c.getMainView().focusClickedRequestInMap(intersectionId);
    }

    @Override
    public boolean loadRequests(ListOfCommands l, Controller c){
        c.getComputeTour().setComputed(false);
        File dt = c.getMainView().chooseFile("Choose a requests file");

        boolean status = false;
        if(dt != null){
            status = c.getDeliveryTour().read(dt.getAbsolutePath(), c.getCityMap());
            if(status)  {
                l.getListCommands().clear();
                l.setI(-1);
            }
        }

        return status;
    }
}