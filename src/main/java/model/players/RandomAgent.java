package main.java.model.players;

import main.java.controller.IControls;
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
        super(name, color, controls);
    }

    /**
     * Returns a random move
     * @param validMoves all possible moves to make
     * @return the selected move
     */
    @Override
    public void selectMove(ArrayList<Move> validMoves) {
        if (validMoves.size() > 0) {
            int index = new Random().nextInt(validMoves.size());
            Move move = validMoves.get(index);
            makeMove(move);
        }else{
            System.out.println("ERRORS! HAVE NO MOVES TO MAKE!");
        }
    }
}
