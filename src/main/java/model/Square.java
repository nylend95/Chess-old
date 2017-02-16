package main.java.model;

/**
 * Created by Jesper Nylend on 14.02.2017.
 * s305070
 */
public class Square {
    private int row;
    private int column;

    public Square(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString(){
        return "[" + getRow() + ", " + getColumn() + "] | ";
    }

}
