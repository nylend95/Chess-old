package model;

import model.pieces.Piece;

import java.util.ArrayList;

/**
 * Created by Jesper Nylend on 10.02.2017.
 * s305070
 */
public class Board {
    private Square[][] board;
    private PieceSet whitePieceSet;
    private PieceSet blackPieceSet;
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;

    public Board() {
        whitePieces = new ArrayList<Piece>(16);
        blackPieces = new ArrayList<Piece>(16);
        board = new Square[8][8];
        whitePieceSet = new PieceSet(whitePieces, PieceColor.WHITE);
        blackPieceSet = new PieceSet(blackPieces, PieceColor.BLACK);
    }

    public Square[][] getBoard() {
        return board;
    }
}
