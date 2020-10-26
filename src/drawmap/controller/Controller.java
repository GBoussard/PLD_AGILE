package drawmap.controller;

import drawmap.model.CityMap;
import drawmap.model.ComputeTour;
import drawmap.model.DeliveryTour;
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


    public Controller(MainView mainView) {
        this.mainView = mainView;
        cm = new CityMap();
        dt = new DeliveryTour();
        ct = new ComputeTour(cm, dt);
    }

    public void setMapCanvasScale(double s) {
        mainView.setMapCanvasScale(s);
    }

    public void loadMap(){
        File map = mainView.chooseFile("Choose a map file");
        if(map != null){
            cm.read(map.getAbsolutePath());
        }
    }

    public void loadRequest(){
        File dt = mainView.chooseFile("Choose a requests file");
        if(dt != null){
            this.dt.read(dt.getAbsolutePath(), this.cm);
        }
    }

    public DeliveryTour getDeliveryTour() {
        return dt;
    }

    public CityMap getCityMap() {
        return cm;
    }

    public ComputeTour getComputeTour() {
        return ct;
    }

    public void computeTour(){
        ct.computeTour();
    }

}
