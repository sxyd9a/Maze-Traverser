package ca.mcmaster.se2aa4.mazerunner.command;

public class TurnLeftCommand implements MazeMoveCommand {
    private final MazeContext context;
    private int prevDirection;

    public TurnLeftCommand(MazeContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        prevDirection = context.direction;
        context.direction = (context.direction + 3) % 4;
    }

    @Override
    public void undo() {
        context.direction = prevDirection;
    }
}
