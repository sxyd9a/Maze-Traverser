package ca.mcmaster.se2aa4.mazerunner.maze;

import ca.mcmaster.se2aa4.mazerunner.command.MazeMoveCommand;

public class MazeContext {
    private int row;
    private int col;
    private int direction; // 0 - North, 1 - East, 2 - South, 3 - West

    public MazeContext(int row, int col, int direction) {
        this.row = row;
        this.col = col;
        this.direction = direction;
    }

    //getters
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getDirection() {
        return direction;
    }

    //setters
    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    //execute command using this context
    public boolean executeCommand(MazeMoveCommand command) {
        command.execute();
        return true; //
    }

    //undo a previously executed command
    public void undoCommand(MazeMoveCommand command) {
        command.undo();
    }
}