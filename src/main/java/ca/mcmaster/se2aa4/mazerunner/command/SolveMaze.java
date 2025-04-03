package ca.mcmaster.se2aa4.mazerunner.command;

import ca.mcmaster.se2aa4.mazerunner.maze.TileType;
import ca.mcmaster.se2aa4.mazerunner.strategy.MazeSolverStrategy;

public class SolveMaze {
    private final MazeSolverStrategy solver;
    private final TileType[][] maze;
    private final int[] start;
    private final int[] finish;

    public SolveMaze(MazeSolverStrategy solver, TileType[][] maze, int[] start, int[] finish) {
        this.solver = solver;
        this.maze = maze;
        this.start = start;
        this.finish = finish;
    }

    // Executes the rhs or bfs algorithm to find a path through the maze
    public void completePath() {
        String canonical = solver.canonicalPathSearch(maze, start, finish);
        if (canonical.isEmpty()) {
            System.out.println("There is no path through the maze");
        } else {
            System.out.println("Below is a canonical path through the maze:");
            System.out.println(canonical);
            System.out.println("Below is the same path factorized:");
            System.out.println(solver.factorizePath(canonical));
        }
    }
}
