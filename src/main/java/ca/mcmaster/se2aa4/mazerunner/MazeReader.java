package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MazeReader {

    private static final Logger log = LogManager.getLogger();
    private int[][] mazeGrid; //store the maze

    public int[][] loadMaze(String mazeFile) { //method to lad text file into maze array
        System.out.println("** Initializing Maze Loader");

        try {
            log.info("**** Loading maze from file: " + mazeFile);
            BufferedReader fileReader = new BufferedReader(new FileReader(mazeFile));

            int maxRows = 0;
            int maxCols = 0;
            String line;

            while ((line = fileReader.readLine()) != null) { //determining maze boundries
                maxRows++;
                maxCols = Math.max(maxCols, line.length());
            }

            fileReader.close();

            mazeGrid = new int[maxRows][maxCols]; //creating grid based on max rows and cols

            BufferedReader reader = new BufferedReader(new FileReader(mazeFile));
            int rowIndex = 0;

            while ((line = reader.readLine()) != null) { //Storing an empty space as a 1 and a wall as a 0 in a 2D array
                for (int colIndex = 0; colIndex < line.length(); colIndex++) {
                    mazeGrid[rowIndex][colIndex] = (line.charAt(colIndex) == ' ') ? 1 : 0;
                }
                rowIndex++;
            }

            reader.close();
            return mazeGrid;

        } catch (IOException e) { //Error interpreting the text file
            log.error("Error reading maze file", e);
            return null;
        } catch (IllegalArgumentException e){ //File itself is not correctly formatted
            log.error("Invalid file format: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
        } catch (RuntimeException e) { //Unidentified Error
            log.error("Unexpected error occurred", e);
        }
        return null;
    }

}
