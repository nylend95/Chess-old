package main.java.model.pieces;

import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.Square;

import java.util.ArrayList;

/**
 * Created by mikke on 14-Feb-17.
 */
public class Bishop extends Piece {
    public Bishop(PieceColor color, Square square) {
        super(color, square);
    }

    @Override
    public ArrayList<Square> attackSquares(int[][] bitmapPositions) {
        return null;
    }

    @Override
    public ArrayList<Move> validMoves(int[][] bitmapPositions, int[][] bitmapAttackingPositions) {
        int[][] dir = {{1, 1}, {-1, -1}, {-1, 1}, {1, -1}};
        return checkDirections(dir, bitmapPositions, 7);
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
        return "Bishop, " + super.toString();
    }
}
