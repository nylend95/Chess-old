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

    @Override
    public String toString(){
        return "Rook, " + super.toString();
    }
}
