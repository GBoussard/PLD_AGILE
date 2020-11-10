package drawmap.controller;

import drawmap.model.CityMap;
import drawmap.model.ComputeTour;
import drawmap.model.DeliveryTour;
import drawmap.model.Request;
import drawmap.view.MainView;
import drawmap.view.MapCanvas;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {


    private CityMap cm;
    private DeliveryTour dt;
    private ComputeTour ct;
    private MainView mainView;
    private State currentState;
    private ListOfCommands l;


    public Controller(MainView mainView) {
        this.mainView = mainView;
        cm = new CityMap();
        dt = new DeliveryTour();
        ct = new ComputeTour(cm, dt);
        currentState = new InitialState();
    }



    public void undo(){
        currentState.undo(l);
    }

    public void redo(){
        currentState.redo(l);
    }

    public void addRequest(Request request){
        currentState.addRequest(this, request, l);
    }

    public void removeRequest(Request request){
        currentState.removeRequest(this, request, l);
    }

    public void setMapCanvasScale(double s) {
        mainView.setMapCanvasScale(s);
    }

    public void loadMap(){
        currentState.loadMap(this);
        currentState = new MapLoadedState();
    }

    public void loadRequest(){
        currentState.loadRequests(this);
        currentState = new RequestsLoadedState();
    }



    public DeliveryTour getDeliveryTour() {
        return dt;
    }

    public MainView getMainView(){
        return mainView;
    }

    public CityMap getCityMap() {
        return cm;
    }

    public ComputeTour getComputeTour() {
        return ct;
    }

    public void computeTour(){
        currentState.computeTour(this);
        currentState = new ComputedTourState();
    }

    public void focusClickedRequestPointInMap(String intersectionId) {
        currentState.highlightRequestPointInMap(this, intersectionId);
    }

    public void focusClickedRequestPointInRequestView(String intersectionId) {
        currentState.highlightRequestPointInRequestView(this, intersectionId);
    }
}
