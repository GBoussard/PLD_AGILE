package drawmap.controller;
import drawmap.model.CityMap;

import java.io.File;

public class MapLoadedState implements State {

    @Override
    public void loadRequests(Controller c){
        File dt = c.getMainView().chooseFile("Choose a requests file");
        if(dt != null){
            c.getDeliveryTour().read(dt.getAbsolutePath(), c.getCityMap());
        }
    }

    @Override
    public void undo(ListOfCommands l){
        l.undo();
    }

    @Override
    public void redo(ListOfCommands l){
        l.redo();
    }
}
