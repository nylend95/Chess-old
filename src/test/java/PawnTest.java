package test.java;

import main.java.model.Board;
import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.Square;
import main.java.model.pieces.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static test.java.Utils.testMoves;

/**
 * Created by mikke on 20-Feb-17.
 */
public class PawnTest {
    @Test
    public void testValidMoves() {
        ArrayList<Piece> white = new ArrayList<>();
        Pawn whitePawn = new Pawn(PieceColor.WHITE, new Square(6, 4));
        white.add(whitePawn);

        ArrayList<Piece> black = new ArrayList<>();

        Board board = new Board(white, black);

        ArrayList<Square> endSquares = new ArrayList<>();
        endSquares.add(new Square(5, 4));
        endSquares.add(new Square(4, 4));

        ArrayList<Move> generatedValidMoves = board.generateValidMoves(whitePawn);
        ArrayList<Move> validMoves = Utils.convertEndSquaresToMoves(endSquares, whitePawn);
        testMoves(validMoves, generatedValidMoves);

        board.movePiece(new Move(whitePawn.getSquare(), new Square(4, 4), whitePawn), true);

        endSquares = new ArrayList<>();
        endSquares.add(new Square(3, 4));

        generatedValidMoves = board.generateValidMoves(whitePawn);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, whitePawn);
        testMoves(validMoves, generatedValidMoves);


        // Test capture
        white = new ArrayList<>();
        whitePawn = new Pawn(PieceColor.WHITE, new Square(4, 4));
        whitePawn.setMoved(true);
        white.add(whitePawn);

        black = new ArrayList<>();
        black.add(new Queen(PieceColor.BLACK, new Square(3, 3)));

        board = new Board(white, black);

        endSquares = new ArrayList<>();
        endSquares.add(new Square(3, 4));
        endSquares.add(new Square(3, 3));

        generatedValidMoves = board.generateValidMoves(whitePawn);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, whitePawn);
        testMoves(validMoves, generatedValidMoves);


        // Test En Passsant
        white = new ArrayList<>();
        whitePawn = new Pawn(PieceColor.WHITE, new Square(4, 4));
        whitePawn.setMoved(true);
        white.add(whitePawn);
        white.add(new King(PieceColor.WHITE, new Square(7, 7)));


        black = new ArrayList<>();
        Pawn blackPawn = new Pawn(PieceColor.BLACK, new Square(1, 3));
        black.add(blackPawn);
        black.add(new King(PieceColor.BLACK, new Square(0, 0)));

        board = new Board(white, black);
        board.movePiece(new Move(whitePawn.getSquare(), new Square(3, 4), whitePawn), true);
        board.movePiece(new Move(blackPawn.getSquare(), new Square(3, 3), blackPawn), true);

        endSquares = new ArrayList<>();
        endSquares.add(new Square(2, 4));
        endSquares.add(new Square(2, 3));

        generatedValidMoves = board.generateValidMoves(whitePawn);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, whitePawn);
        testMoves(validMoves, generatedValidMoves);


        // Test promotion
        white = new ArrayList<>();
        whitePawn = new Pawn(PieceColor.WHITE, new Square(2, 4));
        whitePawn.setMoved(true);
        white.add(whitePawn);
        white.add(new King(PieceColor.WHITE, new Square(7, 7)));

        black = new ArrayList<>();
        blackPawn = new Pawn(PieceColor.BLACK, new Square(3, 3));
        black.add(blackPawn);
        black.add(new King(PieceColor.BLACK, new Square(2, 0)));

        board = new Board(white, black);
        board.movePiece(new Move(whitePawn.getSquare(), new Square(1, 4), whitePawn), true);
        board.movePiece(new Move(blackPawn.getSquare(), new Square(4, 3), blackPawn), true);

        endSquares = new ArrayList<>();
        endSquares.add(new Square(0, 4));

        generatedValidMoves = board.generateValidMoves(whitePawn);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, whitePawn);
        testMoves(validMoves, generatedValidMoves);

        board.movePiece(new Move(whitePawn.getSquare(), new Square(0, 4), whitePawn), true);

        Assert.assertEquals(true, whitePawn.isPromoted());
        Assert.assertEquals(true, whitePawn.getPromotedTo() instanceof Queen);

        endSquares = new ArrayList<>();
        endSquares.add(new Square(1, 3));
        endSquares.add(new Square(2, 2));
        endSquares.add(new Square(3, 1));
        endSquares.add(new Square(4, 0));

        endSquares.add(new Square(1, 5));
        endSquares.add(new Square(2, 6));
        endSquares.add(new Square(3, 7));

        endSquares.add(new Square(1, 4));
        endSquares.add(new Square(2, 4));
        endSquares.add(new Square(3, 4));
        endSquares.add(new Square(4, 4));
        endSquares.add(new Square(5, 4));
        endSquares.add(new Square(6, 4));
        endSquares.add(new Square(7, 4));

        endSquares.add(new Square(0, 0));
        endSquares.add(new Square(0, 1));
        endSquares.add(new Square(0, 2));
        endSquares.add(new Square(0, 3));
        endSquares.add(new Square(0, 5));
        endSquares.add(new Square(0, 6));
        endSquares.add(new Square(0, 7));

        generatedValidMoves = board.generateValidMoves(whitePawn);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, whitePawn.getPromotedTo());
        testMoves(validMoves, generatedValidMoves);

        //Self blocking normal move
        white = new ArrayList<>();
        whitePawn = new Pawn(PieceColor.WHITE, new Square(6, 1));
        Bishop whiteBishop = new Bishop(PieceColor.WHITE, new Square(5, 1));
        white.add(whitePawn);
        white.add(whiteBishop);

        black = new ArrayList<>();

        board = new Board(white, black);

        endSquares = new ArrayList<>();

        generatedValidMoves = board.generateValidMoves(whitePawn);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, whitePawn);

        testMoves(validMoves, generatedValidMoves);

        //Self blocking double move
        white = new ArrayList<>();
        whitePawn = new Pawn(PieceColor.WHITE, new Square(6, 1));
        whiteBishop = new Bishop(PieceColor.WHITE, new Square(4, 1));
        white.add(whitePawn);
        white.add(whiteBishop);

        black = new ArrayList<>();

        board = new Board(white, black);

        endSquares = new ArrayList<>();
        endSquares.add(new Square(5, 1));

        generatedValidMoves = board.generateValidMoves(whitePawn);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, whitePawn);
        testMoves(validMoves, generatedValidMoves);
    }
}
