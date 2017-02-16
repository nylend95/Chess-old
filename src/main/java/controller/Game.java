package main.java.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import main.java.model.Board;
import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.Player;
import main.java.model.pieces.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Jesper Nylend on 10.02.2017.
 * s305070
 */
public class Game implements Initializable, IControls {
    private static final double CELL_SIZE = 98;
    private Board board; //Column | Row
    private Player p1;
    private Player p2;


    @FXML
    Canvas cv;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        board = new Board();

        p1 = new Player("White", PieceColor.WHITE, this);
        p2 = new Player("Black", PieceColor.WHITE, this);

        GraphicsContext gc = cv.getGraphicsContext2D();
        initDraw(gc);
        draw(gc);
    }

    private void initDraw(GraphicsContext gc) {
        for (int c = 0; c < 9; c++) {
            for (int r = 0; r < 9; r++) {
                gc.strokeLine(r * 100, 0, r * 100, 800);
            }
        }
        for (int c = 0; c < 9; c++) {
            for (int r = 0; r < 9; r++) {
                gc.strokeLine(0, r * 100, 800, r * 100);
            }
        }
        for (int c = 0; c < 900; c += 100) {
            if ((c - 100) % 200 != 0) {
                for (int r = 0; r < 900; r += 100) {
                    if (r % 200 != 0) {
                        gc.setFill(Color.GREY);
                        gc.fillRect(c + 1, r + 1, CELL_SIZE, CELL_SIZE);
                    }
                }
            } else {
                for (int r = 0; r < 900; r += 100) {
                    if (r % 200 == 0) {
                        gc.setFill(Color.GREY);
                        gc.fillRect(c + 1, r + 1, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
    }

    public boolean doMove(Move move) {
        // TODO validate move! Return false if this move is not valid!
        board.movePiece(move);
        return true;
    }

    private void draw(GraphicsContext gc) {
        ArrayList<Piece> whitePieces = board.getWhitePieces();
        ArrayList<Piece> blackPieces = board.getBlackPieces();

        for (int i = 0; i < whitePieces.size(); i++) {
            Piece piece = whitePieces.get(i);
            if (piece instanceof Pawn) {
                gc.drawImage(new Image("pieces/white_pawn.png"), (piece.getSquare().getRow() * 100) + 1, (piece.getSquare().getColumn() * 100) + 1, CELL_SIZE, CELL_SIZE);
            } else if (piece instanceof Bishop) {
                gc.drawImage(new Image("pieces/white_bishop.png"), (piece.getSquare().getRow() * 100) + 1, (piece.getSquare().getColumn() * 100) + 1, CELL_SIZE, CELL_SIZE);
            } else if (piece instanceof Knight) {
                gc.drawImage(new Image("pieces/white_knight.png"), (piece.getSquare().getRow() * 100) + 1, (piece.getSquare().getColumn() * 100) + 1, CELL_SIZE, CELL_SIZE);
            } else if (piece instanceof Rook) {
                gc.drawImage(new Image("pieces/white_rook.png"), (piece.getSquare().getRow() * 100) + 1, (piece.getSquare().getColumn() * 100) + 1, CELL_SIZE, CELL_SIZE);
            } else if (piece instanceof Queen) {
                gc.drawImage(new Image("pieces/white_queen.png"), (piece.getSquare().getRow() * 100) + 1, (piece.getSquare().getColumn() * 100) + 1, CELL_SIZE, CELL_SIZE);
            } else {
                gc.drawImage(new Image("pieces/white_king.png"), (piece.getSquare().getRow() * 100) + 1, (piece.getSquare().getColumn() * 100) + 1, CELL_SIZE, CELL_SIZE);
            }
        }
        for (int i = 0; i < blackPieces.size(); i++) {
            Piece piece = blackPieces.get(i);
            if (piece instanceof Pawn) {
                gc.drawImage(new Image("pieces/black_pawn.png"), (piece.getSquare().getRow() * 100) + 1, (piece.getSquare().getColumn() * 100) + 1, CELL_SIZE, CELL_SIZE);
            } else if (piece instanceof Bishop) {
                gc.drawImage(new Image("pieces/black_bishop.png"), (piece.getSquare().getRow() * 100) + 1, (piece.getSquare().getColumn() * 100) + 1, CELL_SIZE, CELL_SIZE);
            } else if (piece instanceof Knight) {
                gc.drawImage(new Image("pieces/black_knight.png"), (piece.getSquare().getRow() * 100) + 1, (piece.getSquare().getColumn() * 100) + 1, CELL_SIZE, CELL_SIZE);
            } else if (piece instanceof Rook) {
                gc.drawImage(new Image("pieces/black_rook.png"), (piece.getSquare().getRow() * 100) + 1, (piece.getSquare().getColumn() * 100) + 1, CELL_SIZE, CELL_SIZE);
            } else if (piece instanceof Queen) {
                gc.drawImage(new Image("pieces/black_queen.png"), (piece.getSquare().getRow() * 100) + 1, (piece.getSquare().getColumn() * 100) + 1, CELL_SIZE, CELL_SIZE);
            } else {
                gc.drawImage(new Image("pieces/black_king.png"), (piece.getSquare().getRow() * 100) + 1, (piece.getSquare().getColumn() * 100) + 1, CELL_SIZE, CELL_SIZE);
            }
        }
    }
}
