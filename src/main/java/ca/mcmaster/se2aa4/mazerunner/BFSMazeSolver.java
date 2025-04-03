package ca.mcmaster.se2aa4.mazerunner;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class BFSMazeSolver implements MazeSolverStrategy {

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

            if (pos[0] == end[0] && pos[1] == end[1]) {
                break;
            }

            for (int d = 0; d < 4; d++) {
                int newRow = pos[0] + directions[d][0];
                int newCol = pos[1] + directions[d][1];
                if (isValidMove(maze, newRow, newCol) && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    queue.add(new int[]{newRow, newCol});
                    String from = pos[0] + "," + pos[1];
                    String to = newRow + "," + newCol;
                    cameFrom.put(to, from);
                    moveFrom.put(to, "URDL".charAt(d)); 
                }
            }
        }

        
        StringBuilder path = new StringBuilder();
        String current = end[0] + "," + end[1];
        while (cameFrom.containsKey(current)) {
            path.insert(0, moveFrom.get(current));
            current = cameFrom.get(current);
        }

        return path.toString();
    }

    @Override
    public boolean validatePath(TileType[][] maze, int[] start, int[] end, String userPath) {
        int row = start[0], col = start[1];

        for (int i = 0; i < userPath.length(); i++) {
            char move = userPath.charAt(i);
            int newRow = row, newCol = col;

            switch (move) {
                case 'U' -> newRow--;
                case 'D' -> newRow++;
                case 'L' -> newCol--;
                case 'R' -> newCol++;
                default -> {
                    return false;
                }
            }

            if (!isValidMove(maze, newRow, newCol)) return false;

            row = newRow;
            col = newCol;
        }

        return row == end[0] && col == end[1];
    }

    private boolean isValidMove(TileType[][] maze, int row, int col) {
        return row >= 0 && row < maze.length && col >= 0 && col < maze[0].length && maze[row][col] == TileType.OPEN;
    }

    @Override
    public String factorizePath(String path) {
        if (path == null || path.isEmpty()) return "";

        StringBuilder result = new StringBuilder();
        int count = 1;
        char prev = path.charAt(0);

        for (int i = 1; i < path.length(); i++) {
            char curr = path.charAt(i);
            if (curr == prev) {
                count++;
            } else {
                if (count == 1) result.append(prev);
                else result.append(count).append(prev);
                prev = curr;
                count = 1;
            }
        }

        if (count == 1) result.append(prev);
        else result.append(count).append(prev);

        return result.toString();
    }

    @Override
    public int[] determineStartPos(TileType[][] maze) {
        return MazePositionUtils.findStart(maze);
    }

    @Override
    public int[] determineFinalPos(TileType[][] maze) {
        return MazePositionUtils.findEnd(maze);
    }

    
}
