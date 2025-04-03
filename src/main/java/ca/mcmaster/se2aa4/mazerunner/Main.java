package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.mazerunner.command.MazeCommand;
import ca.mcmaster.se2aa4.mazerunner.command.MazeCommandInvoker;
import ca.mcmaster.se2aa4.mazerunner.command.SolveMazeCommand;
import ca.mcmaster.se2aa4.mazerunner.command.ValidatePathCommand;
import ca.mcmaster.se2aa4.mazerunner.maze.MazeReader;
import ca.mcmaster.se2aa4.mazerunner.maze.TileType;
import ca.mcmaster.se2aa4.mazerunner.strategy.MazeSolverFactory;
import ca.mcmaster.se2aa4.mazerunner.strategy.MazeSolverStrategy;

public class Main {

    private static final Logger log = LogManager.getLogger();

    private static final MazeReader reader = new MazeReader();

    //parse cli arguments
    private static final ArgumentProcessor argumentProcessor = new ArgumentProcessor();

    //Holds the current maze-solving strategy
    private static MazeSolverStrategy solver;

    public static void main(String[] args) {
        log.info("** Starting Maze Application");

        try {
            //parser tracking
            CommandLine cmd = argumentProcessor.parseArguments(args);

            //Get maze file path and solver strategy
            String mazePath = cmd.getOptionValue("i");
            String strategy = cmd.getOptionValue("s");

            //Use factory to match with correct solver strategy
            solver = MazeSolverFactory.getSolver(strategy);

            //Load maze into a 2D array
            TileType[][] maze = reader.loadMaze(mazePath);

            printMaze(maze);

            //determine maze starting and ending positions
            int[] start = solver.determineStartPos(maze);
            int[] finish = solver.determineFinalPos(maze);

            MazeCommandInvoker invoker = new MazeCommandInvoker();

            //If a path is provided, validate. Otherwise, solve the maze path
            if (cmd.hasOption("p")) {
                MazeCommand validate = new ValidatePathCommand(solver, maze, start, finish, cmd.getOptionValue("p"));
                invoker.setCommand(validate);
            } else {
                MazeCommand solve = new SolveMazeCommand(solver, maze, start, finish);
                invoker.setCommand(solve);
            }

            invoker.runCommand();

        } catch (ParseException e) {
            //Error when parsing CLI arguments
            log.error("Argument parsing failed: {}", e.getMessage());
            System.err.println("Error: " + e.getMessage());
            argumentProcessor.printHelp();
        } catch (IllegalArgumentException e) {
            //Error when factory is given an invalid solver strategy
            log.error("Invalid input: {}", e.getMessage());
            System.err.println("Error: " + e.getMessage());
            argumentProcessor.printHelp();
        }

        log.info("**** End of Maze Application");
    }

    //Prints layout of maze on console for user
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
