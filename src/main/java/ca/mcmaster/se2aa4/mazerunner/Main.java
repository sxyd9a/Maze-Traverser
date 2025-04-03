package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger log = LogManager.getLogger();
    private static final MazeReader reader = new MazeReader();
    private static final ArgumentProcessor argumentProcessor = new ArgumentProcessor();
    private static MazeSolverStrategy solver;

    public static void main(String[] args) {
        log.info("** Starting Maze Application");

        try {
            CommandLine cmd = argumentProcessor.parseArguments(args);
            String mazePath = cmd.getOptionValue("i");
            String strategy = cmd.getOptionValue("s"); 
            solver = MazeSolverFactory.getSolver(strategy);

            TileType[][] maze = reader.loadMaze(mazePath);
            printMaze(maze);

            int[] start = solver.determineStartPos(maze);
            int[] finish = solver.determineFinalPos(maze);

            if (cmd.hasOption("p")) {
                String userPath = cmd.getOptionValue("p");
                boolean isFactorized = userPath.chars().anyMatch(Character::isDigit);
                String pathToCheck = isFactorized ? userPath : solver.factorizePath(userPath);
                boolean valid = solver.validatePath(maze, start, finish, pathToCheck);
                System.out.println(valid ? "Your path works!" : "Invalid Path!");
            } else {
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

        } catch (ParseException e) {
            log.error("Argument parsing failed: {}", e.getMessage());
            System.err.println("Error: " + e.getMessage());
            argumentProcessor.printHelp();
        } catch (IllegalArgumentException e) {
            log.error("Invalid input: {}", e.getMessage());
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
