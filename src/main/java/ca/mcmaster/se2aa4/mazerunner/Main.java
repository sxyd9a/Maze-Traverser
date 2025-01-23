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

    public static void main(String[] args) {
        log.info("** Starting Maze Application");

        Options cliOptions = new Options();

        //Apache Commons Library for parsing cmd line args incorporated below:
        Option fileOption = Option.builder("i")
                .longOpt("input file")
                .desc("Path to the maze input file")
                .hasArg()
                .argName("MAZE_FILE")
                .required(true)
                .build();
        cliOptions.addOption(fileOption);

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

        //error handling:
        } catch (ParseException e) {
            log.error("Failed to parse command-line arguments: " + e.getMessage()); 
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("MazeRunner", cliOptions);
        } catch (RuntimeException e) {
            log.error("Unexpected error occurred", e);
            e.printStackTrace();
        }

        log.info("**** End of Maze Application");
    }
}
