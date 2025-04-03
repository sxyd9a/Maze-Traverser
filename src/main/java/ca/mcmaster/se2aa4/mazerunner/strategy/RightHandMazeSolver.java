package ca.mcmaster.se2aa4.mazerunner.strategy;

import ca.mcmaster.se2aa4.mazerunner.command.MazeContext;
import ca.mcmaster.se2aa4.mazerunner.command.MazeMoveCommand;
import ca.mcmaster.se2aa4.mazerunner.command.MazeMoveHistory;
import ca.mcmaster.se2aa4.mazerunner.command.MoveForwardCommand;
import ca.mcmaster.se2aa4.mazerunner.command.TurnLeftCommand;
import ca.mcmaster.se2aa4.mazerunner.command.TurnRightCommand;
import ca.mcmaster.se2aa4.mazerunner.maze.MazeUtils;
import ca.mcmaster.se2aa4.mazerunner.maze.TileType;

public class RightHandMazeSolver implements MazeSolverStrategy {

    @Override
    public String canonicalPathSearch(TileType[][] maze, int[] startPos, int[] finalPos){
        StringBuilder canonicalPath = new StringBuilder();

        MazeContext context = new MazeContext(startPos[0], startPos[1], 1); //start East
        MazeMoveHistory history = new MazeMoveHistory();
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        while (!(context.row == finalPos[0] && context.col == finalPos[1])) {
            int rightDir = (context.direction + 1) % 4;
            int rightRow = context.row + directions[rightDir][0];
            int rightCol = context.col + directions[rightDir][1];

            if (MazeUtils.validMove(maze, rightRow, rightCol)) {
                MazeMoveCommand turnRight = new TurnRightCommand(context);
                MazeMoveCommand moveForward = new MoveForwardCommand(context);
                turnRight.execute();
                moveForward.execute();
                history.push(turnRight);
                history.push(moveForward);
                canonicalPath.append("R").append("F");
            } else {
                int forwardRow = context.row + directions[context.direction][0];
                int forwardCol = context.col + directions[context.direction][1];

                if (MazeUtils.validMove(maze, forwardRow, forwardCol)) {
                    MazeMoveCommand moveForward = new MoveForwardCommand(context);
                    moveForward.execute();
                    history.push(moveForward);
                    canonicalPath.append("F");
                } else {
                    MazeMoveCommand turnLeft = new TurnLeftCommand(context);
                    turnLeft.execute();
                    history.push(turnLeft);
                    canonicalPath.append("L");
                }
            }
        }

        return canonicalPath.toString();
    }


    @Override
    public boolean validatePath(TileType[][] maze, int[] startPos, int[] finalPos, String userPath) {
        MazeContext context = new MazeContext(startPos[0], startPos[1], 1); //start facing East
        MazeMoveHistory history = new MazeMoveHistory(); //store executed commands
        int motions = 0;

        for (int i = 0; i < userPath.length(); i++) {
            char move = userPath.charAt(i);

            if (Character.isDigit(move)) {
                motions = motions * 10 + Character.getNumericValue(move);
                continue;
            }

            motions = (motions == 0) ? 1 : motions;

            for (int j = 0; j < motions; j++) {
                MazeMoveCommand command = switch (move) {
                    case 'F' -> new MoveForwardCommand(context);
                    case 'R' -> new TurnRightCommand(context);
                    case 'L' -> new TurnLeftCommand(context);
                    default -> null;
                };

                if (command == null) return false;

                command.execute();
                history.push(command); 

                if (move == 'F' && !MazeUtils.validMove(maze, context.row, context.col)) {
                    while (!history.isEmpty()) {
                        history.pop().undo();
                    }
                    return false;
                }
            }

            motions = 0;
        }

        return context.row == finalPos[0] && context.col == finalPos[1];
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
