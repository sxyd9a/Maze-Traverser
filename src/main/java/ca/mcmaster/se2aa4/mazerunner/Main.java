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

            int[][] maze = reader.loadMaze(mazePath);

            System.out.println("**** Maze Layout ****");
            for (int[] row : maze) {
                for (int cell : row) {
                    System.out.print(cell + " ");
                }
                System.out.println();
            }

            int[] start = finder.determineStartPos(maze);
            int[] finish = finder.determineFinalPos(maze);

            if(cmd.hasOption("p")){
                String userPath = cmd.getOptionValue("p");
                boolean alrdyFactorzd = false;
                for(int i = 0; i<userPath.length(); i++){
                    if(Character.isDigit(userPath.charAt(i))){
                        alrdyFactorzd = true; 
                        break;
                    }
                }
                boolean check;
                if(!alrdyFactorzd){
                    check = finder.validatePath(maze, start, finish, finder.factorizePath(userPath));
                }
                else{
                    check = finder.validatePath(maze, start, finish, userPath);
                }
                System.out.println(check ? "Your path works!" : "Invalid Path!");
            } else{
                System.out.println("Below is a canonical path through the maze:");
                String canonical = finder.canonicalPathSearch(maze, start, finish);
                System.out.println(canonical);
                System.out.println("Below is the same path factorized:");
                System.out.println(finder.factorizePath(canonical));
            }

        //error handling:
        } catch (ParseException e) {
            log.error("Failed to parse command-line arguments: " + e.getMessage()); 
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("MazeRunner", cliOptions);
        } catch (RuntimeException e) {
            log.error("Unexpected error occurred", e);
        } catch (OutOfMemoryError e) {
            log.error("Memory Overflow - Maze has no entry/exit points");
            System.out.println("Maze is unsolvable");
        }

        log.info("**** End of Maze Application");
    }
}
