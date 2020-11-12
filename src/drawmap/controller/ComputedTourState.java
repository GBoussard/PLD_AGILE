package drawmap.controller;

import drawmap.model.ComputeTour;
import drawmap.model.Request;

import java.io.File;

public class ComputedTourState implements State{

    @Override
    public void computeRoadMap(ComputeTour ct){

    }

    @Override
    public void undo(ListOfCommands l){
        l.undo();
    }

    @Override
    public void redo(ListOfCommands l){
        l.redo();
    }

    @Override
    public void addRequest(Controller c, Request r, ListOfCommands l){
        if(r != null){
            l.addCommands(new AddCommand(c.getDeliveryTour(), r));
        }
    }

    @Override
    public void removeRequest(Controller c, Request r, ListOfCommands l){
        if(r != null){
            l.addCommands(new ReverseCommand(new AddCommand(c.getDeliveryTour(), r)));
        }
    }

    @Override
    public void loadMap(Controller c){
        c.getDeliveryTour().removeAllRequests();
        c.getComputeTour().setComputed(false);
        File map = c.getMainView().chooseFile("Choose a map file");
        if(map != null) {
            c.getCityMap().read(map.getAbsolutePath());
        }
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
    public void loadRequests(Controller c){
        c.getComputeTour().setComputed(false);
        File dt = c.getMainView().chooseFile("Choose a requests file");
        if(dt != null){
            c.getDeliveryTour().read(dt.getAbsolutePath(), c.getCityMap());
        }
    }
}