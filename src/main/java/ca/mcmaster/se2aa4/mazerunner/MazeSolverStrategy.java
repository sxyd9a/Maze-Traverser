package ca.mcmaster.se2aa4.mazerunner;

//Interface that defines a standard strategy for solving mazes
public interface MazeSolverStrategy {

    //Method that finds a canonical format of a path through the maze
    String canonicalPathSearch(TileType[][] maze, int[] startPos, int[] finalPos);

    //Method that checks if the path entered by the user leads to maze finish
    boolean validatePath(TileType[][] maze, int[] startPos, int[] finalPos, String userPath);

    //Method to convert any canonical path into its factorized version
    String factorizePath(String canonicalPath);

    //Method to determine the start position of the maze
    int[] determineStartPos(TileType[][] maze);

    //Method to determine the end position of the maze
    int[] determineFinalPos(TileType[][] maze);
}
