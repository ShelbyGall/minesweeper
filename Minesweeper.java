package minesweeper;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;

import java.util.Arrays;


public class Minesweeper {
    final HashMap<String, String> colors = new HashMap<>();
    static final String RESET = "\u001B[0m";

    private int gridSize;
    private int numMines;
    private HashSet<String> minePositions;
    private String[][] mineField;

    public int getGridSize() {
        return this.gridSize;
    }

    public int getNumMines() {
        return this.numMines;
    }

    public String[][] getMineField() {
        return this.mineField;
    }

    // Logic for adding mines in random places in the matrix
    public void placeMines() {
        Random rand = new Random();
        this.minePositions = new HashSet<>();

        while (minePositions.size() < this.numMines) {
            // generate random indicies for the mine position
            int i = rand.nextInt(this.gridSize);
            int j = rand.nextInt(this.gridSize);
    
            // pack the indicies into a string for easy set comparison
            String posStr = "%d,%d".formatted(i, j);
            // if the random indicies exist in the set then they have already  
            // been added to the minefield so retry
            if (this.minePositions.contains(posStr)) {
                continue;
            }
            // add new mine position
            this.minePositions.add(posStr);
            this.mineField[i][j] = "*";
        }
    }

    // Logic for placing the numbers adjacent to the mines
    public void populateAdjacentNumbers() {
        // populate the different adjacent cell variations to check for
        // when populating the numbers for adjacent mines
        String[] searchAreas = {"-1,1","0,1","1,1,","1,0","1,-1","0,-1","-1,-1","-1,0"};

        // iterate over all the randomly generated mine positions
        for (String pos : this.minePositions) {
            int i = Integer.parseInt(pos.split(",")[0]);
            int j = Integer.parseInt(pos.split(",")[1]);

            // iterate over my adjcent cell areas
            for (String searchArea : searchAreas) {
                int a = Integer.parseInt(searchArea.split(",")[0]);
                int b = Integer.parseInt(searchArea.split(",")[1]);

                int x = i + a;
                int y = j + b;
                // check if the adjacent cells are possible to be accessed in the minefield matrix
                if ( (this.gridSize > x && x >= 0) && (this.gridSize > y && y >= 0) && !("*".equals(this.mineField[x][y])) ) {
                    // if the search area is " ", populate it with a 1
                    if (" ".equals(this.mineField[x][y])) {
                        this.mineField[x][y] = "1";
                    }
                    // if the search area has already been populated then increment the search area
                    else {
                        this.mineField[x][y] = "%d".formatted( Integer.parseInt(this.mineField[x][y]) + 1 );
                    }
                }
            }
        }
    }

    public Minesweeper(int gridSize) {
        this(gridSize, 5);
    }
    
    public Minesweeper(int gridSize, int numMines) {
        // checks invalid arguments for the minefield size and number of mines
        if (gridSize < 2) {
            throw new IllegalArgumentException("Invalid mine field size");
        }
        if (numMines >= gridSize * gridSize) {
            throw new IllegalArgumentException("Invalid number of mines inputted");
        }

        // initialize class/object variables
        colors.put("1","\u001B[34m");       // blue
        colors.put("2","\u001B[32m");       // green
        colors.put("3","\u001B[31m");       // red
        colors.put("4","\u001B[35m");       // magenta
        colors.put("5","\u001B[33m");       // yellow
        colors.put("6","\u001B[38;5;202m"); // orange
        colors.put("7","\u001B[36m");       // cyan
        colors.put("8","\u001B[38;5;219m"); // pink
        colors.put(" ","\u001B[37m");       // white
        colors.put("*","\u001B[30m");       // black
        this.gridSize = gridSize;
        this.numMines = numMines;
        this.mineField = new String[this.gridSize][this.gridSize];
        
        // populate empty mine field
        for (String[] row : this.mineField) {
            Arrays.fill(row, " ");
        }
        
        // place mines randomly in the mine field
        placeMines();

        // populate the numbers to show how many mines each cell touches
        populateAdjacentNumbers();
    }

    public void printMineField() {
        System.out.print("   ");
        for (int i = 65; i < 65 + this.gridSize; i++) {
            System.out.printf("  %c ",(char) i);
        }
        System.out.println("\n   ┌───" + "┬───".repeat(this.gridSize - 1) + "┐");

        String separator = "   " + "├───" + "┼───".repeat(this.gridSize - 1) + "┤";
        for (int i = 0; i < this.gridSize; i++) {
            System.out.print("   │");
            for (int j = 0; j < this.gridSize; j++) {
                System.out.printf(" %s%s%s │", colors.get(this.mineField[i][j]), this.mineField[i][j], RESET);
            }
            System.out.printf(" %d", i);
            if (i == this.gridSize - 1){
                System.out.println("\n   └───" + "┴───".repeat(this.gridSize - 1) + "┘");
                break;
            }
            System.out.println("\n" + separator);
        }
    }
}