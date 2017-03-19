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

        // Test checkmate
        white = new ArrayList<>();
        king = new King(PieceColor.WHITE, new Square(7, 7));
        white.add(king);

        black = new ArrayList<>();
        black.add(new Queen(PieceColor.BLACK, new Square(3, 6)));
        black.add(new Rook(PieceColor.BLACK, new Square(4, 7)));

        board = new Board(white, black);

        endSquares = new ArrayList<>(); // No possible moves. Checkmate

        generatedValidMoves = board.generateValidMoves(king);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, king);

        testMoves(validMoves, generatedValidMoves);
    }

    @Test
    public void testCastling() {
        // Test castling
        ArrayList<Piece> white = new ArrayList<>();
        King whiteKing = new King(PieceColor.WHITE, new Square(7, 4));
        white.add(whiteKing);
        white.add(new Pawn(PieceColor.WHITE, new Square(6, 4)));
        white.add(new Pawn(PieceColor.WHITE, new Square(6, 3)));
        white.add(new Pawn(PieceColor.WHITE, new Square(6, 5)));
        white.add(new Rook(PieceColor.WHITE, new Square(7, 0)));
        white.add(new Rook(PieceColor.WHITE, new Square(7, 7)));

        ArrayList<Piece> black = new ArrayList<>();

        Board board = new Board(white, black);

        ArrayList<Square> endSquares = new ArrayList<>();
        endSquares.add(new Square(7, 5));
        endSquares.add(new Square(7, 3));
        endSquares.add(new Square(7, 2)); // Castling left
        endSquares.add(new Square(7, 6)); // Castling right

        ArrayList<Move> generatedValidMoves = board.generateValidMoves(whiteKing);
        ArrayList<Move> validMoves = Utils.convertEndSquaresToMoves(endSquares, whiteKing);

        testMoves(validMoves, generatedValidMoves);

        // Test non-valid castling because of check
        white = new ArrayList<>();
        whiteKing = new King(PieceColor.WHITE, new Square(7, 4));
        white.add(whiteKing);
        white.add(new Pawn(PieceColor.WHITE, new Square(6, 4)));
        white.add(new Pawn(PieceColor.WHITE, new Square(6, 3)));
        white.add(new Pawn(PieceColor.WHITE, new Square(6, 5)));
        white.add(new Rook(PieceColor.WHITE, new Square(7, 0)));
        white.add(new Rook(PieceColor.WHITE, new Square(7, 7)));

        black = new ArrayList<>();
        black.add(new Rook(PieceColor.BLACK, new Square(0, 2)));

        board = new Board(white, black);

        endSquares = new ArrayList<>();
        endSquares.add(new Square(7, 5));
        endSquares.add(new Square(7, 3));
        endSquares.add(new Square(7, 6)); // Only castling right possible because of rook on column 2

        generatedValidMoves = board.generateValidMoves(whiteKing);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, whiteKing);

        testMoves(validMoves, generatedValidMoves);


        // Test non-valid castling
        white = new ArrayList<>();
        whiteKing = new King(PieceColor.WHITE, new Square(7, 4));
        white.add(whiteKing);
        white.add(new Pawn(PieceColor.WHITE, new Square(6, 4)));
        white.add(new Pawn(PieceColor.WHITE, new Square(6, 3)));
        white.add(new Pawn(PieceColor.WHITE, new Square(6, 5)));
        white.add(new Rook(PieceColor.WHITE, new Square(7, 0)));
        white.add(new Rook(PieceColor.WHITE, new Square(6, 7)));

        black = new ArrayList<>();
        King blackKing = new King(PieceColor.BLACK, new Square(2, 2));
        black.add(blackKing);

        board = new Board(white, black);
        board.setBlackKing(blackKing);
        board.setWhiteKing(whiteKing);
        board.movePiece(new Move(whiteKing.getSquare(), new Square(7, 5), whiteKing), true);
        board.movePiece(new Move(blackKing.getSquare(), new Square(2, 3), blackKing), true);
        board.movePiece(new Move(whiteKing.getSquare(), new Square(7, 4), whiteKing), true);
        board.movePiece(new Move(blackKing.getSquare(), new Square(2, 4), blackKing), true);


        endSquares = new ArrayList<>();
        endSquares.add(new Square(7, 5));
        endSquares.add(new Square(7, 3));

        generatedValidMoves = board.generateValidMoves(whiteKing);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, whiteKing);

        testMoves(validMoves, generatedValidMoves);
    }

}
