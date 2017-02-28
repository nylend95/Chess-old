package test.java;

import main.java.model.Board;
import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.Square;
import main.java.model.pieces.Bishop;
import main.java.model.pieces.Pawn;
import main.java.model.pieces.Piece;
import main.java.model.pieces.Rook;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static test.java.Utils.testMoves;

/**
 * Created by mikke on 20-Feb-17.
 */
public class PawnTest {
    @Test
    public void testValidMoves() {

        //Allows double move when pawn is not moved
        ArrayList<Piece> white = new ArrayList<>();
        Pawn pawn1 = new Pawn(PieceColor.WHITE, new Square(6, 1));
        white.add(pawn1);

        Board board = new Board(white, new ArrayList<>());

        ArrayList<Square> endSquares = new ArrayList<>();
        endSquares.add(new Square(5, 1));
        endSquares.add(new Square(4, 1));

        ArrayList<Move> generatedValidMoves = board.generateValidMoves(pawn1);
        ArrayList<Move> validMoves = Utils.convertEndSquaresToMoves(endSquares, pawn1);

        testMoves(validMoves, generatedValidMoves);

        //Not double move when pawn is moved
        white = new ArrayList<>();
        pawn1 = new Pawn(PieceColor.WHITE, new Square(5, 1));
        pawn1.setMoved(true);
        white.add(pawn1);

        board = new Board(white, new ArrayList<>());

        endSquares = new ArrayList<>();
        endSquares.add(new Square(4, 1));

        generatedValidMoves = board.generateValidMoves(pawn1);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, pawn1);

        testMoves(validMoves, generatedValidMoves);


        //Opponent blocking double move when pawn is not moved
        white = new ArrayList<>();
        pawn1 = new Pawn(PieceColor.WHITE, new Square(6, 1));
        white.add(pawn1);

        ArrayList<Piece> black = new ArrayList<>();
        Bishop blackBishop = new Bishop(PieceColor.BLACK, new Square(4,1));
        black.add(blackBishop);

        board = new Board(white, black);

        endSquares = new ArrayList<>();
        endSquares.add(new Square(5, 1));

        generatedValidMoves = board.generateValidMoves(pawn1);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, pawn1);

        testMoves(validMoves, generatedValidMoves);

        //Allows capture diagonally
        white = new ArrayList<>();
        pawn1 = new Pawn(PieceColor.WHITE, new Square(6, 1));
        white.add(pawn1);

        black = new ArrayList<>();
        blackBishop = new Bishop(PieceColor.BLACK, new Square(5, 2));
        Rook blackRook = new Rook(PieceColor.BLACK, new Square(5, 0));
        black.add(blackBishop);
        black.add(blackRook);

        board = new Board(white, black);

        endSquares = new ArrayList<>();
        endSquares.add(new Square(4, 1));
        endSquares.add(new Square(5, 1));
        endSquares.add(new Square(5, 2));
        endSquares.add(new Square(5, 0));

        generatedValidMoves = board.generateValidMoves(pawn1);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, pawn1);

        testMoves(validMoves, generatedValidMoves);

        //Self blocking double move

        white = new ArrayList<>();
        pawn1 = new Pawn(PieceColor.WHITE, new Square(6, 1));
        Bishop whiteBishop = new Bishop(PieceColor.WHITE, new Square(4,1));
        white.add(pawn1);
        white.add(whiteBishop);

        black = new ArrayList<>();

        board = new Board(white, black);

        endSquares = new ArrayList<>();
        endSquares.add(new Square(5, 1));

        generatedValidMoves = board.generateValidMoves(pawn1);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, pawn1);

        testMoves(validMoves, generatedValidMoves);


        //Self blocking normal move
        white = new ArrayList<>();
        pawn1 = new Pawn(PieceColor.WHITE, new Square(6, 1));
        whiteBishop = new Bishop(PieceColor.WHITE, new Square(5,1));
        white.add(pawn1);
        white.add(whiteBishop);

        black = new ArrayList<>();

        board = new Board(white, black);

        endSquares = new ArrayList<>();

        generatedValidMoves = board.generateValidMoves(pawn1);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, pawn1);

        testMoves(validMoves, generatedValidMoves);




        //TODO: Test En Pessant
        //En Pessant
        /*
        white = new ArrayList<>();
        pawn1 = new Pawn(PieceColor.WHITE, new Square(3, 1));
        pawn1.setMoved(true);
        white.add(pawn1);

        black = new ArrayList<>();
        Pawn blackPawn = new Pawn(PieceColor.BLACK, new Square(1, 2));
        black.add(blackPawn);

        board = new Board(white, black);
        board.movePiece(new Move(new Square(1, 2), new Square(3, 2), blackPawn), true);

        endSquares = new ArrayList<>();
        endSquares.add(new Square(2, 2));
        endSquares.add(new Square(2, 1));

        generatedValidMoves = board.generateValidMoves(pawn1);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, pawn1);

        testMoves(validMoves, generatedValidMoves);
        */
    }
}
