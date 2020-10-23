package drawmap.view;
import drawmap.model.*;

import java.util.List;
import java.util.Map.Entry;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.control.Slider;
//https://docs.oracle.com/javafx/2/ui_controls/slider.htm and https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Slider.html and http://johnthecodingarchitect.blogspot.com/2013/11/scaling-vs-zooming-in-javafx.html

import java.util.Iterator;

public class MapCanvas extends Pane {

    private CityMap map;
    private DeliveryTour tour;
    private List<Segment> computedTour;

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

    public MapCanvas(CityMap m, DeliveryTour dt, Integer width, Integer height) {
        super();
        this.setHeight(height);
        this.setWidth(width);
        this.setMaxSize(width, height);
        this.setMinSize(width, height);
        this.width = width;
        this.height = height;
        this.map = m;
        this.tour = dt;


        a_h = -this.height / scale;
        b_h = (-1.0)*a_h*offset_h;
        a_w = -a_h;
        b_w = (-1.0)*a_w*offset_w;


        this.setOnMousePressed(e-> {
            //System.out.println(e.getX() + " " + e.getY());
            old_x = e.getX();
            old_y = e.getY();
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

        this.setOnMouseReleased(e-> {
            old_x = null;
            old_y = null;
        });

        this.setOnScroll(s-> {
            //System.out.println(s.getTextDeltaY());
            scale+= 0.005*Math.signum(s.getTextDeltaY());
            scale = constrain(scale, 0.02, 2);
            a_h = -this.height / scale;
            b_h = (-1.0)*a_h*offset_h;
            a_w = -a_h;
            b_w = (-1.0)*a_w*offset_w;


            drawMap();
        });


    }

    public void setComputedTour(List<Segment> tour){
        this.computedTour = tour;
    }

    public void setScale(double s) {
        this.scale = s;
        a_h = -this.height / scale;
        b_h = (-1.0)*a_h*offset_h;
        a_w = -a_h;
        b_w = (-1.0)*a_w*offset_w;
        drawMap();
    }

    public void drawMap() {
        this.getChildren().clear();
        Iterator it_segment = this.map.getSegmentIterator();


        //System.out.println(a_w + " " + b_w + " " + a_h + " " + b_h);

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

        while(it_requests.hasNext()) {
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
            rect_origin.setFill(r.getColor());
            Circle circle_destination = new Circle(compute_x1,compute_y1,10);
            circle_destination.setFill(r.getColor());

            this.getChildren().add(rect_origin);
            this.getChildren().add(circle_destination);

        }

        // ******* DRAW PATH *******

        if(computedTour != null){
            Iterator it_path = computedTour.iterator();
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

                compute_x0 = constrain(compute_x0, 0, width);
                compute_x1 = constrain(compute_x1, 0, width);
                compute_y0 = constrain(compute_y0, 0, height);
                compute_y1 = constrain(compute_y1, 0, height);



                //System.out.println(s.getName());
                //System.out.println(x0 + " " + (a_w*x0+b_w)+ " " + y0 + " " + (a_h*y0+b_h));
                Line l = new Line(compute_x0,compute_y0,compute_x1,compute_y1);
                l.setStrokeWidth(3*(scale/2.0)+5);
                l.setStrokeType(StrokeType.CENTERED);
                l.setStroke(Color.BLUE);
                this.getChildren().add(l);
            }
        }

    }

    private double constrain(double value, double min, double max) {
        if(value < min) value = min;
        if(value > max) value = max;
        return value;
    }

}
