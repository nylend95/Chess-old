package main.model.pieces;

import main.model.PieceColor;
import main.model.Square;

import java.util.ArrayList;

/**
 * Created by mikke on 14-Feb-17.
 */
public class Knight extends Piece {
    public Knight(PieceColor color, Square square) {
        super(color, square);
    }

    @Override
    public ArrayList<Square> validMoves(int[][] bitmap) {
        return null;
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
