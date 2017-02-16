package main.java.model;

import main.java.model.pieces.Piece;

/**
 * Created by mikke on 16-Feb-17.
 */
public class Move {
    private final Square startSquare;
    private final Square endSquare;
    private final Piece piece;
    private final Piece capturedPiece;

    public Move(Square startSquare, Square endSquare, Piece piece, Piece capturedPiece) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.piece = piece;
        this.capturedPiece = capturedPiece;
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
}
