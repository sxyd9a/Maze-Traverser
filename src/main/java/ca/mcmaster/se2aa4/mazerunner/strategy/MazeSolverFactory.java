package ca.mcmaster.se2aa4.mazerunner.strategy;

public class MazeSolverFactory {

    public static MazeSolverStrategy getSolver(String strategyName) {
        if (strategyName == null) {
            throw new IllegalArgumentException("Solver strategy must be specified.");
        }

        SolverType type = SolverType.fromString(strategyName);

        return switch (type) {
            case RHS -> new RightHandMazeSolver(); //if user wants to find path using rhs algorithm
            case BFS -> new BFSMazeSolver(); //if user wants to find path using bfs algorithm
        };
    }
}
