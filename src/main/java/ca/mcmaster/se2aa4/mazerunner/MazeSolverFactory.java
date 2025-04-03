package ca.mcmaster.se2aa4.mazerunner;

public class MazeSolverFactory {

    public static MazeSolverStrategy getSolver(String strategyName) {
        if (strategyName == null) {
            throw new IllegalArgumentException("Solver strategy must be specified.");
        }

        if (strategyName.equalsIgnoreCase("rhs")) {
            return new RightHandMazeSolver();
        } else if (strategyName.equalsIgnoreCase("bfs")) {
            return new BFSMazeSolver();
        }

        throw new IllegalArgumentException("Unknown solver strategy: " + strategyName);
    }
}
