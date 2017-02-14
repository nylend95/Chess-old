package model.pieces;

import model.Piece;
import model.PieceColor;

/**
 * Created by mikke on 14-Feb-17.
 */
public class Pawn extends Piece {
    private boolean promoted;
    private Piece promotedTo;
    private final boolean moveUp;

    public Pawn(PieceColor color, String square, boolean moveUp) {
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
    public void validMoves() {

    }

    @Override
    public void attackSquares() {

    }

    @Override
    public void captureFreeMoves() {

    }

    @Override
    public boolean toBeCaptured() {
        return false;
    }
}
