package main.java.model;


import main.java.model.pieces.Piece;

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

    public Board() {
        whitePieces = new ArrayList<>(16);
        blackPieces = new ArrayList<>(16);
        board = new Square[8][8];
        bitmap = new int[8][8];
        whitePieceSet = new PieceSet(whitePieces, PieceColor.WHITE);
        blackPieceSet = new PieceSet(blackPieces, PieceColor.BLACK);
    }

    public int[][] getBitmap() {
        return bitmap;
    }

    public Square[][] getBoard() {
        return board;
    }
}
