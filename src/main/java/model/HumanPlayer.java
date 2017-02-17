package main.java.model;

import main.java.controller.IControls;
import main.java.model.pieces.Piece;

import java.util.ArrayList;

/**
 * Created by Jesper Nylend on 16.02.2017.
 * s305070
 */
public class HumanPlayer extends Player {

    public HumanPlayer(String name, PieceColor color, IControls controls) {
        super(name, color, controls);
    }

}
