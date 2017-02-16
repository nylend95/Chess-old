package main.java.model.pieces;


import main.java.model.PieceColor;
import main.java.model.Square;

import java.util.ArrayList;

/**
 * Created by mikke on 14-Feb-17.
 */
public class Rook extends Piece {
    public Rook(PieceColor color, Square square) {
        super(color, square);
    }

    @Override
    public ArrayList<Square> validMoves(int[][] bitmapPositions, int[][] bitmapAttackingPositions) {
        int[][] dir = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};
        return checkDirections(dir, bitmapPositions);
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
        return "Rook, " + super.toString();
    }
}
