package drawmap.controller;

public class ReverseCommand implements Command {

    private Command command;

    public ReverseCommand(Command command){
        this.command = command;
    }

    @Override
    public void doCommand(){
        command.undoCommand();
    }

    @Override
    public void undoCommand(){
        command.doCommand();
    }
}
