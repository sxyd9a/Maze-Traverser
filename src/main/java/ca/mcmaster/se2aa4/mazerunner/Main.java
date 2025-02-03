package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//Name: Syed Abbas
//Project: A1 (Maze Path Finder)
//Date: 02/02/2025

public class Main {

    private static final Logger log = LogManager.getLogger();
    private static final MazeReader reader = new MazeReader();
    private static final PathFinder finder = new RightHandMazeSolver();

    public static void main(String[] args) {
        log.info("** Starting Maze Application");

        Options cliOptions = new Options();

        //Apache Commons Library for parsing cmd line args incorporated below:
        Option fileOption = Option.builder("i") //Using -i flag to input a path to the maze file
                .longOpt("input file")
                .desc("Path to the maze input file")
                .hasArg()
                .argName("MAZE_FILE")
                .required(true)
                .build();
        cliOptions.addOption(fileOption);

        Option pathOption = Option.builder("p") //Using -p flag to check if a path through the maze inputted by the user is correct
                .longOpt("path")
                .desc("Path inputted by user for validation check")
                .hasArg()
                .argName("PATH")
                .required(false)
                .build();
        cliOptions.addOption(pathOption);

        CommandLineParser cmdParser = new DefaultParser();

        try {
            CommandLine cmd = cmdParser.parse(cliOptions, args);

            String mazePath = cmd.getOptionValue("i");

            int[][] maze = reader.loadMaze(mazePath); //load the maze into a 2D array

            System.out.println("**** Maze Layout ****");
            for (int[] row : maze) { //print maze as a grid of 1s and 0s
                for (int cell : row) {
                    System.out.print(cell + " ");
                }
                System.out.println();
            }

            int[] start = finder.determineStartPos(maze);
            int[] finish = finder.determineFinalPos(maze);

            if(cmd.hasOption("p")){ //case when user inputs a path to be checked
                String userPath = cmd.getOptionValue("p");
                boolean alrdyFactorzd = false;
                for(int i = 0; i<userPath.length(); i++){ //check if the path has already been factorized
                    if(Character.isDigit(userPath.charAt(i))){
                        alrdyFactorzd = true; 
                        break;
                    }
                }
                boolean check;
                if(!alrdyFactorzd){ //If inputted path not factorized, do so and then validate
                    check = finder.validatePath(maze, start, finish, finder.factorizePath(userPath));
                }
                else{
                    check = finder.validatePath(maze, start, finish, userPath);
                }
                System.out.println(check ? "Your path works!" : "Invalid Path!");
            } else{ //Case where user wants to find a path through the maze
                String canonical = finder.canonicalPathSearch(maze, start, finish); //Store canonical path
                if(canonical.isEmpty()){ //print message when no path is found
                    System.out.println("There is no path through the maze");
                } else {
                    System.out.println("Below is a canonical path through the maze:");
                    System.out.println(canonical);
                    System.out.println("Below is the same path factorized:");
                    System.out.println(finder.factorizePath(canonical)); //print factorized path
                }
            }

        //error handling:
        } catch (ParseException e) { //check if necessary/optional flags are included (i.e. -i, -p)
            log.error("Failed to parse command-line arguments: " + e.getMessage()); 
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("MazeRunner", cliOptions);
        } catch (RuntimeException e) { //handle unidentified errors
            log.error("Unexpected error occurred", e);
        } catch (OutOfMemoryError e) { //handle when maze is inaccessible
            log.error("Memory Overflow - Maze has no entry/exit points");
            System.out.println("Maze is unsolvable");
        }

        log.info("**** End of Maze Application");
    }
}
