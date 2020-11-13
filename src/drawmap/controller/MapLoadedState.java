package drawmap.controller;
import drawmap.model.CityMap;

import java.io.File;

public class MapLoadedState implements State {

    @Override
    public boolean loadRequests(Controller c){
        c.getComputeTour().setComputed(false);
        File dt = c.getMainView().chooseFile("Choose a requests file");
        if(dt != null){
            return c.getDeliveryTour().read(dt.getAbsolutePath(), c.getCityMap());
        }
        return false;
    }



    @Override
    public boolean loadMap(Controller c){
        c.getDeliveryTour().removeAllRequests();
        File map = c.getMainView().chooseFile("Choose a map file");
        if(map != null) {
            return c.getCityMap().read(map.getAbsolutePath());
        }
        return false;
    }

}
