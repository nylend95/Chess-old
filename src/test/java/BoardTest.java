package test.java;

import main.java.model.Board;
import main.java.model.PieceColor;
import main.java.model.Square;
import main.java.model.pieces.Bishop;
import main.java.model.pieces.Piece;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Jesper Nylend on 16.02.2017.
 * s305070
 */
public class BoardTest {
    @Test
    public void testBitmap(){
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
    public void testMovePiece(){
        ArrayList<Piece> white = new ArrayList<>();
        Bishop bishop = new Bishop(PieceColor.WHITE, new Square(4, 4));
        white.add(bishop);

        Board board = new Board(white, new ArrayList<>());

    }


}
