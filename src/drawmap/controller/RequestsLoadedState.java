package drawmap.controller;

import drawmap.model.ComputeTour;
import drawmap.model.Request;

import java.io.File;

public class RequestsLoadedState implements State{

    @Override
    public void computeTour(Controller c){
        c.getComputeTour().computeTour();
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
            l.addCommands(new AddCommand(c.getDeliveryTour(), r));
        }
    }

    @Override
    public void loadMap(Controller c){
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
}