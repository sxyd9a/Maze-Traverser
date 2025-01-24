package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MazeReader {

    private static final Logger log = LogManager.getLogger();
    private int[][] mazeGrid;

    public int[][] loadMaze(String mazeFile) {
        System.out.println("** Initializing Maze Loader");

        try {
            log.info("**** Loading maze from file: " + mazeFile);
            BufferedReader fileReader = new BufferedReader(new FileReader(mazeFile));

            int maxRows = 0;
            int maxCols = 0;
            String line;

            while ((line = fileReader.readLine()) != null) {
                maxRows++;
                maxCols = Math.max(maxCols, line.length());
            }

            fileReader.close();

            mazeGrid = new int[maxRows][maxCols];

            BufferedReader reader = new BufferedReader(new FileReader(mazeFile));
            int rowIndex = 0;

            while ((line = reader.readLine()) != null) {
                for (int colIndex = 0; colIndex < line.length(); colIndex++) {
                    mazeGrid[rowIndex][colIndex] = (line.charAt(colIndex) == ' ') ? 1 : 0;
                }
                rowIndex++;
            }

            reader.close();
            return mazeGrid;

        } catch (IOException e) {
            log.error("Error reading maze file", e);
            return null;
        }
    }

    public int[][] getMaze(){
        return mazeGrid;
    }
}
