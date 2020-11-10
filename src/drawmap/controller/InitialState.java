package drawmap.controller;

import java.io.File;

public class InitialState implements State {

    @Override
    public void loadMap(Controller c){
        File map = c.getMainView().chooseFile("Choose a map file");
        if(map != null) {
            c.getCityMap().read(map.getAbsolutePath());
        }
    }



}
