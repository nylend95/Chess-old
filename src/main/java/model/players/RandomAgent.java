package main.java.model.players;

import main.java.controller.IControls;
import main.java.model.Board;
import main.java.model.Move;
import main.java.model.Square;
import main.java.model.PieceColor;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mikke on 17-Feb-17.
 */
public class RandomAgent extends Player{

    public RandomAgent(String name, PieceColor color, IControls controls) {
        super(name, color, true, controls);
    }

    /**
     * Returns a random move
     *
     * @param board is a copy of the current board
     * @return the selected move
     */
    @Override
    public void selectMove(Board board) {
        ArrayList<Move> validMoves = board.generateValidMoves(getColor());
        if (validMoves.size() > 0) {
            int index = new Random().nextInt(validMoves.size());
            Move move = validMoves.get(index);
            makeMove(move);
        } else {
            System.out.println("ERRORS! HAVE NO MOVES TO MAKE!");
        }
    }
}
