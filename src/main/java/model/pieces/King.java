package main.java.model.pieces;

import main.java.model.*;

import java.util.ArrayList;

/**
 * Created by mikke on 14-Feb-17.
 */
public class King extends Piece {
    private boolean castlingLeft;
    private boolean castlingRight;

    public King(PieceColor color, Square square) {
        super(color, square);
        castlingLeft = true;
    }

    public boolean isCastlingLeft() {
        return castlingLeft;
    }

    public void setCastlingLeft(boolean castlingLeft) {
        this.castlingLeft = castlingLeft;
    }

    public boolean isCastlingRight() {
        return castlingRight;
    }

    public void setCastlingRight(boolean castlingRight) {
        this.castlingRight = castlingRight;
    }

    ArrayList<Move> checkDirections(int[][] dir, int[][] bitmapPositions, int[][] attackingPositions) {
        ArrayList<Move> validMoves = new ArrayList<>();

        int selfValue = (getColor() == PieceColor.WHITE) ? 1 : -1;
        int xStart = getSquare().getColumn();
        int yStart = getSquare().getRow();

        for (int[] d : dir) {
            for (int i = 1; i <= 1; i++) {
                int x_new = xStart + i * d[0];
                int y_new = yStart + i * d[1];

                // Out of bounds
                if (x_new < 0 || x_new > 7 || y_new < 0 || y_new > 7) {
                    break;
                }

                if (attackingPositions[y_new][x_new] != 0){
                    break;
                }

                // Capture or crash in own piece
                if (bitmapPositions[y_new][x_new] != 0) {
                    if (bitmapPositions[y_new][x_new] != selfValue) {
                        validMoves.add(new Move(getSquare(), new Square(y_new, x_new), this));
                    }
                    break;
                }

                // Valid move
                validMoves.add(new Move(getSquare(), new Square(y_new, x_new), this));
            }
        }
        return validMoves;
    }

    @Override
    public ArrayList<Move> validMoves(int[][] bitmapPositions, int[][] bitmapAttackingPositions) {
        int[][] dir = {{1, 1}, {-1, -1}, {-1, 1}, {1, -1}, {-1, 0}, {0, -1}, {0, 1}, {1, 0}};
        ArrayList<Move> validMoves = checkDirections(dir, bitmapPositions, 1);
        if (!isMoved() && castlingLeft){
            if (getColor() == PieceColor.WHITE && bitmapPositions[7][2] == 0 && bitmapPositions[7][3] == 0){
                // TODO
            }
        }

        // Check for legal moves
        ArrayList<Move> legalMoves = new ArrayList<>();
        for (Move validMove : validMoves) {
            if (bitmapAttackingPositions[validMove.getEndSquare().getRow()][validMove.getEndSquare().getColumn()] == 0){
                legalMoves.add(validMove);
            }else{
                System.out.println("removing illegal move!");
            }
        }


        return legalMoves;
    }

    @Override
    public ArrayList<Square> attackSquares(int[][] bitmapPositions) {
        int[][] dir = {{1, 1}, {-1, -1}, {-1, 1}, {1, -1}, {-1, 0}, {0, -1}, {0, 1}, {1, 0}};
        return checkAttackDirections(dir, bitmapPositions, 1);
    }

    @Override
    public void captureFreeMoves() {

    }

    @Override
    public String toString() {
        return "King, " + super.toString();
    }

}
