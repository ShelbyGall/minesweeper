package minesweeper;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;
import java.util.Arrays;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Scanner;


public class Minesweeper {
    final HashMap<String, String> colors = new HashMap<>();
    static final String RESET = "\u001B[0m";

    private int gridSize;
    private int numMines;
    private HashSet<String> minePositions;
    private String[][] mineField;
    private String[][] displayBoard;
    // we can use this variable as a counter to 
    // know what then game should be over
    // GAME OVER -> displayed cells == gridSize * gridSize
    private int displayedCells = 0;

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
    public void placeMines(int firstI, int firstJ) {
        Random rand = new Random();
        this.minePositions = new HashSet<>();
        String firstSelection = "%d,%d".formatted(firstI,firstJ);

        while (minePositions.size() < this.numMines) {
            // generate random indicies for the mine position
            int i = rand.nextInt(this.gridSize);
            int j = rand.nextInt(this.gridSize);
    
            // pack the indicies into a string for easy set comparison
            String posStr = "%d,%d".formatted(i, j);
            // if the random indicies exist in the set then they have already  
            // been added to the minefield so retry
            if (this.minePositions.contains(posStr) || firstSelection.equals(posStr)) {
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
        String[] searchAreas = {"-1,1","0,1","1,1","1,0","1,-1","0,-1","-1,-1","-1,0"};

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

    public void printMineField() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
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

    public void printDisplayBoard() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
        System.out.print("   ");
        for (int i = 65; i < 65 + this.gridSize; i++) {
            System.out.printf("  %c ",(char) i);
        }
        System.out.println("\n   ┌───" + "┬───".repeat(this.gridSize - 1) + "┐");

        String separator = "   " + "├───" + "┼───".repeat(this.gridSize - 1) + "┤";
        for (int i = 0; i < this.gridSize; i++) {
            System.out.print("   │");
            for (int j = 0; j < this.gridSize; j++) {
                if (this.displayBoard[i][j].equals("_")) {
                    System.out.printf("%s%s%s│", colors.get(this.displayBoard[i][j]), "   ", RESET);
                } else if (this.displayBoard[i][j].equals("F")) {
                    System.out.printf("%s%s%s│", colors.get(this.displayBoard[i][j]), " F ", RESET);
                } else{
                    System.out.printf(" %s%s%s │", colors.get(this.displayBoard[i][j]), this.displayBoard[i][j], RESET);
                }
            }
            System.out.printf(" %d", i);
            if (i == this.gridSize - 1){
                System.out.println("\n   └───" + "┴───".repeat(this.gridSize - 1) + "┘");
                break;
            }
            System.out.println("\n" + separator);
        }
    }

    // BFS for revealing adjacent spaces
    public void reveal(int i, int j) {
        // populate the different adjacent cell variations to check for
        // when populating the numbers for adjacent mines
        String[] searchAreas = {"-1,1","0,1","1,1","1,0","1,-1","0,-1","-1,-1","-1,0"};

        HashSet<String> visited = new HashSet<>();
        Queue<String> adjCells = new ArrayDeque<>();

        adjCells.add("%d,%d".formatted(i,j));
        while (!adjCells.isEmpty()) {
            String currSearch = adjCells.poll();
            visited.add(currSearch);

            int k = Integer.parseInt(currSearch.split(",")[0]);
            int l = Integer.parseInt(currSearch.split(",")[1]);
            
            this.displayBoard[k][l] = this.mineField[k][l];
            ++this.displayedCells;

            // if the currSearch cell is a number than I dont want to search around it
            if (this.mineField[k][l].matches("\\d")) {
                continue;
            }

            for (String searchArea : searchAreas) {
                // get my search area offsets
                int a = Integer.parseInt(searchArea.split(",")[0]);
                int b = Integer.parseInt(searchArea.split(",")[1]);

                // get the cells around the currently looked at cell
                int x = k + a;
                int y = l + b;
                // check if the adjacent cells are possible to be accessed in the minefield matrix
                if ( (this.gridSize > x && x >= 0) && 
                     (this.gridSize > y && y >= 0) && 
                    !("*".equals(this.mineField[x][y])) && 
                    !(visited.contains("%d,%d".formatted(x,y)) ) &&
                    !(adjCells.contains("%d,%d".formatted(x,y))) ) {
                    adjCells.add("%d,%d".formatted(x,y));
                }
            }
        }
    }

    /* 
     * change the displayed board depending on what the next selected cell
     * is and return a boolean value representing the status of the game 
     * true  -> game continues
     * false -> game has ended
     */ 
    public boolean selectCell(int i, int j) {
        // if the selected cell is a number
        if (this.mineField[i][j].matches("\\d")) {
            this.displayBoard[i][j] = this.mineField[i][j];
            ++this.displayedCells;
            return true;
        }
        // if the selected cell is a mine
        else if (this.mineField[i][j].equals("*")) {
            this.displayBoard[i][j] = this.mineField[i][j];
            ++this.displayedCells;
            return false;
        }
        // else the cell is " " therefore reveal for all adjacent spaces
        else {
            reveal(i,j);
            return true;
        }
    }

    public void play() {
        boolean currTurn = true;
        boolean firstTurn = true;
        Scanner scanner = new Scanner(System.in);
        String userInput;
        int i;
        int j;
        
        while (currTurn && (this.displayedCells < (this.gridSize * this.gridSize) - this.numMines) ) {
            this.printDisplayBoard();
            System.out.print("<F col row> to place a flag\nEnter q to quit\nSelect a cell <col row>: ");

            userInput = scanner.nextLine();
            // if the user quits
            if (userInput.equalsIgnoreCase("q")) {
                currTurn = false;
                break;
            }
            // if the user is putting down a flag
            else if (userInput.split(" ")[0].equalsIgnoreCase("f") && userInput.split(" ").length == 3) {
                i = Integer.parseInt(userInput.split(" ")[2]);
                j = ((int) userInput.split(" ")[1].charAt(0)) - 65;
                if (this.displayBoard[i][j].equals("_")) {
                    this.displayBoard[i][j] = "F";
                }
                else if (this.displayBoard[i][j].equals("F")) {
                    this.displayBoard[i][j] = "_";
                }
            }
            // normal cell selection
            else {
                i = Integer.parseInt(userInput.split(" ")[1]);
                j = ((int) userInput.split(" ")[0].charAt(0)) - 65;

                if (firstTurn) {
                    // place mines randomly in the mine field
                    placeMines(i,j);

                    // populate the numbers to show how many mines each cell touches
                    populateAdjacentNumbers();
                    firstTurn = false;
                }
                
                if (!this.displayBoard[i][j].equals("F")) {
                    currTurn = selectCell(i,j);
                }
            }
        }
        this.printMineField();
        if (!currTurn) {
            System.out.println("GAME OVER");
        }
        else {
            System.out.println("YOU WIN!!!!!");
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
        colors.put("_","\u001B[47m");       // white background
        colors.put("F", "\u001B[37;41m");   // white text red background
        this.gridSize = gridSize;
        this.numMines = numMines;
        this.mineField = new String[this.gridSize][this.gridSize];
        this.displayBoard = new String[this.gridSize][this.gridSize];

        // populate empty mine field
        for (String[] row : this.mineField) {
            Arrays.fill(row, " ");
        }
        
        // populate empty display board
        for (String[] row : this.displayBoard) {
            Arrays.fill(row, "_");
        }
    }

}