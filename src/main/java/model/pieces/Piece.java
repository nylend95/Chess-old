package main.java.model.pieces;



import main.java.model.PieceColor;
import main.java.model.Square;

import java.util.ArrayList;

/**
 * Created by mikke on 14-Feb-17.
 */
public abstract class Piece {

    final PieceColor color;
    Square square;
    boolean moved;

    public Piece(PieceColor color, Square square) {
        this.color = color;
        this.square = square;
        this.moved = false;
    }

    public PieceColor getColor() {
        return color;
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public abstract ArrayList<Square> validMoves(int[][] bitmap);

    public abstract void attackSquares();

    public abstract void captureFreeMoves();

    public abstract boolean toBeCaptured();

    @Override
    public String toString() {
        return getColor() + ", " + getSquare().toString();
    }
}
