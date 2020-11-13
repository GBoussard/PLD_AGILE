package drawmap.controller;

import java.io.File;

public class InitialState implements State {

    @Override
    public boolean loadMap(Controller c){
        File map = c.getMainView().chooseFile("Choose a map file");
        if(map != null) {
            return c.getCityMap().read(map.getAbsolutePath());
        }
        return false;
    }



}
