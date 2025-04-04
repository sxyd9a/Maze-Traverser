package ca.mcmaster.se2aa4.mazerunner.command;

import ca.mcmaster.se2aa4.mazerunner.maze.MazeContext;

public class TurnLeftCommand implements MazeMoveCommand {
    private final MazeContext context;
    private int prevDirection;

    public TurnLeftCommand(MazeContext context) {
        this.context = context;
    }

    @Override
    public void execute() { //turning left in an rhs based search
        prevDirection = context.getDirection();
        int newDir = (prevDirection + 3) % 4;
        context.setDirection(newDir);
    }

    @Override
    public void undo() {
        context.setDirection(prevDirection);
    }
}