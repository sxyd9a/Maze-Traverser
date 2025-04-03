package ca.mcmaster.se2aa4.mazerunner.command;

import ca.mcmaster.se2aa4.mazerunner.maze.TileType;
import ca.mcmaster.se2aa4.mazerunner.strategy.MazeSolverStrategy;

public class ValidatePathCommand implements MazeMoveCommand {
    private final MazeSolverStrategy solver;
    private final TileType[][] maze;
    private final int[] start;
    private final int[] finish;
    private final String userPath;

    public ValidatePathCommand(MazeSolverStrategy solver, TileType[][] maze, int[] start, int[] finish, String userPath) {
        this.solver = solver;
        this.maze = maze;
        this.start = start;
        this.finish = finish;
        this.userPath = userPath;
    }

    //executes path validation of a given path based on rhs or bfs format
    @Override
    public void execute() {
        boolean isFactorized = userPath.chars().anyMatch(Character::isDigit);
        String pathToCheck = isFactorized ? userPath : solver.factorizePath(userPath);
        boolean valid = solver.validatePath(maze, start, finish, pathToCheck);
        System.out.println(valid ? "Your path works!" : "Invalid Path!");
    }

    @Override
    public void undo() {
        // Not applicable for this command
    }

}
