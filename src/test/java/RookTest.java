package test.java;

import main.java.model.Board;
import main.java.model.PieceColor;
import main.java.model.Square;
import main.java.model.pieces.*;
import org.junit.Test;

import java.util.ArrayList;

import static test.java.Utils.testMoves;

/**
 * Created by mikke on 17-Feb-17.
 */
public class RookTest {
    @Test
    public void testValidMoves() {
        ArrayList<Piece> white = new ArrayList<>();
        Rook rook = new Rook(PieceColor.WHITE, new Square(4, 4));
        white.add(rook);

        Board board = new Board(white, new ArrayList<>());

        ArrayList<Square> validMoves = new ArrayList<>();
        // Straight lines
        validMoves.add(new Square(0, 4));
        validMoves.add(new Square(1, 4));
        validMoves.add(new Square(2, 4));
        validMoves.add(new Square(3, 4));
        validMoves.add(new Square(5, 4));
        validMoves.add(new Square(6, 4));
        validMoves.add(new Square(7, 4));
        validMoves.add(new Square(4, 0));
        validMoves.add(new Square(4, 1));
        validMoves.add(new Square(4, 2));
        validMoves.add(new Square(4, 3));
        validMoves.add(new Square(4, 5));
        validMoves.add(new Square(4, 6));
        validMoves.add(new Square(4, 7));

        ArrayList<Square> generatedValidMoves = rook.validMoves(board.generateBitmapPositions(), null);

        testMoves(validMoves, generatedValidMoves);

        white = new ArrayList<>();
        rook = new Rook(PieceColor.WHITE, new Square(4, 4));
        white.add(rook);

        ArrayList<Piece> black = new ArrayList<>();
        black.add(new Pawn(PieceColor.BLACK, new Square(3, 3), false));
        black.add(new Pawn(PieceColor.BLACK, new Square(4, 3), false));

        board = new Board(white, black);

        validMoves = new ArrayList<>();
        // Straight lines
        validMoves.add(new Square(0, 4));
        validMoves.add(new Square(1, 4));
        validMoves.add(new Square(2, 4));
        validMoves.add(new Square(3, 4));
        validMoves.add(new Square(5, 4));
        validMoves.add(new Square(6, 4));
        validMoves.add(new Square(7, 4));
        validMoves.add(new Square(4, 3));
        validMoves.add(new Square(4, 5));
        validMoves.add(new Square(4, 6));
        validMoves.add(new Square(4, 7));

        generatedValidMoves = rook.validMoves(board.generateBitmapPositions(), null);

        testMoves(validMoves, generatedValidMoves);

        white = new ArrayList<>();
        rook = new Rook(PieceColor.WHITE, new Square(4, 4));
        white.add(rook);
        white.add(new King(PieceColor.WHITE, new Square(5,3)));

        black = new ArrayList<>();
        black.add(new Pawn(PieceColor.BLACK, new Square(3, 3), false));

        board = new Board(white, black);

        validMoves = new ArrayList<>();
        // Straight lines
        validMoves.add(new Square(0, 4));
        validMoves.add(new Square(1, 4));
        validMoves.add(new Square(2, 4));
        validMoves.add(new Square(3, 4));
        validMoves.add(new Square(5, 4));
        validMoves.add(new Square(6, 4));
        validMoves.add(new Square(7, 4));
        validMoves.add(new Square(4, 0));
        validMoves.add(new Square(4, 1));
        validMoves.add(new Square(4, 2));
        validMoves.add(new Square(4, 3));
        validMoves.add(new Square(4, 5));
        validMoves.add(new Square(4, 6));
        validMoves.add(new Square(4, 7));

        generatedValidMoves = rook.validMoves(board.generateBitmapPositions(), null);

        testMoves(validMoves, generatedValidMoves);
    }
}