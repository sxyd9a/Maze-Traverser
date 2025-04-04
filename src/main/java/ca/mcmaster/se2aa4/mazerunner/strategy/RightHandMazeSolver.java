package ca.mcmaster.se2aa4.mazerunner.strategy;

import ca.mcmaster.se2aa4.mazerunner.command.MazeMoveCommand;
import ca.mcmaster.se2aa4.mazerunner.command.MazeMoveHistory;
import ca.mcmaster.se2aa4.mazerunner.command.MoveForwardCommand;
import ca.mcmaster.se2aa4.mazerunner.command.TurnLeftCommand;
import ca.mcmaster.se2aa4.mazerunner.command.TurnRightCommand;
import ca.mcmaster.se2aa4.mazerunner.maze.MazeContext;
import ca.mcmaster.se2aa4.mazerunner.maze.MazeUtils;
import ca.mcmaster.se2aa4.mazerunner.maze.TileType;

public class RightHandMazeSolver implements MazeSolverStrategy {

    @Override
    public String canonicalPathSearch(TileType[][] maze, int[] startPos, int[] finalPos) {
        StringBuilder canonicalPath = new StringBuilder();

        MazeContext context = new MazeContext(startPos[0], startPos[1], 1); //start facing East
        MazeMoveHistory history = new MazeMoveHistory();
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; //North, South, East, West

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
    public String factorizePath(String canonicalPath) {
        return MazeUtils.factorizePath(canonicalPath);
    }
}