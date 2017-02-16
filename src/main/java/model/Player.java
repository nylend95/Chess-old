package main.java.model;

import main.java.controller.IControls;

/**
 * Created by Jesper Nylend on 10.02.2017.
 * s305070
 */
public abstract class Player {
    private String name;
    private final PieceColor color;
    private final IControls controls;

    public Player(String name, PieceColor color, IControls controls) {
        this.name = name;
        this.color = color;
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

    private void makeMove(Move move) {
        controls.doMove(move);
    }
}
