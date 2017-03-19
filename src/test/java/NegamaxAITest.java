package test.java;

import main.java.controller.IControls;
import main.java.model.Board;
import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.players.NegamaxAI;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mikkel on 19-Mar-17.
 */
public class NegamaxAITest {
    @Test
    public void speedTest() {
        Board board = new Board();
        NegamaxAI aiWhite = new NegamaxAI("W", PieceColor.WHITE, new IControls() {
            @Override
            public void doMove(Move move) {
                // Do nothing as this only is a test
            }
        });

        NegamaxAI aiBlack = new NegamaxAI("B", PieceColor.BLACK, new IControls() {
            @Override
            public void doMove(Move move) {
                // Do nothing as this only is a test
            }
        });

        int timeThreshold = 10000;
        int nTests = 5;
        final long startTime = System.currentTimeMillis();
        Move move;
        for (int i = 0; i < nTests; i++) {
            if (board.getStatus() != 0) {
                board.resetBoard();
            }

            if (i % 2 == 0) {
                // White's turn
                move = aiWhite.selectMove(board.makeCopy());

            } else {
                move = aiBlack.selectMove(board.makeCopy());
            }
            board.movePiece(move, true);
        }

        final long usedTime = System.currentTimeMillis() - startTime;
        System.out.println("Used: " + usedTime + " to play " + nTests + " moves at depth " + aiWhite.getSelectedMaxDept());
        Assert.assertTrue(usedTime < timeThreshold);


    }
}
