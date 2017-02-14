package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesper Nylend on 10.02.2017.
 * s305070
 */
public class Board
{
    private Square[][] board;
    private PieceSet whitePieceSet;
    private PieceSet blackPieceSet;
    ArrayList<Piece> whitePieces;
    ArrayList<Piece> blackPieces;


    public Board()
    {
        whitePieces = new ArrayList<Piece>(16);
        blackPieces = new ArrayList<Piece>(16);
        board = new Square[8][8];
        whitePieceSet = new PieceSet(whitePieces, "white");
        blackPieceSet = new PieceSet(blackPieces, "black");
    }

    public Square[][] getBoard()
    {
        return board;
    }
}
