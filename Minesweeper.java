package minesweeper;

public class Minesweeper {
    private int gridSize;
    private int numMines;
    private int[][] mineField;

    public Minesweeper(int grid) {
        this(grid, 5);
    }
    
    public Minesweeper(int grid, int numMines) {
        this.gridSize = grid;
        this.numMines = numMines;
        this.mineField = new int[this.gridSize][this.gridSize];

        // Logic for adding mines in random places in the matrix
        
    }

    public int getGridSize() {
        return this.gridSize;
    }

    public int getNumMines() {
        return this.numMines;
    }

    public int[][] getMineField() {
        return this.mineField;
    }

    public void printMineField() {
        String separator = "-".repeat((this.getGridSize() * 2) + 1);
        System.out.println(separator);
        for (int i = 0; i < this.getGridSize(); i++) {
            System.out.print('|');
            for (int j = 0; j < this.getGridSize(); j++) {
                System.out.printf("%d|", this.mineField[i][j]);
            }
            System.out.println("\n" + separator);
        }
    }
}