package ca.mcmaster.se2aa4.mazerunner.command;

import ca.mcmaster.se2aa4.mazerunner.maze.MazeContext;

public class TurnRightCommand implements MazeMoveCommand {
    private final MazeContext context;
    private int prevDirection;

    public TurnRightCommand(MazeContext context) {
        this.context = context;
    }

    @Override
    public void execute() { //turning right in an rhs based search
        prevDirection = context.getDirection();
        int newDir = (prevDirection + 1) % 4;
        context.setDirection(newDir);
    }

    @Override
    public void undo() {
        context.setDirection(prevDirection);
    }
}