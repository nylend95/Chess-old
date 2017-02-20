package main.java.model;


import main.java.model.pieces.*;

import java.util.ArrayList;

import static java.lang.StrictMath.abs;

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
            whitePieces.add(new Pawn(PieceColor.WHITE, new Square(6, i), false));
        }
        whitePieces.add(new Rook(PieceColor.WHITE, new Square(7, 0)));
        whitePieces.add(new Rook(PieceColor.WHITE, new Square(7, 7)));
        whitePieces.add(new Bishop(PieceColor.WHITE, new Square(7, 2)));
        whitePieces.add(new Bishop(PieceColor.WHITE, new Square(7, 5)));
        whitePieces.add(new Knight(PieceColor.WHITE, new Square(7, 1)));
        whitePieces.add(new Knight(PieceColor.WHITE, new Square(7, 6)));
        whitePieces.add(new Queen(PieceColor.WHITE, new Square(7, 3)));
        whitePieces.add(new King(PieceColor.WHITE, new Square(7, 4)));

        //Black pieces
        for (int i = 0; i < 8; i++) {
            blackPieces.add(new Pawn(PieceColor.BLACK, new Square(1, i), false));
        }
        blackPieces.add(new Rook(PieceColor.BLACK, new Square(0, 0)));
        blackPieces.add(new Rook(PieceColor.BLACK, new Square(0, 7)));
        blackPieces.add(new Bishop(PieceColor.BLACK, new Square(0, 2)));
        blackPieces.add(new Bishop(PieceColor.BLACK, new Square(0, 5)));
        blackPieces.add(new Knight(PieceColor.BLACK, new Square(0, 1)));
        blackPieces.add(new Knight(PieceColor.BLACK, new Square(0, 6)));
        blackPieces.add(new Queen(PieceColor.BLACK, new Square(0, 3)));
        blackPieces.add(new King(PieceColor.BLACK, new Square(0, 4)));

        init();
    }

    private void init() {
        moveHistory = new ArrayList<>();
        updateBitmapPositions();
        updateBitmapAttackingPositions();
    }

    public boolean movePiece(Move move) {
        /*
         * Make a move on the board
         */
        Piece pieceToBeMoved = move.getPiece();
        ArrayList<Piece> movedList = (pieceToBeMoved.getColor() == PieceColor.WHITE) ? whitePieces : blackPieces;
        ArrayList<Piece> captureList = (pieceToBeMoved.getColor() == PieceColor.WHITE) ? blackPieces : whitePieces;

        // Do castling
        if (isMoveCastling(move)) {
            int row = (pieceToBeMoved.getColor() == PieceColor.WHITE) ? 7 : 0;
            int[] col = (move.getEndSquare().getColumn() == 2) ? new int[]{0, 3} : new int[]{7, 5};
            Square rookSquare = new Square(row, col[0]);
            Rook rookToBeCastled = (Rook) findPiece(pieceToBeMoved.getColor(), rookSquare);
            Move rookMove = new Move(rookSquare, new Square(row, col[1]), rookToBeCastled);
            movePiece(rookMove);
        }

        // Move piece
        for (Piece piece : movedList) {
            if (pieceToBeMoved.equals(piece)) {
                piece.setSquare(move.getEndSquare());
                piece.setMoved(true);
                break;
            }
        }

        // Possibly, capture a piece
        for (Piece piece : captureList) {
            if (piece.getSquare().equals(move.getEndSquare())) {
                captureList.remove(piece);
                move.setCapturedPiece(piece);
                break;
            }
        }

        // Add to history and update bitmaps
        moveHistory.add(move);
        updateBitmapPositions();
        updateBitmapAttackingPositions();

        return true;
    }

    private boolean isMoveCastling(Move move) {
        return (move.getPiece() instanceof King && move.getStartSquare().getColumn() == 4 &&
                abs(move.getEndSquare().getColumn() - move.getStartSquare().getColumn()) == 2);
    }

    public Piece findPiece(PieceColor color, Square square) {
        ArrayList<Piece> pieces = (color == PieceColor.WHITE) ? whitePieces : blackPieces;
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
        ArrayList<Piece> pieces = (color == PieceColor.WHITE) ? whitePieces : blackPieces;

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
        int[][] opponentAttackingSquares = (color == PieceColor.WHITE) ? bitmapAttackingPositionsBlack : bitmapAttackingPositionsWhite;
        return findKing(color).toBeCaptured(opponentAttackingSquares);
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
            if (capturedPiece.getColor() == PieceColor.WHITE) {
                whitePieces.add(capturedPiece);
            } else {
                blackPieces.add(capturedPiece);
            }
        }

        moveHistory.remove(moveHistory.size() - 1);

        // TODO especially this part.
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
        if (isMoveCastling(lastMove)){
            undoLastMove();
        }
    }

    public void updateCastling(PieceColor color) {
        boolean leftPossible = false;
        boolean rightPossible = false;
        ArrayList<Piece> pieces = (color == PieceColor.WHITE) ? whitePieces : blackPieces;
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
        king.setCastlingLeft(leftPossible);
        king.setCastlingRight(rightPossible);
    }

    public ArrayList<Move> generateValidMoves(PieceColor color) {
        /*
         * Returns every legal move for one color
         */
        updateCastling(color);
        ArrayList<Piece> selectedPieceSet = (color == PieceColor.WHITE) ? whitePieces : blackPieces;
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

        PieceColor opponentColor = (piece.getColor() == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;

        ArrayList<Move> moves = piece.validMoves(bitmapPositions, getBitmapAttackingPositions(opponentColor));
        for (Move possibleMove : moves) {
            movePiece(possibleMove);

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
        if (color == PieceColor.BLACK) {
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
}
