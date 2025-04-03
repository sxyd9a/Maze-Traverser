package ca.mcmaster.se2aa4.mazerunner;

public class SolveMazeCommand implements MazeCommand {
    private final MazeSolverStrategy solver;
    private final TileType[][] maze;
    private final int[] start;
    private final int[] finish;

    public SolveMazeCommand(MazeSolverStrategy solver, TileType[][] maze, int[] start, int[] finish) {
        this.solver = solver;
        this.maze = maze;
        this.start = start;
        this.finish = finish;
    }

    //executes the rhs or bfs algorithm to find a path through the maze
    @Override
    public void execute() {
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
