package ca.mcmaster.se2aa4.mazerunner;

public class RightHandMazeSolver implements MazeSolverStrategy {

    @Override
    public String canonicalPathSearch(TileType[][] maze, int[] startPos, int[] finalPos){
        StringBuilder canonicalPath = new StringBuilder();

        int row = startPos[0]; //start at initial position
        int col = startPos[1]; //start at initial position

        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; //Corresponds to North, East, South and West

        int currDirection = 1; //Start facing east (intending to move forward)

        while(!(row == finalPos[0] && col == finalPos[1])){ //Keep checking for a movement so long as the final spot has not been reached
            
            int rightDir = (currDirection + 1) % 4; //Check which ever direction is to the right of the current direction
            int rightRow = row + directions[rightDir][0]; //Determine row to the right
            int rightCol = col + directions[rightDir][1]; //Determine column to the right

            if(validMove(maze, rightRow, rightCol)){ //check if turning right and moving forward is possible
                currDirection = rightDir; //Set new direction faced
                row = rightRow;
                col = rightCol;
                canonicalPath.append("R").append("F");
            }
            else { //check if just moving forward is possible
                int forwardRow = row + directions[currDirection][0]; //Determine row directly ahead
                int forwardCol = col + directions[currDirection][1]; //Determine column directly ahead

                if(validMove(maze, forwardRow, forwardCol)){ //check if moving forward is possible
                    row = forwardRow;
                    col = forwardCol;
                    canonicalPath.append("F");
                }
                else{ 
                    currDirection = (currDirection + 3) % 4; //turn left
                    canonicalPath.append("L");
                }
            }
        }

        return canonicalPath.toString();
    }

    @Override
    public boolean validatePath(TileType[][] maze, int[] startPos, int[] finalPos, String userPath){
        int row = startPos[0]; //Starting row position
        int col = startPos[1]; //Starting column position

        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; //Directions: North, East, South, West

        int currDirection = 1; //Start by facing East 

        int i = 0; //Index to iterate through the user's path string
        int motions = 0; //To store number values before a move 

        while(i < userPath.length()){ //Loop through each character in the path string
            char move = userPath.charAt(i); //Current character (either a digit or a direction command)

            if(Character.isDigit(move)){ //If it's a digit, build the motions count (multi-digit supported)
                motions = motions * 10 + Character.getNumericValue(move);
            }
            else if(move == 'F'){ //Move forward in current direction
                motions = (motions == 0) ? 1 : motions; //Default to 1 if no digit prefix
                for(int j = 0; j < motions; j++){
                    int newRow = row + directions[currDirection][0]; //Calculate new row
                    int newCol = col + directions[currDirection][1]; //Calculate new column
                    if(!validMove(maze, newRow, newCol)) return false; //If next step is invalid, return false
                    row = newRow; //Update current row
                    col = newCol; //Update current column
                }
                motions = 0; //Reset motion counter for next instruction
            }
            else if(move == 'R'){ //Turn right
                motions = (motions == 0) ? 1 : motions; //Default to 1 if no digit prefix
                currDirection = (currDirection + motions) % 4; //Update direction clockwise
                motions = 0; //Reset motion counter
            }
            else if(move == 'L'){ //Turn left
                motions = (motions == 0) ? 1 : motions; //Default to 1 if no digit prefix
                currDirection = (currDirection + 4 - motions % 4) % 4; //Update direction counter-clockwise
                motions = 0; //Reset motion counter
            }
            else return false; //Invalid character

            i++; //Move to next character in path
        }

        //Return true only if we reached the final position
        return row == finalPos[0] && col == finalPos[1];
    }

    
    public boolean validMove(TileType[][] maze, int row, int col){ //check whether move is possible
        return row>=0 && row<maze.length && col>=0 && col<maze[0].length && maze[row][col] == TileType.OPEN;
    }

    @Override
    public String factorizePath(String canonicalPath) {
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


    @Override
    public int[] determineStartPos(TileType[][] maze) {
        return MazePositionUtils.findStart(maze);
    }

    @Override
    public int[] determineFinalPos(TileType[][] maze) {
        return MazePositionUtils.findEnd(maze);
    }

}
