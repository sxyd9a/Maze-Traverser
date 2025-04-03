package ca.mcmaster.se2aa4.mazerunner.command;

public class MoveForwardCommand implements MazeMoveCommand {
    private final MazeContext context;
    private final int[][] directions = {{-1,0},{0,1},{1,0},{0,-1}};
    private int prevRow, prevCol;

    public MoveForwardCommand(MazeContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        prevRow = context.row;
        prevCol = context.col;
        context.row += directions[context.direction][0];
        context.col += directions[context.direction][1];
    }

    @Override
    public void undo() {
        context.row = prevRow;
        context.col = prevCol;
    }
}
