package main.java.controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import main.java.model.*;
import main.java.model.pieces.*;
import main.java.model.players.HumanPlayer;
import main.java.model.players.NegamaxAI;
import main.java.model.players.Player;
import main.java.model.players.RandomAgent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Math.round;
import static java.lang.Thread.sleep;
import static main.java.model.PieceColor.BLACK;
import static main.java.model.PieceColor.WHITE;

/**
 * Created by Jesper Nylend on 10.02.2017.
 * s305070
 */
public class GameEngine implements Initializable, IControls {
    public static final double CANVAS_HEIGHT = 600;
    public static final double TOTAL_WIDTH = 1105;
    private static final double CELL_SIZE = CANVAS_HEIGHT * 0.125;
    private static final double PADDING = CELL_SIZE * 0.01;
    private static final double IMAGE_SIZE = CELL_SIZE - 2 * PADDING;
    private static long ANIMATION_TIME_THRESHOLD = 30; // Minimum anitmation time per move

    private Board board;
    private Player p1;
    private Player p2;
    private boolean whiteToMove = true;
    private Piece selectedPiece;
    private Square startSquare;
    private Square endSquare;

    public Button btnNewGame;
    public ChoiceBox dropPlayer2;
    public ChoiceBox dropPlayer1;
    @FXML
    Canvas cv;
    private GraphicsContext gc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dropPlayer1.getItems().addAll("Human", new Separator(), "RandomAI", "NegamaxAI");
        dropPlayer2.getItems().addAll("Human", new Separator(), "RandomAI", "NegamaxAI");
        dropPlayer1.setValue("Human");
        dropPlayer2.setValue("Human");

        board = new Board();
        p1 = new HumanPlayer("White", WHITE, this);
        p2 = new HumanPlayer("Black", BLACK, this);

        gc = cv.getGraphicsContext2D();
        cv.setOnMouseClicked(event -> {
                    if (!getPlayersTurn().isAi()) {
                        if (selectedPiece == null) {
                            selectPieceToMove(event.getX(), event.getY());
                        } else {
                            drawMove(event.getX(), event.getY(), selectedPiece);
                        }
                    }
                }
        );
        reset(p1, p2);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (board.getStatus() != 0) {
                    return;
                }

                Player playerToMove = getPlayersTurn();
                if (playerToMove.isAi()) {
                    final long startTime = System.currentTimeMillis();
                    // AI's turn
                    playerToMove.selectMove(board.makeCopy());
                    final long usedTime = System.currentTimeMillis() - startTime;
                    if (usedTime < ANIMATION_TIME_THRESHOLD) {
                        try {
                            sleep(ANIMATION_TIME_THRESHOLD - usedTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    drawBoard();
                }
            }
        }.start();
    }

    private void reset(Player player1, Player player2) {
        p1 = player1;
        p2 = player2;
        board.resetBoard();
        whiteToMove = true;
        drawBoard();
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
            gc.fillRect(c * CELL_SIZE + PADDING, r * CELL_SIZE + PADDING, IMAGE_SIZE, IMAGE_SIZE);
            gc.strokeLine(r * CELL_SIZE, 0, r * CELL_SIZE, CELL_SIZE * 8);
            gc.strokeLine(0, r * CELL_SIZE, CELL_SIZE * 8, r * CELL_SIZE);
        }

        ArrayList<Move> moveHistory = board.getMoveHistory();
        if (moveHistory.size() > 0) {
            Move lastMove = moveHistory.get(moveHistory.size() - 1);
            gc.setFill(Color.BISQUE);
            gc.fillRect(lastMove.getStartSquare().getColumn() * CELL_SIZE + PADDING, lastMove.getStartSquare().getRow() * CELL_SIZE + PADDING, IMAGE_SIZE, IMAGE_SIZE);
            gc.fillRect(lastMove.getEndSquare().getColumn() * CELL_SIZE + PADDING, lastMove.getEndSquare().getRow() * CELL_SIZE + PADDING, IMAGE_SIZE, IMAGE_SIZE);
        }

        drawPieces();
    }

    public void doMove(Move move) {
        board.movePiece(move, true);
        drawBoard();

        if (board.isMovePromotion(move)) {
            // Promotion choice
            System.out.println("Piece promoted!!");
        }

        int status = board.getStatus();
        if (status == 1) {
            System.out.println("White wins after " + board.getNumberOfMovesDone() + " moves");
            return;
        } else if (status == -1) {
            System.out.println("Black wins after " + board.getNumberOfMovesDone() + " moves");
            return;
        } else if (status == 2) {
            System.out.println("Remis after " + board.getNumberOfMovesDone() + " moves.");
            return;
        }

        // Next player's turn
        whiteToMove = !whiteToMove;
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
            if (((Pawn) piece).getPromotedTo() == null) {
                gc.drawImage(new Image("pieces/" + piece.getColorString() + "_pawn.png"), (piece.getSquare().getColumn() * CELL_SIZE) + PADDING, (piece.getSquare().getRow() * CELL_SIZE) + PADDING, IMAGE_SIZE, IMAGE_SIZE);
            } else if (((Pawn) piece).getPromotedTo() instanceof Bishop) {
                gc.drawImage(new Image("pieces/" + piece.getColorString() + "_bishop.png"), (piece.getSquare().getColumn() * CELL_SIZE) + PADDING, (piece.getSquare().getRow() * CELL_SIZE) + PADDING, IMAGE_SIZE, IMAGE_SIZE);
            } else if (((Pawn) piece).getPromotedTo() instanceof Knight) {
                gc.drawImage(new Image("pieces/" + piece.getColorString() + "_knight.png"), (piece.getSquare().getColumn() * CELL_SIZE) + PADDING, (piece.getSquare().getRow() * CELL_SIZE) + PADDING, IMAGE_SIZE, IMAGE_SIZE);
            } else if (((Pawn) piece).getPromotedTo() instanceof Rook) {
                gc.drawImage(new Image("pieces/" + piece.getColorString() + "_rook.png"), (piece.getSquare().getColumn() * CELL_SIZE) + PADDING, (piece.getSquare().getRow() * CELL_SIZE) + PADDING, IMAGE_SIZE, IMAGE_SIZE);
            } else if (((Pawn) piece).getPromotedTo() instanceof Queen) {
                gc.drawImage(new Image("pieces/" + piece.getColorString() + "_queen.png"), (piece.getSquare().getColumn() * CELL_SIZE) + PADDING, (piece.getSquare().getRow() * CELL_SIZE) + PADDING, IMAGE_SIZE, IMAGE_SIZE);
            }
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
        selectedPiece = null;
        drawBoard();
    }

    private void drawPossibleMoves(Piece piece) {
        if (piece == null) return;

        int[][] bitmapPositions = board.getBitmapPositions();
        ArrayList<Move> possibleMoves = board.generateValidMoves(piece);

        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(piece.getSquare().getColumn() * CELL_SIZE + PADDING, piece.getSquare().getRow() * CELL_SIZE + PADDING, IMAGE_SIZE, IMAGE_SIZE);
        drawPiece(piece);

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
        if (color.equals(WHITE)) {
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
        PieceColor color = whiteToMove ? WHITE : BLACK;
        startSquare = selectSquare(x, y);
        selectedPiece = getPieceOnSquare(startSquare, color);
        drawPossibleMoves(selectedPiece);
    }

    private Square selectSquare(double x, double y) {
        int row = (int) (y / CELL_SIZE);
        int column = (int) (x / CELL_SIZE);
        return new Square(row, column);
    }

    public void handleNewGame() {
        String player1Type = (String) dropPlayer1.getValue();
        String player2Type = (String) dropPlayer2.getValue();
        reset(getPlayerFromType(player1Type, WHITE), getPlayerFromType(player2Type, BLACK));
    }

    private Player getPlayerFromType(String type, PieceColor color) {

        switch (type) {
            case ("Human"):
                return new HumanPlayer(type, color, this);
            case ("RandomAI"):
                return new RandomAgent(type, color, this);
            case ("NegamaxAI"):
                return new NegamaxAI(type, color, this);
            default:
                return new HumanPlayer("Human", color, this);
        }
    }
}
