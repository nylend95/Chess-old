package model;

/**
 * Created by Jesper Nylend on 10.02.2017.
 * s305070
 */
public class Player {
    private final String color;
    private final PlayerEngine engine;

    public Player(String color, PlayerEngine engine) {
        this.color = color;
        this.engine = engine;
    }

    public String getColor() {
        return color;
    }

    public PlayerEngine getEngine() {
        return engine;
    }
}
