package ca.mcmaster.se2aa4.mazerunner.strategy;

public enum SolverType {
    RHS, BFS;

    public static SolverType fromString(String name) {
        return switch (name.toLowerCase()) {
            case "rhs" -> RHS;
            case "bfs" -> BFS;
            default -> throw new IllegalArgumentException("Unknown solver strategy: " + name);
        };
    }
}
