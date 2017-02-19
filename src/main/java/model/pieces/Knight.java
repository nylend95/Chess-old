package main.java.model.pieces;


import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.Square;

import java.util.ArrayList;

/**
 * Created by mikke on 14-Feb-17.
 */
public class Knight extends Piece {
    public Knight(PieceColor color, Square square) {
        super(color, square);
    }

    @Override
    public ArrayList<Move> validMoves(int[][] bitmapPositions, int[][] bitmapAttackingPositions) {
        int[][] dir = {{1,2}, {1,-2}, {2,-1}, {2,1}, {-1,2}, {-1,-2}, {-2,1}, {-2,-1}};
        return checkDirections(dir, bitmapPositions, 1);
    }

    @Override
    public ArrayList<Square> attackSquares(int[][] bitmapPositions) {
        int[][] dir = {{1,2}, {1,-2}, {2,-1}, {2,1}, {-1,2}, {-1,-2}, {-2,1}, {-2,-1}};
        return checkAttackDirections(dir, bitmapPositions, 1);
    }

    @Override
    public ArrayList<Square> attackSquaresPenetrate(int[][] bitmapPositions) {
        int[][] dir = {{1,2}, {1,-2}, {2,-1}, {2,1}, {-1,2}, {-1,-2}, {-2,1}, {-2,-1}};
        return checkAttackDirectionsPenetrate(dir, bitmapPositions, 1);
    }

    @Override
    public void captureFreeMoves() {

    }


    @Override
    public String toString() {
        return "Knight, " + super.toString();
    }
}
