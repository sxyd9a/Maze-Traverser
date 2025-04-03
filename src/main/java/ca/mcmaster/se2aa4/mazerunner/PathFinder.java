package ca.mcmaster.se2aa4.mazerunner;

public abstract class PathFinder { //Class that holds various methods that are universal for finding a path through maze regardless of strategy

    public int[] determineStartPos(TileType [][] maze){ //Determining the start of the maze
        int[] startPos = new int[2];

        startPos[1] = 0;

        for(int row = 0; row<maze.length; row++){
            if(maze[row][0] == TileType.OPEN){ //As soon as there is an empty space in the first column, that indicates the start position
                startPos[0] = row;
                break;
            }             
        }
        return startPos;
    }

    public int[] determineFinalPos(TileType [][] maze){ //Determining the end of the maze
        int[] finalPos = new int[2];

        finalPos[1] = maze[0].length-1;

        for(int row = maze.length-1; row>=0; row--){
            if(maze[row][maze[0].length-1] == TileType.OPEN){ //First empty space in the last column is indicative of end position 
                finalPos[0] = row;
                break;
            }             
        }
        return finalPos;
    }

    public abstract String canonicalPathSearch(TileType[][] maze, int[] startPos, int[] finalPos); //Method that finds a canonical format of a path through the maze

    public abstract boolean validMove(TileType[][] maze, int row, int col); //Method that checks if the next intended movement is doable

    public abstract boolean validatePath(TileType[][] maze, int[] startPos, int[] finalPos, String userPath); //Method to check if the path entered by the user leads to maze finish

    public abstract String factorizePath(String canonicalPath); //Method to convert any canonical path into its factorized version
}
