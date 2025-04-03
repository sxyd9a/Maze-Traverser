package ca.mcmaster.se2aa4.mazerunner;

public class MazeSolverFactory {

    public static MazeSolverStrategy getSolver(String strategyName) {
        if (strategyName == null || strategyName.equalsIgnoreCase("right")) {
            return new RightHandMazeSolver();
        } else if (strategyName.equalsIgnoreCase("bfs")) {
            return new BFSMazeSolver();
        }
        throw new IllegalArgumentException("Unknown solver strategy: " + strategyName);
    }
}
