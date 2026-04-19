package minesweeper;

import java.util.HashSet;
import java.util.Random;

import java.util.Arrays;

public class Minesweeper {
    private int gridSize;
    private int numMines;
    private String[][] mineField;

    public Minesweeper(int grid) {
        this(grid, 5);
    }
    
    public Minesweeper(int grid, int numMines) {
        this.gridSize = grid;
        this.numMines = numMines;
        this.mineField = new String[this.gridSize][this.gridSize];

        for (String[] row : mineField) {
            Arrays.fill(row, " ");
        }
        
        if (this.gridSize < 2) {
            throw new IllegalArgumentException("Invalid mine field size");
        } else if (this.numMines >= this.gridSize * this.gridSize) {
            throw new IllegalArgumentException("Invalid number of mines inputted");
        }

        // Logic for adding mines in random places in the matrix
        HashSet<String> minePositions = new HashSet<>();
        Random rand = new Random();
        while (minePositions.size() < this.numMines) {
            int i = rand.nextInt(this.gridSize);
            int j = rand.nextInt(this.gridSize);

            String posStr = "%d,%d".formatted(i, j);
            if (minePositions.contains(posStr)) {
                continue;
            }

            minePositions.add(posStr);
            mineField[i][j] = "*";
        }
        
    }

    public int getGridSize() {
        return this.gridSize;
    }

    public int getNumMines() {
        return this.numMines;
    }

    public String[][] getMineField() {
        return this.mineField;
    }

    public void printMineField() {
        String separator = "-".repeat((this.getGridSize() * 2) + 1);
        System.out.println(separator);
        for (int i = 0; i < this.getGridSize(); i++) {
            System.out.print('|');
            for (int j = 0; j < this.getGridSize(); j++) {
                System.out.printf("%s|", this.mineField[i][j]);
            }
            System.out.println("\n" + separator);
        }
    }
}