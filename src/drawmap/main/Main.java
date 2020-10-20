package drawmap.main;

import drawmap.model.*;
import drawmap.view.*;

import javafx.application.Application;
import javafx.scene.Scene;
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

        Pane root = new Pane();

        CityMap cm = new CityMap();
        cm.read("./fichiersXML2020/largeMap.xml");

        MapCanvas canvas = new MapCanvas(cm,1366, 768);
        root.getChildren().add(canvas);


        Scene scene = new Scene(root, 1366, 768);

        canvas.drawMap();
        stage.setTitle("Lines");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
