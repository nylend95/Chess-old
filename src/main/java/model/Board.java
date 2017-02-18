package main.java.model;


import main.java.model.pieces.*;

import java.util.ArrayList;

/**
 * Created by Jesper Nylend on 10.02.2017.
 * s305070
 */
public class Board {
    private Square[][] board;
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;
    private int[][] bitmapPositions;
    private int[][] bitmapAttackingPositionsWhite;
    private int[][] bitmapAttackingPositionsBlack;

    public Board() {
        resetBoard();
    }

    public Board(ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;

        init();
    }

    public void resetBoard() {
        whitePieces = new ArrayList<>(16);
        blackPieces = new ArrayList<>(16);

        //White pieces
        for (int i = 0; i < 8; i++) {
            whitePieces.add(new Pawn(PieceColor.WHITE, new Square(6, i), false));
        }
        whitePieces.add(new Rook(PieceColor.WHITE, new Square(7, 0)));
        whitePieces.add(new Rook(PieceColor.WHITE, new Square(7, 7)));
        whitePieces.add(new Bishop(PieceColor.WHITE, new Square(7, 1)));
        whitePieces.add(new Bishop(PieceColor.WHITE, new Square(7, 6)));
        whitePieces.add(new Knight(PieceColor.WHITE, new Square(7, 2)));
        whitePieces.add(new Knight(PieceColor.WHITE, new Square(7, 5)));
        whitePieces.add(new Queen(PieceColor.WHITE, new Square(7, 3)));
        whitePieces.add(new King(PieceColor.WHITE, new Square(7, 4)));

        //Black pieces
        for (int i = 0; i < 8; i++) {
            blackPieces.add(new Pawn(PieceColor.BLACK, new Square(1, i), false));
        }
        blackPieces.add(new Rook(PieceColor.BLACK, new Square(0, 0)));
        blackPieces.add(new Rook(PieceColor.BLACK, new Square(0, 7)));
        blackPieces.add(new Bishop(PieceColor.BLACK, new Square(0, 1)));
        blackPieces.add(new Bishop(PieceColor.BLACK, new Square(0, 6)));
        blackPieces.add(new Knight(PieceColor.BLACK, new Square(0, 2)));
        blackPieces.add(new Knight(PieceColor.BLACK, new Square(0, 5)));
        blackPieces.add(new Queen(PieceColor.BLACK, new Square(0, 3)));
        blackPieces.add(new King(PieceColor.BLACK, new Square(0, 4)));

        init();
    }

    private void init() {
        board = new Square[8][8];
        updateBitmapPositions();
        updateBitmapAttackingPositions();
    }

    public boolean movePiece(Move move) {
        Piece pieceToBeMoved = move.getPiece();
        ArrayList<Piece> movedList = (pieceToBeMoved.getColor() == PieceColor.WHITE) ? whitePieces : blackPieces;
        ArrayList<Piece> captureList = (pieceToBeMoved.getColor() == PieceColor.WHITE) ? blackPieces : whitePieces;


        for (Piece piece : movedList) {
            if (pieceToBeMoved.equals(piece)) {
                piece.setSquare(move.getEndSquare());
                piece.setMoved(true);
                break;
            }
        }

        for (Piece piece : captureList) {
            if (piece.getSquare().equals(move.getEndSquare())) {
                captureList.remove(piece);
                break;
            }
        }

        updateBitmapPositions();
        updateBitmapAttackingPositions();

        return true;
    }

    public ArrayList<Move> generateValidMoves(PieceColor color) {
        ArrayList<Piece> selectedPieceSet = (color == PieceColor.WHITE) ? whitePieces : blackPieces;
        ArrayList<Move> validMoves = new ArrayList<>();
        for (Piece piece : selectedPieceSet) {
            validMoves.addAll(piece.validMoves(bitmapPositions, getBitmapAttackingPositions(color)));
        }
        return validMoves;
    }

    private void updateBitmapPositions() {
        //-1 for black pieces
        //+1 for white pieces
        bitmapPositions = new int[8][8];

        for (Piece black : blackPieces) {
            bitmapPositions[black.getSquare().getRow()][black.getSquare().getColumn()] = -1;
        }

        for (Piece white : whitePieces) {
            bitmapPositions[white.getSquare().getRow()][white.getSquare().getColumn()] = 1;
        }
    }

    public int[][] getBitmapPositions() {
        return bitmapPositions;
    }

    private void updateBitmapAttackingPositions() {
        //1 for attacking position
        bitmapAttackingPositionsWhite = new int[8][8];
        bitmapAttackingPositionsBlack = new int[8][8];

        for (Piece piece : blackPieces) {
            ArrayList<Square> attackingPositions = piece.attackSquares(bitmapPositions);
            if (attackingPositions != null) {
                for (Square attackingSquare : attackingPositions) {
                    bitmapAttackingPositionsBlack[attackingSquare.getRow()][attackingSquare.getColumn()] = 1;
                }
            }
        }
        for (Piece piece : whitePieces) {
            ArrayList<Square> attackingPositions = piece.attackSquares(bitmapPositions);
            if (attackingPositions != null) {
                for (Square attackingSquare : attackingPositions) {
                    bitmapAttackingPositionsWhite[attackingSquare.getRow()][attackingSquare.getColumn()] = 1;
                }
            }
        }
    }

    public int[][] getBitmapAttackingPositions(PieceColor color) {
        if (color == PieceColor.BLACK) {
            return bitmapAttackingPositionsBlack;
        }
        return bitmapAttackingPositionsWhite;
    }

    public Square[][] getBoard() {
        return board;
    }

    public ArrayList<Piece> getWhitePieces() {
        return whitePieces;
    }

    public ArrayList<Piece> getBlackPieces() {
        return blackPieces;
    }
}
