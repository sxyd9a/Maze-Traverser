package ca.mcmaster.se2aa4.mazerunner;

public abstract class PathFinder {

    public int[] determineStartPos(int [][] maze){
        int[] startPos = new int[2];

        startPos[1] = 0;

        for(int row = 0; row<maze.length; row++){
            if(maze[row][0] == 1){
                startPos[0] = row;
                break;
            }             
        }
        return startPos;
    }

    public int[] determineFinalPos(int [][] maze){
        int[] finalPos = new int[2];

        finalPos[1] = maze[0].length-1;

        for(int row = maze.length-1; row>=0; row--){
            if(maze[row][maze[0].length-1] == 1){
                finalPos[0] = row;
                break;
            }             
        }
        return finalPos;
    }

    public abstract String canonicalPathSearch(int[][] maze, int[] startPos, int[] finalPos);

    public abstract boolean validMove(int[][] maze, int row, int col);

    public abstract boolean validatePath(int[][] maze, int[] startPos, int[] finalPos, String userPath);

    public abstract String factorizePath(String canonicalPath);
}