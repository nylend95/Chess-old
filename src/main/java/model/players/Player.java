package main.java.model.players;

import main.java.controller.IControls;
import main.java.model.Move;
import main.java.model.PieceColor;

/**
 * Created by Jesper Nylend on 10.02.2017.
 * s305070
 */
public abstract class Player implements IAi {
    private String name;
    private final boolean ai;
    private final PieceColor color;
    private final IControls controls;

    public Player(String name, PieceColor color, boolean ai, IControls controls) {
        this.name = name;
        this.color = color;
        this.ai = ai;
        this.controls = controls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PieceColor getColor() {
        return color;
    }

    public boolean isAi() {
        return ai;
    }

    protected void makeMove(Move move) {
        controls.doMove(move);
    }
}
