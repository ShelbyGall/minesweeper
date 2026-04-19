package minesweeper;

public class Main {

    public static void main(String[] args) {
        Minesweeper ms = new Minesweeper(9);
        System.out.println(ms.getGridSize());
        ms.printMineField();

        Minesweeper ms1 = new Minesweeper(2);
        ms1.printMineField();
        Minesweeper ms2 = new Minesweeper(1);

    }
}