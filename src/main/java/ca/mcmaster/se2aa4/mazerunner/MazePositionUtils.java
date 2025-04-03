package ca.mcmaster.se2aa4.mazerunner;

//Utility class to determine start and end positions of the maze
public class MazePositionUtils {

    //Finding the start position 
    public static int[] findStart(TileType[][] maze) {
        for (int row = 0; row < maze.length; row++) {
            if (maze[row][0] == TileType.OPEN) return new int[] {row, 0};
        }
        return new int[] {-1, -1}; //Return invalid if not found
    }

    //Finding the final position
    public static int[] findEnd(TileType[][] maze) {
        int lastCol = maze[0].length - 1;
        for (int row = maze.length - 1; row >= 0; row--) {
            if (maze[row][lastCol] == TileType.OPEN) return new int[] {row, lastCol};
        }
        return new int[] {-1, -1}; //Return invalid if not found
    }
}
