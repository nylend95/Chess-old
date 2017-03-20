package main.java.model.pieces;


import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.Square;

import java.util.ArrayList;

/**
 * Created by mikke on 14-Feb-17.
 */
public class Pawn extends Piece {
    private boolean promoted;
    private Piece promotedTo;
    private final boolean moveUp;

    public Pawn(PieceColor color, Square square) {
        super(color, square);
        this.moveUp = (color == PieceColor.WHITE);
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public Piece getPromotedTo() {
        return promotedTo;
    }

    public void setPromotedTo(Piece promotedTo) {
        this.promotedTo = promotedTo;
    }

    public boolean getMoveDirection() {
        return moveUp;
    }

    @Override
    public void setSquare(Square square) {
        if (promotedTo != null) {
            promotedTo.setSquare(square);
        }
        super.setSquare(square);
    }

    @Override
    public ArrayList<Move> validMoves(int[][] bitmapPositions, int[][] bitmapAttackingPositions) {
        if (promotedTo == null) {
            return normalMoves(bitmapPositions);
        } else {
            return promotedTo.validMoves(bitmapPositions, bitmapAttackingPositions);
        }
    }

    private ArrayList<Move> normalMoves(int[][] bitmapPositions) { //true = white
        ArrayList<Move> legalMoves = new ArrayList<>();
        int[][] whiteDir = {{0, -1}};
        int[][] blackDir = {{0, 1}};
        int[][] whiteAtk = {{1, -1}, {-1, -1}};
        int[][] blackAtk = {{1, 1}, {-1, 1}};

        if (moveUp) {
            if (isMoved()) {
                legalMoves.addAll(attackMoves(whiteAtk, bitmapPositions));
                legalMoves.addAll(checkDirections(whiteDir, bitmapPositions, 1));
            } else {
                legalMoves.addAll(attackMoves(whiteAtk, bitmapPositions));
                legalMoves.addAll(checkDirections(whiteDir, bitmapPositions, 2));
            }
        } else {
            if (isMoved()) {
                legalMoves.addAll(attackMoves(blackAtk, bitmapPositions));
                legalMoves.addAll(checkDirections(blackDir, bitmapPositions, 1));
            } else {
                legalMoves.addAll(attackMoves(blackAtk, bitmapPositions));
                legalMoves.addAll(checkDirections(blackDir, bitmapPositions, 2));
            }
        }
        return legalMoves;
    }

    @Override
    ArrayList<Move> checkDirections(int[][] dir, int[][] bitmapPositions, int maxSteps) {
        ArrayList<Move> validMoves = new ArrayList<>();

        int xStart = getSquare().getColumn();
        int yStart = getSquare().getRow();

        for (int[] d : dir) {
            for (int i = 1; i <= maxSteps; i++) {
                int x_new = xStart + i * d[0];
                int y_new = yStart + i * d[1];

                // Out of bounds or something in the cell
                if (x_new < 0 || x_new > 7 || y_new < 0 || y_new > 7 || bitmapPositions[y_new][x_new] != 0) {
                    break;
                }

                // Valid move
                validMoves.add(new Move(getSquare(), new Square(y_new, x_new), this));
            }
        }
        return validMoves;
    }


    private ArrayList<Move> attackMoves(int[][] dir, int[][] bitmapPositions) {
        ArrayList<Move> attackMoves = new ArrayList<>();

        int selfValue = (moveUp) ? 1 : -1;
        int xStart = getSquare().getColumn();
        int yStart = getSquare().getRow();

        for (int[] d : dir) {
            int x_new = xStart + d[0];
            int y_new = yStart + d[1];

            // Out of bounds
            if (x_new < 0 || x_new > 7 || y_new < 0 || y_new > 7) {
                continue;
            }

            // Capture or crash in own piece
            if (bitmapPositions[y_new][x_new] != 0 && bitmapPositions[y_new][x_new] != selfValue) {
                attackMoves.add(new Move(getSquare(), new Square(y_new, x_new), this));
            }
        }
        return attackMoves;
    }

    @Override
    public ArrayList<Square> attackSquares(int[][] bitmapPositions) {
        if (promotedTo != null) {
            return promotedTo.attackSquares(bitmapPositions);
        }

        ArrayList<Square> attackSquares = new ArrayList<>();
        int[][] atkDir;
        if (moveUp) {
            atkDir = new int[][]{{1, -1}, {-1, -1}};
        } else {
            atkDir = new int[][]{{1, 1}, {-1, 1}};
        }

        int selfValue = (moveUp) ? 1 : -1;
        int xStart = getSquare().getColumn();
        int yStart = getSquare().getRow();

        for (int[] d : atkDir) {
            int x_new = xStart + d[0];
            int y_new = yStart + d[1];

            // Out of bounds
            if (x_new < 0 || x_new > 7 || y_new < 0 || y_new > 7) {
                break;
            }

            // Capture or crash in own piece
            if (bitmapPositions[y_new][x_new] != selfValue) {
                attackSquares.add(new Square(y_new, x_new));
            }
        }
        return attackSquares;
    }

    @Override
    public ArrayList<Square> attackSquaresPenetrate(int[][] bitmapPositions) {
        return attackSquares(bitmapPositions);
    }

    @Override
    public void captureFreeMoves() {

    }

    @Override
    public String toString() {
        return "Pawn, " + super.toString();
    }
}
