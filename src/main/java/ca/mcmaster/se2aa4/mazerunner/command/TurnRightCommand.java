package ca.mcmaster.se2aa4.mazerunner.command;

public class TurnRightCommand implements MazeMoveCommand {
    private final MazeContext context;
    private int prevDirection;

    public TurnRightCommand(MazeContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        prevDirection = context.getDirection();
        int newDir = (prevDirection + 1) % 4;
        context.setDirection(newDir);
    }

    @Override
    public void undo() {
        context.setDirection(prevDirection);
    }
}
