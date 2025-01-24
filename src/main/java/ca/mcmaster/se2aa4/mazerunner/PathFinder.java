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

    public String rightHandSearch(int[][] maze, int[] startPos, int[] finalPos){
        StringBuilder canonicalPath = new StringBuilder();

        int row = startPos[0];
        int col = startPos[1];

        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; //Corresponds to North, East, South and West

        int currDirection = 1; //Start facing east (intending to move forward)

        while(!(row == finalPos[0] && col == finalPos[1])){ //Keep checking for a movement so long as the final spot has not been reached
            
            int rightDir;
            switch (currDirection) { //Check which ever direction is to the right of the current direction
                case 0:
                    rightDir = 1;
                    break;
                case 1:
                    rightDir = 2;
                    break;
                case 2:
                    rightDir = 3;
                    break;
                case 3:
                    rightDir = 0;
                    break;
                default:
                    throw new IllegalStateException("Invalid direction: " + currDirection);
            }

            int rightRow = row + directions[rightDir][0];
            int rightCol = col + directions[rightDir][1];

            if(validMove(maze, rightRow, rightCol)){ //check if turning right and moving forward is possible
                currDirection = rightDir;
                row = rightRow;
                col = rightCol;
                canonicalPath.append("R");
                canonicalPath.append("F");
            }
            else {
                int forwardRow = row + directions[currDirection][0];
                int forwardCol = col + directions[currDirection][1];

                if(validMove(maze, forwardRow, forwardCol)){ //check if moving forward is possible
                    row = forwardRow;
                    col = forwardCol;
                    canonicalPath.append("F");
                }
                else{
                    int leftDir;
                    switch (currDirection) { //check if turning let is possible
                        case 0:
                            leftDir = 3;
                            break;
                        case 1:
                            leftDir = 0;
                            break;
                        case 2:
                            leftDir = 1;
                            break;
                        case 3:
                            leftDir = 2;
                            break;
                        default:
                            throw new IllegalStateException("Invalid direction: " + currDirection);
                    }
                    currDirection = leftDir;
                    canonicalPath.append("L");
                }
            }
           
        }
        return canonicalPath.toString();
    }

    public boolean validMove(int[][] maze, int row, int col){ //check whether move is possibe
        return row>=0 && row<maze.length && col>=0 && col<maze[0].length && maze[row][col] == 1 ? true : false;
    }

}