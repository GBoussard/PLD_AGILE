package drawmap.controller;

public interface Command {

    public void doCommand();
    public void undoCommand();

}
