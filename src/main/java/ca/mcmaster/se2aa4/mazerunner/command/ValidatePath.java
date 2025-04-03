package ca.mcmaster.se2aa4.mazerunner.command;

import ca.mcmaster.se2aa4.mazerunner.maze.TileType;
import ca.mcmaster.se2aa4.mazerunner.strategy.MazeSolverStrategy;

public class ValidatePath {
    private final MazeSolverStrategy solver;
    private final TileType[][] maze;
    private final int[] start;
    private final int[] finish;
    private final String userPath;

    public ValidatePath(MazeSolverStrategy solver, TileType[][] maze, int[] start, int[] finish, String userPath) {
        this.solver = solver;
        this.maze = maze;
        this.start = start;
        this.finish = finish;
        this.userPath = userPath;
    }

    // Executes path validation of a given path based on rhs or bfs format
    public void completeValidation() {
        boolean isFactorized = userPath.chars().anyMatch(Character::isDigit);
        String pathToCheck = isFactorized ? userPath : solver.factorizePath(userPath);
        boolean valid = solver.validatePath(maze, start, finish, pathToCheck);
        System.out.println(valid ? "Your path works!" : "Invalid Path!");
    }
}
