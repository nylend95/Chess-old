package main.model;

/**
 * Created by Jesper Nylend on 10.02.2017.
 * s305070
 */
public class Player {
    private final PieceColor color;
    private final PlayerEngine engine;

    public Player(PieceColor color, PlayerEngine engine) {
        this.color = color;
        this.engine = engine;
    }

    public PieceColor getColor() {
        return color;
    }

    public PlayerEngine getEngine() {
        return engine;
    }
}
