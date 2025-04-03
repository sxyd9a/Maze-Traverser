package ca.mcmaster.se2aa4.mazerunner.command;

public class MazeContext {
    public int row;
    public int col;
    public int direction; //0 - North, 1 - East, 2 - South, 3 - West

    public MazeContext(int row, int col, int direction) { //stores variables that change
        this.row = row;
        this.col = col;
        this.direction = direction;
    }
}