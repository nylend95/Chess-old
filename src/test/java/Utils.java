package test.java;

import main.java.model.Move;
import main.java.model.Square;
import main.java.model.pieces.Piece;
import org.junit.Assert;

import java.util.ArrayList;

/**
 * Created by mikke on 16-Feb-17.
 */
class Utils {

    static void testMoves(ArrayList<Move> validMoves, ArrayList<Move> generatedValidMoves) {
        for (Move gen_move : generatedValidMoves) {
            boolean exists = false;
            for (Move valid_move : validMoves) {
                if (gen_move.getPiece().equals(valid_move.getPiece()) &&
                        gen_move.getEndSquare().getColumn() == valid_move.getEndSquare().getColumn() &&
                        gen_move.getEndSquare().getRow() == valid_move.getEndSquare().getRow()) {
                    exists = true;
                }
            }
            if (!exists){
                System.out.println("Generated moved does not exists! " + gen_move);
            }
            Assert.assertTrue(exists);
        }

        Assert.assertEquals(validMoves.size(), generatedValidMoves.size());
    }

    static ArrayList<Move> convertEndSquaresToMoves(ArrayList<Square> endSquares, Piece piece){
        ArrayList<Move> validMoves = new ArrayList<>();
        for (Square endSquare : endSquares) {
            validMoves.add(new Move(piece.getSquare(), endSquare, piece));
        }
        return validMoves;
    }
}
