package test.java;

import main.java.model.Board;
import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.Square;
import main.java.model.pieces.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static test.java.Utils.testMoves;

/**
 * Created by mikke on 14-Feb-17.
 */
public class KingTest {
    @Test
    public void testValidMoves() {
        // Test regular moves
        ArrayList<Piece> white = new ArrayList<>();
        King king = new King(PieceColor.WHITE, new Square(4, 4));
        white.add(king);

        Board board = new Board(white, new ArrayList<>());

        ArrayList<Square> endSquares = new ArrayList<>();
        endSquares.add(new Square(3, 3));
        endSquares.add(new Square(5, 5));
        endSquares.add(new Square(5, 3));
        endSquares.add(new Square(3, 5));
        endSquares.add(new Square(4, 5));
        endSquares.add(new Square(5, 4));
        endSquares.add(new Square(3, 4));
        endSquares.add(new Square(4, 3));

        ArrayList<Move> generatedValidMoves = board.generateValidMoves(king);
        ArrayList<Move> validMoves = Utils.convertEndSquaresToMoves(endSquares, king);

        testMoves(validMoves, generatedValidMoves);

        // Test capture and check
        white = new ArrayList<>();
        king = new King(PieceColor.WHITE, new Square(4, 4));
        white.add(king);

        ArrayList<Piece> black = new ArrayList<>();
        black.add(new Queen(PieceColor.BLACK, new Square(3, 4)));

        board = new Board(white, black);

        endSquares = new ArrayList<>();
        endSquares.add(new Square(5, 5));
        endSquares.add(new Square(5, 3));
        endSquares.add(new Square(3, 4));

        generatedValidMoves = board.generateValidMoves(king);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, king);

        testMoves(validMoves, generatedValidMoves);

        // Test castling
        white = new ArrayList<>();
        king = new King(PieceColor.WHITE, new Square(7, 4));
        white.add(king);
        white.add(new Pawn(PieceColor.WHITE, new Square(6, 4), true));
        white.add(new Pawn(PieceColor.WHITE, new Square(6, 3), true));
        white.add(new Pawn(PieceColor.WHITE, new Square(6, 5), true));
        white.add(new Rook(PieceColor.WHITE, new Square(7, 0)));
        white.add(new Rook(PieceColor.WHITE, new Square(7, 7)));

        black = new ArrayList<>();

        board = new Board(white, black);

        endSquares = new ArrayList<>();
        endSquares.add(new Square(7, 5));
        endSquares.add(new Square(7, 3));
        endSquares.add(new Square(7, 2)); // Castling left
        endSquares.add(new Square(7, 6)); // Castling right

        generatedValidMoves = board.generateValidMoves(king);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, king);

        testMoves(validMoves, generatedValidMoves);
    }

}
