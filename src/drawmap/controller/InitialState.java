package drawmap.controller;

import java.io.File;

public class InitialState implements State {

    @Override
    public boolean loadMap(ListOfCommands l, Controller c){
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
