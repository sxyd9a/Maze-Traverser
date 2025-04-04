package ca.mcmaster.se2aa4.mazerunner.command;

import ca.mcmaster.se2aa4.mazerunner.maze.MazeUtils;
import ca.mcmaster.se2aa4.mazerunner.maze.TileType;

public class ValidatePath {
    private final TileType[][] maze;
    private final int[] start;
    private final int[] finish;
    private final String userPath;

    public ValidatePath(TileType[][] maze, int[] start, int[] finish, String userPath) {
        this.maze = maze;
        this.start = start;
        this.finish = finish;
        this.userPath = userPath;
    }

    public void completeValidation() {
        MazeContext context = new MazeContext(start[0], start[1], 1); //east
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
                    System.out.println("Invalid Path!");
                    return;
                }

                context.executeCommand(command);
                history.push(command);

                if (move == 'F' && !MazeUtils.validMove(maze, context.getRow(), context.getCol())) {
                    while (!history.isEmpty()) context.undoCommand(history.pop());
                    System.out.println("Invalid Path!");
                    return;
                }
            }
            motions = 0;
        }

        boolean reachedEnd = context.getRow() == finish[0] && context.getCol() == finish[1];
        System.out.println(reachedEnd ? "Your path works!" : "Invalid Path!");
    }
}
