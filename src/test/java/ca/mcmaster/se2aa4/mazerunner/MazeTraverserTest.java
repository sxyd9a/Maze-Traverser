package ca.mcmaster.se2aa4.mazerunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import ca.mcmaster.se2aa4.mazerunner.command.MazeMoveCommand;
import ca.mcmaster.se2aa4.mazerunner.command.MazeMoveHistory;
import ca.mcmaster.se2aa4.mazerunner.command.MoveForwardCommand;
import ca.mcmaster.se2aa4.mazerunner.command.TurnLeftCommand;
import ca.mcmaster.se2aa4.mazerunner.command.ValidatePath;
import ca.mcmaster.se2aa4.mazerunner.maze.MazeContext;
import ca.mcmaster.se2aa4.mazerunner.maze.MazeUtils;
import ca.mcmaster.se2aa4.mazerunner.maze.TileType;
import ca.mcmaster.se2aa4.mazerunner.strategy.BFSMazeSolver;
import ca.mcmaster.se2aa4.mazerunner.strategy.RightHandMazeSolver;

public class MazeTraverserTest {

    @Test
    void testCanonicalPathGeneration_RHS() {
        System.out.println("Running: testCanonicalPathGeneration_RHS");
        TileType[][] maze = {
            {TileType.OPEN, TileType.OPEN},
            {TileType.WALL, TileType.OPEN}
        };
        int[] start = {0, 0};
        int[] finish = {1, 1};

        RightHandMazeSolver solver = new RightHandMazeSolver();
        String path = solver.canonicalPathSearch(maze, start, finish);

        assertNotNull(path);
        assertFalse(path.isEmpty());
        System.out.println("Passed: testCanonicalPathGeneration_RHS");
    }

    @Test
    void testFactorizedPath() {
        System.out.println("Running: testFactorizedPath");
        String canonical = "FFFFLLR";
        String expected = "4F2LR";
        String actual = MazeUtils.factorizePath(canonical);

        assertEquals(expected, actual);
        System.out.println("Passed: testFactorizedPath");
    }

    @Test
    void testValidateCorrectUserPath() {
        System.out.println("Running: testValidateCorrectUserPath");
        TileType[][] maze = {
            {TileType.OPEN, TileType.OPEN, TileType.OPEN},
            {TileType.WALL, TileType.WALL, TileType.OPEN}
        };
        int[] start = {0, 0};
        int[] finish = {0, 2};

        ValidatePath validator = new ValidatePath(maze, start, finish, "2F");
        validator.completeValidation();
        System.out.println("Passed: testValidateCorrectUserPath");
    }

    @Test
    void testValidateIncorrectUserPath() {
        System.out.println("Running: testValidateIncorrectUserPath");
        TileType[][] maze = {
            {TileType.OPEN, TileType.WALL, TileType.OPEN},
            {TileType.WALL, TileType.WALL, TileType.OPEN}
        };
        int[] start = {0, 0};
        int[] finish = {0, 2};

        ValidatePath validator = new ValidatePath(maze, start, finish, "2F");
        validator.completeValidation();
        System.out.println("Passed: testValidateIncorrectUserPath");
    }

    @Test
    void testUndoMoveForward() {
        System.out.println("Running: testUndoMoveForward");
        MazeContext context = new MazeContext(0, 0, 1); //east
        MazeMoveCommand move = new MoveForwardCommand(context);

        move.execute();
        assertEquals(1, context.getCol());

        move.undo();
        assertEquals(0, context.getCol());
        System.out.println("Passed: testUndoMoveForward");
    }

    @Test
    void testTurnLeftAndUndo() {
        System.out.println("Running: testTurnLeftAndUndo");
        MazeContext context = new MazeContext(0, 0, 1); //east
        MazeMoveCommand turnLeft = new TurnLeftCommand(context);

        turnLeft.execute();
        assertEquals(0, context.getDirection()); //north

        turnLeft.undo();
        assertEquals(1, context.getDirection()); //east
        System.out.println("Passed: testTurnLeftAndUndo");
    }

    @Test
    void testCanonicalPath_BFS() {
        System.out.println("Running: testCanonicalPath_BFS");
        TileType[][] maze = {
            {TileType.OPEN, TileType.OPEN},
            {TileType.WALL, TileType.OPEN}
        };
        int[] start = {0, 0};
        int[] finish = {1, 1};

        BFSMazeSolver bfs = new BFSMazeSolver();
        String path = bfs.canonicalPathSearch(maze, start, finish);

        assertNotNull(path);
        assertTrue(path.matches("[FLR]+"));
        System.out.println("Passed: testCanonicalPath_BFS");
    }

    @Test
    void testInvalidOutOfBoundsMove() {
        System.out.println("Running: testInvalidOutOfBoundsMove");
        TileType[][] maze = {
            {TileType.OPEN}
        };
        MazeContext context = new MazeContext(0, 0, 1); //east
        MazeMoveCommand move = new MoveForwardCommand(context);

        move.execute();
        assertFalse(MazeUtils.validMove(maze, context.getRow(), context.getCol()));
        System.out.println("Passed: testInvalidOutOfBoundsMove");
    }

    @Test
    void testStartIsFinish() {
        System.out.println("Running: testStartIsFinish");
        TileType[][] maze = {
            {TileType.OPEN}
        };
        int[] start = {0, 0};
        int[] finish = {0, 0};

        RightHandMazeSolver solver = new RightHandMazeSolver();
        String path = solver.canonicalPathSearch(maze, start, finish);

        assertEquals("", path);
        System.out.println("Passed: testStartIsFinish");
    }

    @Test
    void testHistoryStackPushPop() {
        System.out.println("Running: testHistoryStackPushPop");
        MazeMoveHistory history = new MazeMoveHistory();
        MazeContext context = new MazeContext(0, 0, 1);
        MazeMoveCommand move = new MoveForwardCommand(context);

        move.execute();
        history.push(move);

        assertFalse(history.isEmpty());
        history.pop().undo();
        assertEquals(0, context.getCol());
        System.out.println("Passed: testHistoryStackPushPop");
    }
}
