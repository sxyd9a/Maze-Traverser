package ca.mcmaster.se2aa4.mazerunner;

public class RightHandMazeSolver implements MazeSolverStrategy {

    @Override
    public String canonicalPathSearch(TileType[][] maze, int[] startPos, int[] finalPos){
        StringBuilder canonicalPath = new StringBuilder();

        int row = startPos[0]; //start at initial position
        int col = startPos[1]; //start at initial position

        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; //corresponds to North, East, South and West

        int currDirection = 1; //start facing east 

        while (!(row == finalPos[0] && col == finalPos[1])) { //keep checking for a movement so long as the final spot has not been reached
            
            int rightDir = (currDirection + 1) % 4; //check which ever direction is to the right of the current direction
            int rightRow = row + directions[rightDir][0]; //determine row to the right
            int rightCol = col + directions[rightDir][1]; //determine column to the right

            if (MazeUtils.validMove(maze, rightRow, rightCol)) { //check if turning right and moving forward is possible
                currDirection = rightDir; //set new direction faced
                row = rightRow;
                col = rightCol;
                canonicalPath.append("R").append("F");
            }
            else { //check if just moving forward is possible
                int forwardRow = row + directions[currDirection][0]; //determine row directly ahead
                int forwardCol = col + directions[currDirection][1]; //determine column directly ahead

                if (MazeUtils.validMove(maze, forwardRow, forwardCol)) { //check if moving forward is possible
                    row = forwardRow;
                    col = forwardCol;
                    canonicalPath.append("F");
                }
                else { 
                    currDirection = (currDirection + 3) % 4; //turn left
                    canonicalPath.append("L");
                }
            }
        }

        return canonicalPath.toString();
    }

    @Override
    public boolean validatePath(TileType[][] maze, int[] startPos, int[] finalPos, String userPath){
        int row = startPos[0]; //starting row position
        int col = startPos[1]; //starting column position

        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; 

        int currDirection = 1; //start by facing East 

        int i = 0; 
        int motions = 0; //store number values before a move 

        while (i < userPath.length()) { //loop through each character in the user path string
            char move = userPath.charAt(i); //current character (either a digit or a direction command)

            if (Character.isDigit(move)) { //if it's a digit, build the motions count (multi-digit supported)
                motions = motions * 10 + Character.getNumericValue(move);
            }
            else if (move == 'F') { //move forward in current direction
                motions = (motions == 0) ? 1 : motions; //default to 1 if no digit prefix
                for (int j = 0; j < motions; j++) {
                    int newRow = row + directions[currDirection][0]; //calculate new row
                    int newCol = col + directions[currDirection][1]; //calculate new column
                    if (!MazeUtils.validMove(maze, newRow, newCol)){
                        return false; //if next step is invalid, return false
                    } 
                    row = newRow; 
                    col = newCol; 
                }
                motions = 0;
            }
            else if (move == 'R') { //turn right
                motions = (motions == 0) ? 1 : motions; 
                currDirection = (currDirection + motions) % 4; //update direction clockwise
                motions = 0; 
            }
            else if (move == 'L') { //turn left
                motions = (motions == 0) ? 1 : motions; 
                currDirection = (currDirection + 4 - motions % 4) % 4; //update direction counter-clockwise
                motions = 0; 
            }
            else return false; //invalid character

            i++; //move to next character in path
        }

        //return true only if we reached the final position
        return row == finalPos[0] && col == finalPos[1];
    }

    @Override
    public String factorizePath(String canonicalPath) {
        return MazeUtils.factorizePath(canonicalPath);
    }

    @Override
    public int[] determineStartPos(TileType[][] maze) {
        return MazeUtils.findStart(maze);
    }

    @Override
    public int[] determineFinalPos(TileType[][] maze) {
        return MazeUtils.findEnd(maze);
    }
}
