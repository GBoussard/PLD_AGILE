package drawmap.controller;

import drawmap.view.MapCanvas;

public class MapCanvasController {

    private MapCanvas controlled;

    public MapCanvasController() {}

    public MapCanvasController(MapCanvas mc) {
        this.controlled = mc;

    }

    public void setMapCanvasScale(double s) {
        controlled.setScale(s);
    }
}
