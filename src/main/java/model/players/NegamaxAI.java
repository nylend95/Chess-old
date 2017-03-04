package main.java.model.players;

import main.java.controller.IControls;
import main.java.model.Board;
import main.java.model.Move;
import main.java.model.PieceColor;
import main.java.model.pieces.Pawn;
import main.java.model.pieces.Piece;

import java.util.ArrayList;

import static java.lang.Double.max;
import static main.java.model.Board.getIdOfPiece;

/**
 * Created by mikkel on 25-Feb-17.
 * TODO currently VERY slow. Game needs optimization and this needs alpha-beta pruning
 * TODO switch PieceColor to int in negamax
 */
public class NegamaxAI extends Player {
    private int selectedMaxDept = 2; // Bug when selected max dept above 2

    public NegamaxAI(String name, PieceColor color, IControls controls) {
        super(name, color, true, controls);
    }

    @Override
    public void selectMove(Board board) {
        long startTime = System.currentTimeMillis();
        ArrayList<Move> possibleMoves = board.generateValidMoves(getColor());
        double currentBestMoveValue = 0.0;
        int blackOrWhite = (getColor() == PieceColor.WHITE) ? 1 : -1;
        Move currentBestMove = possibleMoves.get(0);
        ArrayList<Double> moveValues = new ArrayList<>();

        // Running negamax over possible moves to find the best possible move
        // TODO make this multi-core?
        for (Move rootChild : possibleMoves) {
            double value = negamax(rootChild, board.makeCopy(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, selectedMaxDept, switchPlayer(getColor()));
            double convertedValue = value * blackOrWhite;
            moveValues.add(convertedValue);

            if (convertedValue >= 1000) {
                currentBestMoveValue = convertedValue;
                currentBestMove = rootChild;
                break;
            } else if (convertedValue > currentBestMoveValue) {
                currentBestMoveValue = convertedValue;
                currentBestMove = rootChild;
            }
        }

        // Do move
        final long usedTime = System.currentTimeMillis() - startTime;
        System.out.println(getColor() + " : " + currentBestMove + " : value " + currentBestMoveValue + " : " + possibleMoves.size() + " moves : " + usedTime / 1000 + "(s)");
        makeMove(currentBestMove);
    }

    public static double negamax(Move move, Board board, double alpha, double beta, int dept, PieceColor nextPlayer) {
        board.movePiece(move, true);

        if (board.getStatus() != 0) {
            if (board.getStatus() == 2) {
                return 0.0;
            }
            return 1000 * board.getStatus();
        }

        // boardScore = calculateBoardScore(board)
        if (dept == 0 /*abs boardScore > n */) {
            return calculateBoardScore(board);
        }

        double bestValue = Double.NEGATIVE_INFINITY;
        ArrayList<Move> validMoves = board.generateValidMoves(nextPlayer);
        // TODO order validMoves based on score

        for (Move validMove : validMoves) {
            double value = -negamax(validMove, board.makeCopy(), -beta, -alpha, dept - 1, switchPlayer(nextPlayer));
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
        double boardScore = 0.0;

        // Piece values
        for (Piece whitePiece : board.getWhitePieces()) {
            int id = getIdOfPiece(whitePiece);
            if (id == 1 && whitePiece instanceof Pawn){
                Pawn pawn = (Pawn) whitePiece;
                if (pawn.isPromoted() && pawn.getPromotedTo() != null){
                    id = getIdOfPiece(pawn.getPromotedTo());
                }
            }
            boardScore += calculatePieceValue(id);
        }
        for (Piece blackPiece : board.getBlackPieces()) {
            int id = getIdOfPiece(blackPiece);
            if (id == 1 && blackPiece instanceof Pawn){
                Pawn pawn = (Pawn) blackPiece;
                if (pawn.isPromoted() && pawn.getPromotedTo() != null){
                    id = getIdOfPiece(pawn.getPromotedTo());
                }
            }
            boardScore -= calculatePieceValue(id);
        }

        // Most attacking squares
        double attackingValue = 0.0;
        int[][] whiteAttackingSquares = board.getBitmapAttackingPositions(PieceColor.WHITE);
        int[][] blackAttackingSquares = board.getBitmapAttackingPositions(PieceColor.BLACK);
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
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
        }
        attackingValue *= 0.05;

        return boardScore;
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
}
