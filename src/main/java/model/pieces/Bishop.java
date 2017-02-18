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
    public ArrayList<Move> validMoves(int[][] bitmapPositions, int[][] bitmapAttackingPositions) {
        int[][] dir = {{1, 1}, {-1, -1}, {-1, 1}, {1, -1}};
        return checkDirections(dir, bitmapPositions, 7);
    }

    @Override
    public ArrayList<Square> attackSquares(int[][] bitmapPositions) {
        int[][] dir = {{1, 1}, {-1, -1}, {-1, 1}, {1, -1}};
        return checkAttackDirections(dir, bitmapPositions, 7);
    }

    @Override
    public void captureFreeMoves() {

    }

    @Override
    public String toString() {
        return "Bishop, " + super.toString();
    }
}
