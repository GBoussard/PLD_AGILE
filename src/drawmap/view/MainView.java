package drawmap.view;

import com.sun.javafx.event.EventHandlerManager;
import drawmap.controller.ComputedTourState;
import drawmap.controller.Controller;
import drawmap.controller.RequestsLoadedState;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.util.Pair;

import javafx.scene.input.MouseButton;


import java.io.File;
import java.util.Optional;

public class MainView extends Application {

    private Stage stage;
    private Controller controller;
    private MapCanvas mapCanvas;
    private RequestView requestView;
    private Pair<Double, Double> coordPickup;
    private Pair<Double, Double> coordDelivery;
    private Pair<Double, Double> previous;
    private Pair<Double, Double> next;
    private Pair<Double, Double> coordRemove;
    private int indexAdding;
    private int indexRemoving;

    @Override
    public void start(Stage stage) {

        this.stage = stage;
        this.controller = new Controller(this);
        initUI(stage);

    }


    private void initUI(Stage stage) {

        Pane root = new HBox();

        mapCanvas = new MapCanvas(controller.getCityMap(), controller.getDeliveryTour(), controller.getComputeTour(),1200, 1000, this.controller);
        root.getChildren().add(mapCanvas);

        Slider slider = new Slider(0.005, 0.15,0.05);
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
        loadBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        textualView.getChildren().add(loadBox);

        requestView = new RequestView(controller.getDeliveryTour(), controller.getComputeTour(), this.controller);

        ScrollPane scrollPaneRequestView = new ScrollPane();
        scrollPaneRequestView.setContent(requestView);
        scrollPaneRequestView.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        scrollPaneRequestView.setPrefSize(750,1000);
        scrollPaneRequestView.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        textualView.getChildren().addAll(requestView, scrollPaneRequestView);


        loadBox.setPrefSize(750,200);
        Button but_map = new Button("Load a map");
        but_map.setOnAction(e -> {

            controller.loadMap();

        });
        loadBox.getChildren().add(but_map);
        but_map.prefHeightProperty().bind(loadBox.heightProperty());
        but_map.prefWidthProperty().bind(loadBox.widthProperty().divide(7));
        but_map.setWrapText(true);

        Button but_req = new Button("Load a request file");
        but_req.setOnAction(e -> {

            controller.loadRequest();

        });
        loadBox.getChildren().add(but_req);
        but_req.prefHeightProperty().bind(loadBox.heightProperty());
        but_req.prefWidthProperty().bind(loadBox.widthProperty().divide(7));
        but_req.setWrapText(true);

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
        but_comp.prefWidthProperty().bind(loadBox.widthProperty().divide(7));
        but_comp.setWrapText(true);


        Button but_add = new Button("Add request");
        but_add.setOnAction(e-> {

            int pickUpDuration = showInputTextDialog("0", "Pickup Duration", "Please Enter the Pickup Duration (minutes)", "Pickup Duration");
            int deliveryDuration = showInputTextDialog("0", "Delivery Duration", "Please Enter the Delivery Duration (minutes)", "Delivery Duration");
            indexAdding = 0;
            mapCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        if (mouseEvent.getClickCount() == 2) {
                            switch (indexAdding){
                                case 0:
                                    coordPickup = mapCanvas.getCoordClick(mouseEvent);
                                    System.out.println("Pickup: "+coordPickup);
                                    break;
                                case 1:
                                    coordDelivery = mapCanvas.getCoordClick(mouseEvent);
                                    System.out.println("Delivery: "+coordDelivery);
                                    if (controller.getCurrentState() instanceof RequestsLoadedState){
                                        controller.addRequest(coordPickup, coordDelivery, null, null, pickUpDuration, deliveryDuration);
                                    }
                                    break;
                                case 2:
                                    if (controller.getCurrentState() instanceof ComputedTourState) {
                                        previous = mapCanvas.getCoordClick(mouseEvent);
                                        System.out.println("previous: " + previous);
                                    }
                                    break;
                                case 3:
                                    if(controller.getCurrentState() instanceof ComputedTourState) {
                                        next = mapCanvas.getCoordClick(mouseEvent);
                                        System.out.println("next: " + next);
                                        controller.addRequest(coordPickup, coordDelivery, previous, next, pickUpDuration, deliveryDuration);
                                    }
                                    break;
                            }
                            indexAdding++;
                        }
                    }
                }
            });
        });
        loadBox.getChildren().add(but_add);
        but_add.prefHeightProperty().bind(loadBox.heightProperty());
        but_add.prefWidthProperty().bind(loadBox.widthProperty().divide(7));
        but_add.setWrapText(true);
        Button but_remove = new Button("Remove a request");
        but_remove.setOnAction(e ->{
            indexRemoving = 0;
            mapCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        if (mouseEvent.getClickCount() == 2) {
                            if(indexRemoving == 0){
                                coordRemove = mapCanvas.getCoordClick(mouseEvent);
                                controller.removeRequest(coordRemove);
                                indexRemoving++;
                            }
                        }
                    }
                }
            });
        });
        loadBox.getChildren().add(but_remove);
        but_remove.prefHeightProperty().bind(loadBox.heightProperty());
        but_remove.prefWidthProperty().bind(loadBox.widthProperty().divide(7));
        but_remove.setWrapText(true);

        Button but_undo = new Button("Undo");
        but_undo.setOnAction(e ->{
            controller.undo();
        });
        loadBox.getChildren().add(but_undo);
        but_undo.prefHeightProperty().bind(loadBox.heightProperty());
        but_undo.prefWidthProperty().bind(loadBox.widthProperty().divide(7));
        but_undo.setWrapText(true);

        Button but_redo = new Button("Redo");
        but_redo.setOnAction(e ->{
            controller.redo();
        });
        loadBox.getChildren().add(but_redo);
        but_redo.prefHeightProperty().bind(loadBox.heightProperty());
        but_redo.prefWidthProperty().bind(loadBox.widthProperty().divide(7));
        but_redo.setWrapText(true);

        root.getChildren().add(textualView);
        Scene scene = new Scene(root, 1920, 1000);
        mapCanvas.drawMap();
        stage.setTitle("Lines");
        stage.setScene(scene);
        stage.show();

        displayAlert("test");
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

    public int showInputTextDialog(String preFilled, String title, String HeaderText, String contentText){
        TextInputDialog tid = new TextInputDialog(preFilled);
        String text = "0";
        tid.setTitle(title);
        tid.setHeaderText(HeaderText);
        tid.setContentText(contentText);
        Optional<String> resultPickup = tid.showAndWait();
        if(resultPickup.isPresent()){
            text = tid.getEditor().getText();
        }
        int result = Integer.parseInt(text);
        return result;
    }

    public Pair<Double, Double> getPoint(MouseEvent event){
        return mapCanvas.getCoordClick(event);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setMapCanvasScale(double scale){
        mapCanvas.setScale(scale);
    }

    public void focusClickedRequestInMap(String intersectionId){
        this.mapCanvas.highlighClickedRequest(intersectionId);
    }

    public void focusClickedRequestInRequestView(String intersectionId) {
        this.requestView.highlighClickedRequest(intersectionId);
    }

    public void displayAlert(String cause) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(cause);

        alert.showAndWait();
    }

}
