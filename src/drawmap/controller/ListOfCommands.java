package drawmap.controller;

import java.util.LinkedList;

public class ListOfCommands {
    private LinkedList<Command> listCommands;
    private int i;

    public ListOfCommands() {
        i = -1;
        listCommands = new LinkedList<Command>();
    }

    public void addCommands(Command com){
        i++;
        listCommands.add(i, com);
        com.doCommand();
    }

    public void undo(){
        if(i>=0) {
            listCommands.get(i).undoCommand();
            i--;
        }

    }
    public void redo(){
        if(i+1 < listCommands.size()) {
            i++;
            listCommands.get(i).doCommand();
        }
    }
}