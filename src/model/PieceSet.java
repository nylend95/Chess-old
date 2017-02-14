package model;

import java.util.ArrayList;

/**
 * Created by mikke on 14-Feb-17.
 */
public class PieceSet {
    private ArrayList<Piece> pieces;
    private final String color;

    public PieceSet(ArrayList<Piece> pieces, String color) {
        this.pieces = pieces;
        this.color = color;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
    }

    public String getColor() {
        return color;
    }
}
