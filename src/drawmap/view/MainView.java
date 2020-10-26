package drawmap.view;

import drawmap.controller.Controller;
import drawmap.model.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

public class MainView extends Application {

    private Stage stage;
    private Controller controller;
    private MapCanvas mapCanvas;

    @Override
    public void start(Stage stage) {

        this.stage = stage;
        this.controller = new Controller(this);

        initUI(stage);

    }


    private void initUI(Stage stage) {

        Pane root = new HBox();



        mapCanvas = new MapCanvas(controller.getCityMap(), controller.getDeliveryTour(), controller.getComputeTour(),1200, 1000);
        root.getChildren().add(mapCanvas);

        Slider slider = new Slider(0.02, 0.05,0.05);
        slider.setOrientation(Orientation.VERTICAL);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //     System.out.println(newValue.doubleValue());
                controller.setMapCanvasScale(newValue.doubleValue());
            }
        });

        root.getChildren().add(slider);

        VBox textualView = new VBox();

        HBox loadBox = new HBox(10);
        textualView.getChildren().add(loadBox);
        VBox requestBox = new VBox();
        textualView.getChildren().add(requestBox);

        loadBox.setPrefSize(400,200);
        Button but_map = new Button("Load a map");
        but_map.setOnAction(e -> {

            controller.loadMap();

        });
        loadBox.getChildren().add(but_map);
        but_map.prefHeightProperty().bind(loadBox.heightProperty());
        but_map.prefWidthProperty().bind(loadBox.widthProperty().divide(3));

        Button but_req = new Button("Load a request file");
        but_req.setOnAction(e -> {

            controller.loadRequest();

        });
        loadBox.getChildren().add(but_req);
        but_req.prefHeightProperty().bind(loadBox.heightProperty());
        but_req.prefWidthProperty().bind(loadBox.widthProperty().divide(3));

        Button but_comp = new Button("Compute tour");
        but_comp.setOnAction(e-> {
            /** TODO : implement tour computing **/
//            LinkedList<Segment> tour = ComputeTour.computeTour(cm,dt);
//
//            mapCanvas.setComputedTour(tour);

            controller.computeTour();
        });
        loadBox.getChildren().add(but_comp);
        but_comp.prefHeightProperty().bind(loadBox.heightProperty());
        but_comp.prefWidthProperty().bind(loadBox.widthProperty().divide(3));




        root.getChildren().add(textualView);

        Scene scene = new Scene(root, 1920, 1000);
        mapCanvas.drawMap();
        stage.setTitle("Lines");
        stage.setScene(scene);
        stage.show();
    }

    public File chooseFile(String title){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("title");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );

        File file = fileChooser.showOpenDialog(stage);
        if(file != null) {
            return file;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Loading error");
            alert.setContentText("The file you selected couldn't be loaded.");

            alert.showAndWait();
            return null;
        }

    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setMapCanvasScale(double scale){

    }
}
