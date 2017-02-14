package model.pieces;

import model.PieceColor;
import model.Square;

/**
 * Created by mikke on 14-Feb-17.
 */
public abstract class Piece {
    private final PieceColor color;
    private Square square;
    private boolean moved;

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

    public abstract void validMoves();

    public abstract void attackSquares();

    public abstract void captureFreeMoves();

    public abstract boolean toBeCaptured();


}
