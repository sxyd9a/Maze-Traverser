package ca.mcmaster.se2aa4.mazerunner.command;

import ca.mcmaster.se2aa4.mazerunner.maze.MazeContext;

public class MoveForwardCommand implements MazeMoveCommand {
    private final MazeContext context;
    private int prevRow, prevCol;

    private static final int[][] directions = {
        {-1, 0}, {0, 1}, {1, 0}, {0, -1}
    };

    public MoveForwardCommand(MazeContext context) {
        this.context = context;
    }

    @Override
    public void execute() { //moving forward in an rhs based search
        prevRow = context.getRow();
        prevCol = context.getCol();

        int dir = context.getDirection();
        context.setRow(prevRow + directions[dir][0]);
        context.setCol(prevCol + directions[dir][1]);
    }

    @Override
    public void undo() {
        context.setRow(prevRow);
        context.setCol(prevCol);
    }
}