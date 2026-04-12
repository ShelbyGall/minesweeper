package minesweeper;

public class Main {

    public static void main(String[] args) {
        Minesweeper ms = new Minesweeper(9);
        System.out.println(ms.getGrid());
    }
}