package ca.mcmaster.se2aa4.mazerunner.strategy;

//Interface that defines a standard strategy for solving mazes

import ca.mcmaster.se2aa4.mazerunner.maze.TileType;

public interface MazeSolverStrategy {

    //method that finds a canonical format of a path through the maze
    String canonicalPathSearch(TileType[][] maze, int[] startPos, int[] finalPos);

    //method to convert any canonical path into its factorized version
    String factorizePath(String canonicalPath);

}
