package sk.tuke.gamestudio.UI;


public class Game {
    public static boolean[][] parseSolution(String solution) {
        String[] lines = solution.split("\n");
        int rows = lines.length;
        int cols = lines[0].length();
        boolean[][] grid = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = lines[i].charAt(j) == '#';
            }
        }
        return grid;
    }

}

