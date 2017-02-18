package main.java.model.pieces;

import main.java.model.*;

import java.util.ArrayList;

/**
 * Created by mikke on 14-Feb-17.
 */
public class King extends Piece {
    public King(PieceColor color, Square square) {
        super(color, square);
    }

    @Override
    public ArrayList<Move> validMoves(int[][] bitmapPositions, int[][] bitmapAttackingPositions) {
        return new ArrayList<>();
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
        return "King, " + super.toString();
    }

}
