package test.java;

import main.java.model.Board;
import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.Square;
import main.java.model.pieces.Bishop;
import main.java.model.pieces.Knight;
import main.java.model.pieces.Pawn;
import main.java.model.pieces.Piece;
import org.junit.Test;

import java.util.ArrayList;

import static test.java.Utils.testMoves;
/**
 * Created by Jesper Nylend on 18.02.2017.
 * s305070
 */
public class KnightTest {

    @Test
    public void TestValidMoves(){
        //Testing knight by itself
        ArrayList<Piece> white = new ArrayList<>();
        Knight knight = new Knight(PieceColor.WHITE, new Square(4, 4));
        white.add(knight);

        Board board = new Board(white, new ArrayList<>());

        ArrayList<Square> endSquares = new ArrayList<>();
        endSquares.add(new Square(6,5));
        endSquares.add(new Square(6,3));
        endSquares.add(new Square(5,6));
        endSquares.add(new Square(5,2));
        endSquares.add(new Square(3,6));
        endSquares.add(new Square(3,2));
        endSquares.add(new Square(2,5));
        endSquares.add(new Square(2,3));

        ArrayList<Move> generatedValidMoves = knight.validMoves(board.getBitmapPositions(), null);
        ArrayList<Move> validMoves = Utils.convertEndSquaresToMoves(endSquares, knight);

        testMoves(validMoves, generatedValidMoves);

        //Adding opponent piece
        ArrayList<Piece> black = new ArrayList<>();
        Bishop blackBishop = new Bishop(PieceColor.BLACK, new Square(6,5));
        black.add(blackBishop);

        endSquares = new ArrayList<>();
        endSquares.add(new Square(6,5));
        endSquares.add(new Square(6,3));
        endSquares.add(new Square(5,6));
        endSquares.add(new Square(5,2));
        endSquares.add(new Square(3,6));
        endSquares.add(new Square(3,2));
        endSquares.add(new Square(2,5));
        endSquares.add(new Square(2,3));

        board = new Board(white, black);

        generatedValidMoves = knight.validMoves(board.getBitmapPositions(), null);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, knight);

        testMoves(validMoves, generatedValidMoves);


        //Adding own piece on "valid move"
        Bishop bishop = new Bishop(PieceColor.WHITE, new Square(3,2));
        white.add(bishop);

        endSquares = new ArrayList<>();
        endSquares.add(new Square(6,5));
        endSquares.add(new Square(6,3));
        endSquares.add(new Square(5,6));
        endSquares.add(new Square(5,2));
        endSquares.add(new Square(3,6));
        endSquares.add(new Square(2,5));
        endSquares.add(new Square(2,3));

        board = new Board(white, black);

        generatedValidMoves = knight.validMoves(board.getBitmapPositions(), null);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, knight);

        testMoves(validMoves, generatedValidMoves);

        //Initial positions
        Knight knightStart = new Knight(PieceColor.WHITE, new Square(7,6));
        Pawn pawn1 = new Pawn(PieceColor.WHITE, new Square(6,7), false);
        Pawn pawn2 = new Pawn(PieceColor.WHITE, new Square(6,6), false);
        Pawn pawn3 = new Pawn(PieceColor.WHITE, new Square(6,5), false);
        Pawn pawn4 = new Pawn(PieceColor.WHITE, new Square(6,4), false);
        white = new ArrayList<>();
        white.add(knightStart);
        white.add(pawn1);
        white.add(pawn2);
        white.add(pawn3);
        white.add(pawn4);

        endSquares = new ArrayList<>();
        endSquares.add(new Square(5,5));
        endSquares.add(new Square(5,7));

        board = new Board(white, new ArrayList<>());

        generatedValidMoves = knightStart.validMoves(board.getBitmapPositions(), null);
        validMoves = Utils.convertEndSquaresToMoves(endSquares, knightStart);

        testMoves(validMoves, generatedValidMoves);



    }

}
