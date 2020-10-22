package drawmap.main;

import drawmap.model.*;
import drawmap.view.*;
import drawmap.controller.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        initUI(stage);

    }

    private void initUI(Stage stage) {

        Pane root = new HBox();


        CityMap cm = new CityMap();
        //cm.read("./fichiersXML2020/largeMap.xml");

        DeliveryTour dt = new DeliveryTour();
        //dt.read("./fichiersXML2020/requestsLarge7.xml", cm);

        MapCanvas canvas = new MapCanvas(cm, dt,1200, 1000);
        MapCanvasController canvasController = new MapCanvasController(canvas);
        root.getChildren().add(canvas);

        Slider slider = new Slider(0.02, 0.05,0.05);
        slider.setOrientation(Orientation.VERTICAL);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println(newValue.doubleValue());
                canvasController.setMapCanvasScale(newValue.doubleValue());
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
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Please select a map file");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("XML Files", "*.xml")
            );

            File file = fileChooser.showOpenDialog(stage);
            if(file != null) {
                cm.read(file.getAbsolutePath());
                canvas.drawMap();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Loading error");
                alert.setContentText("The file you selected couldn't be loaded.");

                alert.showAndWait();
            }

        });
        loadBox.getChildren().add(but_map);
        but_map.prefHeightProperty().bind(loadBox.heightProperty());
        but_map.prefWidthProperty().bind(loadBox.widthProperty().divide(3));

        Button but_req = new Button("Load a request file");
        but_req.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Please select a request file");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("XML Files", "*.xml")
            );

            File file = fileChooser.showOpenDialog(stage);
            if(file != null) {
                dt.read(file.getAbsolutePath(), cm);
                canvas.drawMap();
                Iterator it_requests = dt.getRequestIterator();
                while(it_requests.hasNext()) {
                    Request r = (Request) it_requests.next();

                    HBox requestLine = new HBox(20);
                    requestLine.getChildren().add(new Rectangle(20,20, r.getColor()));
                    requestLine.getChildren().add(
                            new Label(r.getDelivery().getLatitude() + ";" + r.getDelivery().getLongitude()));
                    requestLine.getChildren().add(
                            new Label(r.getPickup().getLatitude() + ";" + r.getPickup().getLongitude()));

                    requestBox.getChildren().add(requestLine);



                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Loading error");
                alert.setContentText("The file you selected couldn't be loaded.");

                alert.showAndWait();
            }

        });
        loadBox.getChildren().add(but_req);
        but_req.prefHeightProperty().bind(loadBox.heightProperty());
        but_req.prefWidthProperty().bind(loadBox.widthProperty().divide(3));

        Button but_comp = new Button("Compute tour");
        but_comp.setOnAction(e-> {
            /** TODO : implement tour computing **/
        });
        loadBox.getChildren().add(but_comp);
        but_comp.prefHeightProperty().bind(loadBox.heightProperty());
        but_comp.prefWidthProperty().bind(loadBox.widthProperty().divide(3));




        root.getChildren().add(textualView);

        Scene scene = new Scene(root, 1920, 1000);
        canvas.drawMap();
        stage.setTitle("Lines");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
