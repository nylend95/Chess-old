package main.java.model;


import main.java.model.pieces.*;

import java.util.ArrayList;

/**
 * Created by Jesper Nylend on 10.02.2017.
 * s305070
 */
public class Board {
    private Square[][] board;
    private int[][] bitmap;
    private PieceSet whitePieceSet;
    private PieceSet blackPieceSet;
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;
    //TODO: Skal man gjøre det slik?
    public Board() {
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
        bitmap = new int[8][8];
        whitePieceSet = new PieceSet(whitePieces, PieceColor.WHITE);
        blackPieceSet = new PieceSet(blackPieces, PieceColor.BLACK);
    }

    //TODO: Lag bitmap
    public int[][] getBitmap() {
        return bitmap;
    }

    public Square[][] getBoard() {
        return board;
    }
}
