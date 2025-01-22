package ca.mcmaster.se2aa4.mazerunner;

public class PathFinder {

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

}