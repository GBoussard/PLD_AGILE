package drawmap.view;
import drawmap.model.*;
import java.util.Map.Entry;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import java.util.Iterator;

public class MapCanvas extends Pane {

    private CityMap map;

    // geometric attributes and modifiers
    private Integer width;
    private Integer height;
    private Double offset_w = 4.857;
    private Double offset_h = 45.765;
    private Double scale = 0.02;

    //control attributes
    private Double old_x=null, old_y = null;

    public MapCanvas(CityMap m, Integer width, Integer height) {
        super();
        this.setPrefSize(width, height);

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
            //System.out.println(offset_h + " " + offset_w);
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
            if(scale < 0.02) scale=0.02;
            else if(scale > 2) scale=2.0;
            else {
                // TODO : try to zoom into the cursor-designed point
                
            }

            drawMap();
        });

        this.width = width;
        this.height = height;
        this.map = m;
    }

    public void drawMap() {
        this.getChildren().clear();
        Iterator it_segment = this.map.getSegmentIterator();
        double a_h = -this.height / scale;
        double b_h = (-1.0)*a_h*offset_h;
        double a_w = -a_h;
        double b_w = (-1.0)*a_w*offset_w;

        //System.out.println(a_w + " " + b_w + " " + a_h + " " + b_h);

        while(it_segment.hasNext()) {
            Segment s = (Segment) it_segment.next();
            double x0 = s.getOrigin().getLongitude();
            double y0 = s.getOrigin().getLatitude();
            double x1 = s.getDestination().getLongitude();
            double y1 = s.getDestination().getLatitude();


            //System.out.println(s.getName());
            //System.out.println(x0 + " " + (a_w*x0+b_w)+ " " + y0 + " " + (a_h*y0+b_h));
            Line l = new Line(a_w*x0+b_w,a_h*y0+b_h,a_w*x1+b_w,a_h*y1+b_h);
            l.setStrokeWidth(3*(scale/2.0)+1);
            l.setStrokeType(StrokeType.OUTSIDE);
            Text t = new Text(0,0,s.getName());
            t.setFill(Color.BLUE);
            this.getChildren().add(t);
            t.setVisible(false);
            l.setOnMouseEntered(e-> {
                System.out.println("entered");
                System.out.println(s.getName());
            });
            l.setOnMouseMoved(e-> {
                System.out.println("moved");
                t.setX(e.getX());
                t.setY(e.getY());
                t.setVisible(true);

            });
            l.setOnMouseExited(e-> {
                System.out.println("exited");
                t.setVisible(false);
            });

            this.getChildren().add(l);
        }

    }

}
