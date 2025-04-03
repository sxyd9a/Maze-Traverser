package ca.mcmaster.se2aa4.mazerunner.command;

public class MazeCommandInvoker {
    private MazeMoveCommand command;

    //sets the user's desired task (validate or solve)
    public void setCommand(MazeMoveCommand command) {
        this.command = command;
    }

    //runs user's desired task
    public void runCommand() {
        if (command != null) {
            command.execute();
        } else {
            System.err.println("No command has been set.");
        }
    }
}
