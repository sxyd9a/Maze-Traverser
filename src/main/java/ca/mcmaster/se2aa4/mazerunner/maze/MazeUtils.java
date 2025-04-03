package ca.mcmaster.se2aa4.mazerunner.maze;

public class MazeUtils {

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

    public static boolean validMove(TileType[][] maze, int row, int col){ //check whether move is possible
        return row>=0 && row<maze.length && col>=0 && col<maze[0].length && maze[row][col] == TileType.OPEN;
    }

    public static String factorizePath(String canonicalPath) {
        if (canonicalPath == null || canonicalPath.isEmpty()) return "";

        StringBuilder factorizedPath = new StringBuilder();
        int moveCount = 1; //Start at 1 since 1 move minimum
        char prevMove = canonicalPath.charAt(0);

        for (int i = 1; i < canonicalPath.length(); i++) {
            char move = canonicalPath.charAt(i);
            if (move == prevMove) {
                moveCount++;
            } else {
                //Append previous move group
                if (moveCount == 1) {
                    factorizedPath.append(prevMove);
                } else {
                    factorizedPath.append(moveCount).append(prevMove);
                }
                //Reset for new move
                prevMove = move;
                moveCount = 1;
            }
        }

        //Append the last move group
        if (moveCount == 1) {
            factorizedPath.append(prevMove);
        } else {
            factorizedPath.append(moveCount).append(prevMove);
        }

        return factorizedPath.toString();
    }
}
