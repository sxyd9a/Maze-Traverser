package ca.mcmaster.se2aa4.mazerunner;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

//Implements breadth-first search to find the shortest path through the maze
public class BFSMazeSolver implements MazeSolverStrategy {

    int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; //Up, Right, Down, Left

    @Override
    public String canonicalPathSearch(TileType[][] maze, int[] start, int[] end) {
        int rows = maze.length;
        int cols = maze[0].length;

        boolean[][] visited = new boolean[rows][cols]; //tracks visited positions
        Map<String, String> cameFrom = new HashMap<>(); //maps a cell to its parent
        Map<String, Character> moveFrom = new HashMap<>(); //stores the move used to reach each cell
        Queue<int[]> queue = new LinkedList<>(); //BFS queue

        queue.add(start);
        visited[start[0]][start[1]] = true;

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();

            if (pos[0] == end[0] && pos[1] == end[1]) {
                break; //Reached goal
            } 

            for (int d = 0; d < 4; d++) {
                int newRow = pos[0] + directions[d][0];
                int newCol = pos[1] + directions[d][1];

                if (MazeUtils.validMove(maze, newRow, newCol) && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    queue.add(new int[]{newRow, newCol});

                    String from = pos[0] + "," + pos[1];
                    String to = newRow + "," + newCol;
                    cameFrom.put(to, from); //mark parent
                    moveFrom.put(to, "URDL".charAt(d)); //store direction used to reach this cell
                }
            }
        }

        //reconstruct the path backwards
        StringBuilder path = new StringBuilder();
        String current = end[0] + "," + end[1];
        while (cameFrom.containsKey(current)) {
            path.insert(0, moveFrom.get(current));
            current = cameFrom.get(current);
        }

        return path.toString();
    }

    @Override
    public boolean validatePath(TileType[][] maze, int[] startPos, int[] finalPos, String userPath) {
        int row = startPos[0];
        int col = startPos[1];

        int i = 0;
        int motions = 0;

        //check path piece by piece
        while (i < userPath.length()) {
            char move = userPath.charAt(i);

            if (Character.isDigit(move)) {
                motions = motions * 10 + Character.getNumericValue(move); //account for 2+ digits
            }
            else if (move == 'U' || move == 'D' || move == 'L' || move == 'R') {
                motions = (motions == 0) ? 1 : motions;

                //perform the motion
                for (int j = 0; j < motions; j++) {
                    int newRow = row;
                    int newCol = col;

                    switch (move) {
                        case 'U' -> newRow--;
                        case 'D' -> newRow++;
                        case 'L' -> newCol--;
                        case 'R' -> newCol++;
                    }

                    if (!MazeUtils.validMove(maze, newRow, newCol)) return false;

                    row = newRow;
                    col = newCol;
                }

                motions = 0; 
            }
            else return false; //invalid character

            i++;
        }

        //check if reached the correct end point
        return row == finalPos[0] && col == finalPos[1];
    }

    @Override
    public String factorizePath(String path) {
        return MazeUtils.factorizePath(path);
    }

    @Override
    public int[] determineStartPos(TileType[][] maze) {
        return MazeUtils.findStart(maze);
    }

    @Override
    public int[] determineFinalPos(TileType[][] maze) {
        return MazeUtils.findEnd(maze);
    }
}
