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
        castlingRight = true;
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

    @Override
    public ArrayList<Move> validMoves(int[][] bitmapPositions, int[][] bitmapAttackingPositions) {
        int[][] dir = {{1, 1}, {-1, -1}, {-1, 1}, {1, -1}, {-1, 0}, {0, -1}, {0, 1}, {1, 0}};
        ArrayList<Move> validMoves = checkDirections(dir, bitmapPositions, 1);

        // Ugly code to check castling to both sides
        if (!isMoved() && castlingLeft) {
            if (getColor() == PieceColor.WHITE && bitmapPositions[7][2] == 0 && bitmapPositions[7][3] == 0 &&
                    bitmapAttackingPositions[7][2] == 0 && bitmapAttackingPositions[7][3] == 0 && bitmapAttackingPositions[7][4] == 0) {
                validMoves.add(new Move(getSquare(), new Square(7,2), this));
            }else if(getColor() == PieceColor.BLACK && bitmapPositions[0][2] == 0 && bitmapPositions[0][3] == 0 &&
                    bitmapAttackingPositions[0][2] == 0 && bitmapAttackingPositions[0][3] == 0 && bitmapAttackingPositions[0][4] == 0){
                validMoves.add(new Move(getSquare(), new Square(0,2), this));
            }
        }
        if (!isMoved() && castlingRight) {
            if (getColor() == PieceColor.WHITE && bitmapPositions[7][5] == 0 && bitmapPositions[7][6] == 0 &&
                    bitmapAttackingPositions[7][5] == 0 && bitmapAttackingPositions[7][6] == 0 && bitmapAttackingPositions[7][4] == 0) {
                validMoves.add(new Move(getSquare(), new Square(7,6), this));
            }else if(getColor() == PieceColor.BLACK && bitmapPositions[0][5] == 0 && bitmapPositions[0][6] == 0 &&
                    bitmapAttackingPositions[0][5] == 0 && bitmapAttackingPositions[0][6] == 0 && bitmapAttackingPositions[0][4] == 0){
                validMoves.add(new Move(getSquare(), new Square(0,6), this));
            }
        }

        // Check for legal moves
        ArrayList<Move> legalMoves = new ArrayList<>();
        for (Move validMove : validMoves) {
            if (bitmapAttackingPositions[validMove.getEndSquare().getRow()][validMove.getEndSquare().getColumn()] == 0) {
                legalMoves.add(validMove);
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
    public ArrayList<Square> attackSquaresPenetrate(int[][] bitmapPositions) {
        int[][] dir = {{1, 1}, {-1, -1}, {-1, 1}, {1, -1}, {-1, 0}, {0, -1}, {0, 1}, {1, 0}};
        return checkAttackDirectionsPenetrate(dir, bitmapPositions, 1);
    }

    @Override
    public void captureFreeMoves() {

    }

    @Override
    public String toString() {
        return "King, " + super.toString();
    }

}
