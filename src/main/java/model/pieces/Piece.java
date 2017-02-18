package main.java.model.pieces;


import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.Square;

import java.util.ArrayList;

/**
 * Created by mikke on 14-Feb-17.
 */
public abstract class Piece {

    private final PieceColor color;
    private Square square;
    private boolean moved;

    public Piece(PieceColor color, Square square) {
        this.color = color;
        this.square = square;
        this.moved = false;
    }

    public PieceColor getColor() {
        return color;
    }

    public String getColorString() {
        return (color == PieceColor.WHITE) ? "white" : "black";
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    ArrayList<Move> checkDirections(int[][] dir, int[][] bitmapPositions, int maxSteps) {
        ArrayList<Move> validMoves = new ArrayList<>();

        int selfValue = (color == PieceColor.WHITE) ? 1 : -1;
        int xStart = square.getColumn();
        int yStart = square.getRow();

        for (int[] d : dir) {
            for (int i = 1; i <= maxSteps; i++) {
                int x_new = xStart + i * d[0];
                int y_new = yStart + i * d[1];

                // Out of bounds
                if (x_new < 0 || x_new > 7 || y_new < 0 || y_new > 7) {
                    break;
                }

                // Capture or crash in own piece
                if (bitmapPositions[y_new][x_new] != 0) {
                    if (bitmapPositions[y_new][x_new] != selfValue) {
                        validMoves.add(new Move(square, new Square(y_new, x_new), this));
                    }
                    break;
                }

                // Valid move
                validMoves.add(new Move(square, new Square(y_new, x_new), this));
            }
        }
        return validMoves;
    }

    public abstract ArrayList<Move> validMoves(int[][] bitmapPositions, int[][] bitmapAttackingPositions);

    public abstract ArrayList<Square> attackSquares(int[][] bitmapPositions);

    public abstract void captureFreeMoves();

    public abstract boolean toBeCaptured();

    @Override
    public String toString() {
        return getColor() + ", " + getSquare().toString();
    }
}
