package ca.mcmaster.se2aa4.mazerunner;

public class RightHandMazeSolver extends PathFinder {

    @Override
    public String canonicalPathSearch(int[][] maze, int[] startPos, int[] finalPos){
        StringBuilder canonicalPath = new StringBuilder();

        int row = startPos[0]; //start at initial position
        int col = startPos[1]; //start at initial position

        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; //Corresponds to North, East, South and West

        int currDirection = 1; //Start facing east (intending to move forward)

        while(!(row == finalPos[0] && col == finalPos[1])){ //Keep checking for a movement so long as the final spot has not been reached
            
            int rightDir;
            switch (currDirection) { //Check which ever direction is to the right of the current direction
                case 0 -> rightDir = 1;
                case 1 -> rightDir = 2;
                case 2 -> rightDir = 3;
                case 3 -> rightDir = 0;
                default -> throw new IllegalStateException("Invalid direction: " + currDirection);
            }

            int rightRow = row + directions[rightDir][0]; //Determine row to the right
            int rightCol = col + directions[rightDir][1]; //Determine column to the right

            if(validMove(maze, rightRow, rightCol)){ //check if turning right and moving forward is possible
                currDirection = rightDir; //Set new direction faced
                row = rightRow;
                col = rightCol;
                canonicalPath.append("R");
                canonicalPath.append("F");
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
                    int leftDir;
                    switch (currDirection) { //check if turning let is possible
                        case 0 -> leftDir = 3;
                        case 1 -> leftDir = 0;
                        case 2 -> leftDir = 1;
                        case 3 -> leftDir = 2;
                        default -> throw new IllegalStateException("Invalid direction: " + currDirection);
                    }
                    //check if turning let is possible
                    currDirection = leftDir; //Set new direction
                    canonicalPath.append("L");
                }
            }
           
        }
        return canonicalPath.toString();
    }

    @Override
    public boolean validatePath(int[][] maze, int[] startPos, int[] finalPos, String userPath){
        int row = startPos[0];
        int col = startPos[1];

        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        int currDirection = 1;

        int i = 0; //index to track which part of user's path we're on
        int motions = 0; //tracking the number of forward, left or right movements at a time
        while(i<userPath.length()){
            char move = userPath.charAt(i); //hold current part of path

            if(Character.isDigit(move)){ //If a number is encountered, set it to be the number of movements needed
                motions = Character.getNumericValue(move);
            }
            else if(move == 'F'){ //Handle case we're a forward signal is met
                if(motions > 0){ //Multiple forward movements:
                    for(int j = 0; j<motions; j++){ //Conduct each forward movement seperately
                        //Determine placement ahead
                        int newRow = row + directions[currDirection][0]; 
                        int newCol = col + directions[currDirection][1];
    
                        if(!validMove(maze, newRow, newCol)){ //Check if motion is doable
                            return false;
                        }
    
                        //set new position
                        row = newRow;
                        col = newCol;
                    }
                    motions = 0; //reset motions to 0 for next group
                } else { //Singular forward movement
                    row += directions[currDirection][0];
                    col += directions[currDirection][1];
                }

            }
            else if(move == 'R'){ //Handle case where a right signal is met
                if(motions>0){ //Multiple right turns:
                    for(int j = 0; j<motions; j++){
                        switch (currDirection) { //Get corresponding right direction
                            case 0 -> currDirection = 1;
                            case 1 -> currDirection = 2;
                            case 2 -> currDirection = 3;
                            case 3 -> currDirection = 0;
                            default -> throw new IllegalStateException("Invalid direction: " + currDirection);
                        }
                    }
                    motions = 0; //reset motions to 0 for next group
                } else { //Singular right turn
                    switch (currDirection) { 
                        case 0 -> currDirection = 1;
                        case 1 -> currDirection = 2;
                        case 2 -> currDirection = 3;
                        case 3 -> currDirection = 0;
                        default -> throw new IllegalStateException("Invalid direction: " + currDirection);
                    }
                }
            }
            else if(move == 'L'){ //Handle case where a left signal is met
                if(motions>0){ //Multiple left turns:
                    for(int j = 0; j<motions; j++){
                        switch (currDirection) { //Get corresponding left direction
                            case 0 -> currDirection = 3;
                            case 1 -> currDirection = 0;
                            case 2 -> currDirection = 1;
                            case 3 -> currDirection = 2;
                            default -> throw new IllegalStateException("Invalid direction: " + currDirection);
                        }
                    }
                    motions = 0; //Reset motions to 0 for next group
                } else { //Singular left turn
                    switch (currDirection) { 
                        case 0 -> currDirection = 3;
                        case 1 -> currDirection = 0;
                        case 2 -> currDirection = 1;
                        case 3 -> currDirection = 2;
                        default -> throw new IllegalStateException("Invalid direction: " + currDirection);
                    }
                }
            }
            else{
                return false;
            }
            i++;
        }

        return row == finalPos[0] && col == finalPos[1];
    }
    
    @Override
    public boolean validMove(int[][] maze, int row, int col){ //check whether move is possibe
        return row>=0 && row<maze.length && col>=0 && col<maze[0].length && maze[row][col] == 1;
    }

    @Override
    public String factorizePath(String canonicalPath) {
        StringBuilder factorizedPath = new StringBuilder(); //store factorized version of path
        int moveCount = 0; //count each move regardless of type
        char prevMove = canonicalPath.charAt(0); //store the previous made move

        for(char move : canonicalPath.toCharArray()){ //Iterate through each character in the canonical path
            if(move == prevMove){ //If the current move is the same as prior, assume we are counting one group
                moveCount++;
            }
            else{ //Add a completed group to the factorized path
                if(moveCount == 1){ //If the previous group (forward, right, or left) was one movement, no need to append a count
                    factorizedPath.append(prevMove);
                } else {
                    factorizedPath.append(moveCount).append(prevMove); //Otherwise append both the number of movements and the type of motion corresponding to the group
                }
                prevMove = move; //Set previous move to the move most recently made for next group
                moveCount = 1; //Count current iteration as a move
            }
        }

        //append last move group
        if(moveCount == 1){
            factorizedPath.append(prevMove);
        } else {
            factorizedPath.append(moveCount).append(prevMove);
        }

        return factorizedPath.toString();
    }
}