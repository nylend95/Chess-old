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

    public Board() {
        resetBoard();
    }

    public Board(ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;

        board = new Square[8][8];
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

        board = new Square[8][8];
    }

    public boolean movePiece(Move move) {
        Piece pieceToBeMoved = move.getPiece();
        ArrayList<Piece> selectedList = (pieceToBeMoved.getColor() == PieceColor.WHITE) ? whitePieces : blackPieces;
        for (Piece piece : selectedList) {
            if (pieceToBeMoved.equals(piece)) {
                piece.setSquare(move.getEndSquare());
                piece.setMoved(true);
                return true;
            }
        }
        return false;
    }

    public int[][] generateBitmapPositions() {
        //-1 for black pieces
        //+1 for white pieces
        int[][] bitmap = new int[8][8];

        for (Piece black : blackPieces) {
            bitmap[black.getSquare().getRow()][black.getSquare().getColumn()] = -1;
        }

        for (Piece white : whitePieces) {
            bitmap[white.getSquare().getRow()][white.getSquare().getColumn()] = 1;
        }

        return bitmap;
    }

    public int[][] generateBitmapAttackingPositions(PieceColor pieceColor) {
        //1 for attacking position
        int[][] bitmapPositions = generateBitmapPositions();

        int[][] bitmap = new int[8][8];

        if (pieceColor == PieceColor.BLACK) {
            for (Piece piece : blackPieces) {
                ArrayList<Square> attackingPositions = piece.attackSquares(bitmapPositions);
                if (attackingPositions != null) {
                    for (Square attackingSquare : attackingPositions) {
                        bitmap[attackingSquare.getRow()][attackingSquare.getColumn()] = 1;
                    }
                }
            }
        } else {
            for (Piece piece : whitePieces) {
                ArrayList<Square> attackingPositions = piece.attackSquares(bitmapPositions);
                if (attackingPositions != null) {
                    for (Square attackingSquare : attackingPositions) {
                        bitmap[attackingSquare.getRow()][attackingSquare.getColumn()] = 1;
                    }
                }
            }
        }

        return bitmap;
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
