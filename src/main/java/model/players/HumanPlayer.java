package main.java.model.players;

import main.java.controller.IControls;
import main.java.model.Board;
import main.java.model.Move;
import main.java.model.PieceColor;

import java.util.ArrayList;

/**
 * Created by Jesper Nylend on 16.02.2017.
 * s305070
 */
public class HumanPlayer extends Player {

    public HumanPlayer(String name, PieceColor color, IControls controls) {
        super(name, color, false, controls);
    }

    @Override
    public Move selectMove(Board board) {
        return null;
    }
}
