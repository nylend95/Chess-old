package test.java;

import main.java.model.Board;
import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.Square;
import main.java.model.pieces.*;
import org.junit.Test;

import java.util.ArrayList;

import static test.java.Utils.testMoves;

/**
 * Created by mikke on 16-Feb-17.
 */
public class QueenTest {
    @Test
    public void testValidMoves() {
        ArrayList<Piece> white = new ArrayList<>();
        Queen queen = new Queen(PieceColor.WHITE, new Square(4, 4));
        white.add(queen);

        Board board = new Board(white, new ArrayList<>());

        ArrayList<Square> validEndSquares = new ArrayList<>();
        // Diagonal lines
        validEndSquares.add(new Square(3, 3));
        validEndSquares.add(new Square(2, 2));
        validEndSquares.add(new Square(1, 1));
        validEndSquares.add(new Square(0, 0));
        validEndSquares.add(new Square(3, 5));
        validEndSquares.add(new Square(2, 6));
        validEndSquares.add(new Square(1, 7));
        validEndSquares.add(new Square(5, 3));
        validEndSquares.add(new Square(6, 2));
        validEndSquares.add(new Square(7, 1));
        validEndSquares.add(new Square(5, 5));
        validEndSquares.add(new Square(6, 6));
        validEndSquares.add(new Square(7, 7));

        // Straight lines
        validEndSquares.add(new Square(0, 4));
        validEndSquares.add(new Square(1, 4));
        validEndSquares.add(new Square(2, 4));
        validEndSquares.add(new Square(3, 4));
        validEndSquares.add(new Square(5, 4));
        validEndSquares.add(new Square(6, 4));
        validEndSquares.add(new Square(7, 4));
        validEndSquares.add(new Square(4, 0));
        validEndSquares.add(new Square(4, 1));
        validEndSquares.add(new Square(4, 2));
        validEndSquares.add(new Square(4, 3));
        validEndSquares.add(new Square(4, 5));
        validEndSquares.add(new Square(4, 6));
        validEndSquares.add(new Square(4, 7));

        ArrayList<Move> generatedValidMoves = queen.validMoves(board.getBitmapPositions(), null);
        ArrayList<Move> validMoves = Utils.convertEndSquaresToMoves(validEndSquares, queen);

        testMoves(validMoves, generatedValidMoves);

        white = new ArrayList<>();
        queen = new Queen(PieceColor.WHITE, new Square(4, 4));
        white.add(queen);

        ArrayList<Piece> black = new ArrayList<>();
        black.add(new Pawn(PieceColor.BLACK, new Square(3, 3), false));
        black.add(new Pawn(PieceColor.BLACK, new Square(4, 3), false));

        board = new Board(white, black);

        validEndSquares = new ArrayList<>();
        validEndSquares.add(new Square(3, 3));
        validEndSquares.add(new Square(3, 5));
        validEndSquares.add(new Square(2, 6));
        validEndSquares.add(new Square(1, 7));
        validEndSquares.add(new Square(5, 3));
        validEndSquares.add(new Square(6, 2));
        validEndSquares.add(new Square(7, 1));
        validEndSquares.add(new Square(5, 5));
        validEndSquares.add(new Square(6, 6));
        validEndSquares.add(new Square(7, 7));

        // Straight lines
        validEndSquares.add(new Square(0, 4));
        validEndSquares.add(new Square(1, 4));
        validEndSquares.add(new Square(2, 4));
        validEndSquares.add(new Square(3, 4));
        validEndSquares.add(new Square(5, 4));
        validEndSquares.add(new Square(6, 4));
        validEndSquares.add(new Square(7, 4));
        validEndSquares.add(new Square(4, 3));
        validEndSquares.add(new Square(4, 5));
        validEndSquares.add(new Square(4, 6));
        validEndSquares.add(new Square(4, 7));

        generatedValidMoves = queen.validMoves(board.getBitmapPositions(), null);
        validMoves = Utils.convertEndSquaresToMoves(validEndSquares, queen);

        testMoves(validMoves, generatedValidMoves);

        white = new ArrayList<>();
        queen = new Queen(PieceColor.WHITE, new Square(4, 4));
        white.add(queen);
        white.add(new King(PieceColor.WHITE, new Square(5,3)));

        black = new ArrayList<>();
        black.add(new Pawn(PieceColor.BLACK, new Square(3, 3), false));

        board = new Board(white, black);

        validEndSquares = new ArrayList<>();
        validEndSquares.add(new Square(3, 3));
        validEndSquares.add(new Square(3, 5));
        validEndSquares.add(new Square(2, 6));
        validEndSquares.add(new Square(1, 7));
        validEndSquares.add(new Square(5, 5));
        validEndSquares.add(new Square(6, 6));
        validEndSquares.add(new Square(7, 7));

        // Straight lines
        validEndSquares.add(new Square(0, 4));
        validEndSquares.add(new Square(1, 4));
        validEndSquares.add(new Square(2, 4));
        validEndSquares.add(new Square(3, 4));
        validEndSquares.add(new Square(5, 4));
        validEndSquares.add(new Square(6, 4));
        validEndSquares.add(new Square(7, 4));
        validEndSquares.add(new Square(4, 0));
        validEndSquares.add(new Square(4, 1));
        validEndSquares.add(new Square(4, 2));
        validEndSquares.add(new Square(4, 3));
        validEndSquares.add(new Square(4, 5));
        validEndSquares.add(new Square(4, 6));
        validEndSquares.add(new Square(4, 7));

        generatedValidMoves = queen.validMoves(board.getBitmapPositions(), null);
        validMoves = Utils.convertEndSquaresToMoves(validEndSquares, queen);

        testMoves(validMoves, generatedValidMoves);
    }
}
