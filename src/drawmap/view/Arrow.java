package drawmap.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 *
 * @author kn
 */
public class Arrow extends Path{
    private static final double defaultArrowHeadSize = 5.0;

    public Arrow(double startX, double startY, double endX, double endY, double scale, double arrowHeadSize){
        super();
        strokeProperty().bind(fillProperty());
        setFill(Color.BLUE);
        setStrokeWidth(3*(scale/2.0)+5);

        //Line
        getElements().add(new MoveTo(startX, startY));
        getElements().add(new LineTo(endX, endY));

        //ArrowHead
        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        getElements().add(new MoveTo(startX + (endX-startX)/2.0 , startY + (endY-startY)/2.0 ));
        //point1
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX -   (endX-startX)/2.0 ;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY - (endY-startY)/2.0 ;

        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX -   (endX-startX)/2.0;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY - (endY-startY)/2.0;

        getElements().add(new LineTo(x1 , y1 ));
        getElements().add(new LineTo(x2, y2));

        getElements().add(new LineTo(startX + (endX-startX)/2.0, startY + (endY-startY)/2.0));
    }

    public Arrow(double startX, double startY, double endX, double endY, double scale){
        this(startX, startY, endX, endY, scale, defaultArrowHeadSize);
    }
}