package main.java.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import main.java.model.*;
import main.java.model.pieces.*;
import main.java.model.players.HumanPlayer;
import main.java.model.players.Player;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Jesper Nylend on 10.02.2017.
 * s305070
 */
public class GameEngine implements Initializable, IControls {
    private static final double IMAGE_SIZE = 98;
    private static final int CELL_SIZE = 100;
    private static final double PADDING = 1.0;
    private Board board;
    private Player p1;
    private Player p2;
    private boolean whiteToMove = true;
    private Piece selectedPiece;
    private Square startSquare;
    private Square endSquare;


    @FXML
    Canvas cv;
    private GraphicsContext gc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        board = new Board();
        p1 = new HumanPlayer("White", PieceColor.WHITE, this);
        p2 = new HumanPlayer("Black", PieceColor.BLACK, this);

        gc = cv.getGraphicsContext2D();
        cv.setOnMouseClicked(event -> {
                    if (selectedPiece == null) {
                        selectPieceToMove(event.getX(), event.getY());
                    } else {
                        drawMove(event.getX(), event.getY(), selectedPiece);
                    }
                }
        );

        drawBoard();
    }

    private void reset() {
        p1 = new HumanPlayer("White", PieceColor.WHITE, this);
        p2 = new HumanPlayer("Black", PieceColor.BLACK, this);
        board.resetBoard();
        whiteToMove = true;
    }

    private void drawBoard() {
        for (int i = 0; i < 64; i++) {
            int c = (i % 8);
            int r = (i / 8) % 8;
            if (r % 2 == 0 && (i + 1) % 2 == 0) {
                gc.setFill(Color.GREY);
            } else if (r % 2 != 0 && i % 2 == 0) {
                gc.setFill(Color.GREY);
            } else {
                gc.setFill(Color.WHITE);
            }
            gc.fillRect(c * CELL_SIZE + 1, r * CELL_SIZE + 1, IMAGE_SIZE, IMAGE_SIZE);
            gc.strokeLine(r * CELL_SIZE, 0, r * CELL_SIZE, CELL_SIZE * 8);
            gc.strokeLine(0, r * CELL_SIZE, CELL_SIZE * 8, r * CELL_SIZE);
        }
        drawPieces();
    }

    public void doMove(Move move) {
        board.movePiece(move);

        // TODO check for win/remis

        // Next player's turn
        whiteToMove = !whiteToMove;
        Player playerToMove = getPlayersTurn();
        if (!(playerToMove instanceof HumanPlayer)) {
            // AI's turn
            ArrayList<Move> legalMoves = board.generateValidMoves(playerToMove.getColor());
            playerToMove.selectMove(legalMoves);
        }
    }

    private Player getPlayersTurn() {
        if (whiteToMove) {
            return p1;
        }
        return p2;
    }

    private void drawPieces() {
        for (Piece piece : board.getWhitePieces()) {
            drawPiece(piece);
        }
        for (Piece piece : board.getBlackPieces()) {
            drawPiece(piece);
        }
    }

    private void drawPiece(Piece piece) {
        if (piece instanceof Pawn) {
            gc.drawImage(new Image("pieces/" + piece.getColorString() + "_pawn.png"), (piece.getSquare().getColumn() * CELL_SIZE) + PADDING, (piece.getSquare().getRow() * CELL_SIZE) + PADDING, IMAGE_SIZE, IMAGE_SIZE);
        } else if (piece instanceof Bishop) {
            gc.drawImage(new Image("pieces/" + piece.getColorString() + "_bishop.png"), (piece.getSquare().getColumn() * CELL_SIZE) + PADDING, (piece.getSquare().getRow() * CELL_SIZE) + PADDING, IMAGE_SIZE, IMAGE_SIZE);
        } else if (piece instanceof Knight) {
            gc.drawImage(new Image("pieces/" + piece.getColorString() + "_knight.png"), (piece.getSquare().getColumn() * CELL_SIZE) + PADDING, (piece.getSquare().getRow() * CELL_SIZE) + PADDING, IMAGE_SIZE, IMAGE_SIZE);
        } else if (piece instanceof Rook) {
            gc.drawImage(new Image("pieces/" + piece.getColorString() + "_rook.png"), (piece.getSquare().getColumn() * CELL_SIZE) + PADDING, (piece.getSquare().getRow() * CELL_SIZE) + PADDING, IMAGE_SIZE, IMAGE_SIZE);
        } else if (piece instanceof Queen) {
            gc.drawImage(new Image("pieces/" + piece.getColorString() + "_queen.png"), (piece.getSquare().getColumn() * CELL_SIZE) + PADDING, (piece.getSquare().getRow() * CELL_SIZE) + PADDING, IMAGE_SIZE, IMAGE_SIZE);
        } else {
            gc.drawImage(new Image("pieces/" + piece.getColorString() + "_king.png"), (piece.getSquare().getColumn() * CELL_SIZE) + PADDING, (piece.getSquare().getRow() * CELL_SIZE) + PADDING, IMAGE_SIZE, IMAGE_SIZE);
        }
    }


    private void drawMove(double x, double y, Piece piece) {
        endSquare = selectSquare(x, y);
        boolean legalClick = false;

        ArrayList<Move> possibleMoves = board.generateValidMoves(piece);
        for (Move move : possibleMoves) {
            if (endSquare.equals(move.getEndSquare())) {
                legalClick = true;
            }
        }

        if (legalClick) {
            Move move = new Move(startSquare, endSquare, piece);
            doMove(move);
            startSquare = endSquare = null;
        }

        drawBoard();
        selectedPiece = null;
    }

    private void drawPossibleMoves(Piece piece) {
        if (piece == null) return;

        int[][] bitmapPositions = board.getBitmapPositions();
        ArrayList<Move> possibleMoves = board.generateValidMoves(piece);

        for (Move validMove : possibleMoves) {
            int vx = validMove.getEndSquare().getColumn();
            int vy = validMove.getEndSquare().getRow();
            if (bitmapPositions[vy][vx] != 0) {
                gc.drawImage(new Image("attack_sprite.png"), (vx * CELL_SIZE) + PADDING, (vy * CELL_SIZE) + PADDING, IMAGE_SIZE, IMAGE_SIZE);
            } else {
                gc.drawImage(new Image("move_sprite.png"), (vx * CELL_SIZE) + PADDING, (vy * CELL_SIZE) + PADDING, IMAGE_SIZE, IMAGE_SIZE);
            }
        }
    }

    // Util methods
    private Piece getPieceOnSquare(Square square, PieceColor color) {
        Piece pieceOnSquare = null;
        if (color.equals(PieceColor.WHITE)) {
            for (Piece piece : board.getWhitePieces()) {
                if (square.getRow() == piece.getSquare().getRow() && square.getColumn() == piece.getSquare().getColumn()) {
                    pieceOnSquare = piece;
                }
            }
        } else {
            for (Piece piece : board.getBlackPieces()) {
                if (square.getRow() == piece.getSquare().getRow() && square.getColumn() == piece.getSquare().getColumn()) {
                    pieceOnSquare = piece;
                }
            }
        }
        return pieceOnSquare;
    }

    private void selectPieceToMove(double x, double y) {
        PieceColor color = whiteToMove ? PieceColor.WHITE : PieceColor.BLACK;
        startSquare = selectSquare(x, y);
        selectedPiece = getPieceOnSquare(startSquare, color);
        drawPossibleMoves(selectedPiece);
    }

    private Square selectSquare(double x, double y) {
        int row = (int) y / CELL_SIZE;
        int column = (int) x / CELL_SIZE;
        return new Square(row, column);

    }
}
