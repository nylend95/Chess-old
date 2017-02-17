package main.java.model.pieces;


import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.Square;

import java.util.ArrayList;

/**
 * Created by mikke on 14-Feb-17.
 */
public class Pawn extends Piece {
    private boolean promoted;
    private Piece promotedTo;
    private final boolean moveUp;

    public Pawn(PieceColor color, Square square, boolean moveUp) {
        super(color, square);
        this.moveUp = moveUp;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public Piece getPromotedTo() {
        return promotedTo;
    }

    public void setPromotedTo(Piece promotedTo) {
        this.promotedTo = promotedTo;
    }

    public boolean getMoveDirection() {
        return moveUp;
    }

    @Override
    public ArrayList<Move> validMoves(int[][] bitmapPositions, int[][] bitmapAttackingPositions) {
        return null;
    }

    @Override
    public ArrayList<Square> attackSquares(int[][] bitmapPositions) {
        return null;
    }

    @Override
    public void captureFreeMoves() {

    }

    @Override
    public boolean toBeCaptured() {
        return false;
    }

    @Override
    public String toString() {
        return "Pawn, " + super.toString();
    }
}
