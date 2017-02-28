package main.java.model;

import main.java.model.pieces.Piece;

/**
 * Created by mikke on 16-Feb-17.
 */
public class Move {
    private final Square startSquare;
    private final Square endSquare;
    private final Piece piece;
    private Piece capturedPiece;

    public Move(Square startSquare, Square endSquare, Piece piece) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.piece = piece;
    }

    public Square getStartSquare() {
        return startSquare;
    }

    public Square getEndSquare() {
        return endSquare;
    }

    public Piece getPiece() {
        return piece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

    @Override
    public String toString() {
        return "Move{" +
                "startSquare=" + startSquare +
                ", endSquare=" + endSquare +
                ", piece=" + piece +
                ", capturedPiece=" + capturedPiece +
                '}';
    }
}
