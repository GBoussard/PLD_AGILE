package drawmap.controller;
import drawmap.model.CityMap;

import java.io.File;

public class MapLoadedState implements State {

    @Override
    public boolean loadRequests(ListOfCommands l, Controller c){
        c.getComputeTour().setComputed(false);
        File dt = c.getMainView().chooseFile("Choose a requests file");
        boolean status = false;
        if(dt != null){
            status = c.getDeliveryTour().read(dt.getAbsolutePath(), c.getCityMap());
            if(status)  {
                l.getListCommands().clear();
                l.setI(-1);
            }
        }
        return status;
    }



    @Override
    public boolean loadMap(ListOfCommands l, Controller c){
        c.getDeliveryTour().removeAllRequests();
        File map = c.getMainView().chooseFile("Choose a map file");
        boolean status = false;
        if(map != null) {
            status = c.getCityMap().read(map.getAbsolutePath());
            if(status)  {
                l.getListCommands().clear();
                l.setI(-1);
            }
        }
        return status;
    }

}
