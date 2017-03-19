package main.java.model.players;

import main.java.controller.IControls;
import main.java.model.Board;
import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.pieces.Pawn;
import main.java.model.pieces.Piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

import static java.lang.Double.max;
import static main.java.model.Board.getIdOfPiece;

/**
 * Created by mikkel on 25-Feb-17.
 * TODO currently VERY slow. Game needs optimization
 */
public class NegamaxAI extends Player {
    private int selectedMaxDept = 2; // Bug when selected max dept above 2
    private boolean isLogging = true;
    private Comparator<MoveScore> comp = Comparator.comparingDouble(o -> o.score);

    public NegamaxAI(String name, PieceColor color, IControls controls) {
        super(name, color, true, controls);
    }

    @Override
    public Move selectMove(Board board) {
        long startTime = System.currentTimeMillis();
        ArrayList<Move> validMoves = board.generateValidMoves(getColor());
        int blackOrWhite = (getColor() == PieceColor.WHITE) ? 1 : -1;

        MoveScore[] scoreMoves = new MoveScore[validMoves.size()];

        for (int i = 0; i < validMoves.size(); i++) {
            Board copy = board.makeCopy();
            copy.movePiece(validMoves.get(i), true);
            double score = calculateBoardScore(copy) * blackOrWhite;
            scoreMoves[i] = new MoveScore(validMoves.get(i), score, copy);
        }
        Arrays.sort(scoreMoves, comp.reversed());

        // Running negamax over possible moves in parallel to find the best possible move
        IntStream.range(0, scoreMoves.length).parallel().forEach(i -> {
            double value = negamax(scoreMoves[i].board, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, selectedMaxDept, switchPlayer(getColor()));
            scoreMoves[i].score = value * blackOrWhite;
        });

        Arrays.sort(scoreMoves, comp.reversed());

        // Do best move
        final long usedTime = System.currentTimeMillis() - startTime;
        if (isLogging)
            System.out.println(getColor() + " : " + scoreMoves[0].move + " : value " + scoreMoves[0].score + " : " + validMoves.size() + " moves : " + usedTime / 1000.0 + "(s)");

        makeMove(scoreMoves[0].move);
        return scoreMoves[0].move;
    }

    public int getSelectedMaxDept() {
        return selectedMaxDept;
    }

    public void setSelectedMaxDept(int selectedMaxDept) {
        this.selectedMaxDept = selectedMaxDept;
    }

    public boolean isLogging() {
        return isLogging;
    }

    public void setLogging(boolean logging) {
        isLogging = logging;
    }

    public static double negamax(Board board, double alpha, double beta, int dept, PieceColor nextPlayer) {
        if (dept == 0 || board.getStatus() != 0) {
            return calculateBoardScore(board);
        }

        double bestValue = Double.NEGATIVE_INFINITY;
        ArrayList<Move> validMoves = board.generateValidMoves(nextPlayer);

        //int c = 0; // Could be used to make it faster
        for (Move validMove : validMoves) {
//            Board copy = board.makeCopy();
//            copy.movePiece(validMove, true);
            board.movePiece(validMove, true);
            double value = -negamax(board, -beta, -alpha, dept - 1, switchPlayer(nextPlayer));
            board.undoLastMove();
            bestValue = max(bestValue, value);
            alpha = max(alpha, value);

            if (alpha >= beta) {
                break;
            }
        }
        return bestValue;
    }

    private static PieceColor switchPlayer(PieceColor currentPlayer) {
        return (currentPlayer == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
    }

    private static double calculateBoardScore(Board board) {
        if (board.getStatus() != 0) {
            if (board.getStatus() == 2) {
                return 0.0;
            }
            return 1000 * board.getStatus();
        }

        double boardScore = 0.0;

        // Piece values
        for (Piece whitePiece : board.getWhitePieces()) {
            int id = getIdOfPiece(whitePiece, 1);
            if (id == 1 && whitePiece instanceof Pawn) {
                Pawn pawn = (Pawn) whitePiece;
                if (pawn.isPromoted() && pawn.getPromotedTo() != null) {
                    id = getIdOfPiece(pawn.getPromotedTo(), 1);
                }
            }
            boardScore += calculatePieceValue(id);
        }
        for (Piece blackPiece : board.getBlackPieces()) {
            int id = getIdOfPiece(blackPiece, -1);
            if (id == -1 && blackPiece instanceof Pawn) {
                Pawn pawn = (Pawn) blackPiece;
                if (pawn.isPromoted() && pawn.getPromotedTo() != null) {
                    id = getIdOfPiece(pawn.getPromotedTo(), -1);
                }
            }
            boardScore -= calculatePieceValue(-id);
        }


        // Most attacking squares
        double attackingValue = 0.0;
        int[][] whiteAttackingSquares = board.getBitmapAttackingPositions(PieceColor.WHITE);
        int[][] blackAttackingSquares = board.getBitmapAttackingPositions(PieceColor.BLACK);
        for (int i = 0; i < 64; i++) {
            int c = (i % 8);
            int r = (i / 8) % 8;
            if (whiteAttackingSquares[r][c] != 0) {
                attackingValue++;
                if ((r == 3 || r == 4) && (c == 3 || c == 4)) {
                    attackingValue++;
                }
            }
            if (blackAttackingSquares[r][c] != 0) {
                attackingValue--;
                if ((r == 3 || r == 4) && (c == 3 || c == 4)) {
                    attackingValue--;
                }
            }
        }
        attackingValue *= 0.0001;

        return boardScore + attackingValue;
    }

    private static double calculatePieceValue(int pieceId) {
        switch (pieceId) {
            case 5:
                return 9;
            case 4:
                return 5;
            case 3:
                return 3;
            case 2:
                return 3;
            default:
                return 1;
        }
    }

    private static class MoveScore {
        protected Move move;
        protected double score;
        protected Board board;

        public MoveScore(Move move, double score, Board board) {
            this.move = move;
            this.score = score;
            this.board = board;
        }
    }
}
