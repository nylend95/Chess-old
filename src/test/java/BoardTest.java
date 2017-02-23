package test.java;

import main.java.model.Board;
import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.Square;
import main.java.model.pieces.Bishop;
import main.java.model.pieces.King;
import main.java.model.pieces.Piece;
import main.java.model.pieces.Rook;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Jesper Nylend on 16.02.2017.
 * s305070
 */
public class BoardTest {
    @Test
    public void testBitmap() {
        int[][] testmap =
                {
                        {-1, -1, -1, -1, -1, -1, -1, -1},
                        {-1, -1, -1, -1, -1, -1, -1, -1},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {1, 1, 1, 1, 1, 1, 1, 1},
                        {1, 1, 1, 1, 1, 1, 1, 1}};

        Board board = new Board();
        Assert.assertEquals(testmap, board.getBitmapPositions());

    }


    @Test
    public void testMovePiece() {
        ArrayList<Piece> white = new ArrayList<>();
        Bishop bishop = new Bishop(PieceColor.WHITE, new Square(4, 4));
        white.add(bishop);

        Board board = new Board(white, new ArrayList<>());

        Square moveToSquare = new Square(2, 2);
        board.movePiece(new Move(bishop.getSquare(), moveToSquare, bishop), true);

        Assert.assertEquals(bishop.getSquare(), moveToSquare);
    }

    @Test
    public void testMoveHistory() {
        ArrayList<Piece> white = new ArrayList<>();
        Bishop bishop = new Bishop(PieceColor.WHITE, new Square(4, 4));
        white.add(bishop);

        Board board = new Board(white, new ArrayList<>());

        Square moveToSquare = new Square(2, 2);
        board.movePiece(new Move(bishop.getSquare(), moveToSquare, bishop), true);

        ArrayList<Move> moveHistory = board.getMoveHistory();
        Assert.assertNotEquals(0, moveHistory.size());
        Assert.assertEquals(moveHistory.get(moveHistory.size() - 1).getStartSquare(), new Square(4, 4));
        Assert.assertEquals(moveHistory.get(moveHistory.size() - 1).getEndSquare(), new Square(2, 2));

        // Check again that nothing has been added to move history
        board.generateValidMoves(bishop);
        Assert.assertNotEquals(0, moveHistory.size());
        Assert.assertEquals(moveHistory.get(moveHistory.size() - 1).getStartSquare(), new Square(4, 4));
        Assert.assertEquals(moveHistory.get(moveHistory.size() - 1).getEndSquare(), new Square(2, 2));
    }

    @Test
    public void testUpdateStatus() {
        // Checkmate for white
        ArrayList<Piece> white = new ArrayList<>();
        Rook whiteRook1 = new Rook(PieceColor.WHITE, new Square(4, 2));
        Rook whiteRook2 = new Rook(PieceColor.WHITE, new Square(6, 1));
        King whiteKing = new King(PieceColor.WHITE, new Square(3, 3));
        white.add(whiteRook1);
        white.add(whiteKing);
        white.add(whiteRook2);

        ArrayList<Piece> black = new ArrayList<>();
        King blackKing = new King(PieceColor.BLACK, new Square(2, 0));
        black.add(blackKing);

        Board board = new Board(white, black);

        board.updateStatus();

        Assert.assertEquals(0, board.getStatus());
        board.movePiece(new Move(whiteRook1.getSquare(), new Square(4, 3), whiteRook1), true);
        Assert.assertEquals(0, board.getStatus());
        board.movePiece(new Move(blackKing.getSquare(), new Square(1, 0), blackKing), true);
        Assert.assertEquals(0, board.getStatus());
        board.movePiece(new Move(whiteRook1.getSquare(), new Square(4, 0), whiteRook1), true);
        Assert.assertEquals(1, board.getStatus());
    }


}
