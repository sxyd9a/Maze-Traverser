package ca.mcmaster.se2aa4.mazerunner.command;

public class TurnRightCommand implements MazeMoveCommand {
    private final MazeContext context;
    private int prevDirection;

    public TurnRightCommand(MazeContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        prevDirection = context.direction;
        context.direction = (context.direction + 1) % 4;
    }

    @Override
    public void undo() {
        context.direction = prevDirection;
    }
}
