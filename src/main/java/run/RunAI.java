package main.java.run;

import main.java.controller.IControls;
import main.java.model.Board;
import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.players.NegamaxAI;
import main.java.model.players.Player;
import main.java.model.players.RandomAgent;

/**
 * Created by mikkel on 20-Mar-17.
 */
public class RunAI {
    public static void main(String[] args) {
        Board board = new Board();
        Player aiWhite = new RandomAgent("W", PieceColor.WHITE, new IControls() {
            @Override
            public void doMove(Move move) {
                // Do nothing as this only is a test
            }
        });

        Player aiBlack = new RandomAgent("B", PieceColor.BLACK, new IControls() {
            @Override
            public void doMove(Move move) {
                // Do nothing as this only is a test
            }
        });

        final long startTime = System.currentTimeMillis();
        int[] stats = new int[3]; // white wins, black wins or remis
        int nGames = 100;
        int gameCounter = 0;
        int moveCounter = 0;
        int superMoveCounter = 0;
        Move move;
        while (gameCounter < nGames){
            if (board.getStatus() != 0) {
                //System.out.println("Status: " + board.getStatus());
                moveCounter += board.getNumberOfMovesDone();
                if (board.getStatus() == 1){
                    stats[0]++;
                }else if (board.getStatus() == -1){
                    stats[1]++;
                }else {
                    stats[2]++;
                }
                board = new Board();
                gameCounter++;
            }

            if (superMoveCounter % 2 == 0) {
                // White's turn
                move = aiWhite.selectMove(board.makeCopy());

            } else {
                move = aiBlack.selectMove(board.makeCopy());
            }
            board.movePiece(move, true);
            superMoveCounter++;
        }

        final long usedTime = System.currentTimeMillis() - startTime;
        System.out.println("Used: " + usedTime / 1000.0 + "(s) to play " + nGames + " games" + ", and " + moveCounter + " moves");
        System.out.println("White wins:" + stats[0] + ", Black wins:" + stats[1] + ", remis:" + stats[2]);
    }
}
