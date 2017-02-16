package test.java;

import main.java.model.Board;
import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertEquals(testmap, board.getBitmap());

    }


}
