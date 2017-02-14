package main.model;

import main.model.pieces.Piece;

import java.util.ArrayList;

/**
 * Created by mikke on 14-Feb-17.
 */
public class PieceSet {
    private ArrayList<Piece> pieces;
    private final PieceColor color;

    public PieceSet(ArrayList<Piece> pieces, PieceColor color) {
        this.pieces = pieces;
        this.color = color;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
    }

    public PieceColor getColor() {
        return color;
    }
}
