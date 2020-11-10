package drawmap.view;

import drawmap.controller.Controller;
import drawmap.model.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

public class RequestView extends Pane implements Observer {

    private DeliveryTour tour;
    private ComputeTour computedTour;
    private Controller controller;
    private HBox previousHighlightedPoint;
    private int countNbRequestPrinted;

    public RequestView(DeliveryTour dt, ComputeTour ct, Controller c){
        this.tour = dt;
        this.computedTour = ct;
        this.controller = c;
        this.countNbRequestPrinted = 0;
        tour.addObserver(this);
        computedTour.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        this.drawRequestView();
    }

    // Helper method
    private String format(double val) {
        String in = Integer.toHexString((int) Math.round(val * 255));
        return in.length() == 1 ? "0" + in : in;
    }

    public String toHexString(Color value) {
        return "#" + (format(value.getRed()) + format(value.getGreen()) + format(value.getBlue()) + format(value.getOpacity()))
                .toUpperCase();
    }

    private class HBoxClickHandler implements EventHandler<Event>{
        private String IntersectionId;
        private Controller controller;
        private  RequestView requestView;

        public HBoxClickHandler(String intersectionId, Controller c, RequestView r){
            this.IntersectionId = intersectionId;
            this.controller = c;
            this.requestView = r;
        }
        @Override
        public void handle(Event evt) {
            if (this.requestView.previousHighlightedPoint != null){
                this.requestView.previousHighlightedPoint.setStyle("-fx-background-color: None;");
            }
            this.controller.focusClickedRequestPointInMap(this.IntersectionId);
        }
    }


    public void drawRequestView() {
        this.countNbRequestPrinted = 0;
        Iterator it_requests = tour.getRequestIterator();
        // on vide le textual view
        this.getChildren().removeAll(this.getChildren());

        VBox verticalrequestContainer = new VBox();
        verticalrequestContainer.setSpacing(15);
        verticalrequestContainer.setId("VerticalContainer");

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String roadMap = "";
        int num = 0;
        Boolean neighboorsHaveName = false;

        // on affiche le depot
        HBox depotContener = this.createLineInfosAboutIntersection(this.tour.getOrigin(), Color.RED, 0);
        verticalrequestContainer.getChildren().addAll(depotContener);
        if (this.computedTour.getComputed()){
            Iterator it_compute_tour = computedTour.getIntersectionsDateIterator();

            List<Request> requests = this.tour.getRequests();
            Pair<Intersection, Date> p = null;
            while(it_compute_tour.hasNext()){
                p = (Pair<Intersection, Date>) it_compute_tour.next();
                Intersection i = p.getKey();
                Date d = p.getValue();
                double intersectionLong = i.getLongitude();
                double intersectionLat = i.getLatitude();
                Long intersectionId = i.getId();
                String date =  df.format(d);

                // on genere la feuille de route

                roadMap += "Intersection de :\n";

                neighboorsHaveName = false;
                ArrayList<Pair<Segment, Intersection>> voisins = i.getVoisins();
                for( Pair<Segment, Intersection> v : voisins){
                    String name = v.getKey().getName();
                    if (!name.equals("")){
                        roadMap += v.getKey().getName()+" \n ";
                        neighboorsHaveName = true;
                    }
                }

                if (!neighboorsHaveName){
                    roadMap += "Long: "+intersectionLong+" , Lat: "+intersectionLat+"\n";
                }

                roadMap += "\n";

                // pour chaque intersection on va chercher dans la liste des requetes du delivery tour
                // si elle y est, on affiche
                for (Request r: requests){
                    if (r.getPickup().getId() == intersectionId) {
                        HBox PickupContener = this.createLineInfosAboutIntersection(r.getPickup(), r.getColor(), r.getPickupDuration());
                        verticalrequestContainer.getChildren().add(PickupContener);
                    }
                    else if(r.getDelivery().getId() ==  intersectionId){
                        HBox intersectionContener = this.createLineInfosAboutIntersection(r.getDelivery(), r.getColor(), r.getDeliveryDuration());
                        verticalrequestContainer.getChildren().add(intersectionContener);
                    }
                }

            }

            try {
                FileWriter myWriter = new FileWriter("Roadmap.txt");
                myWriter.write(roadMap);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            // on rajoute le temps total pour faire la tournée
            it_compute_tour = computedTour.getIntersectionsDateIterator();
            if (it_compute_tour.hasNext()){
                Pair<Intersection, Date> first =  (Pair<Intersection, Date>)  it_compute_tour.next();
                if (p != null) {
                    // p est le dernier element de l'iterateur dans la boucle du dessus
                    Pair<Intersection, Date> last =  p;
                    String totalDuration = "The tour will take you " + ((last.getValue().getTime() - first.getValue().getTime()) / 60000) + " minutes to complete" ;
                    verticalrequestContainer.getChildren().add(new Label(totalDuration));
                }
            }

        }
        else{
            // s'il n'y a pas de compute tour, on affiche le delivery tour

            while(it_requests.hasNext()){
                Request r = (Request) it_requests.next();

                HBox pickupContener = this.createLineInfosAboutIntersection(r.getPickup(), r.getColor(), r.getPickupDuration());
                HBox deliveryContener = this.createLineInfosAboutIntersection(r.getDelivery(), r.getColor(), r.getDeliveryDuration());

                verticalrequestContainer.getChildren().addAll(pickupContener, deliveryContener);
            }
        }

        this.getChildren().add(verticalrequestContainer);

    }

    public static Predicate<Node> idEquals(String id) {
        return p -> p.getId() != null && p.getId().equals(id);
    }

    public void highlighClickedRequest(String intersectionId) {
        List<Node> verticalContainerList = this.getChildren().filtered(idEquals("VerticalContainer"));
        if (verticalContainerList.size() == 1){
            VBox verticalContainer = (VBox) verticalContainerList.get(0);
            List<Node> requests = verticalContainer.getChildren().filtered(idEquals(intersectionId));
            if (requests.size() == 1){
                HBox intersectionToHighlight = (HBox) requests.get(0);
                if (this.previousHighlightedPoint != null){
                    this.previousHighlightedPoint.setStyle(("-fx-background-color: None;"));
                }
                this.previousHighlightedPoint = intersectionToHighlight;
                intersectionToHighlight.setStyle("-fx-background-color: cornflowerblue;");
            }
        }
    }

    public HBox createLineInfosAboutIntersection(Intersection i, Color color, int duration){
        HBox contener = new HBox();
        contener.setSpacing(15);
        contener.setId(i.getId().toString());

        double pickupLong = i.getLongitude();
        double pickupLat = i.getLatitude();

        String streets = "Intersection de :\n";

        ArrayList<Pair<Segment, Intersection>> voisins = i.getVoisins();
        for( Pair<Segment, Intersection> p : voisins){
            streets += p.getKey().getName()+" \n ";
        }

        streets += "\n";

        this.countNbRequestPrinted++;
        Label numero_pickup = new Label(this.countNbRequestPrinted+") ");
        
        Text intersectionStreets = new Text(streets);
        Text DurationText = new Text("Duration: "+(duration / 60)+" minute(s)");

        Button intersectionButton = new Button();
        String round = (this.countNbRequestPrinted % 2 == 1) ? "-fx-background-radius: 50em;" : "";
        intersectionButton.setStyle("-fx-background-color: "+toHexString(color)+";"+round);

        contener.getChildren().addAll(numero_pickup, intersectionButton, intersectionStreets , DurationText);
        contener.addEventFilter(MouseEvent.MOUSE_CLICKED, new HBoxClickHandler(i.getId().toString(), this.controller, this));
        return contener;
    }
}
