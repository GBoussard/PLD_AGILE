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
import javafx.util.Pair;

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
        l = new ListOfCommands();
        currentState = new InitialState();

    }



    public void undo(){
        currentState.undo(l, this);
    }

    public void redo(){
        currentState.redo(l, this);
    }

    public void addRequest(Pair<Double, Double> coordPickup, Pair<Double,Double> coordDelivery, Pair<Double, Double> previous, Pair<Double, Double> next, int pickupDuration, int deliveryDuration){
        currentState.addRequest(this, coordPickup, coordDelivery, previous, next, pickupDuration, deliveryDuration , l);
    }

    public void removeRequest(Pair<Double, Double> coord){
        currentState.removeRequest(this, coord, l);
    }

    public void setMapCanvasScale(double s) {
        mainView.setMapCanvasScale(s);
    }

    public void loadMap(){
        boolean status;
        status = currentState.loadMap(this);
        if(status) {
            currentState = new MapLoadedState();
        } else {
            mainView.displayAlert("Map loading failed");
        }
    }

    public void loadRequest(){
        boolean status;
        status = currentState.loadRequests(this);
        if(status) {
            currentState = new RequestsLoadedState();
        } else {
            mainView.displayAlert("Request loading failed");
        }
    }



    public DeliveryTour getDeliveryTour() {
        return dt;
    }

    public State getCurrentState(){
        return currentState;
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
