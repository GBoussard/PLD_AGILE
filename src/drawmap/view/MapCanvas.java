package drawmap.view;
import drawmap.controller.Controller;
import drawmap.model.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Predicate;

//https://docs.oracle.com/javafx/2/ui_controls/slider.htm and https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Slider.html and http://johnthecodingarchitect.blogspot.com/2013/11/scaling-vs-zooming-in-javafx.html

/**
 * View class for map visualisation
 */
public class MapCanvas extends Pane implements Observer {

    private CityMap map;
    private DeliveryTour tour;
    private ComputeTour computedTour;
    private Controller controller;

    // geometric attributes and modifiers
    private Integer width;
    private Integer height;
    private Double offset_w = 4.857;
    private Double offset_h = 45.765;
    private Double scale = 0.02;

    double a_h;
    double b_h;
    double a_w;
    double b_w;

    //control attributes
    private Double old_x=null, old_y = null;

    /**
     * Constructor for MapCanvas
     * @param m : map to display
     * @param dt : delivery tour to display
     * @param ct : computed tour to display
     * @param width : map display width
     * @param height : map display height
     * @param c : parent controller
     */
    public MapCanvas(CityMap m, DeliveryTour dt, ComputeTour ct,Integer width, Integer height, Controller c){
        super();
        this.setHeight(height);
        this.setWidth(width);
        this.setMaxSize(width, height);
        this.setMinSize(width, height);
        this.width = width;
        this.height = height;
        this.map = m;
        this.tour = dt;
        this.computedTour = ct;
        this.controller = c;

        map.addObserver(this);
        tour.addObserver(this);
        computedTour.addObserver(this);

        a_h = -this.height / scale;
        b_h = (-1.0)*a_h*offset_h;
        a_w = -a_h;
        b_w = (-1.0)*a_w*offset_w;


        this.setOnMousePressed(e-> {

            old_x = e.getX();
            old_y = e.getY();
            //System.out.println(old_x+" ; "+ old_y);
        });

        this.setOnMouseDragged(e-> {
            //System.out.println(e.getX() + " " + e.getY());
            if(old_x == null || old_y ==null) return;
            offset_h += (e.getY() - old_y)/(this.height/scale);
            offset_w -= (e.getX() - old_x)/(this.height/scale);


            a_h = -this.height / scale;
            b_h = (-1.0)*a_h*offset_h;
            a_w = -a_h;
            b_w = (-1.0)*a_w*offset_w;

            //offset_w = constrain(offset_w, 4.8316, 4.89142);
            //offset_h = constrain(offset_h,45.74748, 45.7708);
            old_x = e.getX();
            old_y = e.getY();
            drawMap();

        });

        /*this.setOnMouseReleased(e-> {
            old_x = null;
            old_y = null;
        });*/

        this.setOnScroll(s-> {
            //System.out.println(s.getTextDeltaY());
            scale+= 0.0025*Math.signum(s.getTextDeltaY());
            scale = constrain(scale, 0.0005, 10);
            a_h = -this.height / scale;
            b_h = (-1.0)*a_h*offset_h;
            a_w = -a_h;
            b_w = (-1.0)*a_w*offset_w;
            drawMap();
        });


    }


    /**
     * Sets map displaying scale
     * @param s
     */
    public void setScale(double s) {
        this.scale = s;
        a_h = -this.height / scale;
        b_h = (-1.0)*a_h*offset_h;
        a_w = -a_h;
        b_w = (-1.0)*a_w*offset_w;
        drawMap();
    }

    private class HBoxClickHandler implements EventHandler<Event> {
        private String IntersectionId;
        private Controller controller;

        public HBoxClickHandler(String intersectionId, Controller c){
            this.IntersectionId = intersectionId;
            this.controller = c;
        }
        @Override
        public void handle(Event evt) {
            this.controller.focusClickedRequestPointInRequestView(this.IntersectionId);
        }
    }

    /**
     * Draws map
     */
    public void drawMap() {
        this.getChildren().clear();
        Iterator it_segment = this.map.getSegmentIterator();


        // ******* DRAW ROADS *******

        while(it_segment.hasNext()) {
            Segment s = (Segment) it_segment.next();
            double x0 = s.getOrigin().getLongitude();
            double y0 = s.getOrigin().getLatitude();
            double x1 = s.getDestination().getLongitude();
            double y1 = s.getDestination().getLatitude();

            double compute_x0 = a_w*x0+b_w;
            double compute_y0 = a_h*y0+b_h;
            double compute_x1 = a_w*x1+b_w;
            double compute_y1 = a_h*y1+b_h;

            if((compute_x0 < 0 && compute_x1 < 0) || (compute_y0 < 0 && compute_y1 < 0) ||
                    (compute_x0 > width && compute_x1 > width) || (compute_y0 > height && compute_y1 > height)) {

                continue;
            }

//            compute_x0 = constrain(compute_x0, 0, width);
//            compute_x1 = constrain(compute_x1, 0, width);
//            compute_y0 = constrain(compute_y0, 0, height);
//            compute_y1 = constrain(compute_y1, 0, height);

            //System.out.println(s.getName());
            //System.out.println(x0 + " " + (a_w*x0+b_w)+ " " + y0 + " " + (a_h*y0+b_h));
            Line l = new Line(compute_x0,compute_y0,compute_x1,compute_y1);
            l.setStrokeWidth(3*(scale/2.0)+1);
            l.setStrokeType(StrokeType.OUTSIDE);
            Text t = new Text(0,0,s.getName());
            t.setFill(Color.BLUE);
            this.getChildren().add(t);
            t.setVisible(false);
            l.setOnMouseEntered(e-> {

            });
            l.setOnMouseMoved(e-> {
                t.setX(e.getX());
                t.setY(e.getY());
                t.setVisible(true);

            });
            l.setOnMouseExited(e-> {
                t.setVisible(false);
            });

            this.getChildren().add(l);
        }


        //******* DRAW REQUESTS *******

        Iterator it_requests = tour.getRequestIterator();

        // on affiche le depot
        Intersection depot = this.tour.getOrigin();
        if (depot != null){
            double depotLong = depot.getLongitude();
            double depotLat = depot.getLatitude();

            double compute_x0 = constrain(a_w*depotLong+b_w,0,width);
            double compute_y0 = constrain(a_h*depotLat+b_h, 0, height);
            Circle circ_depot = new Circle(compute_x0, compute_y0,10);
            circ_depot.setFill(Color.RED);
            circ_depot.setId(depot.getId().toString());
            circ_depot.addEventFilter(MouseEvent.MOUSE_CLICKED, new HBoxClickHandler(depot.getId().toString(), this.controller));

            this.getChildren().addAll(circ_depot);
        }

        while(it_requests.hasNext()) {
            /**
             Intersection depot = tour.getOrigin();
             double depot_x = depot.getLongitude();
             double depot_y = depot.getLatitude();
             double compute_depot_x = constrain(a_w*depot_x+b_w,0,width);
             double compute_depot_y = constrain(a_h*depot_y+b_h, 0, height);
             Rectangle rect_depot = new Rectangle(compute_depot_x, compute_depot_y, 20, 20);
             rect_depot.setFill(Color.BLACK);
             this.getChildren().add(rect_depot);
             **/

            Request r = (Request) it_requests.next();

            double x0 = r.getPickup().getLongitude();
            double y0 = r.getPickup().getLatitude();
            double x1 = r.getDelivery().getLongitude();
            double y1 = r.getDelivery().getLatitude();

            double compute_x0 = constrain(a_w*x0+b_w,0,width);
            double compute_y0 = constrain(a_h*y0+b_h, 0, height);
            double compute_x1 = constrain(a_w*x1+b_w, 0, width);
            double compute_y1 = constrain(a_h*y1+b_h, 0, height);


            Rectangle rect_origin = new Rectangle(compute_x0, compute_y0, 20, 20);
            rect_origin.setId(r.getPickup().getId().toString());
            rect_origin.setFill(r.getColor());
            Circle circle_destination = new Circle(compute_x1,compute_y1,10);
            circle_destination.setId(r.getDelivery().getId().toString());
            circle_destination.setFill(r.getColor());

            rect_origin.addEventFilter(MouseEvent.MOUSE_CLICKED, new HBoxClickHandler(r.getPickup().getId().toString(), this.controller));
            circle_destination.addEventFilter(MouseEvent.MOUSE_CLICKED, new HBoxClickHandler(r.getDelivery().getId().toString(), this.controller));


            this.getChildren().add(rect_origin);
            this.getChildren().add(circle_destination);



        }

        // ******* DRAW PATH *******

        if(computedTour.getComputed()){
            Iterator it_path = computedTour.getPathIterator();
            int arrowNumber = 1;
            while(it_path.hasNext()){
                Segment s = (Segment)it_path.next();
                double x0 = s.getOrigin().getLongitude();
                double y0 = s.getOrigin().getLatitude();
                double x1 = s.getDestination().getLongitude();
                double y1 = s.getDestination().getLatitude();

                double compute_x0 = a_w*x0+b_w;
                double compute_y0 = a_h*y0+b_h;
                double compute_x1 = a_w*x1+b_w;
                double compute_y1 = a_h*y1+b_h;

                if((compute_x0 < 0 && compute_x1 < 0) || (compute_y0 < 0 && compute_y1 < 0) ||
                        (compute_x0 > width && compute_x1 > width) || (compute_y0 > height && compute_y1 > height)) {

                    continue;
                }

                //System.out.println(s.getName());
                //System.out.println(x0 + " " + (a_w*x0+b_w)+ " " + y0 + " " + (a_h*y0+b_h));
          /*      Line l = new Line(compute_x0,compute_y0,compute_x1,compute_y1);
                l.setStrokeWidth(3*(scale/2.0)+5);
                l.setStrokeType(StrokeType.CENTERED);
                l.setStroke(Color.BLUE);*/

                Arrow a = new Arrow(compute_x0,compute_y0,compute_x1,compute_y1,scale);

                Text number = new Text(String.valueOf(arrowNumber));
                arrowNumber++;

                number.setX(compute_x0 + 1.4*(compute_x1 - compute_x0)/2.0);
                number.setY(compute_y0 + 1.4*(compute_y1 - compute_y0)/2.0);
                number.setStroke(Color.RED);

                this.getChildren().add(a);
                this.getChildren().add(number);
            }
        }

    }

    private double constrain(double value, double min, double max) {
        if(value < min) value = min;
        if(value > max) value = max;
        return value;
    }

    public double getOriginalLongitude(){
        if (old_x != null)
            return (old_x - b_w)/a_w;
        return -1;
    }

    public double getOriginalLatitude(){
        if (old_y != null)
            return (old_y - b_h)/a_h;
        return -1;
    }


    @Override
    public void update(Observable o, Object arg) {
        this.drawMap();
    }

    public static Predicate<Node> idEquals(String id) {
        return p -> p.getId() != null && p.getId().equals(id);
    }

    /**
     * Highlights given intersection
     * @param intersectionId
     */
    public void highlighClickedRequest(String intersectionId) {
        List<Node> l = this.getChildren().filtered(idEquals(intersectionId));
        if (l.size() == 1) {
            this.drawMap();
            Shape intersectionToHighligh = (Shape) l.get(0);
            intersectionToHighligh.setFill(Color.CORNFLOWERBLUE);
        }
    }

    public Pair<Double, Double> getCoordClick(MouseEvent event){
        double longitude;
        double latitude;
        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2){
                longitude = getOriginalLongitude();
                latitude = getOriginalLatitude();
                Pair<Double, Double> coord = new Pair(longitude, latitude);
                return coord;
            }
        }
        return null;
    }
}