package main.java.model.players;

import main.java.model.Board;
import main.java.model.Move;

/**
 * Created by mikkel on 25-Feb-17.
 */
public interface IAi {
    Move selectMove(Board board);
}
