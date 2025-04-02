package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Name: Syed Abbas
// Project: A1 (Maze Path Finder)
// Date: 02/02/2025

public class Main {

    private static final Logger log = LogManager.getLogger();
    private static final MazeReader reader = new MazeReader();
    private static final PathFinder finder = new RightHandMazeSolver();
    private static final ArgumentProcessor argumentProcessor = new ArgumentProcessor();

    public static void main(String[] args) {
        log.info("** Starting Maze Application");

        try {
            // Parse the arguments
            CommandLine cmd = argumentProcessor.parseArguments(args);

            String mazePath = cmd.getOptionValue("i");
            int[][] maze = reader.loadMaze(mazePath); // Load the maze into a 2D array

            System.out.println("**** Maze Layout ****");
            for (int[] row : maze) { // Print maze as a grid of 1s and 0s
                for (int cell : row) {
                    System.out.print(cell + " ");
                }
                System.out.println();
            }

            int[] start = finder.determineStartPos(maze);
            int[] finish = finder.determineFinalPos(maze);

            if (cmd.hasOption("p")) { // Case when user inputs a path to be checked
                String userPath = cmd.getOptionValue("p");
                boolean alrdyFactorzd = false;
                for (int i = 0; i < userPath.length(); i++) { // Check if the path has already been factorized
                    if (Character.isDigit(userPath.charAt(i))) {
                        alrdyFactorzd = true;
                        break;
                    }
                }
                boolean check;
                if (!alrdyFactorzd) { // If inputted path not factorized, do so and then validate
                    check = finder.validatePath(maze, start, finish, finder.factorizePath(userPath));
                } else {
                    check = finder.validatePath(maze, start, finish, userPath);
                }
                System.out.println(check ? "Your path works!" : "Invalid Path!");
            } else { // Case where user wants to find a path through the maze
                String canonical = finder.canonicalPathSearch(maze, start, finish); // Store canonical path
                if (canonical.isEmpty()) { // Print message when no path is found
                    System.out.println("There is no path through the maze");
                } else {
                    System.out.println("Below is a canonical path through the maze:");
                    System.out.println(canonical);
                    System.out.println("Below is the same path factorized:");
                    System.out.println(finder.factorizePath(canonical)); // Print factorized path
                }
            }

        } catch (ParseException e) { // Handle errors in argument parsing or runtime
            log.error("Error occurred", e);
            argumentProcessor.printHelp();
        }

        log.info("**** End of Maze Application");
    }
}
