package minesweeper;

public class Main {

    public static void main(String[] args) {
        int gridSize = 9;
        int numMines = 10;
        if (args.length > 0) {
            gridSize = Integer.parseInt(args[0]);
            numMines = Integer.parseInt(args[1]);
        }

        Minesweeper ms = new Minesweeper(gridSize,numMines);
        ms.printMineField();
    }
}