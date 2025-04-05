package ca.mcmaster.se2aa4.mazerunner;

import ca.mcmaster.se2aa4.mazerunner.command.MazeMoveCommand;
import ca.mcmaster.se2aa4.mazerunner.command.MazeMoveHistory;
import ca.mcmaster.se2aa4.mazerunner.command.MoveForwardCommand;
import ca.mcmaster.se2aa4.mazerunner.command.TurnLeftCommand;
import ca.mcmaster.se2aa4.mazerunner.command.TurnRightCommand;
import ca.mcmaster.se2aa4.mazerunner.maze.MazeContext;
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
        int motions = 0; //tracks the number of times a certain type of move is made

        for (int i = 0; i < userPath.length(); i++) {
            char move = userPath.charAt(i);
            if (Character.isDigit(move)) {
                motions = motions * 10 + Character.getNumericValue(move); //handling 2+ digit motions
                continue;
            }

            motions = (motions == 0) ? 1 : motions;
            for (int j = 0; j < motions; j++) { //executing correct command based on motion type
                MazeMoveCommand command = switch (move) {
                    case 'F' -> new MoveForwardCommand(context);
                    case 'R' -> new TurnRightCommand(context);
                    case 'L' -> new TurnLeftCommand(context);
                    default -> null;
                };

                if (command == null) { //no command means path is impossible
                    System.out.println("Invalid Path!");
                    return;
                }

                context.executeCommand(command);
                history.push(command);

                if (move == 'F' && !MazeUtils.validMove(maze, context.getRow(), context.getCol())) { //in case you end up in an invalid spot, undo everything
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