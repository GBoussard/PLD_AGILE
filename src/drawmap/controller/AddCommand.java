package drawmap.controller;

import drawmap.model.DeliveryTour;
import drawmap.model.Request;

public class AddCommand implements Command{
    private DeliveryTour dt;
    private Request request;


    public AddCommand(DeliveryTour dt, Request request){
        this.dt = dt;
        this.request = request;
    }
    @Override
    public void doCommand(){
        this.dt.addRequest(this.request);
    }

    @Override
    public void undoCommand(){
        this.dt.removeRequest(this.request);
    }
}