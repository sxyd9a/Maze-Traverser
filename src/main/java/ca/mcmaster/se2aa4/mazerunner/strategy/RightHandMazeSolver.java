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
    public String canonicalPathSearch(TileType[][] maze, int[] startPos, int[] finalPos) {
        StringBuilder canonicalPath = new StringBuilder();

        MazeContext context = new MazeContext(startPos[0], startPos[1], 1); // Start facing East
        MazeMoveHistory history = new MazeMoveHistory();
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        while (!(context.getRow() == finalPos[0] && context.getCol() == finalPos[1])) {
            int rightDir = (context.getDirection() + 1) % 4;
            int rightRow = context.getRow() + directions[rightDir][0];
            int rightCol = context.getCol() + directions[rightDir][1];

            if (MazeUtils.validMove(maze, rightRow, rightCol)) {
                MazeMoveCommand turnRight = new TurnRightCommand(context);
                MazeMoveCommand moveForward = new MoveForwardCommand(context);
                context.executeCommand(turnRight);
                context.executeCommand(moveForward);
                history.push(turnRight);
                history.push(moveForward);
                canonicalPath.append("R").append("F");
            } else {
                int forwardRow = context.getRow() + directions[context.getDirection()][0];
                int forwardCol = context.getCol() + directions[context.getDirection()][1];

                if (MazeUtils.validMove(maze, forwardRow, forwardCol)) {
                    MazeMoveCommand moveForward = new MoveForwardCommand(context);
                    context.executeCommand(moveForward);
                    history.push(moveForward);
                    canonicalPath.append("F");
                } else {
                    MazeMoveCommand turnLeft = new TurnLeftCommand(context);
                    context.executeCommand(turnLeft);
                    history.push(turnLeft);
                    canonicalPath.append("L");
                }
            }
        }

        return canonicalPath.toString();
    }

    @Override
    public boolean validatePath(TileType[][] maze, int[] startPos, int[] finalPos, String userPath) {
        MazeContext context = new MazeContext(startPos[0], startPos[1], 1); // Start facing East
        MazeMoveHistory history = new MazeMoveHistory();
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

                if (command == null) {
                    return false;
                } 

                context.executeCommand(command);
                history.push(command);

                if (move == 'F' && !MazeUtils.validMove(maze, context.getRow(), context.getCol())) {
                    while (!history.isEmpty()) {
                        context.undoCommand(history.pop());
                    }
                    return false;
                }
            }

            motions = 0;
        }

        return context.getRow() == finalPos[0] && context.getCol() == finalPos[1];
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
