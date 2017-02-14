package model;

/**
 * Created by mikke on 14-Feb-17.
 */
public class King extends Piece {
    public King(String color, String square) {
        super(color, square);
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
