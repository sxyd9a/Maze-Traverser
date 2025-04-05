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
import ca.mcmaster.se2aa4.mazerunner.maze.MazeContext;
import ca.mcmaster.se2aa4.mazerunner.maze.MazeUtils;
import ca.mcmaster.se2aa4.mazerunner.maze.TileType;
import ca.mcmaster.se2aa4.mazerunner.strategy.BFSMazeSolver;
import ca.mcmaster.se2aa4.mazerunner.strategy.RightHandMazeSolver;

public class MazeTraverserTest {

    @Test
    void testCanonicalPathGeneration_RHS() {
        System.out.println("Running: Test Canonical Path Generation (RHS)"); //testing rhs algorithm path finding logic
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
        System.out.println("Passed: Test Canonical Path Generation (RHS)");
    }

    @Test
    void testFactorizedPath() { //testing path factorization
        System.out.println("Running: Test Factorized Path");
        String canonical = "FFFFLLR";
        String expected = "4F2LR";
        String actual = MazeUtils.factorizePath(canonical);

        assertEquals(expected, actual);
        System.out.println("Passed: Test Factorized Path");
    }

    @Test
    void testValidateCorrectUserPath() { //testing path validation logic
        System.out.println("Running: Test Validate Correct User Path");
        TileType[][] maze = {
            {TileType.OPEN, TileType.OPEN, TileType.OPEN},
            {TileType.WALL, TileType.WALL, TileType.OPEN}
        };
        int[] start = {0, 0};
        int[] finish = {0, 2};

        ValidatePath validator = new ValidatePath(maze, start, finish, "2F");
        validator.completeValidation();
        System.out.println("Passed: Test Validate Correct User Path");
    }

    @Test
    void testValidateIncorrectUserPath() { //testing negative case if prior test failed
        System.out.println("Running: Test Validate Incorrect User Path");
        TileType[][] maze = {
            {TileType.OPEN, TileType.WALL, TileType.OPEN},
            {TileType.WALL, TileType.WALL, TileType.OPEN}
        };
        int[] start = {0, 0};
        int[] finish = {0, 2};

        ValidatePath validator = new ValidatePath(maze, start, finish, "2F");
        validator.completeValidation();
        System.out.println("Passed: Test Validate Incorrect User Path");
    }

    @Test
    void testUndoMoveForward() { //testing undoing of a forward command
        System.out.println("Running: Test Undo Move Forward");
        MazeContext context = new MazeContext(0, 0, 1); //1 for east
        MazeMoveCommand move = new MoveForwardCommand(context);

        move.execute();
        assertEquals(1, context.getCol());

        move.undo();
        assertEquals(0, context.getCol());
        System.out.println("Passed: Test Undo Move Forward");
    }

    @Test
    void testTurnLeftAndUndo() { //testing undoing of a left turn command
        System.out.println("Running: Test Turn Left And Undo");
        MazeContext context = new MazeContext(0, 0, 1); 
        MazeMoveCommand turnLeft = new TurnLeftCommand(context);

        turnLeft.execute();
        assertEquals(0, context.getDirection()); 

        turnLeft.undo();
        assertEquals(1, context.getDirection()); 
        System.out.println("Passed: Test Turn Left And Undo");
    }

    @Test
    void testCanonicalGenerationPath_BFS() { //testing bfs algorithm path finding
        System.out.println("Running: Test Canonical Path Generation (BFS)");
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
        System.out.println("Passed: Test Canonical Path Generation (BFS)");
    }

    //testing impossible move in a maze with 1 tile
    @Test
    void testInvalidOutOfBoundsMove() { 
        System.out.println("Running: Test Invalid Out Of Bounds Move");
        TileType[][] maze = {
            {TileType.OPEN}
        };
        MazeContext context = new MazeContext(0, 0, 1); 
        MazeMoveCommand move = new MoveForwardCommand(context);

        move.execute();
        assertFalse(MazeUtils.validMove(maze, context.getRow(), context.getCol()));
        System.out.println("Passed: Test Invalid Out Of Bounds Move");
    }

    @Test
    void testStartIsFinish() { //testing the edge case where the start is the same as the end location
        System.out.println("Running: Test Start To Finish");
        TileType[][] maze = {
            {TileType.OPEN}
        };
        int[] start = {0, 0};
        int[] finish = {0, 0};

        RightHandMazeSolver solver = new RightHandMazeSolver();
        String path = solver.canonicalPathSearch(maze, start, finish);

        assertEquals("", path);
        System.out.println("Passed: Test Start To Finish");
    }

    @Test
    void testHistoryStackPushPop() { //testing the command stack object
        System.out.println("Running: Test History Command Stack Push Pop");
        MazeMoveHistory history = new MazeMoveHistory();
        MazeContext context = new MazeContext(0, 0, 1);
        MazeMoveCommand move = new MoveForwardCommand(context);

        move.execute();
        history.push(move);

        assertFalse(history.isEmpty());
        history.pop().undo();
        assertEquals(0, context.getCol());
        System.out.println("Passed: Test History Command Stack Push Pop");
    }
}
