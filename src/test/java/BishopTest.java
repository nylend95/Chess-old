package test.java;

import main.java.model.Board;
import main.java.model.PieceColor;
import main.java.model.Square;
import main.java.model.pieces.Bishop;
import main.java.model.pieces.King;
import main.java.model.pieces.Pawn;
import main.java.model.pieces.Piece;
import org.junit.Test;

import java.util.ArrayList;

import static test.java.Utils.testMoves;

/**
 * Created by mikke on 14-Feb-17.
 */
public class BishopTest {
    @Test
    public void testValidMoves() {
        ArrayList<Piece> white = new ArrayList<>();
        Bishop bishop = new Bishop(PieceColor.WHITE, new Square(4, 4));
        white.add(bishop);

        Board board = new Board(white, new ArrayList<>());

        ArrayList<Square> validMoves = new ArrayList<>();
        validMoves.add(new Square(3, 3));
        validMoves.add(new Square(2, 2));
        validMoves.add(new Square(1, 1));
        validMoves.add(new Square(0, 0));
        validMoves.add(new Square(3, 5));
        validMoves.add(new Square(2, 6));
        validMoves.add(new Square(1, 7));
        validMoves.add(new Square(5, 3));
        validMoves.add(new Square(6, 2));
        validMoves.add(new Square(7, 1));
        validMoves.add(new Square(5, 5));
        validMoves.add(new Square(6, 6));
        validMoves.add(new Square(7, 7));

        ArrayList<Square> generatedValidMoves = bishop.validMoves(board.generateBitmap());

        testMoves(validMoves, generatedValidMoves);


        white = new ArrayList<>();
        bishop = new Bishop(PieceColor.WHITE, new Square(4, 4));
        white.add(bishop);

        ArrayList<Piece> black = new ArrayList<>();
        black.add(new Pawn(PieceColor.BLACK, new Square(3, 3), false));

        board = new Board(white, black);

        validMoves = new ArrayList<>();
        validMoves.add(new Square(3, 3));
        validMoves.add(new Square(3, 5));
        validMoves.add(new Square(2, 6));
        validMoves.add(new Square(1, 7));
        validMoves.add(new Square(5, 3));
        validMoves.add(new Square(6, 2));
        validMoves.add(new Square(7, 1));
        validMoves.add(new Square(5, 5));
        validMoves.add(new Square(6, 6));
        validMoves.add(new Square(7, 7));

        generatedValidMoves = bishop.validMoves(board.generateBitmap());

        testMoves(validMoves, generatedValidMoves);


        white = new ArrayList<>();
        bishop = new Bishop(PieceColor.WHITE, new Square(4, 4));
        white.add(bishop);
        white.add(new King(PieceColor.WHITE, new Square(5,3)));

        black = new ArrayList<>();
        black.add(new Pawn(PieceColor.BLACK, new Square(3, 3), false));

        board = new Board(white, black);

        validMoves = new ArrayList<>();
        validMoves.add(new Square(3, 3));
        validMoves.add(new Square(3, 5));
        validMoves.add(new Square(2, 6));
        validMoves.add(new Square(1, 7));
        validMoves.add(new Square(5, 5));
        validMoves.add(new Square(6, 6));
        validMoves.add(new Square(7, 7));

        generatedValidMoves = bishop.validMoves(board.generateBitmap());

        testMoves(validMoves, generatedValidMoves);

    }
}
