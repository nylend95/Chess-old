package test.java;

import main.java.controller.IControls;
import main.java.model.Board;
import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.players.SimpleAgent;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mikkel on 19-Mar-17.
 */
public class SimpleAgentTest {
    @Test
    public void speedTest() {
        Board board = new Board();
        SimpleAgent simpleAgentWhite = new SimpleAgent("W", PieceColor.WHITE, new IControls() {
            @Override
            public void doMove(Move move) {

            }
        });
        SimpleAgent simpleAgentBlack = new SimpleAgent("B", PieceColor.BLACK, new IControls() {
            @Override
            public void doMove(Move move) {

            }
        });

        int timeThreshold = 5000;
        int nTests = 2000;
        final long startTime = System.currentTimeMillis();
        Move move;
        for (int i = 0; i < nTests; i++) {
            if (board.getStatus() != 0) {
                board.resetBoard();
            }

            if (i % 2 == 0) {
                // White's turn
                move = simpleAgentWhite.selectMove(board);

            } else {
                move = simpleAgentBlack.selectMove(board);
            }
            board.movePiece(move, true);
        }

        final long usedTime = System.currentTimeMillis() - startTime;
        System.out.println("Used: " + usedTime + " to play " + nTests + " moves");
        Assert.assertTrue(usedTime < timeThreshold);


    }
}
