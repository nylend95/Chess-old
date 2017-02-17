package main.java.model.players;

import main.java.model.Move;

import java.util.ArrayList;

/**
 * Created by mikke on 17-Feb-17.
 */
public interface IPlayer {
    void selectMove(ArrayList<Move> validMoves);
}
