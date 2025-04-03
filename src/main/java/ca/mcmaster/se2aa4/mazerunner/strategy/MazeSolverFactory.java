package ca.mcmaster.se2aa4.mazerunner.strategy;

public class MazeSolverFactory {

    public static MazeSolverStrategy getSolver(String strategyName) {
        if (strategyName == null) { //raise exception when no strategy is selected
            throw new IllegalArgumentException("Solver strategy must be specified.");
        }

        //If user wants to find/check path using rhs algorithm
        if (strategyName.equalsIgnoreCase("rhs")) {
            return new RightHandMazeSolver();
        //If user wants to find/check path using bfs algorithm
        } else if (strategyName.equalsIgnoreCase("bfs")) {
            return new BFSMazeSolver();
        }

        //raise exception when unavailable strategy is selected
        throw new IllegalArgumentException("Unknown solver strategy: " + strategyName);
    }
}
