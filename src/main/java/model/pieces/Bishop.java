package main.java.model.pieces;

import main.java.model.PieceColor;
import main.java.model.Square;
import main.java.model.pieces.Piece;

import java.util.ArrayList;

/**
 * Created by mikke on 14-Feb-17.
 */
public class Bishop extends Piece {
    public Bishop(PieceColor color, Square square) {
        super(color, square);
    }

    @Override
    public ArrayList<Square> validMoves(int[][] bitmap) {
        int x_start = square.getColumn();
        int y_start = square.getRow();

        int selfValue = (color == PieceColor.WHITE) ? 1 : 0;

        int[][] dir = {{1, 1}, {-1, -1}, {-1, 1}, {1, -1}};

        ArrayList<Square> validSquares = new ArrayList<>();

        for (int[] d : dir) {
            for (int i = 1; i <= 8; i++) {
                int x_new = x_start + i * d[0];
                int y_new = y_start + i * d[1];

                // Out of bounds
                if (x_new < 0 || x_new > 8 || y_new < 0 || y_new > 8) {
                    break;
                }

                // Capture or crash in own piece
                if (bitmap[y_new][x_new] != 0) {
                    if (bitmap[y_new][x_new] != selfValue) {
                        validSquares.add(new Square(y_new, x_new));
                    }
                    break;
                }

                // Valid move
                validSquares.add(new Square(y_new, x_new));
            }
        }
        return validSquares;
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

    @Override
    public String toString(){
        return "Bishop, " + super.toString();
    }
}
