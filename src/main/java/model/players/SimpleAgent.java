package main.java.model.players;

import main.java.controller.IControls;
import main.java.model.Board;
import main.java.model.Move;
import main.java.model.PieceColor;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mikkel on 19-Mar-17.
 */
public class SimpleAgent extends Player {
    public SimpleAgent(String name, PieceColor color, IControls controls) {
        super(name, color, true, controls);
    }

    /**
     * Returns a first possible move
     *
     * @param board is a copy of the current board
     * @return the selected move
     */
    @Override
    public Move selectMove(Board board) {
        ArrayList<Move> validMoves = board.generateValidMoves(getColor());
        if (validMoves.size() > 0) {
            Move move = validMoves.get(0);
            makeMove(move);
            return move;
        } else {
            System.out.println("ERRORS! HAVE NO MOVES TO MAKE!");
        }
        return null;
    }
}
