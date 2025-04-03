package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ArgumentProcessor {

    private final Options options;

    public ArgumentProcessor() {
        this.options = defineOptions();
    }

    private Options defineOptions() {
        Options options = new Options();

        Option inputFileOption = Option.builder("i")
                .longOpt("input")
                .desc("Path to the maze input file")
                .hasArg()
                .argName("FILE")
                .required(true)
                .build();
        options.addOption(inputFileOption);

        Option pathOption = Option.builder("p")
                .longOpt("path")
                .desc("User-provided path to validate (e.g., FFFF)")
                .hasArg()
                .argName("PATH")
                .required(false)
                .build();
        options.addOption(pathOption);

        Option solverOption = Option.builder("s")
                .longOpt("solver")
                .desc("Maze solving strategy to use (right or bfs)")
                .hasArg()
                .argName("SOLVER")
                .required(false)
                .build();
        options.addOption(solverOption);

        return options;
    }

    public CommandLine parseArguments(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }

    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MazeRunner", options);
    }
}
