package drawmap.main;

import drawmap.model.*;
import drawmap.view.*;
import drawmap.controller.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        initUI(stage);

    }

    private void initUI(Stage stage) {

        Pane root = new HBox();


        CityMap cm = new CityMap();
        cm.read("./fichiersXML2020/largeMap.xml");

        MapCanvas canvas = new MapCanvas(cm,1200, 1000);
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

        HBox loadBox = new HBox();
        textualView.getChildren().add(loadBox);
        loadBox.setMinSize(400,200);
        loadBox.setMaxSize(400,200);
        javafx.scene.control.Button but = new Button("Load");
        loadBox.getChildren().add(but);
        but.prefHeightProperty().bind(loadBox.heightProperty());
        but.prefWidthProperty().bind(loadBox.widthProperty().divide(3));
        VBox requestBox = new VBox();
        textualView.getChildren().add(requestBox);
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
