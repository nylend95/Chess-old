package model.pieces;

import model.PieceColor;
import model.Square;

/**
 * Created by mikke on 14-Feb-17.
 */
public class Queen extends Piece {
    public Queen(PieceColor color, Square square) {
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
