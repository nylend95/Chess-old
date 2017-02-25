package main.java.model;


import main.java.model.pieces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.StrictMath.abs;
import static main.java.model.PieceColor.*;

/**
 * Created by Jesper Nylend on 10.02.2017.
 * s305070
 */
public class Board {
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;
    private int[][] bitmapPositions;
    private int[][] bitmapPositionsNoKings;
    private int[][] bitmapAttackingPositionsWhite;
    private int[][] bitmapAttackingPositionsBlack;

    private ArrayList<Move> moveHistory;

    private int counterOfNoPawnsNoCaptures; // For 50 moves without moving a pawn or capturing a piece

    private HashMap<String, Integer> boardStateCounter;
    private String lastBoardId;

    private int status; // 0 going, 1 white win, -1 black win, 2 stalemate

    public Board() {
        resetBoard();
    }

    public Board(ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;

        init();
    }

    public void resetBoard() {
        /*
         * Resets the board to a default position
         */
        whitePieces = new ArrayList<>(16);
        blackPieces = new ArrayList<>(16);

        //White pieces
        for (int i = 0; i < 8; i++) {
            whitePieces.add(new Pawn(WHITE, new Square(6, i)));
        }
        whitePieces.add(new Rook(WHITE, new Square(7, 0)));
        whitePieces.add(new Rook(WHITE, new Square(7, 7)));
        whitePieces.add(new Bishop(WHITE, new Square(7, 2)));
        whitePieces.add(new Bishop(WHITE, new Square(7, 5)));
        whitePieces.add(new Knight(WHITE, new Square(7, 1)));
        whitePieces.add(new Knight(WHITE, new Square(7, 6)));
        whitePieces.add(new Queen(WHITE, new Square(7, 3)));
        whitePieces.add(new King(WHITE, new Square(7, 4)));

        //Black pieces
        for (int i = 0; i < 8; i++) {
            blackPieces.add(new Pawn(BLACK, new Square(1, i)));
        }
        blackPieces.add(new Rook(BLACK, new Square(0, 0)));
        blackPieces.add(new Rook(BLACK, new Square(0, 7)));
        blackPieces.add(new Bishop(BLACK, new Square(0, 2)));
        blackPieces.add(new Bishop(BLACK, new Square(0, 5)));
        blackPieces.add(new Knight(BLACK, new Square(0, 1)));
        blackPieces.add(new Knight(BLACK, new Square(0, 6)));
        blackPieces.add(new Queen(BLACK, new Square(0, 3)));
        blackPieces.add(new King(BLACK, new Square(0, 4)));

        init();
    }

    private void init() {
        moveHistory = new ArrayList<>();
        boardStateCounter = new HashMap<>();
        status = 0;
        counterOfNoPawnsNoCaptures = 0;
        updateBitmapPositions();
        updateBitmapAttackingPositions();
    }

    public boolean movePiece(Move move, boolean realMove) {
        /*
         * Make a move on the board
         */
        Piece pieceToBeMoved = move.getPiece();
        ArrayList<Piece> captureList = (pieceToBeMoved.getColor() == WHITE) ? blackPieces : whitePieces;

        // Do castling
        if (isMoveCastling(move)) {
            int row = (pieceToBeMoved.getColor() == WHITE) ? 7 : 0;
            int[] col = (move.getEndSquare().getColumn() == 2) ? new int[]{0, 3} : new int[]{7, 5};
            Square rookSquare = new Square(row, col[0]);
            Rook rookToBeCastled = (Rook) findPiece(pieceToBeMoved.getColor(), rookSquare);
            Move rookMove = new Move(rookSquare, new Square(row, col[1]), rookToBeCastled);
            movePiece(rookMove, realMove);
        }

        // Move piece
        move.getPiece().setSquare(move.getEndSquare());
        move.getPiece().setMoved(true);

        if (isMovePromotion(move)) {
            // Promotion
            Pawn pawn = (Pawn) move.getPiece();
            pawn.setPromoted(true);
            pawn.setPromotedTo(new Queen(pawn.getColor(), pawn.getSquare()));
        }

        if (isMoveEnPasant(move)) {
            Piece lastMovedPawn = moveHistory.get(moveHistory.size() - 1).getPiece();
            for (Piece piece : captureList) {
                if (piece.equals(lastMovedPawn)) {
                    captureList.remove(piece);
                    move.setCapturedPiece(piece);
                    System.out.println("En pasant removed!");
                    break;
                }
            }
        } else {
            // Possibly, capture a piece
            for (Piece piece : captureList) {
                if (piece.getSquare().equals(move.getEndSquare())) {
                    captureList.remove(piece);
                    move.setCapturedPiece(piece);
                    break;
                }
            }
        }

        // Add to history and update bitmaps
        moveHistory.add(move);
        updateBitmapPositions();
        updateBitmapAttackingPositions();

        if (realMove) {
            // Generate string representation of board and count board states
            lastBoardId = Arrays.toString(getIdPositions());
            Integer boardCount = boardStateCounter.get(lastBoardId);
            if (boardCount != null) {
                boardCount += 1;
                boardStateCounter.put(lastBoardId, boardCount);
            } else {
                boardStateCounter.put(lastBoardId, 1);
            }

            // Count moves where no pawn have been moved or no piece have been captured
            counterOfNoPawnsNoCaptures++;
            if (move.getCapturedPiece() != null || move.getPiece() instanceof Pawn)
                counterOfNoPawnsNoCaptures = 0;

            updateStatus();
        }

        return true;
    }

    public boolean isMoveEnPasant(Move move) {
        if (moveHistory.size() == 0)
            return false;

        Piece piece = move.getPiece();
        Move lastMove = moveHistory.get(moveHistory.size() - 1);
        if (lastMove.getPiece() instanceof Pawn && abs(lastMove.getEndSquare().getRow() - lastMove.getStartSquare().getRow()) == 2 && piece instanceof Pawn && ((move.getStartSquare().getRow() == 4 && piece.getColor() == BLACK) || (move.getStartSquare().getRow() == 3 && piece.getColor() == WHITE))) {
            Pawn pawn = (Pawn) piece;

            if (pawn.getPromotedTo() == null && move.getEndSquare().getColumn() == lastMove.getEndSquare().getColumn()) {
                return true;
            }
        }
        return false;
    }

    public int[] getIdPositions() {
        int[] idPositions = new int[64];
        for (Piece whitePiece : whitePieces) {
            int c = whitePiece.getSquare().getColumn(); //(i % 8);
            int r = whitePiece.getSquare().getRow(); //(i / 8) % 8;
            int i = c + (r * 8);
            idPositions[i] = getIdOfPiece(whitePiece);
        }
        for (Piece blackPiece : blackPieces) {
            int c = blackPiece.getSquare().getColumn(); //(i % 8);
            int r = blackPiece.getSquare().getRow(); //(i / 8) % 8;
            int i = c + (r * 8);
            idPositions[i] = getIdOfPiece(blackPiece);
        }
        return idPositions;
    }

    private int getIdOfPiece(Piece piece) {
        if (piece instanceof King) {
            return 6;
        } else if (piece instanceof Queen) {
            return 5;
        } else if (piece instanceof Rook) {
            return 4;
        } else if (piece instanceof Knight) {
            return 3;
        } else if (piece instanceof Bishop) {
            return 2;
        }
        return 1; // Default is pawn
    }

    public boolean isMovePromotion(Move move) {
        return (move.getPiece() instanceof Pawn && (move.getEndSquare().getRow() == 7 || move.getEndSquare().getRow() == 0));
    }

    private boolean isMoveCastling(Move move) {
        return (move.getPiece() instanceof King && move.getStartSquare().getColumn() == 4 &&
                abs(move.getEndSquare().getColumn() - move.getStartSquare().getColumn()) == 2);
    }

    public Piece findPiece(PieceColor color, Square square) {
        ArrayList<Piece> pieces = (color == WHITE) ? whitePieces : blackPieces;
        for (Piece piece : pieces) {
            if (piece.getSquare().equals(square)) {
                return piece;
            }
        }
        return null;
    }


    public King findKing(PieceColor color) {
        /*
         * Finds the king piece for color
         */
        ArrayList<Piece> pieces = (color == WHITE) ? whitePieces : blackPieces;

        for (Piece piece :
                pieces) {
            if (piece instanceof King) {
                return (King) piece;
            }
        }
        return null;
    }

    public boolean isCheck(PieceColor color) {
        /*
         * Is the king for color in check?
         */
        int[][] opponentAttackingSquares = (color == WHITE) ? bitmapAttackingPositionsBlack : bitmapAttackingPositionsWhite;
        King king = findKing(color);
        return king != null && king.toBeCaptured(opponentAttackingSquares);
    }

    public void undoLastMove() {
        /*
         * Undo the last move. Rearrange the positions and remove from move-history. Update bitmaps.
         */
        // TODO this has to be done quicker!

        if (moveHistory == null || moveHistory.size() == 0) {
            return;
        }
        Move lastMove = moveHistory.get(moveHistory.size() - 1);
        Piece movedPiece = lastMove.getPiece();
        movedPiece.setSquare(lastMove.getStartSquare());

        Piece capturedPiece = lastMove.getCapturedPiece();
        if (capturedPiece != null) {
            if (capturedPiece.getColor() == WHITE) {
                whitePieces.add(capturedPiece);
            } else {
                blackPieces.add(capturedPiece);
            }
        }

        moveHistory.remove(moveHistory.size() - 1);

        // TODO Convert moveHistory to hashmap?
        // Resets isMoved() for moved piece if it has not been moved before
        movedPiece.setMoved(false);
        for (Move move : moveHistory) {
            if (move.getPiece().equals(movedPiece)) {
                movedPiece.setMoved(true);
                break;
            }
        }

        updateBitmapPositions();
        updateBitmapAttackingPositions();

        // If last move is castling, undo twice.
        if (isMoveCastling(lastMove)) {
            undoLastMove();
        }

        // Undo promotion
        if (isMovePromotion(lastMove)) {
            Pawn pawn = (Pawn) movedPiece;
            pawn.setPromoted(false);
            pawn.setPromotedTo(null);
        }
    }

    public int getStatus() {
        return status;
    }

    public ArrayList<Move> getMoveHistory() {
        return moveHistory;
    }

    public void updateStatus() {
        if (moveHistory.size() > 0) {
            PieceColor nextTurn = (moveHistory.get(moveHistory.size() - 1).getPiece().getColor() == WHITE) ? BLACK : WHITE;
            PieceColor previousTurn = (nextTurn == WHITE) ? BLACK : WHITE;
            if (generateValidMoves(nextTurn).size() == 0) {
                King king = findKing(nextTurn);
                if (king != null && king.toBeCaptured(getBitmapAttackingPositions(previousTurn))) {
                    status = (nextTurn == WHITE) ? -1 : 1;
                } else {
                    System.out.println("STALEMATE!!");
                    status = 2;
                }
            } else if (counterOfNoPawnsNoCaptures >= 50) {
                System.out.println("50TREKKUTENCAPTUREELLERPAWN!!");
                status = 2;
            } else if (boardStateCounter.get(lastBoardId) >= 3) {
                System.out.println("TREKKGJENTAKELSE!!");
                status = 2;
            }
        }
        if (status != 0) {
            System.out.println("Status:" + status);
        }
    }

    public boolean isBoardRepetition() {
        return false;
    }

    public void updateCastling(PieceColor color) {
        boolean leftPossible = false;
        boolean rightPossible = false;
        ArrayList<Piece> pieces = (color == WHITE) ? whitePieces : blackPieces;
        for (Piece piece : pieces) {
            if (piece instanceof Rook) {
                if (piece.getSquare().getColumn() == 0 && !piece.isMoved()) {
                    leftPossible = true;
                }
                if (piece.getSquare().getColumn() == 7 && !piece.isMoved()) {
                    rightPossible = true;
                }
            }
        }

        King king = findKing(color);
        if (king != null) {
            king.setCastlingLeft(leftPossible);
            king.setCastlingRight(rightPossible);
        }
    }

    public ArrayList<Move> generateValidMoves(PieceColor color) {
        /*
         * Returns every legal move for one color
         */
        updateCastling(color);
        ArrayList<Piece> selectedPieceSet = (color == WHITE) ? whitePieces : blackPieces;
        ArrayList<Move> validMoves = new ArrayList<>();
        for (Piece piece : selectedPieceSet) {
            validMoves.addAll(generateValidMoves(piece));
        }

        return validMoves;
    }


    public ArrayList<Move> generateValidMoves(Piece piece) {
        /*
         * Generates legal moves for one piece
         */
        updateCastling(piece.getColor());

        ArrayList<Move> validMoves = new ArrayList<>();

        PieceColor opponentColor = (piece.getColor() == WHITE) ? BLACK : WHITE;

        ArrayList<Move> moves = piece.validMoves(bitmapPositions, getBitmapAttackingPositions(opponentColor));

        // En passant
        if (moveHistory.size() > 0) {
            Move lastMove = moveHistory.get(moveHistory.size() - 1);
            if (abs(lastMove.getEndSquare().getColumn() - piece.getSquare().getColumn()) == 1 && lastMove.getPiece() instanceof Pawn &&
                    abs(lastMove.getEndSquare().getRow() - lastMove.getStartSquare().getRow()) == 2 && piece instanceof Pawn &&
                    ((piece.getSquare().getRow() == 4 && piece.getColor() == BLACK) || (piece.getSquare().getRow() == 3 && piece.getColor() == WHITE))) {
                Pawn pawn = (Pawn) piece;
                int row = (pawn.getColor() == WHITE) ? 2 : 5;

                if (pawn.getPromotedTo() == null) {
                    moves.add(new Move(pawn.getSquare(), new Square(row, lastMove.getEndSquare().getColumn()), pawn));
                }
            }
        }

        for (Move possibleMove : moves) {
            movePiece(possibleMove, false);

            // Check if move causes a check
            if (!isCheck(piece.getColor())) {
                validMoves.add(possibleMove);
            }
            undoLastMove();
        }

        return validMoves;
    }

    private void updateBitmapPositions() {
        //-1 for black pieces
        //+1 for white pieces
        bitmapPositions = new int[8][8];
        bitmapPositionsNoKings = new int[8][8];

        for (Piece black : blackPieces) {
            bitmapPositions[black.getSquare().getRow()][black.getSquare().getColumn()] = -1;
            if (!(black instanceof King)) {
                bitmapPositionsNoKings[black.getSquare().getRow()][black.getSquare().getColumn()] = -1;
            }
        }

        for (Piece white : whitePieces) {
            bitmapPositions[white.getSquare().getRow()][white.getSquare().getColumn()] = 1;
            if (!(white instanceof King)) {
                bitmapPositionsNoKings[white.getSquare().getRow()][white.getSquare().getColumn()] = 1;
            }
        }
    }

    public int[][] getBitmapPositions() {
        return bitmapPositions;
    }

    private void updateBitmapAttackingPositions() {
        //1 for attacking position
        bitmapAttackingPositionsWhite = new int[8][8];
        bitmapAttackingPositionsBlack = new int[8][8];

        for (Piece piece : blackPieces) {
            ArrayList<Square> attackingPositions = piece.attackSquares(bitmapPositionsNoKings);
            for (Square attackingSquare : attackingPositions) {
                bitmapAttackingPositionsBlack[attackingSquare.getRow()][attackingSquare.getColumn()] = 1;
            }
        }
        for (Piece piece : whitePieces) {
            ArrayList<Square> attackingPositions = piece.attackSquares(bitmapPositionsNoKings);
            for (Square attackingSquare : attackingPositions) {
                bitmapAttackingPositionsWhite[attackingSquare.getRow()][attackingSquare.getColumn()] = 1;
            }
        }
    }

    public int[][] getBitmapAttackingPositions(PieceColor color) {
        if (color == BLACK) {
            return bitmapAttackingPositionsBlack;
        }
        return bitmapAttackingPositionsWhite;
    }

    public ArrayList<Piece> getWhitePieces() {
        return whitePieces;
    }

    public ArrayList<Piece> getBlackPieces() {
        return blackPieces;
    }

    public Board getCopy(){
        // TODO make a copy to avoid AI modifying this instance
        return this;
    }
}
