package main.java.model;


import com.rits.cloning.Cloner;
import main.java.model.pieces.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.StrictMath.abs;
import static main.java.model.PieceColor.*;

/**
 * Created by Jesper Nylend on 10.02.2017.
 * s305070
 */
public class Board implements Serializable {
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;
    private King whiteKing;
    private King blackKing;
    private int[][] bitmapPositions;
    private int[][] bitmapPawnPositions;
    private int[][] bitmapPositionsNoKings;
    private int[][] bitmapAttackingPositionsWhite;
    private int[][] bitmapAttackingPositionsBlack;
    private Integer[] idPositions;

    private ArrayList<Move> moveHistory;

    private int counterOfNoPawnsNoCaptures; // For 50 moves without moving a pawn or capturing a piece

    private HashMap<Integer, Integer> boardStateCounter;
    private Integer lastBoardId;

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

        //Pawns
        for (int i = 0; i < 8; i++) {
            whitePieces.add(new Pawn(WHITE, new Square(6, i)));
            blackPieces.add(new Pawn(BLACK, new Square(1, i)));
        }

        // White pieces
        whitePieces.add(new Rook(WHITE, new Square(7, 0)));
        whitePieces.add(new Rook(WHITE, new Square(7, 7)));
        whitePieces.add(new Bishop(WHITE, new Square(7, 2)));
        whitePieces.add(new Bishop(WHITE, new Square(7, 5)));
        whitePieces.add(new Knight(WHITE, new Square(7, 1)));
        whitePieces.add(new Knight(WHITE, new Square(7, 6)));
        whitePieces.add(new Queen(WHITE, new Square(7, 3)));
        whiteKing = new King(WHITE, new Square(7, 4));
        whitePieces.add(whiteKing);

        // Black pieces
        blackPieces.add(new Rook(BLACK, new Square(0, 0)));
        blackPieces.add(new Rook(BLACK, new Square(0, 7)));
        blackPieces.add(new Bishop(BLACK, new Square(0, 2)));
        blackPieces.add(new Bishop(BLACK, new Square(0, 5)));
        blackPieces.add(new Knight(BLACK, new Square(0, 1)));
        blackPieces.add(new Knight(BLACK, new Square(0, 6)));
        blackPieces.add(new Queen(BLACK, new Square(0, 3)));
        blackKing = new King(BLACK, new Square(0, 4));
        blackPieces.add(blackKing);

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
        PieceColor moveColor = move.getPiece().getColor();
        Piece pieceToBeMoved = findPiece(moveColor, move.getStartSquare());
        if (pieceToBeMoved == null){
            System.out.println("adlkf");
        }
        ArrayList<Piece> captureList = (moveColor == WHITE) ? blackPieces : whitePieces;

        // Check if castling
        if (!pieceToBeMoved.isMoved() && isMoveCastling(move)) {
            int row = (moveColor == WHITE) ? 7 : 0;
            int[] col = (move.getEndSquare().getColumn() == 2) ? new int[]{0, 3} : new int[]{7, 5};
            Square rookSquare = new Square(row, col[0]);
            Piece rookToBeCastled = findPiece(moveColor, rookSquare);
            if (rookToBeCastled != null) {
                rookToBeCastled.setMoved(true);
                rookToBeCastled.setSquare(new Square(row, col[1]));
            }
        }

        // Check if first move to either of rooks
        if (!pieceToBeMoved.isMoved() && pieceToBeMoved instanceof Rook) {
            King king = (moveColor == WHITE) ? whiteKing : blackKing;
            if (pieceToBeMoved.getSquare().getColumn() == 0) {
                king.setCastlingLeft(false);
            }
            if (pieceToBeMoved.getSquare().getColumn() == 7) {
                king.setCastlingRight(false);
            }
        }

        // Move piece
        pieceToBeMoved.setSquare(move.getEndSquare());
        pieceToBeMoved.setMoved(true);
        move = new Move(move.getStartSquare(), move.getEndSquare(), pieceToBeMoved);

        // Check if promotion move
        if (isMovePromotion(move)) {
            Pawn pawn = (Pawn) pieceToBeMoved;
            pawn.setPromoted(true);
            pawn.setPromotedTo(new Queen(pawn.getColor(), pawn.getSquare()));
        }

        // Check if en passant
        if (isMoveEnPassant(move)) {
            Piece lastMovedPawn = moveHistory.get(moveHistory.size() - 1).getPiece();
            for (Piece piece : captureList) {
                if (piece.equals(lastMovedPawn)) {
                    captureList.remove(piece);
                    move.setCapturedPiece(piece);
                    break;
                }
            }
        } else {
            // Possibly, capture a piece
            int opponentValue = (moveColor == WHITE) ? -1 : 1;
            if (bitmapPositions[move.getEndSquare().getRow()][move.getEndSquare().getColumn()] == opponentValue) {
                for (Piece piece : captureList) {
                    if (piece.getSquare().equals(move.getEndSquare())) {
                        if (piece instanceof Rook){
                            King king = (moveColor == WHITE) ? blackKing : whiteKing;
                            if (move.getEndSquare().getColumn() == 0){
                                king.setCastlingLeft(false);
                            }else if (move.getEndSquare().getColumn() == 7){
                                king.setCastlingRight(false);
                            }
                        }
                        captureList.remove(piece);
                        move.setCapturedPiece(piece);
                        break;
                    }
                }
            }
        }

        // Add to history and update bitmaps
        moveHistory.add(move);
        updateBitmapPositions();
        updateBitmapAttackingPositions();

        if (realMove) {
            // Generate string representation of board and count board states
            lastBoardId = Arrays.deepHashCode(idPositions);
            Integer boardCount = boardStateCounter.get(lastBoardId);
            if (boardCount != null) {
                boardCount += 1;
                boardStateCounter.put(lastBoardId, boardCount);
            } else {
                boardStateCounter.put(lastBoardId, 1);
            }

            // Count moves where no pawn have been moved or no piece have been captured
            counterOfNoPawnsNoCaptures++;
            if (move.getCapturedPiece() != null || pieceToBeMoved instanceof Pawn)
                counterOfNoPawnsNoCaptures = 0;

            updateStatus();
        }

        return true;
    }

    private boolean isMoveEnPassant(Move move) {
        if (moveHistory.size() == 0)
            return false;

        Piece piece = move.getPiece();
        Move lastMove = moveHistory.get(moveHistory.size() - 1);
        if (abs(lastMove.getEndSquare().getRow() - lastMove.getStartSquare().getRow()) == 2 && ((move.getStartSquare().getRow() == 4 && piece.getColor() == BLACK) || (move.getStartSquare().getRow() == 3 && piece.getColor() == WHITE)) && lastMove.getPiece() instanceof Pawn && piece instanceof Pawn) {
            Pawn pawn = (Pawn) piece;

            if (pawn.getPromotedTo() == null && move.getEndSquare().getColumn() == lastMove.getEndSquare().getColumn()) {
                return true;
            }
        }
        return false;
    }

    public Integer[] getIdPositions() {
        return idPositions;
    }

    public static int getIdOfPiece(Piece piece, int color) {
        if (piece instanceof King) {
            return 6 * color;
        } else if (piece instanceof Queen) {
            return 5 * color;
        } else if (piece instanceof Rook) {
            return 4 * color;
        } else if (piece instanceof Knight) {
            return 3 * color;
        } else if (piece instanceof Bishop) {
            return 2 * color;
        }
        return color; // Default is pawn
    }

    private boolean isMovePromotionUndo(Move move) {
        if (move.getPiece() instanceof Pawn && (move.getEndSquare().getRow() == 7 || move.getEndSquare().getRow() == 0)) {
            // Check move-history if this Pawn has been promoted before?
            for (Move checkMove : moveHistory) {
                if (checkMove.equals(move)) {
                    return true;
                } else if (checkMove.getPiece().equals(move.getPiece()) &&
                        (checkMove.getEndSquare().getRow() == 7 || checkMove.getEndSquare().getRow() == 0)) {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean isMovePromotion(Move move) {
        if (move.getPiece() instanceof Pawn) {
            Pawn pawn = (Pawn) move.getPiece();
            return (!pawn.isPromoted() && pawn.getPromotedTo() == null && (move.getEndSquare().getRow() == 7 || move.getEndSquare().getRow() == 0));
        }
        return false;
    }

    private boolean isMoveCastling(Move move) {
        return (move.getPiece() instanceof King && move.getStartSquare().getColumn() == 4 &&
                abs(move.getEndSquare().getColumn() - move.getStartSquare().getColumn()) == 2);
    }

    private Piece findPiece(PieceColor color, Square square) {
        ArrayList<Piece> pieces;
        if (color == WHITE) {
            if (bitmapPositions[square.getRow()][square.getColumn()] != 1)
                return null;
            pieces = whitePieces;
        } else {
            if (bitmapPositions[square.getRow()][square.getColumn()] != -1)
                return null;
            pieces = blackPieces;
        }

        for (Piece piece : pieces) {
            if (piece.getSquare().equals(square)) {
                return piece;
            }
        }
        return null;
    }


    /*private King findKing(PieceColor color) {

        ArrayList<Piece> pieces = (color == WHITE) ? whitePieces : blackPieces;

        for (Piece piece : pieces) {
            if (piece instanceof King) {
                return (King) piece;
            }
        }
        return null;
    }*/

    private boolean isCheck(PieceColor color) {
        /*
         * Is the king for color in check?
         */
        int[][] opponentAttackingSquares;
        King king;
        if (color == WHITE) {
            opponentAttackingSquares = bitmapAttackingPositionsBlack;
            king = whiteKing;
        } else {
            opponentAttackingSquares = bitmapAttackingPositionsWhite;
            king = blackKing;
        }
        return king != null && king.toBeCaptured(opponentAttackingSquares);
    }

    public void undoLastMove() {
        /*
         * Undo the last move. Rearrange the positions and remove from move-history. Update bitmaps.
         */

        if (moveHistory == null || moveHistory.size() == 0) {
            return;
        }

        Move lastMove = moveHistory.get(moveHistory.size() - 1);
        Piece movedPiece = findPiece(lastMove.getPiece().getColor(), lastMove.getEndSquare());
        if (movedPiece == null){
            System.out.println("asdjfla");
        }
        movedPiece.setSquare(lastMove.getStartSquare());


        if (lastMove.getCapturedPiece() != null) {
            Piece capturedPiece = lastMove.getCapturedPiece();
            if (!capturedPiece.isMoved() && capturedPiece instanceof Rook){
                King king = (capturedPiece.getColor() == WHITE) ? whiteKing : blackKing;
                if (lastMove.getEndSquare().getColumn() == 0){
                    king.setCastlingLeft(true);
                }else if (lastMove.getEndSquare().getColumn() == 7){
                    king.setCastlingRight(true);
                }
            }
            if (capturedPiece.getColor() == WHITE) {
                whitePieces.add(capturedPiece);
            } else {
                blackPieces.add(capturedPiece);
            }
        }

        // Undo promotion
        // TODO isMovePromotion will never go in here now because pawn.isPromoted() alwyas is true for a promoted pawn.
        if (isMovePromotionUndo(lastMove)) {
            Pawn pawn = (Pawn) movedPiece;
            pawn.setPromoted(false);
            pawn.setPromotedTo(null);
        }

        moveHistory.remove(lastMove);

        // TODO another way of setting a piece to moved/unmoved
        movedPiece.setMoved(false);
        for (Move move : moveHistory) {
            if (move.getPiece().equals(movedPiece)) {
                movedPiece.setMoved(true);
                break;
            }
        }

        // If last move is castling, undo twice.
        if (isMoveCastling(lastMove)) {
            int col = (lastMove.getEndSquare().getColumn() == 2) ? 3 : 5;
            int endCol = (lastMove.getEndSquare().getColumn() == 2) ? 0 : 7;
            int row = lastMove.getStartSquare().getRow();
            Piece rookInCastling = findPiece(movedPiece.getColor(), new Square(row, col));
            if (rookInCastling == null){
                System.out.println("asdfø");
            }
            rookInCastling.setMoved(false);
            rookInCastling.setSquare(new Square(row, endCol));
        }

        if (movedPiece instanceof Rook) {
            King king = (movedPiece.getColor() == WHITE) ? whiteKing : blackKing;
            if (movedPiece.isMoved()) {
                if (movedPiece.getSquare().getColumn() == 0) {
                    king.setCastlingLeft(false);
                }
                if (movedPiece.getSquare().getColumn() == 7) {
                    king.setCastlingRight(false);
                }
            }else{
                if (movedPiece.getSquare().getColumn() == 0) {
                    king.setCastlingLeft(true);
                }
                if (movedPiece.getSquare().getColumn() == 7) {
                    king.setCastlingRight(true);
                }
            }
        }

        updateBitmapPositions();
        updateBitmapAttackingPositions();
    }

    public int getStatus() {
        return status;
    }

    public King getWhiteKing() {
        return whiteKing;
    }

    public void setWhiteKing(King whiteKing) {
        this.whiteKing = whiteKing;
    }

    public King getBlackKing() {
        return blackKing;
    }

    public void setBlackKing(King blackKing) {
        this.blackKing = blackKing;
    }

    public int getNumberOfMovesDone() {
        return (int) Math.ceil(moveHistory.size() / 2);
    }

    public void updateStatus() {
        if (moveHistory.size() > 0) {
            PieceColor nextTurn = (moveHistory.get(moveHistory.size() - 1).getPiece().getColor() == WHITE) ? BLACK : WHITE;
            PieceColor previousTurn = (nextTurn == WHITE) ? BLACK : WHITE;
            King king = (nextTurn == WHITE) ? whiteKing : blackKing;
            int numPossibleMoves = generateValidMoves(nextTurn).size();
            if (king != null && king.toBeCaptured(getBitmapAttackingPositions(previousTurn)) && numPossibleMoves == 0) {
                status = (nextTurn == WHITE) ? -1 : 1;
            } else if (numPossibleMoves == 0) {
                // STALEMATE
                status = 2;
            } else if (counterOfNoPawnsNoCaptures >= 50) {
                // 50 MOVES NO CAPTURE OR PAWN
                status = 2;
            } else if (boardStateCounter.get(lastBoardId) >= 3) {
                //MOVE REPETITION
                status = 2;
            }
        }
    }


    /*private void updateCastling(PieceColor color) {
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
    }*/

    public ArrayList<Move> generateValidMoves(PieceColor color) {
        /*
         * Returns every legal move for one color
         */
        //updateCastling(color); // TODO fix this in move-method
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
        bitmapPawnPositions = new int[8][8];
        bitmapPositionsNoKings = new int[8][8];
        idPositions = new Integer[64];

        for (Piece black : blackPieces) {
            int row = black.getSquare().getRow();
            int col = black.getSquare().getColumn();
            int i = col + (row * 8);
            idPositions[i] = getIdOfPiece(black, -1);
            bitmapPositions[row][col] = -1;

            if (black instanceof Pawn) {
                bitmapPawnPositions[row][col] = -1;
            }
            if (!(black instanceof King)) {
                bitmapPositionsNoKings[row][col] = -1;
            }
        }

        for (Piece white : whitePieces) {
            int row = white.getSquare().getRow();
            int col = white.getSquare().getColumn();
            int i = col + (row * 8);
            idPositions[i] = getIdOfPiece(white, 1);
            bitmapPositions[row][col] = 1;
            if (white instanceof Pawn) {
                bitmapPawnPositions[row][col] = 1;
            }
            if (!(white instanceof King)) {
                bitmapPositionsNoKings[row][col] = 1;
            }
        }
    }

    public int[][] getBitmapPositions() {
        return bitmapPositions;
    }

    public int[][] getBitmapPawnPositions() {
        return bitmapPawnPositions;
    }

    private void updateBitmapAttackingPositions() {
        //1 for attacking position
        bitmapAttackingPositionsWhite = new int[8][8];
        bitmapAttackingPositionsBlack = new int[8][8];

        ArrayList<Square> attackingPositions = new ArrayList<>();
        for (Piece piece : blackPieces) {
            attackingPositions.addAll(piece.attackSquares(bitmapPositionsNoKings));
        }

        ArrayList<Square> calcAttackingPositions = new ArrayList<>();
        for (Square attackingSquare : attackingPositions) {
            if (!calcAttackingPositions.contains(attackingSquare)) {
                bitmapAttackingPositionsBlack[attackingSquare.getRow()][attackingSquare.getColumn()] = 1;
                calcAttackingPositions.add(attackingSquare);
            }
        }

        attackingPositions = new ArrayList<>();
        for (Piece piece : whitePieces) {
            attackingPositions.addAll(piece.attackSquares(bitmapPositionsNoKings));
        }

        calcAttackingPositions = new ArrayList<>();
        for (Square attackingSquare : attackingPositions) {
            if (!calcAttackingPositions.contains(attackingSquare)) {
                bitmapAttackingPositionsWhite[attackingSquare.getRow()][attackingSquare.getColumn()] = 1;
                calcAttackingPositions.add(attackingSquare);
            }
        }
    }

    public int[][] getBitmapAttackingPositions(PieceColor color) {
        if (color == BLACK) {
            return bitmapAttackingPositionsBlack;
        }
        return bitmapAttackingPositionsWhite;
    }

    public ArrayList<Move> getMoveHistory() {
        return moveHistory;
    }

    public ArrayList<Piece> getWhitePieces() {
        return whitePieces;
    }

    public ArrayList<Piece> getBlackPieces() {
        return blackPieces;
    }

    public Board makeCopy() {
        /*
         * Link: http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22uk.com.robust-it%22%20AND%20a%3A%22cloning%22
         * Github: https://github.com/kostaskougios/cloning
         */
        Cloner cloner = new Cloner();
        return cloner.deepClone(this);

        /*ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            return (Board) new ObjectInputStream(bais).readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;*/
    }

}
