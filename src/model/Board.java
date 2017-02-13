package model;

/**
 * Created by Jesper Nylend on 10.02.2017.
 * s305070
 */
public class Board {
    private int[][] board;

    public Board() {
        board = new int[8][8];
    }

    public int[][] getBoard() {
        return board;
    }
}
