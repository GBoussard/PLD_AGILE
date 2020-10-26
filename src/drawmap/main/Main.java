package drawmap.main;

import drawmap.controller.Controller;
import drawmap.model.*;
import drawmap.view.MainView;
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

public class Main {



    public static void main(String[] args) {
        MainView view = new MainView();
        Application.launch(view.getClass());
    }

}
