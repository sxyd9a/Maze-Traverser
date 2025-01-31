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
            //Check which ever direction is to the right of the current direction

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
                        case 0 -> leftDir = 3;
                        case 1 -> leftDir = 0;
                        case 2 -> leftDir = 1;
                        case 3 -> leftDir = 2;
                        default -> throw new IllegalStateException("Invalid direction: " + currDirection);
                    }
                    //check if turning let is possible
                    currDirection = leftDir;
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

        int i = 0;
        int fwds = 0;
        while(i<userPath.length()){
            char move = userPath.charAt(i);

            if(Character.isDigit(move)){
                fwds = Character.getNumericValue(move);
            }
            else if(move == 'F'){
                for(int j = 0; j<fwds; j++){
                    int newRow = row + directions[currDirection][0];
                    int newCol = col + directions[currDirection][1];

                    if(!validMove(maze, newRow, newCol)){
                        return false;
                    }

                    row = newRow;
                    col = newCol;
                }

            }
            else if(move == 'R'){
                switch (currDirection) { //Get right direction
                    case 0 -> currDirection = 1;
                    case 1 -> currDirection = 2;
                    case 2 -> currDirection = 3;
                    case 3 -> currDirection = 0;
                    default -> throw new IllegalStateException("Invalid direction: " + currDirection);
                }
            }
            else if(move == 'L'){
                switch (currDirection) { //Get left direction
                    case 0 -> currDirection = 3;
                    case 1 -> currDirection = 0;
                    case 2 -> currDirection = 1;
                    case 3 -> currDirection = 2;
                    default -> throw new IllegalStateException("Invalid direction: " + currDirection);
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
        StringBuilder factorizedPath = new StringBuilder();
        int moveCount = 0;
        char prevMove = canonicalPath.charAt(0);

        for(char move : canonicalPath.toCharArray()){
            if(move == prevMove){
                moveCount++;
            }
            else{
                if(moveCount == 1){
                    factorizedPath.append(prevMove);
                } else {
                    factorizedPath.append(moveCount).append(prevMove);
                }
                prevMove = move;
                moveCount = 1;
            }
        }
        return factorizedPath.toString();
    }
}