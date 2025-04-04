package ca.mcmaster.se2aa4.mazerunner.strategy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import ca.mcmaster.se2aa4.mazerunner.maze.MazeUtils;
import ca.mcmaster.se2aa4.mazerunner.maze.TileType;

public class BFSMazeSolver implements MazeSolverStrategy {

    // Directions: North, East, South, West
    int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    @Override
    public String canonicalPathSearch(TileType[][] maze, int[] start, int[] end) {
        int rows = maze.length;
        int cols = maze[0].length;

        boolean[][] visited = new boolean[rows][cols];
        Map<String, String> cameFrom = new HashMap<>();
        Map<String, Character> moveFrom = new HashMap<>();
        Queue<int[]> queue = new LinkedList<>();

        queue.add(start);
        visited[start[0]][start[1]] = true;

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();

            if (pos[0] == end[0] && pos[1] == end[1]) break;

            for (int d = 0; d < 4; d++) {
                int newRow = pos[0] + directions[d][0];
                int newCol = pos[1] + directions[d][1];

                if (MazeUtils.validMove(maze, newRow, newCol) && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    queue.add(new int[]{newRow, newCol});

                    String from = pos[0] + "," + pos[1];
                    String to = newRow + "," + newCol;
                    cameFrom.put(to, from);
                    moveFrom.put(to, "NESW".charAt(d)); 
                }
            }
        }

        //reconstruct NSEW path
        StringBuilder nsewPath = new StringBuilder();
        String current = end[0] + "," + end[1];
        while (cameFrom.containsKey(current)) {
            nsewPath.insert(0, moveFrom.get(current));
            current = cameFrom.get(current);
        }

        // Convert to FRL path
        return convertToFRL(nsewPath.toString());
    }

    @Override
    public String factorizePath(String path) {
        return MazeUtils.factorizePath(path);
    }

    private String convertToFRL(String nsewPath) {
        StringBuilder frlPath = new StringBuilder();
        char current = 'E'; //start facing East

        for (int i = 0; i < nsewPath.length(); i++) {
            char next = nsewPath.charAt(i);
            String turn = getTurn(current, next);
            frlPath.append(turn);
            current = next;
        }

        return frlPath.toString();
    }

    private String getTurn(char from, char to) {
        String dirs = "NESW";
        int fromIndex = dirs.indexOf(from);
        int toIndex = dirs.indexOf(to);
        int diff = (toIndex - fromIndex + 4) % 4;

        return switch (diff) {
            case 0 -> "F";
            case 1 -> "RF";
            case 2 -> "RR" + "F"; //Uturn
            case 3 -> "LF";
            default -> "";
        };
    }
}