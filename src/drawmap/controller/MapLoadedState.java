package drawmap.controller;
import drawmap.model.CityMap;

import java.io.File;

public class MapLoadedState implements State {

    @Override
    public void loadRequests(Controller c){
        c.getComputeTour().setComputed(false);
        File dt = c.getMainView().chooseFile("Choose a requests file");
        if(dt != null){
            c.getDeliveryTour().read(dt.getAbsolutePath(), c.getCityMap());
        }
    }



    @Override
    public void loadMap(Controller c){
        c.getDeliveryTour().removeAllRequests();
        File map = c.getMainView().chooseFile("Choose a map file");
        if(map != null) {
            c.getCityMap().read(map.getAbsolutePath());
        }
    }

}
