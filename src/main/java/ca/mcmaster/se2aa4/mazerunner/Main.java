package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.mazerunner.command.SolveMaze;
import ca.mcmaster.se2aa4.mazerunner.command.ValidatePath;
import ca.mcmaster.se2aa4.mazerunner.maze.MazeReader;
import ca.mcmaster.se2aa4.mazerunner.maze.MazeUtils;
import ca.mcmaster.se2aa4.mazerunner.maze.TileType;
import ca.mcmaster.se2aa4.mazerunner.strategy.MazeSolverFactory;
import ca.mcmaster.se2aa4.mazerunner.strategy.MazeSolverStrategy;

public class Main {

    private static final Logger log = LogManager.getLogger();
    private static final MazeReader reader = new MazeReader();
    private static final ArgumentProcessor argumentProcessor = new ArgumentProcessor();

    public static void main(String[] args) {
        log.info("** Starting Maze Application");

        try {
            CommandLine cmd = argumentProcessor.parseArguments(args);
            String mazePath = cmd.getOptionValue("i");

            boolean hasStrategy = cmd.hasOption("s");
            boolean hasPath = cmd.hasOption("p");

            if (!hasStrategy && !hasPath) {
                throw new IllegalArgumentException("You must specify either a solver strategy (-s) or a path to validate (-p).");
            }

            String strategy = hasStrategy ? cmd.getOptionValue("s") : "rhs";


            MazeSolverStrategy solver = MazeSolverFactory.getSolver(strategy);
            TileType[][] maze = reader.loadMaze(mazePath);
            printMaze(maze);

            int[] start = MazeUtils.findStart(maze);
            int[] finish = MazeUtils.findEnd(maze);


            if (cmd.hasOption("p")) {
                ValidatePath validate = new ValidatePath(maze, start, finish, cmd.getOptionValue("p"));
                validate.completeValidation();
            } else {
                SolveMaze solve = new SolveMaze(solver, maze, start, finish);
                solve.completePath();
            }

        } catch (ParseException | IllegalArgumentException e) {
            log.error("Error occurred: {}", e.getMessage());
            System.err.println("Error: " + e.getMessage());
            argumentProcessor.printHelp();
        }

        log.info("**** End of Maze Application");
    }

    private static void printMaze(TileType[][] maze) {
        System.out.println("**** Maze Layout ****");
        for (TileType[] row : maze) {
            for (TileType cell : row) {
                System.out.print(cell == TileType.OPEN ? " " : "#");
            }
            System.out.println();
        }
    }
}